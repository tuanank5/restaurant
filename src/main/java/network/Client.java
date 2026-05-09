package network;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import network.common.Request;
import network.common.Response;
import org.openjfx.App;

public class Client {

	private static final int CONNECT_TIMEOUT_MS = 15_000;

	/** Một lần gửi + tối đa một lần kết nối lại nếu EOF/socket lỗi khi đọc phản hồi. */
	private static final int MAX_SEND_ATTEMPTS = 2;

	private final String host;
	private final int port;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client() throws Exception {
		this(ClientConfig.DEFAULT_HOST, ClientConfig.DEFAULT_PORT);
	}

	public Client(String host, int port) throws Exception {
		this.host = host;
		this.port = port;
		connect();
	}

	/**
	 * Phía client: ObjectOutputStream trước, flush, rồi ObjectInputStream.
	 * Phía server ({@code ClientHandler}) phải tạo ObjectInputStream trước, rồi ObjectOutputStream + flush
	 * — nếu cả hai cùng OOS trước dễ deadlock / {@link java.net.SocketException} / connection reset.
	 */
	private void connect() throws Exception {
		closeStreamsOnly();
		Socket s = new Socket();
		s.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
		s.setTcpNoDelay(true);
		s.setKeepAlive(true);
		this.socket = s;

		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
	}

	public static Client tryCreate() {
		try {
			return App.ClientManager.getClient();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized Response send(Request request) throws Exception {
		Exception lastFailure = null;

		for (int attempt = 0; attempt < MAX_SEND_ATTEMPTS; attempt++) {
			try {
				if (socket == null || socket.isClosed() || out == null || in == null) {
					connect();
				}

				out.writeObject(request);
				out.flush();
				out.reset();

				return (Response) in.readObject();

			} catch (EOFException e) {
				lastFailure = e;
				closeStreamsOnly();
				if (attempt + 1 < MAX_SEND_ATTEMPTS) {
					System.err.println("Client: EOF khi đọc phản hồi — server có thể đã đóng socket. Kết nối lại và gửi lại request.");
					connect();
					continue;
				}
				throw new java.io.IOException("Server đóng kết nối trước khi gửi xong phản hồi (EOF).", e);

			} catch (SocketException e) {
				lastFailure = e;
				closeStreamsOnly();
				if (attempt + 1 < MAX_SEND_ATTEMPTS) {
					System.err.println("Client: lỗi socket — kết nối lại và gửi lại request.");
					connect();
					continue;
				}
				throw e;
			}
		}

		throw new java.io.IOException("Không nhận được phản hồi sau " + MAX_SEND_ATTEMPTS + " lần thử.", lastFailure);
	}

	private void closeStreamsOnly() {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ignored) {
		} finally {
			in = null;
		}
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ignored) {
		} finally {
			out = null;
		}
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (Exception ignored) {
		} finally {
			socket = null;
		}
	}

	public void close() {
		closeStreamsOnly();
	}
}
