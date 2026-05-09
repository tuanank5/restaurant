package controller;

import java.sql.Date;
import java.time.LocalDateTime;

import dto.TaiKhoan_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;
import util.MapperUtil;

public class QuenMatKhau_Controller {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnLamMoi;

	@FXML
	private Button btnXacNhan;

	@FXML
	private TextField txtmaXacNhan;

	@FXML
	private PasswordField txtmatKhauMoi;

	@FXML
	private PasswordField txtxacNhanMatKhau;

	private TaiKhoan_DTO taiKhoan;
	private String otpGui;
	private LocalDateTime otpTime; // thời gian gửi OTP
	private Client client;

	// Khởi tạo dữ liệu từ Login_Controller
	public void initData(Object tk, String otp) {
		this.taiKhoan = tk == null ? null : MapperUtil.map(tk, TaiKhoan_DTO.class);
		this.otpGui = otp;
		this.otpTime = LocalDateTime.now(); // lưu thời gian OTP gửi

		client = Client.tryCreate();
	}

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();
		if (src == btnLamMoi) {
			resetAllField();
		} else if (src == btnXacNhan) {
			btnXacNhan(event);
		}
	}

	@FXML
	void btnXacNhan(ActionEvent event) {
		if (taiKhoan == null) {
			AlertUtil.showAlert("Lỗi", "Không tìm thấy tài khoản để đặt lại mật khẩu", Alert.AlertType.ERROR);
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		if (otpTime.plusMinutes(10).isBefore(now)) {
			AlertUtil.showAlert("Lỗi", "Mã OTP đã hết hạn. Vui lòng gửi lại.", Alert.AlertType.ERROR);
			return;
		}

		if (!txtmaXacNhan.getText().equals(otpGui)) {
			AlertUtil.showAlert("Lỗi", "Mã xác nhận không đúng", Alert.AlertType.ERROR);
			return;
		}

		String matKhauMoi = txtmatKhauMoi.getText();
		String matKhauXacNhan = txtxacNhanMatKhau.getText();

		if (matKhauMoi.length() < 6) {
			AlertUtil.showAlert("Lỗi", "Mật khẩu mới phải từ 6 ký tự trở lên", Alert.AlertType.ERROR);
			return;
		}

		if (!matKhauMoi.equals(matKhauXacNhan)) {
			AlertUtil.showAlert("Lỗi", "Mật khẩu xác nhận không khớp", Alert.AlertType.ERROR);
			return;
		}

		// Kiểm tra trùng mật khẩu cũ (plain text)
		if (matKhauMoi.equals(taiKhoan.getMatKhau())) {
			AlertUtil.showAlert("Lỗi", "Mật khẩu mới không được trùng mật khẩu cũ", Alert.AlertType.ERROR);
			return;
		}

		taiKhoan.setMatKhau(matKhauMoi);
		taiKhoan.setNgaySuaDoi(Date.valueOf(java.time.LocalDate.now()));

		if (!capNhatMatKhau(taiKhoan)) {
			AlertUtil.showAlert("Lỗi", "Không thể cập nhật mật khẩu", Alert.AlertType.ERROR);
			return;
		}

		AlertUtil.showAlert("Thành công", "Đặt lại mật khẩu thành công", Alert.AlertType.INFORMATION);

		// Đóng cửa sổ Quên mật khẩu và quay về Login
		Stage stage = (Stage) btnXacNhan.getScene().getWindow();
		stage.close();
	}

	private void resetAllField() {
		txtmaXacNhan.setText("");
		txtmatKhauMoi.setText("");
		txtxacNhanMatKhau.setText("");
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
