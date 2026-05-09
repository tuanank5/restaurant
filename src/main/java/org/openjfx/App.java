package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.Client;

/**
 * JavaFX App
 */
public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		ClientManager.init();

		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/KhuyenMai/KhuyenMai.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Login.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNV.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNVQL.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Đăng Nhập");
		stage.setScene(scene);
		stage.show();
	}

	public class ClientManager {

		private static Client client;

		public static void init() {
			if (client == null) {
				try {
					client = new Client();
				} catch (Exception e) {
					throw new RuntimeException("Không thể kết nối server", e);
				}
			}
		}

		public static Client getClient() {
			return client;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}