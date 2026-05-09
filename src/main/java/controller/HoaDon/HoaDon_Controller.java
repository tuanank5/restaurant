package controller.HoaDon;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.HoaDon_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class HoaDon_Controller implements Initializable {

	@FXML
	private DatePicker AfterDay;

	@FXML
	private DatePicker BeforDay;

	@FXML
	private ComboBox<String> cmbLoc;

	@FXML
	private TableColumn<HoaDon_DTO, String> colKH;

	@FXML
	private TableColumn<HoaDon_DTO, String> colKM;

	@FXML
	private TableColumn<HoaDon_DTO, String> colKieuThanhToan;

	@FXML
	private TableColumn<HoaDon_DTO, String> colMaHD;

	@FXML
	private TableColumn<HoaDon_DTO, String> colNV;

	@FXML
	private TableColumn<HoaDon_DTO, String> colNgayLap;

	@FXML
	private TableColumn<HoaDon_DTO, String> colTongTienThu;

	@FXML
	private TableColumn<HoaDon_DTO, String> colTrangThai;

	@FXML
	private TableView<HoaDon_DTO> tableView;

	@FXML
	private TextField txtTimKiem;
	private ObservableList<HoaDon_DTO> danhSachHoaDon = FXCollections.observableArrayList();
	private ObservableList<HoaDon_DTO> danhSachHoaDonDB = FXCollections.observableArrayList();
	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cauHinhCot();
		cauHinhLoc();
		client = Client.tryCreate();
		loadData();
		xuLyTimKiem();
		tableView.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				HoaDon_DTO hd = tableView.getSelectionModel().getSelectedItem();
				if (hd != null) {
					MenuNV_Controller.instance.readyUI("HoaDon/ChiTiet");
				}
			}
		});
		BeforDay.setTooltip(new Tooltip("Lọc hoá đơn từ ngày!"));
		AfterDay.setTooltip(new Tooltip("Lọc hoá đơn đến ngày!"));
		cmbLoc.setTooltip(new Tooltip("Lọc theo trạng thái hoá đơn!"));
		txtTimKiem.setTooltip(new Tooltip("Tìm kiếm hoá đơn theo tên Khách hàng!"));
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		List<HoaDon_DTO> list = List.of();
		try {
			Request request = Request.builder().commandType(CommandType.HOADON_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			if (data instanceof List<?> rawList) {
				list = (List<HoaDon_DTO>) rawList;
			}
		} catch (Exception e) {
			list = List.of();
		}
		List<HoaDon_DTO> hdHT = FXCollections.observableArrayList();
		for (HoaDon_DTO hd : list) {
			if (!hd.getKieuThanhToan().equals("Chưa thanh toán")) {
				hdHT.add(hd);
			}
		}

		danhSachHoaDonDB.setAll(hdHT);
		danhSachHoaDon.setAll(hdHT);
		tableView.setItems(danhSachHoaDon);
	}

	private void cauHinhCot() {
		colMaHD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaHD()));

		colNgayLap.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getNgayLap() != null ? data.getValue().getNgayLap().toString() : ""));

		colKH.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getMaKhachHang() != null ? data.getValue().getMaKhachHang() : ""));

		colNV.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getMaNhanVien() != null ? data.getValue().getMaNhanVien() : ""));

		colKM.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getMaKhuyenMai() != null ? data.getValue().getMaKhuyenMai() : "Không có"));

		colTrangThai.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getTrangThai() != null ? data.getValue().getTrangThai() : ""));

		colKieuThanhToan.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue().getKieuThanhToan() != null ? data.getValue().getKieuThanhToan() : ""));

		colTongTienThu.setCellValueFactory(
				data -> new SimpleStringProperty(String.format("%,.0f", data.getValue().getTongTien())));
	}

	private void cauHinhLoc() {
		cmbLoc.setItems(FXCollections.observableArrayList("Tất cả", "Tiền mặt", "Chuyển khoản"));
		cmbLoc.setValue("Tất cả");
		cmbLoc.setOnAction(e -> locDuLieu());
		AfterDay.setOnAction(e -> locDuLieu());
		BeforDay.setOnAction(e -> locDuLieu());
	}

	private void locDuLieu() {
		danhSachHoaDon.setAll(danhSachHoaDonDB.stream().filter(this::locTheoNgay).filter(this::locTheoTrangThai)
				.collect(java.util.stream.Collectors.toList()));
	}

	private boolean locTheoNgay(HoaDon_DTO hd) {
		if (hd.getNgayLap() == null)
			return false;
		LocalDate from = AfterDay.getValue();
		LocalDate to = BeforDay.getValue();
		LocalDate ngayLap = hd.getNgayLap().toLocalDate();

		if (from == null && to == null)
			return true;

		if (from != null && to == null)
			return !ngayLap.isBefore(from);

		if (from == null && to != null)
			return !ngayLap.isAfter(to);

		if (from.isAfter(to)) {
			LocalDate temp = from;
			from = to;
			to = temp;
		}
		return !ngayLap.isBefore(from) && !ngayLap.isAfter(to);
	}

	private boolean locTheoTrangThai(HoaDon_DTO hd) {
		String loc = cmbLoc.getValue();
		if (loc == null || loc.equals("Tất cả"))
			return true;

		if (loc.equalsIgnoreCase("Tiền mặt") || loc.equalsIgnoreCase("Chuyển khoản")) {
			if (hd.getKieuThanhToan() == null)
				return false;
			return hd.getKieuThanhToan().equalsIgnoreCase(loc);
		}

		if (loc.equalsIgnoreCase("Chưa thanh toán")) {
			if (hd.getTrangThai() == null)
				return false;
			return hd.getTrangThai().equalsIgnoreCase("Chưa thanh toán");
		}
		return true;
	}

	private void xuLyTimKiem() {
		txtTimKiem.textProperty().addListener((obs, oldText, newText) -> {
			String keyword = newText.toLowerCase().trim();

			danhSachHoaDon.setAll(danhSachHoaDonDB.stream()
					.filter(hd -> hd.getMaHD().toLowerCase().contains(keyword)
							|| (hd.getMaKhachHang() != null && hd.getMaKhachHang().toLowerCase().contains(keyword)))
					.filter(this::locTheoNgay).filter(this::locTheoTrangThai)
					.collect(java.util.stream.Collectors.toList()));
		});
	}

}
