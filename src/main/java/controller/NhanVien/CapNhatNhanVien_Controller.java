package controller.NhanVien;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNVQL_Controller;
import dto.NhanVien_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;

public class CapNhatNhanVien_Controller implements Initializable {

	@FXML
	private BorderPane borderPane;
	@FXML
	private TextField txtMaNV;
	@FXML
	private TextField txtTenNV;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtDiaChi;
	@FXML
	private DatePicker txtNamSinh;
	@FXML
	private DatePicker txtNgayVaoLam;
	@FXML
	private Label lblDanhSachNhanVien;
	@FXML
	private ComboBox<String> cmbGioiTinh;
	@FXML
	private ComboBox<String> cmbChucVu;
	@FXML
	private ComboBox<String> cmbTrangThai;
	@FXML
	private Button btnLuu;
	@FXML
	private Button btnHuy;
	@FXML
	private Button btnQuayLai;

	private NhanVien_DTO nhanVien; // object gốc
	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmbGioiTinh.setItems(javafx.collections.FXCollections.observableArrayList("Nam", "Nữ"));
		cmbChucVu.setItems(javafx.collections.FXCollections.observableArrayList("Nhân viên", "Quản lý"));
		cmbTrangThai.setItems(javafx.collections.FXCollections.observableArrayList("Đang Làm", "Đã Nghĩ"));

		client = Client.tryCreate();
	}

	public void setNhanVien(NhanVien_DTO nhanVien) {
		this.nhanVien = nhanVien;
		hienThiThongTin(nhanVien);
	}

	private void hienThiThongTin(NhanVien_DTO nv) {
		txtMaNV.setText(nv.getMaNV());
		txtTenNV.setText(nv.getTenNV());
		txtEmail.setText(nv.getEmail());
		txtDiaChi.setText(nv.getDiaChi());
		txtNamSinh.setValue(nv.getNamSinh().toLocalDate());
		txtNgayVaoLam.setValue(nv.getNgayVaoLam().toLocalDate());
		cmbGioiTinh.setValue(nv.isGioiTinh() ? "Nữ" : "Nam");
		cmbChucVu.setValue(nv.getChucVu());
		cmbTrangThai.setValue(nv.isTrangThai() ? "Đang Làm" : "Đã Nghĩ");
		txtMaNV.setEditable(false);
	}

	@FXML
	void controller(ActionEvent event) {
		if (event.getSource() == btnLuu) {
			luuCapNhat();
		} else if (event.getSource() == btnQuayLai) {
			xacNhanQuayLai();
		} else if (event.getSource() == btnHuy) {
			hoanTac();
		}
	}

	@FXML
	void keyPressed(KeyEvent event) throws IOException {
		Object src = event.getSource();
		if (src == borderPane) {
			if (event.isControlDown() && event.getCode() == KeyCode.S) {
				luuCapNhat();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				if (event.getSource() == lblDanhSachNhanVien) {
					MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
				}
			}
		}
	}

	private void luuCapNhat() {
		NhanVien_DTO nhanVienNew = getNhanVienNew();
		if (nhanVienNew == null)
			return;

		Optional<ButtonType> option = AlertUtil.showAlertConfirm("Bạn có chắc chắn muốn lưu thay đổi?");
		if (option.get().getButtonData() == ButtonBar.ButtonData.NO)
			return;

		boolean check = capNhatNhanVien(nhanVienNew);

		if (check) {
			AlertUtil.showAlert("Thông báo", "Cập nhật nhân viên thành công!", Alert.AlertType.INFORMATION);
			this.nhanVien = nhanVienNew;
			quayLai();
		} else {
			AlertUtil.showAlert("Lỗi", "Cập nhật thất bại!", Alert.AlertType.ERROR);
		}
	}

	private NhanVien_DTO getNhanVienNew() {
		if (!validate())
			return null;

		return NhanVien_DTO.builder()
				.maNV(txtMaNV.getText().trim())
				.tenNV(txtTenNV.getText().trim())
				.email(txtEmail.getText().trim())
				.diaChi(txtDiaChi.getText().trim())
				.namSinh(Date.valueOf(txtNamSinh.getValue()))
				.ngayVaoLam(Date.valueOf(txtNgayVaoLam.getValue()))
				.gioiTinh(cmbGioiTinh.getValue().equals("Nữ"))
				.chucVu(cmbChucVu.getValue())
				.trangThai(cmbTrangThai.getValue().equals("Đang Làm"))
				.build();
	}

	private boolean validate() {

		if (txtTenNV.getText().trim().isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Tên nhân viên không được rỗng!", Alert.AlertType.WARNING);
			return false;
		}

		if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			AlertUtil.showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
			return false;
		}

		if (txtNamSinh.getValue() == null || Period.between(txtNamSinh.getValue(), LocalDate.now()).getYears() < 18) {
			AlertUtil.showAlert("Cảnh báo", "Nhân viên phải đủ 18 tuổi!", Alert.AlertType.WARNING);
			return false;
		}

		if (txtNgayVaoLam.getValue() == null) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng chọn ngày vào làm!", Alert.AlertType.WARNING);
			return false;
		}

		if (cmbTrangThai.getValue() == null) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng chọn trạng thái!", Alert.AlertType.WARNING);
			return false;
		}

		return true;
	}

	private void xacNhanQuayLai() {
		Optional<ButtonType> option = AlertUtil.showAlertConfirm("Bạn có muốn lưu thay đổi?");
		if (option.get().getButtonData() == ButtonBar.ButtonData.YES) {
			luuCapNhat();
		} else {
			quayLai();
		}
	}

	private void hoanTac() {
		Optional<ButtonType> buttonType = AlertUtil.showAlertConfirm("Bạn có chắc chắn muốn hoàn tác?");
		if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
			return;
		}
		boolean check = capNhatNhanVien(nhanVien);
		if (check) {
			AlertUtil.showAlert("Thông báo", "Hoàn tác thành công!", Alert.AlertType.INFORMATION);
			hienThiThongTin(nhanVien);
		} else {
			AlertUtil.showAlert("Thông báo", "Hoàn tác thất bại!", Alert.AlertType.WARNING);
		}
	}

	private void quayLai() {
		MenuNVQL_Controller.instance.readyUI("NhanVien/ThongTinChiTietNV");
	}

	private boolean capNhatNhanVien(NhanVien_DTO dto) {
		try {
			Request request = Request.builder().commandType(CommandType.NHANVIEN_UPDATE).data(dto).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
