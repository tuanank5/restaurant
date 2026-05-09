package network.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.common.Request;
import network.common.Response;

/**
 * Handshake Object*Stream với {@link network.Client}:
 * <ul>
 *   <li><b>Client</b>: ObjectOutputStream → flush → ObjectInputStream (gửi header trước)</li>
 *   <li><b>Server</b>: ObjectInputStream → ObjectOutputStream → flush (đọc header client trước, rồi gửi header)</li>
 * </ul>
 * Nếu <i>cả hai</i> cùng tạo ObjectOutputStream trước, trên nhiều môi trường sẽ deadlock hoặc
 * {@link java.net.SocketException} / connection reset khi khởi tạo ObjectInputStream.
 */
public class ClientHandler implements Runnable {

	private final Socket socket;
	private final RequestDispatcher requestDispatcher = new RequestDispatcher();

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);

			// Server: OIS trước — chờ header từ phía Client (Client đã tạo OOS trước)
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			while (true) {
				Request request = (Request) in.readObject();
				System.out.println("RECEIVED = " + request);
				Response response = requestDispatcher.dispatch(request);
				System.out.println("RESPONSE = " + response);

				out.writeObject(response);
				out.flush();
				out.reset();
			}
		} catch (java.io.EOFException | java.net.SocketException e) {
			System.out.println("Client disconnected: " + e.getMessage());
		} catch (Throwable e) {
			System.err.println("ClientHandler error:");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ignored) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception ignored) {
			}
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (Exception ignored) {
			}
		}
	}
}
