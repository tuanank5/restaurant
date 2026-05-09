package controller.ThongKe;

import java.io.IOException;

import dto.NhanVien_DTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import session.SessionManager;

public class ThongKeNVScroll_Controller {
	@FXML
	public ScrollPane pane_Dashboard;

	@FXML
	public void initialize() {
		loadTabContent(pane_Dashboard, "/view/fxml/ThongKe/ThongKeNV.fxml");
	}

	private void loadTabContent(ScrollPane pane, String fxmlFile) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Node content = loader.load();
			pane.setContent(content);
			pane.setFitToHeight(true);
			pane.setFitToWidth(true);
			ThongKeNV_Controller controller = loader.getController();
			NhanVien_DTO nv = SessionManager.getCurrentNhanVien();
			if (controller != null && nv != null) {
				controller.setNhanVien(nv);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
