package controller.DatBan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.DatMon.DoiMonTruoc_Controller;
import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class DonDatBan_Controller implements Initializable {
	@FXML
	private ComboBox<String> cmbTrangThai;

	@FXML
	private DatePicker dpNgayDatBan;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblGioDen;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblKhachHang;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblSoBan;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblSoNguoi;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblTienCoc;

	@FXML
	private TableColumn<DonDatBan_DTO, String> tblTrangThai;

	@FXML
	private TableView<DonDatBan_DTO> tblView;

	@FXML
	private TextField txtKH;

	@FXML
	private TextField txtTongDonDat;

	@FXML
	private Button btnDatBan;

	@FXML
	private Button btnDoiMon;

	@FXML
	private Button btnHuyDon;

	@FXML
	private Button btnThayDoi;

	private DonDatBan_DTO donDangChon;
	private Client client;
	private ObservableList<DonDatBan_DTO> danhSachDonDatBan = FXCollections.observableArrayList();
	private List<DonDatBan_DTO> danhSachDonDatBanDB = List.of();

	private void capNhatTongDon() {
		txtTongDonDat.setText(String.valueOf(tblView.getItems().size()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		client = Client.tryCreate();
		dpNgayDatBan.valueProperty().addListener((obs, oldVal, newVal) -> timTheoNgayDatBan());
		cmbTrangThai.valueProperty().addListener((obs, oldVal, newVal) -> timTheoTrangThai());
		txtKH.textProperty().addListener((obs, oldVal, newVal) -> timTheoTenKH());
		khoiTaoComboBoxes();
		setValueTable();
		loadData();
		tblView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			donDangChon = newVal == null ? null : newVal;
		});
		tblView.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends DonDatBan_DTO> change) -> {
			capNhatTongDon();
		});
		// cập nhật lần đầu
		capNhatTongDon();
		tblView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				DonDatBan_DTO don = tblView.getSelectionModel().getSelectedItem();
				if (don != null) {
					hienThiDialogThongTin(don);
				}
			}
		});
		cmbTrangThai.setTooltip(new Tooltip("Lọc theo trạng thái đơn đặt bàn!"));
		dpNgayDatBan.setTooltip(new Tooltip("Chọn ngày đặt bàn để lọc theo đơn đặt bàn!"));
		txtKH.setTooltip(new Tooltip("Nhập tên khách hàng để tìm kiếm trong đơn đặt bàn!"));
		btnDatBan.setTooltip(new Tooltip("Thông báo cho nút Đặt Bàn!"));
		btnDoiMon.setTooltip(new Tooltip("Thông báo cho nút Đổi Món!"));
		btnThayDoi.setTooltip(new Tooltip("Thông báo cho nút Đổi Bàn!"));
		btnHuyDon.setTooltip(new Tooltip("Thông báo cho nút Huỷ Đơn Bàn!"));
	}

	@FXML
	void btnDatBan(ActionEvent event) {
		MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
	}

	@FXML
	void btnHuyDon(ActionEvent event) {
		huyDonKhachGoiTruoc(donDangChon);
	}

	@FXML
	void btnDoiMon(ActionEvent event) {
		if (donDangChon == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn một đơn đặt bàn!");
			return;
		}

		DoiMonTruoc_Controller.donDatBanDuocChon = donDangChon;
		MenuNV_Controller.instance.readyUI("MonAn/DoiMon");
	}

	@FXML
	void btnThayDoi(ActionEvent event) {
		if (donDangChon == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Cảnh báo");
			alert.setHeaderText(null);
			alert.setContentText("Vui lòng chọn một đơn đặt bàn trước khi thay đổi!");
			alert.showAndWait();
			return;
		}
		// Lưu tạm dữ liệu chuyển màn hình
		ThayDoiBanTruoc_Controller.donDatBanDuocChon = donDangChon;
		MenuNV_Controller.instance.readyUI("DatBan/ThayDoiBanTruoc");
	}

	private void khoiTaoComboBoxes() {
		cmbTrangThai.getItems().clear();
		cmbTrangThai.getItems().addAll("Tất cả", "Đã nhận bàn", "Chưa nhận bàn");
		cmbTrangThai.getSelectionModel().select("Tất cả");
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request request = Request.builder().commandType(CommandType.DONDATBAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> rawList)) {
				danhSachDonDatBanDB = List.of();
			} else {
				danhSachDonDatBanDB = (List<DonDatBan_DTO>) rawList;
			}
		} catch (Exception e) {
			danhSachDonDatBanDB = List.of();
		}
		danhSachDonDatBan.clear();
		danhSachDonDatBan.addAll(danhSachDonDatBanDB);

		tblView.setItems(danhSachDonDatBan);
		capNhatTongDon();
	}

	private void setValueTable() {
		tblKhachHang.setCellValueFactory(cellData -> {
			DonDatBan_DTO don = cellData.getValue();
			if (don == null)
				return new SimpleStringProperty("");
			KhachHang_DTO kh = getKhachHangByMaDatBan(don.getMaDatBan());
			return new SimpleStringProperty(kh != null ? kh.getTenKH() : "");
		});

		tblSoBan.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getBan() != null ? cellData.getValue().getBan().getMaBan() : ""));

		tblSoNguoi.setCellValueFactory(
				cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong())));

		tblGioDen.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getGioBatDau() != null ? cellData.getValue().getGioBatDau().toString() : ""));

		tblTienCoc.setCellValueFactory(cellData -> new SimpleStringProperty("0")); // nếu có tiền cọc thực tế thì thay

		tblTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getTrangThai() != null ? cellData.getValue().getTrangThai() : ""));
	}

	private void hienThiDialogThongTin(DonDatBan_DTO don) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Thông tin khách hàng");
		dialog.setHeaderText("Chi tiết đơn đặt bàn");

		// Nút Xác nhận và Hủy
		ButtonType btnXacNhan = new ButtonType("Cập nhật trạng thái", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(btnXacNhan, ButtonType.CANCEL);

		KhachHang_DTO kh = getKhachHangByMaDatBan(don.getMaDatBan());
		// Các thông tin khác chỉ xem, không được chỉnh sửa
		TextField txtTen = new TextField(kh != null ? kh.getTenKH() : "");
		txtTen.setDisable(true);
		TextField txtSDTDialog = new TextField(kh != null ? kh.getSdt() : "");
		txtSDTDialog.setDisable(true);
		TextField txtSoLuong = new TextField(String.valueOf(don.getSoLuong()));
		txtSoLuong.setDisable(true);

		// Chỉ cho phép thay đổi trạng thái
		ComboBox<String> cmbTrangThaiDialog = new ComboBox<>();
		cmbTrangThaiDialog.getItems().addAll("Đã nhận bàn", "Chưa nhận bàn");
		cmbTrangThaiDialog.setValue(don.getTrangThai() != null ? don.getTrangThai() : "Chưa nhận bàn");

		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(15);
		grid.setPadding(new Insets(15));

		grid.addRow(0, new Label("Tên khách hàng:"), txtTen);
		grid.addRow(1, new Label("Số điện thoại:"), txtSDTDialog);
		grid.addRow(2, new Label("Số lượng:"), txtSoLuong);
		grid.addRow(3, new Label("Trạng thái:"), cmbTrangThaiDialog);
		dialog.getDialogPane().setContent(grid);
		// Xử lý khi nhấn XÁC NHẬN
		dialog.setResultConverter(button -> {
			if (button == btnXacNhan) {
				try {
					// Chỉ cập nhật trạng thái
					don.setTrangThai(cmbTrangThaiDialog.getValue());

					updateDonDatBan(don);
					tblView.refresh();
					showAlert(Alert.AlertType.INFORMATION, "Cập nhật trạng thái thành công!");
				} catch (Exception e) {
					e.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Không thể cập nhật trạng thái!");
				}
			}
			return null;
		});
		dialog.showAndWait();
	}

	@FXML
	private void timTheoNgayDatBan() {
		LocalDate ngayChon = dpNgayDatBan.getValue();
		if (ngayChon == null) {
			tblView.setItems(danhSachDonDatBan); // hiển thị tất cả
			capNhatTongDon();
			return;
		}

		FilteredList<DonDatBan_DTO> filtered = new FilteredList<>(danhSachDonDatBan, d -> true);
		filtered.setPredicate(don -> {
			LocalDate ngayDon = don.getNgayGioLapDon() == null ? null : don.getNgayGioLapDon().toLocalDate();
			return ngayDon.equals(ngayChon);
		});

		tblView.setItems(filtered);
		capNhatTongDon();
	}

	private void timTheoTenKH() {
		String key = txtKH.getText() != null ? txtKH.getText().trim().toLowerCase() : "";
		if (key.isEmpty()) {
			tblView.setItems(danhSachDonDatBan);
			capNhatTongDon();
			return;
		}

		FilteredList<DonDatBan_DTO> filtered = new FilteredList<>(danhSachDonDatBan, d -> true);
		filtered.setPredicate(don -> {
			if (don == null)
				return false;

			KhachHang_DTO kh = getKhachHangByMaDatBan(don.getMaDatBan());
			if (kh == null || kh.getTenKH() == null)
				return false;

			// So khớp tên khách (không phân biệt hoa/thường)
			return kh.getTenKH().toLowerCase().contains(key);
		});

		tblView.setItems(filtered);
		capNhatTongDon();
	}

	@FXML
	private void timTheoTrangThai() {
		String trangThaiChon = cmbTrangThai.getValue(); // giá trị hiện tại
		if (trangThaiChon == null || "Tất cả".equals(trangThaiChon)) {
			tblView.setItems(danhSachDonDatBan);
			capNhatTongDon();
			return;
		}

		FilteredList<DonDatBan_DTO> filtered = new FilteredList<>(danhSachDonDatBan, d -> true);
		filtered.setPredicate(don -> {
			String tt = don.getTrangThai();
			if (tt == null)
				return false;
			return tt.trim().equalsIgnoreCase(trangThaiChon.trim());
		});

		tblView.setItems(filtered);
		capNhatTongDon();
	}

	private void huyDonKhachGoiTruoc(DonDatBan_DTO don) {
		if (don == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn đơn đặt bàn!");
			return;
		}

		if (!"Chưa nhận bàn".equalsIgnoreCase(don.getTrangThai())) {
			showAlert(Alert.AlertType.WARNING, "Đơn này không thể hủy!");
			return;
		}

		LocalDateTime hienTai = LocalDateTime.now();
		LocalDateTime gioBatDau = LocalDateTime.of(don.getNgayGioLapDon().toLocalDate(), don.getGioBatDau());

		if (hienTai.isAfter(gioBatDau.minusHours(1))) {
			showAlert(Alert.AlertType.WARNING, "Khách chỉ được hủy trước giờ đến ít nhất 1 tiếng!");
			return;
		}

		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle("Xác nhận hủy đơn");
		confirm.setHeaderText(null);
		confirm.setContentText("Khách hàng xác nhận hủy đơn đặt bàn này?");
		Optional<ButtonType> rs = confirm.showAndWait();

		if (rs.isEmpty() || rs.get() != ButtonType.OK)
			return;
		try {
			don.setTrangThai("Đã hủy");
			updateDonDatBan(don);
			HoaDon_DTO hoaDon = getHoaDonByMaDatBan(don.getMaDatBan());
			if (hoaDon != null) {
				hoaDon.setTrangThai("Đã hủy");
				updateHoaDon(hoaDon);
			}
			Ban_DTO ban = don.getBan();
			if (ban != null) {
				ban.setTrangThai("Trống");
				updateBan(ban);
			}
			KhachHang_DTO kh = getKhachHangByMaDatBan(don.getMaDatBan());

			showAlert(Alert.AlertType.INFORMATION,
					"Đã hủy đơn đặt bàn của khách " + (kh != null ? kh.getTenKH() : "") + " theo yêu cầu.");
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Không thể hủy đơn đặt bàn!");
		}
	}

	private void showAlert(Alert.AlertType type, String msg) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	private KhachHang_DTO getKhachHangByMaDatBan(String maDatBan) {
		return null;
	}

	private HoaDon_DTO getHoaDonByMaDatBan(String maDatBan) {
		try {
			Request request = Request.builder().commandType(CommandType.HOADON_GET_BY_MADATBAN).data(maDatBan).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.getData() instanceof HoaDon_DTO dto ? dto : null;
		} catch (Exception e) {
			return null;
		}
	}

	private void updateHoaDon(HoaDon_DTO dto) {
		Request request = Request.builder().commandType(CommandType.HOADON_UPDATE).data(dto).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore, caller handles state
			}
		}
	}

	private void updateDonDatBan(DonDatBan_DTO dto) {
		Request request = Request.builder().commandType(CommandType.DONDATBAN_UPDATE).data(dto).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore, caller handles state
			}
		}
	}

	private void updateBan(Ban_DTO dto) {
		Request request = Request.builder().commandType(CommandType.BAN_UPDATE).data(dto).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore, caller handles state
			}
		}
	}
}
