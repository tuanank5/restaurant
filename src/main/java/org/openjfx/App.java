package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import network.Client;
import network.ClientConfig;

/**
 * JavaFX App
 */
public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		ClientManager.init();

		if (ClientManager.getClient() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kết nối server");
			alert.setHeaderText(null);
			alert.setContentText(
					"Không thể kết nối tới máy chủ tại "
							+ ClientConfig.DEFAULT_HOST + ":" + ClientConfig.DEFAULT_PORT + ".\n\n"
							+ "Hãy chạy network.server.Server trước, rồi đăng nhập / dùng chức năng.\n"
							+ "Gợi ý JVM: -Drestaurant.socket.host=localhost -Drestaurant.socket.port=9090");
			alert.showAndWait();
		}

		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Login.fxml"));

		Scene scene = new Scene(root);
		stage.setTitle("Đăng Nhập");
		stage.setScene(scene);
		stage.show();
	}

	public static class ClientManager {

		private static Client client;

		/**
		 * Thử kết nối; không ném RuntimeException — app vẫn khởi động được.
		 */
		public static void init() {
			tryConnect();
		}

		private static void tryConnect() {
			if (client != null) {
				return;
			}
			try {
				client = new Client();
			} catch (Exception e) {
				System.err.println("Không kết nối được server tại "
						+ ClientConfig.DEFAULT_HOST + ":" + ClientConfig.DEFAULT_PORT);
				e.printStackTrace();
				client = null;
			}
		}

		/**
		 * Trả về client đã kết nối; nếu chưa có thì thử kết nối lại một lần (ví dụ server vừa bật).
		 */
		public static Client getClient() {
			if (client == null) {
				tryConnect();
			}
			return client;
		}

		public static void resetConnection() {
			if (client != null) {
				try {
					client.close();
				} catch (Exception ignored) {
				}
				client = null;
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
