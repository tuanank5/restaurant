package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.common.Request;
import network.common.Response;
import org.openjfx.App;

public class Client {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client() throws Exception {
		connect();
	}

	private void connect() throws Exception {
		socket = new Socket("localhost", 9090);

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

			close();

			connect();

			out.writeObject(request);
			out.flush();
			out.reset();

			return (Response) in.readObject();
		}
	}

	public void close() {
		try {
			if (in != null)
				in.close();

			if (out != null)
				out.close();

			if (socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}