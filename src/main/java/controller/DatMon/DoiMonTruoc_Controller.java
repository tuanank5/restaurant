package controller.DatMon;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.MonAn_DTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class DoiMonTruoc_Controller implements Initializable {

	@FXML
	private TextField txtTim;
	@FXML
	private Button btnTroLai;
	@FXML
	private Button btnXacNhan;
	@FXML
	private ComboBox<String> comBoxPhanLoai;
	@FXML
	private ScrollPane scrollPaneMon;
	@FXML
	private GridPane gridPaneMon;
	@FXML
	private TableView<ChiTietHoaDon_DTO> tblMonCu;
	@FXML
	private TableColumn<ChiTietHoaDon_DTO, Integer> colSTT;
	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> colTenMonCu;
	@FXML
	private TableColumn<ChiTietHoaDon_DTO, Integer> colSoLuongCu;
	@FXML
	private TableColumn<ChiTietHoaDon_DTO, Double> colDonGia;

	private ObservableList<ChiTietHoaDon_DTO> danhSachMon = FXCollections.observableArrayList();
	private List<MonAn_DTO> dsMonAn = List.of();
	private Map<MonAn_DTO, Integer> dsMonAnDat = new LinkedHashMap<>();

	public static Ban_DTO banChonStatic;
	public static DonDatBan_DTO donDatBanDuocChon;

	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = Client.tryCreate();
		dsMonAn = getAllMonAn();
		khoiTaoComboBoxPhanLoai();

		if (dsMonAn != null && !dsMonAn.isEmpty()) {
			loadMonAnToGrid(dsMonAn);
		}

		txtTim.textProperty().addListener((obs, o, n) -> timMonTheoTen());

		tblMonCu.setItems(danhSachMon);

		colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tblMonCu.getItems().indexOf(col.getValue()) + 1));

		colTenMonCu.setCellValueFactory(c -> {
			ChiTietHoaDon_DTO ct = c.getValue();
			return new ReadOnlyObjectWrapper<>(ct.getTenMon() == null ? "" : ct.getTenMon());
		});

		colSoLuongCu.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getSoLuong()));

		colDonGia.setCellValueFactory(c ->
				new ReadOnlyObjectWrapper<>(c.getValue().getDonGia())
		);

		colDonGia.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : dinhDangTien(item));
			}
		});

		javafx.application.Platform.runLater(() -> {
			loadMonCu();
		});

		btnTroLai.setTooltip(new Tooltip("Thông báo cho nút Trở lại!"));
		btnXacNhan.setTooltip(new Tooltip("Thông báo cho nút Xác nhận!"));
		txtTim.setTooltip(new Tooltip("Nhập tên món ăn để tìm!"));
		comBoxPhanLoai.setTooltip(new Tooltip("Lọc để tìm món ăn!"));
	}

	// CHỌN MÓN
	private void chonMon(MonAn_DTO mon) {
		if (dsMonAnDat.containsKey(mon)) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Món đã chọn");
			alert.setHeaderText(mon.getTenMon());
			alert.setContentText("Bạn muốn thao tác gì?");

			ButtonType btnTang = new ButtonType("➕ Tăng");
			ButtonType btnGiam = new ButtonType("➖ Giảm");
			ButtonType btnXoa = new ButtonType("❌ Xoá");
			ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(btnTang, btnGiam, btnXoa, btnHuy);

			Button btnTangNode = (Button) alert.getDialogPane().lookupButton(btnTang);
			Button btnGiamNode = (Button) alert.getDialogPane().lookupButton(btnGiam);
			Button btnXoaNode = (Button) alert.getDialogPane().lookupButton(btnXoa);

			btnTangNode.addEventFilter(ActionEvent.ACTION, e -> {
				dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
				hienThiMonMoi();
				e.consume();
			});

			btnGiamNode.addEventFilter(ActionEvent.ACTION, e -> {
				int sl = dsMonAnDat.get(mon) - 1;
				if (sl <= 0) {
					dsMonAnDat.remove(mon);
					hienThiMonMoi();
					alert.close();
				} else {
					dsMonAnDat.put(mon, sl);
					hienThiMonMoi();
					e.consume();
				}
			});

			btnXoaNode.addEventHandler(ActionEvent.ACTION, e -> {
				dsMonAnDat.remove(mon);
				hienThiMonMoi();
			});
			alert.show();
		} else {
			dsMonAnDat.put(mon, 1);
			hienThiMonMoi();
		}
	}

	// HIỂN THỊ MÓN MỚI
	private void hienThiMonMoi() {
		danhSachMon.clear();
		for (Map.Entry<MonAn_DTO, Integer> e : dsMonAnDat.entrySet()) {
			MonAn_DTO mon = e.getKey();
			int soLuong = e.getValue();
			ChiTietHoaDon_DTO ct = ChiTietHoaDon_DTO.builder()
					.maMonAn(mon != null ? mon.getMaMon() : null)
					.tenMon(mon != null ? mon.getTenMon() : null)
					.donGia(mon != null ? mon.getDonGia() : 0.0)
					.soLuong(soLuong)
					.thanhTien(mon != null ? mon.getDonGia() * soLuong : 0.0)
					.build();
			danhSachMon.add(ct);
		}
		tblMonCu.refresh();
	}

	// Lấy món cũ lên
	private void loadMonCu() {
		if (donDatBanDuocChon == null)
			return;
		if (donDatBanDuocChon == null)
			return;
		HoaDon_DTO hd = getHoaDonTheoDatBan(donDatBanDuocChon.getMaDatBan());

		if (hd == null)
			return;
		List<ChiTietHoaDon_DTO> dsCTHD = getChiTietByHoaDon(hd.getMaHD());

		dsMonAnDat.clear();
		danhSachMon.clear();

		for (ChiTietHoaDon_DTO ct : dsCTHD) {
			MonAn_DTO mon = timMonTheoMa(ct.getMaMonAn());
			if (mon != null) {
				dsMonAnDat.put(mon, ct.getSoLuong());
			}
		}
		hienThiMonMoi();
	}

	// TÌM MÓN
	private void timMonTheoTen() {
		String tuKhoa = txtTim.getText().trim().toLowerCase();
		List<MonAn_DTO> dsLoc = new ArrayList<>();

		if (tuKhoa.isEmpty())
			dsLoc = dsMonAn;
		else {
			for (MonAn_DTO mon : dsMonAn) {
				if (mon.getTenMon().toLowerCase().contains(tuKhoa))
					dsLoc.add(mon);
			}
		}

		loadMonAnToGrid(dsLoc);
	}

	// LỌC LOẠI MÓN
	private void locMonTheoLoai() {
		String loaiChon = comBoxPhanLoai.getValue();
		List<MonAn_DTO> dsLoc = new ArrayList<>();

		if (loaiChon.equals("Tất cả"))
			dsLoc = dsMonAn;
		else {
			for (MonAn_DTO mon : dsMonAn) {
				if (loaiChon.equals(mon.getLoaiMon()))
					dsLoc.add(mon);
			}
		}

		loadMonAnToGrid(dsLoc);
	}

	private void khoiTaoComboBoxPhanLoai() {
		List<String> dsLoai = new ArrayList<>();
		dsLoai.add("Tất cả");
		for (MonAn_DTO m : dsMonAn) {
			if (!dsLoai.contains(m.getLoaiMon()))
				dsLoai.add(m.getLoaiMon());
		}
		comBoxPhanLoai.getItems().setAll(dsLoai);
		comBoxPhanLoai.setOnAction(e -> locMonTheoLoai());
	}

	// LOAD GRID MÓN ĂN
	private void loadMonAnToGrid(List<MonAn_DTO> danhSach) {
		gridPaneMon.getChildren().clear();
		gridPaneMon.getColumnConstraints().clear();
		gridPaneMon.getRowConstraints().clear();

		int columns = 4;
		int col = 0, row = 0;

		for (int i = 0; i < columns; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0 / columns);
			gridPaneMon.getColumnConstraints().add(cc);
		}

		int totalRows = (int) Math.ceil(danhSach.size() / (double) columns);
		for (int i = 0; i < totalRows; i++) {
			gridPaneMon.getRowConstraints().add(new RowConstraints(220));
		}

		for (MonAn_DTO mon : danhSach) {
			ImageView img = new ImageView();
			try {
				img.setImage(new Image("file:" + mon.getDuongDanAnh()));
			} catch (Exception ex) {
			}

			img.setFitWidth(120);
			img.setFitHeight(120);

			Label lblTen = new Label(mon.getTenMon());
			lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
			Label lblGia = new Label(dinhDangTien(mon.getDonGia()));

			Button btnChon = new Button("Chọn");
			btnChon.setOnAction(e -> chonMon(mon));

			VBox box = new VBox(img, lblTen, lblGia, btnChon);
			box.setSpacing(6);
			box.setStyle("-fx-alignment:center; -fx-border-color:#ccc; -fx-background-color:white;");

			gridPaneMon.add(box, col, row);
			col++;

			if (col == columns) {
				col = 0;
				row++;
			}
		}
	}

	private String dinhDangTien(double soTien) {
		return String.format("%,.0f", soTien);
	}

	@FXML
	private void hanhDongTroLai(ActionEvent e) {
		MenuNV_Controller.getInstance().readyUI("DatBan/DonDatBan");
	}

	@FXML
	private void hanhDongXacNhan(ActionEvent e) {
		if (donDatBanDuocChon == null) {
			alert("Lỗi", "Chưa chọn đơn đặt bàn!");
			return;
		}

		System.out.println("Đơn đặt bàn: " + donDatBanDuocChon.getMaDatBan());
		HoaDon_DTO hd1 = getHoaDonTheoDatBan(donDatBanDuocChon.getMaDatBan());

		System.out.println("HoaDon: " + hd1);
		HoaDon_DTO hd = getHoaDonTheoDatBan(donDatBanDuocChon.getMaDatBan());

		if (hd == null) {
			alert("Lỗi", "Không tìm thấy hóa đơn!");
			return;
		}

		try {
			// Xóa món cũ
			deleteChiTietByHoaDon(hd.getMaHD());
			// Ghi món mới
			for (Map.Entry<MonAn_DTO, Integer> eMon : dsMonAnDat.entrySet()) {
				MonAn_DTO mon = eMon.getKey();
				int sl = eMon.getValue();
				ChiTietHoaDon_DTO ct = ChiTietHoaDon_DTO.builder().maHoaDon(hd.getMaHD())
						.maMonAn(mon == null ? null : mon.getMaMon()).soLuong(sl)
						.thanhTien((mon == null ? 0 : mon.getDonGia()) * sl).build();
				addChiTiet(ct);
			}
			alert("Thành công", "Đổi món thành công!");
			MenuNV_Controller.getInstance().readyUI("DatBan/DonDatBan");
		} catch (Exception ex) {
			alert("Lỗi", "Không lưu được: " + ex.getMessage());
		}
	}

	private void alert(String t, String m) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle(t);
		a.setHeaderText(null);
		a.setContentText(m);
		a.showAndWait();
	}

	@SuppressWarnings("unchecked")
	private List<MonAn_DTO> getAllMonAn() {
		try {
			Request request = Request.builder().commandType(CommandType.MONAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			return data instanceof List<?> raw ? (List<MonAn_DTO>) raw : List.of();
		} catch (Exception e) {
			return List.of();
		}
	}

	private MonAn_DTO timMonTheoMa(String maMon) {
		return dsMonAn.stream().filter(m -> m != null && maMon != null && maMon.equals(m.getMaMon())).findFirst().orElse(null);
	}

	private double timDonGia(String maMon) {
		MonAn_DTO mon = timMonTheoMa(maMon);
		return mon == null ? 0.0 : mon.getDonGia();
	}

	private HoaDon_DTO getHoaDonTheoDatBan(String maDatBan) {
		try {
			Request request = Request.builder().commandType(CommandType.HOADON_GET_BY_MADATBAN).data(maDatBan).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.getData() instanceof HoaDon_DTO dto ? dto : null;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<ChiTietHoaDon_DTO> getChiTietByHoaDon(String maHoaDon) {
		try {
			Request request = Request.builder().commandType(CommandType.CTHD_GET_BY_MAHD).data(maHoaDon).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			return data instanceof List<?> raw ? (List<ChiTietHoaDon_DTO>) raw : List.of();
		} catch (Exception e) {
			return List.of();
		}
	}

	private void deleteChiTietByHoaDon(String maHoaDon) {
		Request request = Request.builder().commandType(CommandType.CTHD_DELETE_BY_MAHD).data(maHoaDon).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore to keep UI responsive
			}
		}
	}

	private void addChiTiet(ChiTietHoaDon_DTO dto) {
		Request request = Request.builder().commandType(CommandType.CTHD_ADD_BATCH).data(List.of(dto)).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore to keep UI responsive
			}
		}
	}

}
