package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import network.common.Request;
import network.common.Response;
import org.openjfx.App;

public class Client {

	private static final int CONNECT_TIMEOUT_MS = 15_000;

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
	 * — nếu cả hai cùng OOS trước dễ deadlock / {@link java.net.SocketException Connection reset}.
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

		try {

			out.writeObject(request);
			out.flush();
			out.reset();

			return (Response) in.readObject();

		} catch (Exception e) {

			System.out.println("Mất kết nối server, reconnect...");

			closeStreamsOnly();
			connect();

			out.writeObject(request);
			out.flush();
			out.reset();

			return (Response) in.readObject();
		}
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
