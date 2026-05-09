package controller.DatBan;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.MonAn_DTO;
import entity.Ban;
import entity.MonAn;
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

public class ADoiMon_Controller implements Initializable {

	@FXML
	private Button btnQuayLai;

	@FXML
	private Button btnXacNhan;

	@FXML
	private ComboBox<String> cmbLoaiMon;

	@FXML
	private TableColumn<MonAn, Double> colDonGia;

	@FXML
	private TableColumn<MonAn, Integer> colSTT;

	@FXML
	private TableColumn<MonAn, Integer> colSoLuong;

	@FXML
	private TableColumn<MonAn, Integer> colSoLuong1;

	@FXML
	private TableColumn<MonAn, String> colTenMon;

	@FXML
	private GridPane gridPaneMon;

	@FXML
	private Label lblTongTien;

	@FXML
	private ScrollPane scrollPaneMon;

	@FXML
	private TableView<MonAn> tblDS;

	@FXML
	private TextField txtKhachHang;

	@FXML
	private TextField txtMaBan;

	@FXML
	private TextField txtSoLuongKhach;

	// -----Bàn---------
	private Ban banDangChon;

	private Client client;
	private List<MonAn> dsMonAn;
	private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
	private DonDatBan_DTO donDatBanHienTai;
	private HoaDon_DTO hoaDonHienTai;

	@FXML
	void btnQuayLai(ActionEvent event) {
		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	@FXML
	void btnXacNhan(ActionEvent event) {
		if (hoaDonHienTai == null) {
			showAlert("Lỗi", "Không tìm thấy hóa đơn!", Alert.AlertType.ERROR);
			return;
		}

		try {
			List<ChiTietHoaDon_DTO> items = new ArrayList<>();
			for (Map.Entry<MonAn, Integer> entry : dsMonAnDat.entrySet()) {
				items.add(ChiTietHoaDon_DTO.builder()
						.maHoaDon(hoaDonHienTai.getMaHD())
						.maMonAn(entry.getKey().getMaMon())
						.soLuong(entry.getValue())
						.thanhTien(entry.getKey().getDonGia() * entry.getValue())
						.build());
			}
			Map<String, Object> payload = new HashMap<>();
			payload.put("maHD", hoaDonHienTai.getMaHD());
			payload.put("items", items);

			Response res = client.send(new Request(CommandType.CTHD_REPLACE_BY_MAHD, payload));
			if (res == null || !res.isSuccess()) {
				showAlert("Lỗi", "Không thể cập nhật món ăn!", Alert.AlertType.ERROR);
				return;
			}
			updateTongTienHoaDon();
		} catch (Exception ex) {
			ex.printStackTrace();
			showAlert("Lỗi", "Không thể cập nhật món ăn!", Alert.AlertType.ERROR);
			return;
		}

		showAlert("Thành công", "Cập nhật món ăn thành công!", Alert.AlertType.INFORMATION);

		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
		hoaDonHienTai = MenuNV_Controller.aBanHienTai_HD;
		donDatBanHienTai = MenuNV_Controller.donDatBanDangDoi;
		dsMonAn = loadMonAnFromServer();
		// Khởi tạo ComboBox phân loại (dùng dsMonAn)
		khoiTaoComboBoxPhanLoai();
		loadThongTinKhachVaSoLuong();
		loadMonAnToGrid(dsMonAn);
		colSTT.setCellValueFactory(col -> {
			int index = tblDS.getItems().indexOf(col.getValue());
			return new ReadOnlyObjectWrapper<>(index >= 0 ? index + 1 : 0);
		});
		colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
		colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
		colDonGia.setCellFactory(col -> new TableCell<MonAn, Double>() {
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
		if (MenuNV_Controller.banDangChon != null) {
			this.banDangChon = MenuNV_Controller.banDangChon;
			txtMaBan.setText(banDangChon.getMaBan());
			loadMonCuaBan();
		}
		tblDS.setOnMouseClicked(e -> capNhatTongTien());
		this.donDatBanHienTai = MenuNV_Controller.donDatBanDangDoi;
		loadMonDaDatTuHoaDon();

		Tooltip toolTipQL = new Tooltip("Quay lại danh sách bàn");
		btnQuayLai.setTooltip(toolTipQL);

		Tooltip toolTipLM = new Tooltip("Lọc danh sách theo loại món");
		cmbLoaiMon.setTooltip(toolTipLM);

		Tooltip toolTipXN = new Tooltip("Xác nhận đổi món ăn");
		btnXacNhan.setTooltip(toolTipXN);
	}

	public void setBanDangChon(Ban ban) {
		this.banDangChon = ban;
		loadMonCuaBan();
	}

	private void khoiTaoComboBoxPhanLoai() {
		// Lấy danh sách loại món duy nhất
		List<String> danhSachLoai = new ArrayList<>();
		danhSachLoai.add("Tất cả"); // để hiển thị toàn bộ món
		for (MonAn mon : dsMonAn) {
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

		// FIX NULL
		if (loaiChon == null || loaiChon.equals("Tất cả")) {
			loadMonAnToGrid(dsMonAn);
			return;
		}

		List<MonAn> dsLoc = new ArrayList<>();
		for (MonAn mon : dsMonAn) {
			if (loaiChon.equals(mon.getLoaiMon())) {
				dsLoc.add(mon);
			}
		}

		loadMonAnToGrid(dsLoc);
	}

	private void loadMonAnToGrid(List<MonAn> danhSach) {
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
		for (MonAn mon : danhSach) {
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
			dsMonAnDat = MenuNV_Controller.dsMonTheoBan.get(banDangChon.getMaBan());
		}

		// Cập nhật bảng
		tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
		tblDS.refresh();
		capNhatTongTien();
	}

	private void chonMon(MonAn mon) {
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

	private void loadMonDaDatTuHoaDon() {
		if (hoaDonHienTai == null) {
			System.out.println("❌ hoaDonHienTai = null");
			return;
		}

		dsMonAnDat.clear();

		try {
			Response res = client.send(new Request(CommandType.CTHD_GET_BY_MAHD, hoaDonHienTai.getMaHD()));
			if (res != null && res.isSuccess()) {
				@SuppressWarnings("unchecked")
				List<ChiTietHoaDon_DTO> dsCT = (List<ChiTietHoaDon_DTO>) res.getData();
				for (ChiTietHoaDon_DTO ct : dsCT) {
					MonAn mon = findMonById(ct.getMaMonAn());
					if (mon != null) dsMonAnDat.put(mon, ct.getSoLuong());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
		tblDS.refresh();
		capNhatTongTien();
	}

	public void setDonDatBanHienTai(DonDatBan_DTO don) {
		this.donDatBanHienTai = don;
		loadMonDaDatTuHoaDon();
	}

	private void showAlert(String title, String content, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.show();
	}

	private void loadThongTinKhachVaSoLuong() {
		if (donDatBanHienTai == null) {
			txtKhachHang.setText("Khách lẻ");
			txtSoLuongKhach.clear();
			txtMaBan.clear();
			return;
		}

		// ===== TÊN KHÁCH =====
		if (hoaDonHienTai != null && hoaDonHienTai.getKhachHang() != null) {
			txtKhachHang.setText(hoaDonHienTai.getKhachHang().getTenKH());
		} else {
			txtKhachHang.setText("Khách lẻ");
		}

		// ===== SỐ LƯỢNG KHÁCH =====
		if (donDatBanHienTai != null) {
			txtSoLuongKhach.setText(String.valueOf(donDatBanHienTai.getSoLuong()));
		}

		// ===== MÃ BÀN =====
		if (donDatBanHienTai.getBan() != null) {
			txtMaBan.setText(donDatBanHienTai.getBan().getMaBan());
		} else {
			txtMaBan.clear();
		}
	}

	private void updateTongTienHoaDon() {
		if (hoaDonHienTai == null) return;
		double tongTienMoi = dsMonAnDat.entrySet()
				.stream()
				.mapToDouble(e -> e.getKey().getDonGia() * e.getValue())
				.sum();
		hoaDonHienTai.setTongTien(tongTienMoi);
		try {
			client.send(new Request(CommandType.HOADON_UPDATE, hoaDonHienTai));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<MonAn> loadMonAnFromServer() {
		List<MonAn> result = new ArrayList<>();
		try {
			Response res = client.send(new Request(CommandType.MONAN_GET_ALL, null));
			if (res != null && res.isSuccess()) {
				List<MonAn_DTO> list = (List<MonAn_DTO>) res.getData();
				for (MonAn_DTO dto : list) {
					result.add(new MonAn(
							dto.getMaMon(),
							dto.getTenMon(),
							dto.getDonGia(),
							dto.getDuongDanAnh(),
							dto.getLoaiMon()
					));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private MonAn findMonById(String maMon) {
		for (MonAn mon : dsMonAn) {
			if (mon.getMaMon().equals(maMon)) return mon;
		}
		return null;
	}
}
