package util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class AlertUtil {

	public static void showAlert(String title, String content, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.show();
	}

	public static Optional<ButtonType> showAlertConfirm(String content) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Thông báo");
		alert.setHeaderText(content);
		ButtonType buttonLuu = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType buttonKhongLuu = new ButtonType("No", ButtonBar.ButtonData.NO);
		ButtonType buttonHuy = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonLuu, buttonKhongLuu, buttonHuy);
		return alert.showAndWait();
	}
}
