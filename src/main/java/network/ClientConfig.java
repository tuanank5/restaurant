package network;

/**
 * Cấu hình socket dùng chung cho Client và Server (cùng host/port).
 * Có thể ghi đè: -Drestaurant.socket.host=127.0.0.1 -Drestaurant.socket.port=9090
 */
public final class ClientConfig {

	public static final String DEFAULT_HOST =
			System.getProperty("restaurant.socket.host", "100.104.223.6");

	public static final int DEFAULT_PORT =
			Integer.parseInt(System.getProperty("restaurant.socket.port", "9090"));

	private ClientConfig() {
	}
}
