package network.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.ClientConfig;

public class Server {
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		int port = ClientConfig.DEFAULT_PORT;

		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(port));
		} catch (BindException e) {
			System.err.println("Không thể lắng nghe cổng " + port + ": cổng đang được dùng (Address already in use).");
			System.err.println("Cách xử lý:");
			System.err.println("  1) Tắt tiến trình Server cũ (Run/Debug trước đó trong IDE hoặc java.exe trong Task Manager).");
			System.err.println("  2) Hoặc đổi cổng cho cả Server và Client, ví dụ VM options / environment:");
			System.err.println("     -Drestaurant.socket.port=9091");
			System.exit(1);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		try {
			System.out.println("Server running on " + ClientConfig.DEFAULT_HOST + ":" + port + " ...");

			while (true) {
				Socket socket = serverSocket.accept();
				pool.submit(new ClientHandler(socket));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException ignored) {
			}
			pool.shutdown();
		}
	}
}
