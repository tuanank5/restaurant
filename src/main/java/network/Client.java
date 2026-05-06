package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.common.Request;
import network.common.Response;

public class Client {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client() throws Exception {
		socket = new Socket("localhost", 9090);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	public static Client tryCreate() {
		try {
			return new Client();
		} catch (Exception e) {
			return null;
		}
	}

	public Response send(Request request) throws Exception {
		out.writeObject(request);
		out.flush();
		return (Response) in.readObject();
	}
}