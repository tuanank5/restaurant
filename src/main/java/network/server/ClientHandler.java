package network.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.common.Request;
import network.common.Response;

public class ClientHandler implements Runnable {

	private Socket socket;
	private RequestDispatcher requestDispatcher = new RequestDispatcher();

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
		) {
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
			System.out.println("Client disconnected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
