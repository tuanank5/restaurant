package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.Menu.MenuNV_Controller;
import dto.HangKhachHang_DTO;
import dto.KhachHang_DTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;

public class CapNhatKhachHang_Controller {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnHoanTac;

	@FXML
	private Button btnLuuLai;

	@FXML
	private Button btnQuayLai;

	@FXML
	private ComboBox<String> comBoxHangKH;

	@FXML
	private Label lblDanhSachKhachHang;

	@FXML
	private Label lblThongTinChiTiet;

	@FXML
	private TextField txtDiaChi;

	@FXML
	private TextField txtDiemTichLuy;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtMaKH;

	@FXML
	private TextField txtSDT;

	@FXML
	private TextField txtTenKH;

	private KhachHang_DTO khachHang;

	private List<HangKhachHang_DTO> danhSachHangKhachHangDB = List.of();
	private final Map<String, String> tenHangByMaHang = new HashMap<>();
	private Client client;

	@FXML
	public void initialize() {
		Platform.runLater(() -> txtTenKH.requestFocus());

		client = Client.tryCreate();

		loadData();
	}

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();
		if (src == btnLuuLai) {
			luuLai();
		} else if (src == btnHoanTac) {
			hoanTac();
		} else if (src == btnQuayLai) {
			MenuNV_Controller.instance.readyUI("KhachHang/ThongTinChiTietKhachHang");
		}
	}

	@FXML
	void mouseClicked(MouseEvent event) throws IOException {
		Object source = event.getSource();
		// Khi người dùng click vào label "Danh Sách Khách Hàng"
		if (source == lblDanhSachKhachHang) {
			xacNhanLuu("KhachHang/KhachHang");
		}
		// Khi người dùng click vào label "Thông Tin Chi Tiết"
		else if (source == lblThongTinChiTiet) {
			xacNhanLuu("KhachHang/ThongTinChiTietKhachHang");
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		if (event.isControlDown() && event.getCode() == KeyCode.S) {
			luuLai();
		} else if (event.getCode() == KeyCode.ESCAPE) {
			troLai("KhachHang/ThongTinChiTietKhachHang");
		} else if (event.isControlDown() && event.getCode() == KeyCode.Z) {
			hoanTac();
		}
	}

	// Sự kiện
	private void luuLai() {
		KhachHang_DTO khachHangNew = getKhachHangNew();
		if (khachHangNew != null) {
			Optional<ButtonType> buttonType = AlertUtil.showAlertConfirm("Bạn có chắc chắn muốn lưu thay đổi?");
			if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
				return;
			}

			if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
				boolean check = capNhatKhachHang(khachHangNew);

				if (check) {
					AlertUtil.showAlert("Thông báo", "Cập nhật thông tin khách hàng thành công!",
							Alert.AlertType.INFORMATION);
					this.khachHang = khachHangNew;
					troLai("KhachHang/ThongTinChiTietKhachHang");
				} else {
					AlertUtil.showAlert("Thông báo", "Cập nhật thất bại!", Alert.AlertType.WARNING);
				}
			}
		} else {
			AlertUtil.showAlert("Lỗi", "Xảy ra lỗi khi cập nhật thông tin khách hàng!", Alert.AlertType.ERROR);
		}
	}

	private void hoanTac() {
		Optional<ButtonType> buttonType = AlertUtil.showAlertConfirm("Bạn có chắc chắn muốn hoàn tác?");
		if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
			return;
		}
		boolean check = capNhatKhachHang(khachHang);
		if (check) {
			AlertUtil.showAlert("Thông báo", "Hoàn tác thành công!", Alert.AlertType.INFORMATION);
			hienThiThongTin(khachHang);
		} else {
			AlertUtil.showAlert("Thông báo", "Hoàn tác thất bại!", Alert.AlertType.WARNING);
		}
	}

	private void xacNhanLuu(String ui) {
		KhachHang_DTO khachHangNew = getKhachHangNew();
		if (khachHangNew != null && isChanged(khachHangNew, khachHang)) {
			Optional<ButtonType> buttonType = AlertUtil.showAlertConfirm("Bạn có muốn lưu cập nhật?");

			if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
				troLai(ui);
			} else if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
				// Lưu dữ liệu khách hàng vào CSDL
				boolean check = capNhatKhachHang(khachHangNew);
				if (check) {
					AlertUtil.showAlert("Thông báo", "Cập nhật thành công!", Alert.AlertType.INFORMATION);
					this.khachHang = khachHangNew;
					troLai(ui);
				} else {
					AlertUtil.showAlert("Thông báo", "Cập nhật thất bại!", Alert.AlertType.WARNING);
				}
			}
		} else {
			troLai(ui);
		}
	}

	private void troLai(String ui) {
		if (khachHang != null && ui.equalsIgnoreCase("KhachHang/ThongTinChiTietKhachHang")) {
			ThongTinKhachHang_Controller thongTinKhachHangController = MenuNV_Controller.instance.readyUI(ui)
					.getController();
			thongTinKhachHangController.setKhachHang(khachHang);
		} else {
			MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
		}
	}

	public void hienThiThongTin(KhachHang_DTO khachHang) {
		if (khachHang != null) {
			txtMaKH.setText(khachHang.getMaKH());
			txtTenKH.setText(khachHang.getTenKH());
			txtSDT.setText(khachHang.getSdt());
			txtEmail.setText(khachHang.getEmail());
			txtDiaChi.setText(khachHang.getDiaChi());
			txtDiemTichLuy.setText(String.valueOf(khachHang.getDiemTichLuy()));
			String tenHang = tenHangByMaHang.get(khachHang.getMaHangKhachHang());
			comBoxHangKH.setValue(tenHang == null ? "" : tenHang);
		}
	}

	public void setKhachHang(KhachHang_DTO khachHang) {
		this.khachHang = khachHang;
		hienThiThongTin(khachHang);
	}

	private KhachHang_DTO getKhachHangNew() {
		String maKH = txtMaKH.getText().trim();
		String tenKH = txtTenKH.getText().trim();
		String sdt = txtSDT.getText().trim();
		String email = txtEmail.getText().trim();
		String diaChi = txtDiaChi.getText().trim();
		String diemStr = txtDiemTichLuy.getText().trim();
		String hangKH = comBoxHangKH.getValue();

		// Kiểm tra các trường bắt buộc
		if (tenKH.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập tên khách hàng!", Alert.AlertType.WARNING);
			txtTenKH.requestFocus();
			return null;
		}

		if (sdt.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập số điện thoại!", Alert.AlertType.WARNING);
			txtSDT.requestFocus();
			return null;
		} else {
			String regexPhone = "(84|0)[35789][0-9]{8,9}\\b";
			if (!sdt.matches(regexPhone)) {
				AlertUtil.showAlert("Cảnh báo", "Số điện thoại không hợp lệ!", Alert.AlertType.WARNING);
				txtSDT.requestFocus();
				return null;
			}
		}

		if (email.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập email!", Alert.AlertType.WARNING);
			txtEmail.requestFocus();
			return null;
		} else {
			if (!email.matches("^[a-zA-Z][a-zA-Z0-9._]*@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
				AlertUtil.showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
				txtEmail.requestFocus();
				return null;
			}
		}

		if (diaChi.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
			txtDiaChi.requestFocus();
			return null;
		}

		int diemTichLuy = 0;
		if (!diemStr.isEmpty()) {
			try {
				diemTichLuy = Integer.parseInt(diemStr);
				if (diemTichLuy < 0) {
					AlertUtil.showAlert("Cảnh báo", "Điểm tích lũy không được âm!", Alert.AlertType.WARNING);
					txtDiemTichLuy.requestFocus();
					return null;
				}
			} catch (NumberFormatException e) {
				AlertUtil.showAlert("Cảnh báo", "Điểm tích lũy phải là số nguyên!", Alert.AlertType.WARNING);
				txtDiemTichLuy.requestFocus();
				return null;
			}
		}
		if (hangKH == null) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng chọn hạng khách hàng!", Alert.AlertType.WARNING);
			comBoxHangKH.requestFocus();
			return null;
		}

		String maHang = danhSachHangKhachHangDB.stream().filter(h -> h != null && hangKH.equals(h.getTenHang()))
				.map(HangKhachHang_DTO::getMaHang).findFirst().orElse(null);

		return KhachHang_DTO.builder().maKH(maKH).tenKH(tenKH).sdt(sdt).email(email).diaChi(diaChi)
				.diemTichLuy(diemTichLuy).maHangKhachHang(maHang).build();
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

			tenHangByMaHang.clear();
			comBoxHangKH.getItems().clear();

			for (HangKhachHang_DTO hang : danhSachHangKhachHangDB) {
				if (hang != null && hang.getMaHang() != null) {
					tenHangByMaHang.put(hang.getMaHang(), hang.getTenHang());
					comBoxHangKH.getItems().add(hang.getTenHang());
				}
			}

			comBoxHangKH.getSelectionModel().selectFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean capNhatKhachHang(KhachHang_DTO dto) {
		try {
			Request request = Request.builder().commandType(CommandType.KHACHHANG_UPDATE).data(dto).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean isChanged(KhachHang_DTO a, KhachHang_DTO b) {
		if (a == null && b == null)
			return false;
		if (a == null || b == null)
			return true;
		return !safeEq(a.getMaKH(), b.getMaKH()) || !safeEq(a.getTenKH(), b.getTenKH()) || !safeEq(a.getSdt(), b.getSdt())
				|| !safeEq(a.getEmail(), b.getEmail()) || !safeEq(a.getDiaChi(), b.getDiaChi())
				|| a.getDiemTichLuy() != b.getDiemTichLuy() || !safeEq(a.getMaHangKhachHang(), b.getMaHangKhachHang());
	}

	private boolean safeEq(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
}
