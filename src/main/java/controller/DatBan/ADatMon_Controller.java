package controller.DatBan;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import dto.MonAn_DTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AutoIDUitl;

public class ADatMon_Controller implements Initializable {

	@FXML
	private Button btnGiam;

	@FXML
	private TextField txtKhachHang;

	@FXML
	private Button btnHuy;

	@FXML
	private Button btnQuayLai;

	@FXML
	private Button btnTang;

	@FXML
	private Button btnXacNhan;

	@FXML
	private ComboBox<String> cmbLoaiMon;

	@FXML
	private TableColumn<MonAn_DTO, Double> colDonGia;

	@FXML
	private TableColumn<MonAn_DTO, Integer> colSTT;

	@FXML
	private TableColumn<MonAn_DTO, Integer> colSoLuong;

	@FXML
	private TableColumn<MonAn_DTO, String> colTenMon;

	@FXML
	private GridPane gridPaneMon;

	@FXML
	private Label lblTongTien;

	@FXML
	private ScrollPane scrollPaneMon;

	@FXML
	private TableView<MonAn_DTO> tblDS;

	@FXML
	private TextField txtMaBan;

	@FXML
	private TextField txtSoLuong;

	// -----Bàn---------
	private Ban_DTO banDangChon;
	private List<MonAn_DTO> dsMonAn = List.of();
	private final Map<MonAn_DTO, Integer> dsMonAnDat = new LinkedHashMap<>();
	private Client client;

	public void setBanDangChon(Ban_DTO ban) {
		this.banDangChon = ban;
		loadMonCuaBan();
	}

	@FXML
	private void controller(ActionEvent event) {

	}

	private boolean checkTTbangKo = ABanHienTai_Controller.aBHT.checkTTbangKo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = Client.tryCreate();
		dsMonAn = getAllMonAn();
		loadTenKhachHang();
		// lấy sluong khách
		if (checkTTbangKo == true) {
			txtSoLuong.setText(MenuNV_Controller.aBanHienTai_HD == null || MenuNV_Controller.aBanHienTai_HD.getDonDatBan() == null
					? "0"
					: String.valueOf(MenuNV_Controller.aBanHienTai_HD.getDonDatBan().getSoLuong()));
		} else {
			txtSoLuong.setText(String.valueOf(MenuNV_Controller.soLuongKhach));
		}
		// Khởi tạo ComboBox phân loại (dùng dsMonAn)
		khoiTaoComboBoxPhanLoai();

		// Load toàn bộ món lên giao diện
		loadMonAnToGrid(dsMonAn);

		colSTT.setCellValueFactory(col -> {
			int index = tblDS.getItems().indexOf(col.getValue());
			return new ReadOnlyObjectWrapper<>(index >= 0 ? index + 1 : 0);
		});
		colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
		colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
		colDonGia.setCellFactory(col -> new TableCell<MonAn_DTO, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(dinhDangTien(item));
				}
			}
		});
		colSoLuong.setCellValueFactory(col -> {
			Integer soLuong = dsMonAnDat.get(col.getValue());
			return new ReadOnlyObjectWrapper<>(soLuong != null ? soLuong : 0);
		});
		tblDS.setItems(FXCollections.observableArrayList());
		// --- Nhận dữ liệu bàn và KH từ MenuNV_Controller ---
		if (checkTTbangKo == true) {
			txtMaBan.setText(MenuNV_Controller.aBanHienTai_HD == null || MenuNV_Controller.aBanHienTai_HD.getDonDatBan() == null
					? ""
					: MenuNV_Controller.aBanHienTai_HD.getDonDatBan().getMaDatBan());
		} else if (MenuNV_Controller.banDangChon != null) {
			this.banDangChon = util.MapperUtil.map(MenuNV_Controller.banDangChon, Ban_DTO.class);
			txtMaBan.setText(banDangChon.getMaBan());
			loadMonCuaBan();
		}
		tblDS.setOnMouseClicked(e -> capNhatTongTien());

		if (checkTTbangKo == false) {
			Tooltip toolTipQL = new Tooltip("Quay lại danh sách bàn");
			btnQuayLai.setTooltip(toolTipQL);

			Tooltip toolTipLM = new Tooltip("Lọc danh sách theo loại món");
			cmbLoaiMon.setTooltip(toolTipLM);

			Tooltip toolTipHuy = new Tooltip("Hủy đặt bàn và đặt món");
			btnHuy.setTooltip(toolTipHuy);

			Tooltip toolTipXN = new Tooltip("Xác nhận đặt bàn và đặt món");
			btnXacNhan.setTooltip(toolTipXN);
		} else {
			Tooltip toolTipQL2 = new Tooltip("Quay lại danh sách bàn");
			btnQuayLai.setTooltip(toolTipQL2);

			Tooltip toolTipHuy2 = new Tooltip("Hủy đặt món");
			btnHuy.setTooltip(toolTipHuy2);

			Tooltip toolTipXN = new Tooltip("Xác nhận đặt món");
			btnXacNhan.setTooltip(toolTipXN);
		}

		btnHuy.setOnAction(e -> quayLaiGD());
	}

	private void quayLaiGD() {
		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	@FXML
	void btnQuayLai(ActionEvent event) {
		if (checkTTbangKo == true) {
			MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
		} else {
			MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");
		}
	}

	public void setKhachHang(KhachHang_DTO kh) {
		if (kh != null) {
			txtKhachHang.setText(kh.getTenKH());
		}
	}

	private void loadTenKhachHang() {
		if (checkTTbangKo == false) {
			txtKhachHang.setText(MenuNV_Controller.khachHangDangChon == null ? "" : MenuNV_Controller.khachHangDangChon.getTenKH());
		} else {
			txtKhachHang.setText(MenuNV_Controller.aBanHienTai_HD == null || MenuNV_Controller.aBanHienTai_HD.getKhachHang() == null
					? ""
					: MenuNV_Controller.aBanHienTai_HD.getKhachHang().getTenKH());
		}
	}

	private void khoiTaoComboBoxPhanLoai() {
		// Lấy danh sách loại món duy nhất
		List<String> danhSachLoai = new ArrayList<>();
		danhSachLoai.add("Tất cả"); // để hiển thị toàn bộ món
		for (MonAn_DTO mon : dsMonAn) {
			String loai = mon.getLoaiMon();
			if (loai != null && !danhSachLoai.contains(loai)) {
				danhSachLoai.add(loai);
			}
		}
		cmbLoaiMon.getItems().setAll(danhSachLoai);

		// Thêm sự kiện chọn
		cmbLoaiMon.setOnAction(e -> locMonTheoLoai());
	}

	private void locMonTheoLoai() {
		String loaiChon = cmbLoaiMon.getValue();
		List<MonAn_DTO> dsLoc = new ArrayList<>();
		if (loaiChon.equals("Tất cả")) {
			dsLoc = dsMonAn;
		} else {
			for (MonAn_DTO mon : dsMonAn) {
				if (loaiChon.equals(mon.getLoaiMon())) {
					dsLoc.add(mon);
				}
			}
		}

		loadMonAnToGrid(dsLoc);
	}

	@FXML
	void handleThanhToan(ActionEvent event) {
		try {
			// Gán dữ liệu cho hóa đơn như bạn đang làm
			MenuNV_Controller.banDangChon = banDangChon == null ? null : util.MapperUtil.map(banDangChon, entity.Ban.class);
			Map<entity.MonAn, Integer> dsMonEntity = new LinkedHashMap<>();
			for (Map.Entry<MonAn_DTO, Integer> e : dsMonAnDat.entrySet()) {
				if (e.getKey() != null) {
					dsMonEntity.put(util.MapperUtil.map(e.getKey(), entity.MonAn.class), e.getValue());
				}
			}
			MenuNV_Controller.dsMonAnDangChon = dsMonEntity;
			if (banDangChon != null) {
				banDangChon.setTrangThai("Trống");
				updateBan(banDangChon);
			}
			// Xóa món ăn đã lưu tạm cho bàn này
			MenuNV_Controller.dsMonTheoBan.remove(banDangChon.getMaBan());
			// Cập nhật giao diện danh sách bàn trong MenuNV
			MenuNV_Controller.instance.refreshBanUI();
			// Mở UI hóa đơn
			MenuNV_Controller.instance.readyUI("HoaDon/ChiTiet");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handleXacNhan(ActionEvent event) {
		if (dsMonAnDat.isEmpty()) {
			showAlert("Thông báo", "Vui lòng chọn món ăn", Alert.AlertType.INFORMATION);
		} else {
			if (checkTTbangKo == true) {
				showAlert("Thông báo", "Lưu hóa đơn tạm thành công!", Alert.AlertType.INFORMATION);
				HoaDon_DTO hoaDonTam = MenuNV_Controller.aBanHienTai_HD == null ? null
						: util.MapperUtil.map(MenuNV_Controller.aBanHienTai_HD, HoaDon_DTO.class);
				themChiTietHoaDon(hoaDonTam, dsMonAnDat);
				MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
			} else {
				DonDatBan_DTO ddb = themDDB();
				themHD(ddb);
			}
		}
	}

	private DonDatBan_DTO themDDB() {
		LocalDateTime nowDate = LocalDateTime.now();
		LocalTime nowHour = LocalTime.now();

		DonDatBan_DTO ddb = DonDatBan_DTO.builder().maDatBan(AutoIDUitl.sinhMaDonDatBan()).ngayGioLapDon(nowDate)
				.soLuong(MenuNV_Controller.soLuongKhach)
				.ban(MenuNV_Controller.banDangChon == null ? null
						: util.MapperUtil.map(MenuNV_Controller.banDangChon, Ban_DTO.class))
				.gioBatDau(nowHour)
				.trangThai("Đã nhận bàn").build();
		Request req = Request.builder().commandType(CommandType.DONDATBAN_ADD).data(ddb).build();
		try {
			Response response = client == null ? null : client.send(req);
			return response != null && response.isSuccess() ? ddb : null;
		} catch (Exception e) {
			return null;
		}
	}

	private void themHD(DonDatBan_DTO ddb) {
		HoaDon_DTO hd = HoaDon_DTO.builder().maHD(AutoIDUitl.sinhMaHoaDon())
				.ngayLap(java.sql.Date.valueOf(LocalDate.now())).trangThai("Hiện tại").kieuThanhToan("Chưa thanh toán")
				.maKhachHang(MenuNV_Controller.khachHangDangChon == null ? null : MenuNV_Controller.khachHangDangChon.getMaKH())
				.maNhanVien(MenuNV_Controller.taiKhoan == null || MenuNV_Controller.taiKhoan.getNhanVien() == null ? null
						: MenuNV_Controller.taiKhoan.getNhanVien().getMaNV())
				.maDonDatBan(ddb == null ? null : ddb.getMaDatBan()).build();
		try {
			Request req = Request.builder().commandType(CommandType.HOADON_ADD).data(hd).build();
			Response response = client == null ? null : client.send(req);
			if (response != null && response.isSuccess()) {
				showAlert("Thông báo", "Lưu hóa đơn tạm thành công!", Alert.AlertType.INFORMATION);
				if (!dsMonAnDat.isEmpty()) {
					themChiTietHoaDon(hd, dsMonAnDat);
					MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
				}
			} else {
				showAlert("Thông báo", "Lưu hóa đơn tạm thất bại!", Alert.AlertType.WARNING);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Lỗi", "ADatMon_Controller lỗi", Alert.AlertType.ERROR);
		}
	}

	private void themChiTietHoaDon(HoaDon_DTO hd, Map<MonAn_DTO, Integer> dsMonAn) {
		if (hd == null || hd.getMaHD() == null) {
			showAlert("Lỗi", "Không tìm thấy hóa đơn để lưu chi tiết món.", Alert.AlertType.ERROR);
			return;
		}
		List<ChiTietHoaDon_DTO> cts = new ArrayList<>();
		for (Map.Entry<MonAn_DTO, Integer> entry : dsMonAn.entrySet()) {
			cts.add(ChiTietHoaDon_DTO.builder().maHoaDon(hd.getMaHD())
					.maMonAn(entry.getKey() == null ? null : entry.getKey().getMaMon()).soLuong(entry.getValue())
					.thanhTien((entry.getKey() == null ? 0 : entry.getKey().getDonGia()) * entry.getValue()).build());
		}
		Request req = Request.builder().commandType(CommandType.CTHD_ADD_BATCH).data(cts).build();
		if (client == null) {
			return;
		}
		try {
			client.send(req);
		} catch (Exception e) {
			showAlert("Lỗi", "Không thể gửi chi tiết hóa đơn lên server.", Alert.AlertType.ERROR);
		}
	}

	private void loadMonAnToGrid(List<MonAn_DTO> danhSach) {
		gridPaneMon.getChildren().clear();
		gridPaneMon.getColumnConstraints().clear();
		gridPaneMon.getRowConstraints().clear();
		gridPaneMon.setHgap(15);
		gridPaneMon.setVgap(30);
		gridPaneMon.setPadding(new Insets(15));

		scrollPaneMon.setFitToWidth(true);
		gridPaneMon.prefWidthProperty().bind(scrollPaneMon.widthProperty());

		int columns = 4;
		int col = 0;
		int row = 0;

		// Chia đều cột
		for (int i = 0; i < columns; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0 / columns);
			gridPaneMon.getColumnConstraints().add(cc);
		}

		// TÍNH SỐ ROW THEO DANH SÁCH HIỆN TẠI
		int totalRows = (int) Math.ceil(danhSach.size() / (double) columns);
		for (int i = 0; i < totalRows; i++) {
			RowConstraints rc = new RowConstraints();
			rc.setPrefHeight(220);
			rc.setVgrow(Priority.NEVER);
			gridPaneMon.getRowConstraints().add(rc);
		}

		// HIỂN THỊ MÓN ĂN ĐÚNG DANH SÁCH ĐANG LOAD
		for (MonAn_DTO mon : danhSach) {
			ImageView img = new ImageView();
			String path = mon.getDuongDanAnh();
			if (path != null && !path.isEmpty()) {
				try {
					img.setImage(new Image("file:" + path));
				} catch (Exception e) {
					System.out.println("Không load được ảnh: " + path);
				}
			}
			img.setFitWidth(120);
			img.setFitHeight(120);
			img.setPreserveRatio(true);

			Label lblTen = new Label(mon.getTenMon());
			lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

			Label lblGia = new Label(dinhDangTien(mon.getDonGia()));

			Button btnChon = new Button("Chọn");
			btnChon.setOnAction(e -> chonMon(mon));

			VBox box = new VBox(img, lblTen, lblGia, btnChon);
			box.setSpacing(6);
			box.setPadding(new Insets(8));
			box.setPrefWidth(150);
			box.setStyle("-fx-border-color: #CFCFCF; -fx-background-color:#FFFFFF; "
					+ "-fx-alignment:center; -fx-border-radius:10; -fx-background-radius:10;");
			gridPaneMon.add(box, col, row);
			col++;
			if (col == columns) {
				col = 0;
				row++;
			}
		}

		gridPaneMon.requestLayout();
	}

	private void loadMonCuaBan() {
		if (banDangChon == null)
			return;

		// Nếu bàn đã từng đặt món → lấy lại
		if (MenuNV_Controller.dsMonTheoBan.containsKey(banDangChon.getMaBan())) {
			dsMonAnDat.clear();
			Map<entity.MonAn, Integer> dsMonEntity = MenuNV_Controller.dsMonTheoBan.get(banDangChon.getMaBan());
			if (dsMonEntity != null) {
				for (Map.Entry<entity.MonAn, Integer> e : dsMonEntity.entrySet()) {
					if (e.getKey() != null) {
						dsMonAnDat.put(util.MapperUtil.map(e.getKey(), MonAn_DTO.class), e.getValue());
					}
				}
			}
		}

		// Cập nhật bảng
		tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
		tblDS.refresh();
		capNhatTongTien();
	}

	private void chonMon(MonAn_DTO mon) {
		if (dsMonAnDat.containsKey(mon)) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Món đã chọn");
			alert.setHeaderText("Món '" + mon.getTenMon() + "' đã có trong danh sách.");
			alert.setContentText("Bạn muốn làm gì?");

			ButtonType btnTang = new ButtonType("➕ Tăng số lượng");
			ButtonType btnGiam = new ButtonType("➖ Giảm số lượng");
			ButtonType btnXoa = new ButtonType("❌ Xóa món");
			ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(btnTang, btnGiam, btnXoa, btnHuy);

			Button btnTangNode = (Button) alert.getDialogPane().lookupButton(btnTang);
			Button btnGiamNode = (Button) alert.getDialogPane().lookupButton(btnGiam);
			Button btnXoaNode = (Button) alert.getDialogPane().lookupButton(btnXoa);

			btnTangNode.addEventFilter(ActionEvent.ACTION, e -> {
				dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
				tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
				tblDS.refresh();
				e.consume();
			});

			btnGiamNode.addEventFilter(ActionEvent.ACTION, e -> {
				int soLuong = dsMonAnDat.get(mon) - 1;
				if (soLuong <= 0) {
					dsMonAnDat.remove(mon);
					alert.close();
				} else {
					dsMonAnDat.put(mon, soLuong);
				}
				tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
				tblDS.refresh();
				e.consume();
			});

			btnXoaNode.addEventHandler(ActionEvent.ACTION, e -> {
				dsMonAnDat.remove(mon);
				tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
				tblDS.refresh();
			});
			alert.show();
		} else {
			// Thêm món mới
			dsMonAnDat.put(mon, 1);
		}
		// Cập nhật bảng
		tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
		tblDS.refresh();
		capNhatTongTien();
	}

	private void capNhatTongTien() {
		double tongTruocVAT = dsMonAnDat.entrySet().stream().mapToDouble(e -> e.getKey().getDonGia() * e.getValue())
				.sum();
		lblTongTien.setText(dinhDangTien(tongTruocVAT));
	}

	private String dinhDangTien(double soTien) {
		NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
		return nf.format(soTien);
	}

	private void showAlert(String title, String content, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.show();
	}

	@SuppressWarnings("unchecked")
	private List<MonAn_DTO> getAllMonAn() {
		try {
			Request req = Request.builder().commandType(CommandType.MONAN_GET_ALL).build();
			Response response = client == null ? null : client.send(req);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> rawList)) {
				return List.of();
			}
			return (List<MonAn_DTO>) rawList;
		} catch (Exception e) {
			return List.of();
		}
	}

	private boolean updateBan(Ban_DTO ban) {
		try {
			Request req = Request.builder().commandType(CommandType.BAN_UPDATE).data(ban).build();
			Response response = client == null ? null : client.send(req);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}
}
