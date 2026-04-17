package controller;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import util.AlertUtil;

public class DoiMatKhauQL_Controller {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnLamMoi;

	@FXML
	private Button btnLuuLai;

	@FXML
	private Button btnQuayLai;

	@FXML
	private PasswordField mkCuTextField;

	@FXML
	private PasswordField mkMoiTextField;

	@FXML
	private PasswordField mkNhapLaiTextField;

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();
		if (src == btnQuayLai) {
			btnQuayLai(event);
		} else if (src == btnLuuLai) {
			btnLuuLai(event);
		} else if (src == btnLamMoi) {
			btnLamMoi(event);
		}
	}

	@FXML
	void btnLuuLai(ActionEvent event) {
		TaiKhoan taiKhoan = MenuNVQL_Controller.taiKhoan;
		String mkCu = mkCuTextField.getText();
		String mkMoi = mkMoiTextField.getText();
		String mkNhapLai = mkNhapLaiTextField.getText();

		if (mkCu.isEmpty() || mkMoi.isEmpty() || mkNhapLai.isEmpty()) {
			AlertUtil.showAlert("Thông báo", "Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING);
			return;
		}

		if (!taiKhoan.getMatKhau().equals(mkCu)) {
			AlertUtil.showAlert("Thông báo", "Mật khẩu cũ không đúng", Alert.AlertType.ERROR);
			lamMoi();
			return;
		}

		if (!mkMoi.equals(mkNhapLai)) {
			AlertUtil.showAlert("Thông báo", "Mật khẩu mới không khớp", Alert.AlertType.ERROR);
			lamMoi();
			return;
		}

		if (mkMoi.equals(mkCu)) {
			AlertUtil.showAlert("Thông báo", "Mật khẩu mới phải khác mật khẩu cũ", Alert.AlertType.WARNING);
			lamMoi();
			return;
		}

		taiKhoan.setMatKhau(mkMoi);
		RestaurantApplication.getInstance().getDatabaseContext().newEntity_DAO(TaiKhoan_DAO.class).capNhat(taiKhoan);

		AlertUtil.showAlert("Thông báo", "Đổi mật khẩu thành công", Alert.AlertType.INFORMATION);
		lamMoi();
	}

	@FXML
	void btnLamMoi(ActionEvent event) {
		lamMoi();
	}

	private void lamMoi() {
		mkCuTextField.clear();
		mkMoiTextField.clear();
		mkNhapLaiTextField.clear();
	}

	@FXML
	void btnQuayLai(ActionEvent event) {
		MenuNVQL_Controller.getInstance().dashBoard();
	}
}
