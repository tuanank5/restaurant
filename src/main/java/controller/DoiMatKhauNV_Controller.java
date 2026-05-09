package controller;

import java.sql.Date;
import java.time.LocalDate;

import controller.Menu.MenuNV_Controller;
import dto.TaiKhoan_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;
import util.MapperUtil;

public class DoiMatKhauNV_Controller {
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

	private Client client;

	@FXML
	private void initialize() {
		client = Client.tryCreate();
	}

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
		TaiKhoan_DTO taiKhoan = MenuNV_Controller.taiKhoan;
		String mkCu = mkCuTextField.getText().trim();
		String mkMoi = mkMoiTextField.getText().trim();
		String mkNhapLai = mkNhapLaiTextField.getText().trim();

		if (taiKhoan == null) {
			AlertUtil.showAlert("Thông báo", "Không tìm thấy tài khoản", Alert.AlertType.WARNING);
			return;
		}

		if (taiKhoan.getMaTaiKhoan() == null || taiKhoan.getMaTaiKhoan().isBlank()) {
			AlertUtil.showAlert("Thông báo", "Không xác định được mã tài khoản. Đăng nhập lại.", Alert.AlertType.WARNING);
			return;
		}

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

		TaiKhoan_DTO dtoGui = MapperUtil.map(taiKhoan, TaiKhoan_DTO.class);
		dtoGui.setMatKhau(mkMoi);
		dtoGui.setNgaySuaDoi(Date.valueOf(LocalDate.now()));
		if (!capNhatMatKhau(dtoGui)) {
			AlertUtil.showAlert("Thông báo", "Đổi mật khẩu thất bại", Alert.AlertType.ERROR);
			return;
		}

		taiKhoan.setMatKhau(mkMoi);
		taiKhoan.setNgaySuaDoi(dtoGui.getNgaySuaDoi());
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
		MenuNV_Controller.getInstance().dashBoard();
	}

	private boolean capNhatMatKhau(TaiKhoan_DTO dto) {
		try {
			Request request = Request.builder().commandType(CommandType.TAIKHOAN_UPDATE_PASSWORD).data(dto).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
