package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {

	private double x = 0;
	private double y = 0;
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Login.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNV.fxml"));
//		Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Menu/MenuNVQL.fxml"));
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}