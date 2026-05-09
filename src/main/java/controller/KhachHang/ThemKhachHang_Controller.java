package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.HangKhachHang_DTO;
import dto.KhachHang_DTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;
import util.AutoIDUitl;

public class ThemKhachHang_Controller {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnLuu;

	@FXML
	private Button btnTroLai;

	@FXML
	private Button btnXoa;

	@FXML
	private ComboBox<String> comBoxHangKH;

	@FXML
	private Label lblDanhSachKhachHang;

	@FXML
	private TextField txtDiaChi;

	@FXML
	private TextField txtDiemTichLuy;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtKhachHang;

	@FXML
	private TextField txtSDT;

	private String ui;

	private List<HangKhachHang_DTO> danhSachHangKhachHangDB = List.of();
	private Client client;

	@FXML
	public void initialize() {
		Platform.runLater(() -> txtKhachHang.requestFocus());

		client = Client.tryCreate();

		loadData();
	}

	public FXMLLoader readyUI(String ui) {
		Parent root = null;
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.setLocation(getClass().getResource("/view/fxml/" + ui + ".fxml"));
			root = fxmlLoader.load();
			borderPane.setCenter(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fxmlLoader;
	}

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();

		if (src == btnTroLai) {
			dongDialog();

		} else if (src == btnLuu) {
			luuLai();

		} else if (src == btnXoa) {
			resetAllField();
		}
	}

	private void dongDialog() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.close();
	}

	@FXML
	void keyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			dongDialog();
		} else if (event.isControlDown() && event.getCode() == KeyCode.S) {
			luuLai();
		} else if (event.getCode() == KeyCode.X) {
			resetAllField();
		}
	}

	@FXML
	void mouseClicked(MouseEvent event) {
		Object source = event.getSource();
		if (source == lblDanhSachKhachHang) {
			dongDialog();
		}
	}

	public void luuLai() {
		KhachHang_DTO khachHangNew = getKhachHangNew();
		try {
			if (khachHangNew != null) {
				boolean check = themKhachHang(khachHangNew);
				// Kiểm tra kết quả thêm
				if (check) {
					AlertUtil.showAlert("Thông báo", "Thêm khách hàng thành công!", Alert.AlertType.INFORMATION);
					dongDialog();
					// Nếu không ở form thêm, thì quay lại
					if (!lblDanhSachKhachHang.getText().equalsIgnoreCase("Danh Sách Khách Hàng")) {
						readyUI(ui);
					}
					// Reset lại toàn bộ các ô nhập
					resetAllField();
				} else {
					AlertUtil.showAlert("Thông báo", "Thêm khách hàng thất bại!", Alert.AlertType.WARNING);
				}
			}
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getMessage() != null && e.getCause().getMessage().contains("UNIQUE")) {
				AlertUtil.showAlert("Cảnh Báo", "Dữ liệu bị trùng (SĐT hoặc Email)!", Alert.AlertType.WARNING);
			} else {
				AlertUtil.showAlert("Lỗi", "Xảy ra lỗi khi thêm khách hàng!", Alert.AlertType.ERROR);
			}
		}
	}

	private KhachHang_DTO getKhachHangNew() {
		String tenKH = txtKhachHang.getText().trim();
		String sdt = txtSDT.getText().trim();
		String email = txtEmail.getText().trim();
		String diaChi = txtDiaChi.getText().trim();
		String diemStr = txtDiemTichLuy.getText().trim();
		String hangKH = comBoxHangKH.getValue();

		// Kiểm tra tên khách hàng
		if (tenKH.isEmpty()) {
			AlertUtil.showAlert("Cảnh Báo", "Vui lòng nhập tên khách hàng!", Alert.AlertType.WARNING);
			txtKhachHang.requestFocus();
			return null;
		}
		// Kiểm tra số điện thoại
		if (sdt.isEmpty()) {
			AlertUtil.showAlert("Cảnh Báo", "Vui lòng nhập số điện thoại!", Alert.AlertType.WARNING);
			txtSDT.requestFocus();
			return null;
		} else {
			String regexPhone = "(84|0)[987531][0-9]{8,9}\\b";
			if (!sdt.matches(regexPhone)) {
				AlertUtil.showAlert("Cảnh Báo", "Số điện thoại không hợp lệ!", Alert.AlertType.WARNING);
				txtSDT.requestFocus();
				return null;
			}
		}

		// Kiểm tra email
		if (email.isEmpty()) {
			AlertUtil.showAlert("Cảnh Báo", "Vui lòng nhập email!", Alert.AlertType.WARNING);
			txtEmail.requestFocus();
			return null;
		} else if (!email.matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
			AlertUtil.showAlert("Cảnh Báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
			txtEmail.requestFocus();
			return null;
		}
		// Kiểm tra địa chỉ
		if (diaChi.isEmpty()) {
			AlertUtil.showAlert("Cảnh Báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
			txtDiaChi.requestFocus();
			return null;
		}
		// Kiểm tra điểm tích lũy
		int diemTichLuy = 0;
		try {
			diemTichLuy = Integer.parseInt(diemStr);
			if (diemTichLuy < 0) {
				AlertUtil.showAlert("Cảnh Báo", "Điểm tích lũy không thể âm!", Alert.AlertType.WARNING);
				txtDiemTichLuy.requestFocus();
				return null;
			}
		} catch (NumberFormatException e) {
			AlertUtil.showAlert("Cảnh Báo", "Điểm tích lũy phải là số nguyên!", Alert.AlertType.WARNING);
			txtDiemTichLuy.requestFocus();
			return null;
		}
		// Kiểm tra hạng khách hàng
		if (hangKH == null) {
			AlertUtil.showAlert("Cảnh Báo", "Vui lòng chọn hạng khách hàng!", Alert.AlertType.WARNING);
			comBoxHangKH.requestFocus();
			return null;
		}

		if (daTonTaiLocal("tenKH", tenKH)) {
			AlertUtil.showAlert("Cảnh Báo", "Tên khách hàng đã tồn tại!", Alert.AlertType.WARNING);
			txtKhachHang.requestFocus();
			return null;
		}

		if (daTonTaiLocal("sdt", sdt)) {
			AlertUtil.showAlert("Cảnh Báo", "Số điện thoại đã tồn tại!", Alert.AlertType.WARNING);
			txtSDT.requestFocus();
			return null;
		}

		if (daTonTaiLocal("email", email)) {
			AlertUtil.showAlert("Cảnh Báo", "Email đã tồn tại!", Alert.AlertType.WARNING);
			txtEmail.requestFocus();
			return null;
		}

		String maHang = danhSachHangKhachHangDB.stream().filter(h -> h != null && hangKH.equals(h.getTenHang()))
				.map(HangKhachHang_DTO::getMaHang).findFirst().orElse(null);

		return KhachHang_DTO.builder().maKH(AutoIDUitl.phatSinhMaKH()).tenKH(tenKH).sdt(sdt).email(email).diaChi(diaChi)
				.diemTichLuy(diemTichLuy).maHangKhachHang(maHang).build();
	}

	private boolean daTonTaiLocal(String field, String value) {
		List<KhachHang_DTO> all = getAllKhachHang();
		if (all == null || value == null)
			return false;
		return all.stream().anyMatch(kh -> {
			if (kh == null)
				return false;
			return switch (field) {
			case "tenKH" -> value.equalsIgnoreCase(kh.getTenKH());
			case "sdt" -> value.equals(kh.getSdt());
			case "email" -> value.equalsIgnoreCase(kh.getEmail());
			default -> false;
			};
		});
	}

	private void resetAllField() {
		txtKhachHang.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		txtDiaChi.setText("");
		txtDiemTichLuy.setText("");
		comBoxHangKH.setValue(null);
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request request = Request.builder().commandType(CommandType.HANGKHACHHANG_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();

			if (!(data instanceof List<?> rawList)) {
				return;
			}

			List<HangKhachHang_DTO> list = (List<HangKhachHang_DTO>) rawList;
			danhSachHangKhachHangDB = list == null ? List.of() : list;

			comBoxHangKH.getItems().clear();
			for (HangKhachHang_DTO hang : danhSachHangKhachHangDB) {
				if (hang != null) {
					comBoxHangKH.getItems().add(hang.getTenHang());
				}
			}
			comBoxHangKH.getSelectionModel().selectFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Dùng để lấy ra sau đó setText lại cho đường dẫn
	public void setUrl(String nameUrl, String currentPage) {
		lblDanhSachKhachHang.setText(nameUrl);
		this.ui = currentPage;
	}

	private boolean themKhachHang(KhachHang_DTO dto) throws Exception {
		Request request = Request.builder().commandType(CommandType.KHACHHANG_ADD).data(dto).build();
		Response response = client == null ? null : client.send(request);
		return response != null && response.isSuccess();
	}

	@SuppressWarnings("unchecked")
	private List<KhachHang_DTO> getAllKhachHang() {
		try {
			Request request = Request.builder().commandType(CommandType.KHACHHANG_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> rawList)) {
				return List.of();
			}
			return (List<KhachHang_DTO>) rawList;
		} catch (Exception e) {
			return List.of();
		}
	}
}
