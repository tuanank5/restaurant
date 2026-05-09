package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNVQL_Controller;
import controller.Menu.MenuNV_Controller;
import dto.TaiKhoan_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;
import util.EmailUtil;

public class Login_Controller implements Initializable {

	private TaiKhoan_DTO taiKhoan;
	private Client client;

	@FXML
	private Button btnLogin;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtUserName;

	@FXML
	public void login(ActionEvent event) {
		try {
			String user = txtUserName.getText().trim();
			String pass = txtPassword.getText().trim();
			if (user.isEmpty() && pass.isEmpty()) {
				AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập tài khoản và mật khẩu!", Alert.AlertType.WARNING);
				return;
			}
			if (user.isEmpty()) {
				AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập tài khoản!", Alert.AlertType.WARNING);
				return;
			}
			if (pass.isEmpty()) {
				AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập mật khẩu!", Alert.AlertType.WARNING);
				return;
			}
			if (!user.matches("^(QL|NV)\\d{4}$")) {
				AlertUtil.showAlert("Cảnh báo", "Tài khoản phải bắt đầu bằng 'QL' hoặc 'NV' theo sau là 4 chữ số!",
						Alert.AlertType.WARNING);
				return;
			}
			if (pass.length() < 6) {
				AlertUtil.showAlert("Cảnh báo", "Mật khẩu phải có ít nhất 6 ký tự!", Alert.AlertType.WARNING);
				return;
			} else {
				List<TaiKhoan_DTO> taiKhoans = getAllTaiKhoan();
				taiKhoans = taiKhoans.stream().filter(tk -> tk != null && user.equalsIgnoreCase(tk.getTenTaiKhoan()))
						.toList();

				if (!taiKhoans.isEmpty()) {
					this.taiKhoan = taiKhoans.get(0);

					if (pass.equals(taiKhoan.getMatKhau())) {
						// Cập nhật lại ngày giờ đăng nhập
						LocalDate localDate = LocalDate.now();
						Date dateNow = Date.valueOf(localDate);
						this.taiKhoan.setNgayDangNhap(dateNow);
						capNhatTaiKhoanTaiServer(this.taiKhoan);

						// PHÂN QUYỀN GIAO DIỆN
						String chucVu = taiKhoan.getTenTaiKhoan() != null && taiKhoan.getTenTaiKhoan().startsWith("QL")
								? "Quản lý"
								: "Nhân viên";

						FXMLLoader fxmlLoader;
						Parent root;
						Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						Scene scene;

						if (chucVu != null && chucVu.equalsIgnoreCase("Quản lý")) {
							// Giao diện nhân viên quản lý
							fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/Menu/MenuNVQL.fxml"));
							root = fxmlLoader.load();
							scene = new Scene(root);
							stage.setTitle("Nhà hàng Út Bi - Nhân viên quản lý: " + taiKhoan.getTenNhanVien());
							MenuNVQL_Controller menuNVQLController = fxmlLoader.getController();
							menuNVQLController.setThongTin(taiKhoan);
						} else {
							// Giao diện nhân viên
							fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/Menu/MenuNV.fxml"));
							root = fxmlLoader.load();
							scene = new Scene(root);
							stage.setTitle("Nhà hàng Út Bi - Nhân viên: " + taiKhoan.getTenNhanVien());
							MenuNV_Controller menuNhanVienController = fxmlLoader.getController();
							menuNhanVienController.setThongTin(taiKhoan);
						}

						stage.setScene(scene);
						stage.setMaximized(true);
						stage.show();

						AlertUtil.showAlert("Thông báo", "Đăng nhập thành công!", Alert.AlertType.INFORMATION);

					} else {
						AlertUtil.showAlert("Cảnh báo", "Tên đăng nhập hoặc mật khẩu không chính xác! Vui lòng thử lại",
								Alert.AlertType.WARNING);
					}
				} else {
					AlertUtil.showAlert("Cảnh báo", "Không tìm thấy tài khoản!", Alert.AlertType.WARNING);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@FXML
	void quenmatkhau(ActionEvent event) {
		String tenTK = txtUserName.getText();
		if (tenTK.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập tên tài khoản", Alert.AlertType.WARNING);
			return;
		}
		List<TaiKhoan_DTO> list = getAllTaiKhoan().stream()
				.filter(tk -> tk != null && tenTK.equalsIgnoreCase(tk.getTenTaiKhoan())).toList();
		if (list.isEmpty()) {
			AlertUtil.showAlert("Lỗi", "Không tìm thấy tài khoản", Alert.AlertType.ERROR);
			return;
		}
		TaiKhoan_DTO taiKhoan = list.get(0);
		// Tạo dialog
		javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
		dialog.setTitle("Quên mật khẩu");
		dialog.setHeaderText("Nhập email của nhân viên để nhận OTP");

		ButtonType okButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

		TextField txtEmail = new TextField();
		txtEmail.setPromptText("Nhập email nhân viên");
		txtEmail.setPrefWidth(300);

		javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
		content.setStyle("-fx-padding: 20;");
		content.getChildren().addAll(new javafx.scene.control.Label("Email nhân viên:"), txtEmail);
		dialog.getDialogPane().setContent(content);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return txtEmail.getText();
			}
			return null;
		});

		dialog.showAndWait().ifPresent(emailInput -> {
			if (emailInput == null || emailInput.isBlank()) {
				AlertUtil.showAlert("Lỗi", "Email không hợp lệ", Alert.AlertType.ERROR);
				return;
			}
			String otp = util.AutoIDUitl.generateOTP();
			String subject = "Mã xác nhận đặt lại mật khẩu";
			String contentEmail = "Mã xác nhận của bạn là: " + otp + "\n"
					+ "Vui lòng không chia sẻ mã này cho bất kỳ ai.";
			EmailUtil.sendEmail(emailInput, subject, contentEmail);
			moManHinhQuenMatKhau(taiKhoan, otp);
		});
	}

	private void moManHinhQuenMatKhau(TaiKhoan_DTO tk, String otp) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/QuenMatKhau.fxml"));
			Parent root = loader.load();
			QuenMatKhau_Controller controller = loader.getController();
			controller.initData(tk, otp);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Xác nhận mã & đặt lại mật khẩu");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setDefaultButton(true);
		client = Client.tryCreate();
	}

	@SuppressWarnings("unchecked")
	private List<TaiKhoan_DTO> getAllTaiKhoan() {
		try {
			Request request = Request.builder().commandType(CommandType.TAIKHOAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> rawList)) {
				return List.of();
			}
			return (List<TaiKhoan_DTO>) rawList;
		} catch (Exception e) {
			return List.of();
		}
	}

	private boolean capNhatTaiKhoanTaiServer(TaiKhoan_DTO dto) {
		try {
			Request request = Request.builder().commandType(CommandType.TAIKHOAN_UPDATE_PASSWORD).data(dto).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}

}
