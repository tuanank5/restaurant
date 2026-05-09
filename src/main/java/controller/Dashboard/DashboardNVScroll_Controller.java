package controller.Dashboard;

import java.io.IOException;

import dto.NhanVien_DTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import session.SessionManager;

public class DashboardNVScroll_Controller {
	@FXML
	public ScrollPane pane_Dashboard;

	@FXML
	public void initialize() {
		if (pane_Dashboard != null) {
			pane_Dashboard.setFitToHeight(true);
			pane_Dashboard.setFitToWidth(true);
		}
	}

	/**
	 * Gọi sau khi {@link session.SessionManager} đã có nhân viên đăng nhập (ví dụ từ
	 * {@link controller.Menu.MenuNV_Controller#dashBoard()}).
	 */
	public void loadDashboardContent() {
		NhanVien_DTO nv = SessionManager.getCurrentNhanVien();
		if (nv == null || nv.getMaNV() == null || nv.getMaNV().isBlank()) {
			System.err.println("DashboardNVScroll: chưa có maNV trong session — không load DashboardNV.fxml.");
			return;
		}
		loadTabContent(pane_Dashboard, "/view/fxml/Dashboard/DashboardNV.fxml", nv);
	}

	private void loadTabContent(ScrollPane pane, String fxmlFile, NhanVien_DTO nv) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Node content = loader.load();
			pane.setContent(content);
			pane.setFitToHeight(true);
			pane.setFitToWidth(true);
			DashboardNV_Controller controller = loader.getController();
			if (controller != null) {
				controller.setNhanVien(nv);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
