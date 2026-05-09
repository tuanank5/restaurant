package network.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.ClientConfig;

public class Server {
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		int port = ClientConfig.DEFAULT_PORT;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server running on " + ClientConfig.DEFAULT_HOST + ":" + port + " ...");

			while (true) {
				Socket socket = serverSocket.accept();
				pool.submit(new ClientHandler(socket));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
