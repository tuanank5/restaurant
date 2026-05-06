package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Login.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNV.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNVQL.fxml"));

		Scene scene = new Scene(root);
		stage.setTitle("Đăng Nhập");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}