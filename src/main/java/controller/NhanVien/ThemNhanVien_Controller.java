package controller.NhanVien;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import controller.Menu.MenuNVQL_Controller;
import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import util.AlertUtil;
import util.AutoIDUitl;
import util.ComponentUtil;
import util.EmailUtil;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ThemNhanVien_Controller implements Initializable {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnHuy;

	@FXML
	private Button btnLuu;

	@FXML
	private TextField txtMaNV;

	@FXML
	private Button btnQuayLai;

	@FXML
	private ComboBox<String> cmbChucVu;

	@FXML
	private Label lblDanhSachNhanVien;

	@FXML
	private ComboBox<String> cmbGioiTinh;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblChucVu;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblDiaChi;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblEmail;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblGioiTinh;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblMaNV;

	@FXML
	private TableColumn<NhanVien_DTO, Date> tblNamSinh;

	@FXML
	private DatePicker txtNgayVaoLam;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblTenNV;

	@FXML
	private TableView<NhanVien_DTO> tblThemNV;

	@FXML
	private TableColumn<NhanVien_DTO, String> tblTrangThai;

	@FXML
	private TextField txtDiaChi;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker txtNamSinh;

	@FXML
	private TextField txtTenNV;

	@FXML
	private HBox hBox;

	private ObservableList<NhanVien_DTO> danhSachNhanVien = FXCollections.observableArrayList();
	private List<NhanVien_DTO> danhSachNhanVienDB = List.of();
	private final int LIMIT = 15;
	private String status = "all";

	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setValueTable();

		client = Client.tryCreate();

		loadData();
		loadDataNV();
		hienThiMaNhanVienMoi();
		resetAllField();
		if (danhSachNhanVienDB != null) {
			phanTrang(danhSachNhanVienDB.size());
		}
		borderPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ESCAPE) {
						MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
					} else if (event.isControlDown() && event.getCode() == KeyCode.S) {
						luuLai();
					}
				});
			}
		});
	}

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();
		if (src == btnQuayLai) {
			MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
		} else if (src == btnLuu) {
			luuLai();
		} else if (src == btnHuy) {
			resetAllField();
		}
	}

	@FXML
	void mouseClicked(MouseEvent event) {
		if (event.getSource() == lblDanhSachNhanVien) {
			MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
		}
	}

	@FXML
	void keyPressed(KeyEvent event) throws IOException {
		Object src = event.getSource();
		if (src == borderPane) {
			if (event.isControlDown() && event.getCode() == KeyCode.S) {
				luuLai();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				if (event.getSource() == lblDanhSachNhanVien) {
					MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
				}
			}
		}
	}

	private NhanVien_DTO getNhanVienNew() {
		String tenNV = txtTenNV.getText().trim();
		String email = txtEmail.getText().trim();
		LocalDate namSinh = txtNamSinh.getValue();
		String diaChi = txtDiaChi.getText().trim();
		String gioiTinhStr = cmbGioiTinh.getValue();
		String chucVu = cmbChucVu.getValue();
		LocalDate ngayVaoLam = txtNgayVaoLam.getValue();

		if (tenNV.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập tên nhân viên!", Alert.AlertType.WARNING);
			txtTenNV.requestFocus();
			return null;
		}

		if (email.isEmpty() || !email.matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
			AlertUtil.showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
			txtEmail.requestFocus();
			return null;
		}

		if (emailDaTonTai(email)) {
			AlertUtil.showAlert("Cảnh báo", "Email đã tồn tại!", Alert.AlertType.WARNING);
			return null;
		}

		if (namSinh == null || Period.between(namSinh, LocalDate.now()).getYears() < 18) {
			AlertUtil.showAlert("Cảnh báo", "Nhân viên phải đủ 18 tuổi!", Alert.AlertType.WARNING);
			return null;
		}

		if (diaChi.isEmpty()) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
			return null;
		}

		if (gioiTinhStr == null || chucVu == null) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng chọn giới tính và chức vụ!", Alert.AlertType.WARNING);
			return null;
		}

		if (ngayVaoLam == null) {
			AlertUtil.showAlert("Cảnh báo", "Vui lòng chọn ngày vào làm!", Alert.AlertType.WARNING);
			return null;
		}

		return NhanVien_DTO.builder().maNV(txtMaNV.getText()).tenNV(tenNV).namSinh(Date.valueOf(namSinh))
				.gioiTinh(gioiTinhStr.equals("Nữ")).email(email).diaChi(diaChi).chucVu(chucVu).trangThai(true)
				.ngayVaoLam(Date.valueOf(ngayVaoLam)).build();
	}

	public void luuLai() {
		NhanVien_DTO nvNew = getNhanVienNew();
		try {
			if (nvNew != null) {
				TaiKhoan_DTO taiKhoanMoi = themNhanVienVaTaoTaiKhoan(nvNew);
				if (taiKhoanMoi != null) {
					String noiDungMail = "Chào " + nvNew.getTenNV() + ",\n\n"
							+ "Tài khoản đăng nhập hệ thống của bạn:\n" + "Tên đăng nhập: " + taiKhoanMoi.getTenTaiKhoan()
							+ "\n" + "Mật khẩu: " + taiKhoanMoi.getMatKhau() + "\n\n"
							+ "Vui lòng đổi mật khẩu sau khi đăng nhập.";

					EmailUtil.sendEmail(nvNew.getEmail(), "Thông tin tài khoản nhân viên", noiDungMail);
					AlertUtil.showAlert("Thông báo",
							"Thêm nhân viên thành công!\nTài khoản đã được gửi qua email.", Alert.AlertType.INFORMATION);

					if (!lblDanhSachNhanVien.getText().equalsIgnoreCase("Danh Sách Nhân Viên")) {
						MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
					}
					resetAllField();
					loadData();
				} else {
					AlertUtil.showAlert("Thông báo", "Thêm nhân viên thất bại!", Alert.AlertType.WARNING);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert("Lỗi", "Xảy ra lỗi khi thêm nhân viên!", Alert.AlertType.ERROR);
		}
	}

	private void hienThiMaNhanVienMoi() {
		String maNV = util.AutoIDUitl.sinhMaNhanVien();
		txtMaNV.setText(maNV);
		txtMaNV.setEditable(false);
	}

	private void resetAllField() {
		txtTenNV.setText("");
		txtEmail.setText("");
		txtNamSinh.setValue(null);
		txtDiaChi.setText("");
		cmbGioiTinh.getSelectionModel().selectFirst();
		cmbChucVu.getSelectionModel().selectFirst();
	}

	private void loadDataNV() {
		cmbGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
		cmbGioiTinh.getSelectionModel().selectFirst();
		cmbChucVu.setItems(FXCollections.observableArrayList("Nhân viên", "Quản lý"));
		cmbChucVu.getSelectionModel().selectFirst();
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request request = Request.builder().commandType(CommandType.NHANVIEN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();

			if (!(data instanceof List<?> rawList)) {
				danhSachNhanVienDB = List.of();
				danhSachNhanVien.clear();
				tblThemNV.setItems(danhSachNhanVien);
				return;
			}

			List<NhanVien_DTO> list = (List<NhanVien_DTO>) rawList;
			danhSachNhanVienDB = list == null ? List.of() : list;

			List<NhanVien_DTO> filtered = danhSachNhanVienDB.stream().filter(nv -> nv != null && nv.isTrangThai())
					.toList();

			List<NhanVien_DTO> topLimitNhanVien = filtered.subList(0, Math.min(filtered.size(), LIMIT));
			danhSachNhanVien.setAll(topLimitNhanVien);
			tblThemNV.setItems(danhSachNhanVien);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setValueTable() {
		tblMaNV.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMaNV()));
		tblTenNV.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTenNV()));
		tblEmail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
		tblDiaChi.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDiaChi()));
		tblChucVu.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getChucVu()));

		tblGioiTinh.setCellValueFactory(
				cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().isGioiTinh() ? "Nữ" : "Nam"));

		tblTrangThai.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
				cell.getValue().isTrangThai() ? "Đang làm" : "Nghỉ"));
		tblNamSinh.setCellValueFactory(
				cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getNamSinh()));
	}

	private void loadData(List<NhanVien_DTO> nhanViens) {
		danhSachNhanVien.clear();
		danhSachNhanVien.addAll(nhanViens);
		tblThemNV.setItems(danhSachNhanVien);
	}

	private void phanTrang(int soLuongBanGhi) {
		hBox.getChildren().clear();
		loadCountPage(soLuongBanGhi);
		// Code bắt sự kiện
		hBox.getChildren().forEach(button -> {
			((Button) button).setOnAction(event -> {
				String value = ((Button) button).getText();
				int skip = (Integer.parseInt(value) - 1) * LIMIT;
				// Giới hạn phần tử cuối cùng để tránh vượt quá kích thước của danh sách
				if (status.equalsIgnoreCase("all")) {
					int endIndex = Math.min(skip + LIMIT, danhSachNhanVienDB.size());
					List<NhanVien_DTO> nhanViens = danhSachNhanVienDB.subList(skip, endIndex);
					loadData(nhanViens);
				} else {
					List<NhanVien_DTO> nhanViens = danhSachNhanVienDB.stream()
							.filter(nhanVien -> nhanVien.isTrangThai() == (status.equalsIgnoreCase("active")))
							.collect(Collectors.toCollection(ArrayList::new));
					int endIndex = Math.min(skip + LIMIT, nhanViens.size());
					nhanViens = nhanViens.subList(skip, endIndex);
					loadData(nhanViens);
				}
			});
		});
	}

	private void loadCountPage(int soLuongBanGhi) {
		int soLuongTrang = (int) Math.ceil((double) soLuongBanGhi / LIMIT);
		for (int i = 0; i < soLuongTrang; i++) {
			Button button = ComponentUtil.createButton(String.valueOf(i + 1), 14);
			hBox.getChildren().add(button);
		}
	}

	private boolean emailDaTonTai(String email) {
		if (email == null || email.isBlank())
			return false;
		return danhSachNhanVienDB != null && danhSachNhanVienDB.stream().anyMatch(nv -> nv != null && email.equalsIgnoreCase(nv.getEmail()));
	}

	private TaiKhoan_DTO themNhanVienVaTaoTaiKhoan(NhanVien_DTO nv) throws Exception {
		Request request = Request.builder().commandType(CommandType.NHANVIEN_ADD_WITH_ACCOUNT).data(nv).build();
		Response response = client == null ? null : client.send(request);
		if (response == null || !response.isSuccess()) {
			return null;
		}
		if (response.getData() instanceof TaiKhoan_DTO tk) {
			return tk;
		}
		return null;
	}
}
