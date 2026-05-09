package controller.DatBan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.KhachHang_DTO;
import dto.LoaiBan_DTO;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ThayDoiBanTruoc_Controller implements Initializable {

	@FXML
	private Button btnBan;
	@FXML
	private Button btnTroLai;
	@FXML
	private Button btnXacNhan;
	@FXML
	private ComboBox<String> cmbGioBatDau;
	@FXML
	private ComboBox<String> cmbTrangThai;
	@FXML
	private ComboBox<String> cmbLoaiBan;
	@FXML
	private DatePicker dpNgayDatBan;
	@FXML
	private GridPane gridPaneBan;

	public static DonDatBan_DTO donDatBanDuocChon;
	private List<Ban_DTO> danhSachBan = new ArrayList<>();
	private List<Ban_DTO> danhSachBanDangChon = new ArrayList<>();
	private List<Button> danhSachButtonDangChonUI = new ArrayList<>();
	private Client client;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		client = Client.tryCreate();
		ganGridPaneRongTheoScroll();
		damBaoTenLoaiChoBanTrongDon();
		cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
		cmbTrangThai.setValue("Tất cả");
		cmbTrangThai.setOnAction(e -> loadDanhSachBan());
		dpNgayDatBan.setValue(LocalDate.now().plusDays(1));
		loadGioBatDau();
		khoiTaoLoaiBan();
		loadDanhSachBan();
		if (donDatBanDuocChon != null) {
			loadThongTinBan(donDatBanDuocChon.getBan(), donDatBanDuocChon);
		}

		if (donDatBanDuocChon != null) {
			danhDauBanDangDat(donDatBanDuocChon.getBan());
		}


		btnTroLai.setOnAction(event -> onTroLai(event));
		dpNgayDatBan.valueProperty().addListener((obs, oldV, newV) -> loadDanhSachBan());
		cmbGioBatDau.valueProperty().addListener((obs, oldV, newV) -> loadDanhSachBan());

		btnTroLai.setTooltip(new Tooltip("Thông báo cho nút Trở lại!"));
		cmbLoaiBan.setTooltip(new Tooltip("Lọc theo loại bàn!"));
		cmbGioBatDau.setTooltip(new Tooltip("Lọc theo giờ bắt đầu!"));
		cmbTrangThai.setTooltip(new Tooltip("Lọc theo trạng thái của bàn!"));
		dpNgayDatBan.setTooltip(new Tooltip("Lọc theo ngày đặt bàn!"));
		btnXacNhan.setTooltip(new Tooltip("Thông báo cho nút Xác nhận!"));

	}

	private void ganGridPaneRongTheoScroll() {
		Parent p = gridPaneBan.getParent();
		while (p != null && !(p instanceof ScrollPane)) {
			p = p.getParent();
		}
		if (p instanceof ScrollPane sp) {
			gridPaneBan.prefWidthProperty().bind(Bindings.createDoubleBinding(
					() -> Math.max(400, sp.getViewportBounds().getWidth() - 16),
					sp.viewportBoundsProperty()));
		}
	}

	/** Đơn từ server có thể thiếu {@link Ban_DTO#getTenLoaiBan()} — đồng bộ từ danh sách bàn. */
	private void damBaoTenLoaiChoBanTrongDon() {
		if (donDatBanDuocChon == null || donDatBanDuocChon.getBan() == null) {
			return;
		}
		Ban_DTO b = donDatBanDuocChon.getBan();
		if (b.getTenLoaiBan() != null && !b.getTenLoaiBan().isBlank()) {
			return;
		}
		String ma = b.getMaBan();
		if (ma == null) {
			return;
		}
		for (Ban_DTO x : getAllBan()) {
			if (ma.equals(x.getMaBan())) {
				b.setTenLoaiBan(x.getTenLoaiBan());
				b.setMaLoaiBan(x.getMaLoaiBan());
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void khoiTaoLoaiBan() {
		cmbLoaiBan.getItems().clear();
		cmbLoaiBan.getItems().add("Tất cả");

		try {
			Request request = Request.builder().commandType(CommandType.LOAIBAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			List<LoaiBan_DTO> dsLoaiBan = data instanceof List<?> raw ? (List<LoaiBan_DTO>) raw : List.of();
			for (LoaiBan_DTO lb : dsLoaiBan) {
				cmbLoaiBan.getItems().add(lb.getTenLoaiBan());
			}
		} catch (Exception e) {
			// giữ "Tất cả" nếu lỗi kết nối/server
		}
		cmbLoaiBan.setValue("Tất cả");

		cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
	}

	private void loadGioBatDau() {
		cmbGioBatDau.getItems().clear();
		for (int hour = 8; hour <= 23; hour++) {
			for (int minute = 0; minute < 60; minute += 5) {
				cmbGioBatDau.getItems().add(String.format("%02d:%02d", hour, minute));
			}
		}
		cmbGioBatDau.setValue("08:00");
	}

	@FXML
	void btnDatBan(ActionEvent event) {
		if (danhSachBanDangChon.isEmpty()) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước khi đổi!");
			return;
		}
		Ban_DTO banMoi = danhSachBanDangChon.get(0);
		LocalDate ngayDat = dpNgayDatBan.getValue();
		if (ngayDat == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đổi bàn!");
			return;
		}
		if (!ngayDat.isAfter(LocalDate.now())) {
			showAlert(Alert.AlertType.ERROR, "Ngày đổi bàn phải lớn hơn ngày hiện tại!");
			return;
		}
		String gioDat = cmbGioBatDau.getValue();
		if (gioDat == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn giờ đổi bàn!");
			return;
		}
		try {
			DonDatBan_DTO don = donDatBanDuocChon;
			Ban_DTO banCu = don.getBan();
			banCu.setTrangThai("Trống");
			updateBan(banCu);

			String trangThaiDon = don.getTrangThai();
			if ("Đã nhận bàn".equalsIgnoreCase(trangThaiDon) || "Đang phục vụ".equalsIgnoreCase(trangThaiDon)) {
				banMoi.setTrangThai("Đang phục vụ");
			} else {
				banMoi.setTrangThai("Đã được đặt");
			}

			updateBan(banMoi);

//			int hour = Integer.parseInt(gioDat.substring(0, 2));

			String[] timeParts = gioDat.split(":");
			int hour = Integer.parseInt(timeParts[0]);
			int minute = Integer.parseInt(timeParts[1]);
			don.setBan(banMoi);
			don.setNgayGioLapDon(ngayDat.atTime(hour, minute));
			don.setGioBatDau(LocalTime.of(hour, minute));
			don.setTrangThai("Chưa nhận bàn");
			updateDonDatBan(don);
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}

			showAlert(Alert.AlertType.INFORMATION, "Đổi bàn thành công!");
			MenuNV_Controller.instance.readyUI("DatBan/DonDatBan");
			danhSachBanDangChon.clear();
			danhSachButtonDangChonUI.clear();
			danhSachBan = getAllBan();
			loadDanhSachBan();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onTroLai(ActionEvent event) {
		try {
			MenuNV_Controller.instance.readyUI("DatBan/DonDatBan");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDanhSachBan() {
		gridPaneBan.getChildren().clear();
		danhSachBan = getAllBan();
		int col = 0, row = 0;
		final int MAX_COLS = 5;
		String loaiBanChon = cmbLoaiBan.getValue();
		LocalDate ngay = dpNgayDatBan.getValue();
		String gioStr = cmbGioBatDau.getValue();
		LocalTime gio = parseGio(gioStr);

		for (Ban_DTO ban : danhSachBan) {
			String trangThaiThucTe = getTrangThaiThucTe(ban, ngay, gio,false);
			String trangThaiChon = cmbTrangThai.getValue();
			System.out.println(
					ban.getMaBan() +
							" | trạng thái thực tế = " + trangThaiThucTe +
							" | trạng thái chọn = " + trangThaiChon
			);
			if (!"Tất cả".equals(trangThaiChon) && !trangThaiThucTe.equals(trangThaiChon))
				continue;
			// Lọc theo loại bàn
			boolean matchLoai = "Tất cả".equals(loaiBanChon) || (ban.getTenLoaiBan() != null && ban.getTenLoaiBan().equals(loaiBanChon));

			if (!matchLoai)
				continue;

			Button btnBan = taoNutBan(ban);
			gridPaneBan.add(btnBan, col, row);
			col++;
			if (col >= MAX_COLS) {
				col = 0;
				row++;
			}
		}
		if (donDatBanDuocChon != null) {
			danhDauBanDangDat(donDatBanDuocChon.getBan());
		}
	}

	private Button taoNutBan(Ban_DTO ban) {
		Button btnBan = new Button(ban.getMaBan() + "\n(" + ban.getTenLoaiBan() + ")");
		btnBan.setPrefSize(170, 110);
		LocalDate ngay = dpNgayDatBan.getValue();
		String gioStr = cmbGioBatDau.getValue();
		LocalTime gio = parseGio(gioStr);
		String trangThai = getTrangThaiThucTe(ban, ngay, gio,false);
		btnBan.setStyle(getStyleByStatusAndType(trangThai, ban.getTenLoaiBan()));

		btnBan.setOnMouseClicked(event -> {
			String trangThaiThucTe = getTrangThaiThucTe(ban, dpNgayDatBan.getValue(),
					parseGio(cmbGioBatDau.getValue()),false);
			if (event.getClickCount() == 1) {
				handleChonBan(ban, btnBan);
			} else if (event.getClickCount() == 2) {
				if (trangThaiThucTe.equals("Trống")) {
					moFormThongTinKhachHang();
				} else {
					showAlert(Alert.AlertType.WARNING, "Chỉ có thể mở thông tin khách hàng khi bàn đang TRỐNG!");
				}
			}
		});
		GridPane.setMargin(btnBan, new Insets(5.0));
		return btnBan;
	}

	private void handleChonBan(Ban_DTO ban, Button btnBan) {
		String trangThai = getTrangThaiThucTe(ban, dpNgayDatBan.getValue(), parseGio(cmbGioBatDau.getValue()),false);
		if (!"Trống".equals(trangThai)) {
			showAlert(Alert.AlertType.ERROR, "Chỉ được đổi sang bàn TRỐNG!");
			return;
		}

		if (!danhSachBanDangChon.isEmpty()) {
			Ban_DTO banCu = danhSachBanDangChon.get(0);
			Button btnCu = danhSachButtonDangChonUI.get(0);

			String trangThaiCu = getTrangThaiThucTe(banCu, dpNgayDatBan.getValue(), parseGio(cmbGioBatDau.getValue()),true);
			btnCu.setText(banCu.getMaBan() + "\n(" + banCu.getTenLoaiBan() + ")");

			btnCu.setStyle(getStyleByStatusAndType(trangThaiCu, banCu.getTenLoaiBan()));
			danhSachBanDangChon.clear();
			danhSachButtonDangChonUI.clear();
		}

		danhSachBanDangChon.add(ban);
		danhSachButtonDangChonUI.add(btnBan);

		btnBan.setText("✔ " + ban.getMaBan() + "\n(" + ban.getTenLoaiBan() + ")");
		btnBan.setStyle("""
		-fx-background-color: #ffeb3b;
		-fx-text-fill: black;
		-fx-font-weight: bold;
		-fx-background-radius: 15;
	""");
	}

	private void danhDauBanDangDat(Ban_DTO banDaDat) {
		danhSachBanDangChon.clear();
		danhSachButtonDangChonUI.clear();
		for (javafx.scene.Node node : gridPaneBan.getChildren()) {
			if (node instanceof Button btn) {
				String[] parts = btn.getText().replace("✔", "").split("\n");
				String ma = parts[0].trim();

				if (ma.equals(banDaDat.getMaBan())) {
					danhSachBanDangChon.add(banDaDat);
					danhSachButtonDangChonUI.add(btn);
					btn.setText("✔ " + banDaDat.getMaBan() + "\n(" + banDaDat.getTenLoaiBan() + ")");
					btn.setStyle("""
                    -fx-background-color: #ffeb3b;
                    -fx-text-fill: black;
                    -fx-font-weight: bold;
                    -fx-background-radius: 15;
                """);
					break;
				}
			}
		}
	}

//	private void filterBanTheoTrangThai() {
//		String trangThaiChon = cmbTrangThai.getValue();
//		gridPaneBan.getChildren().clear();
//
//		int col = 0, row = 0;
//		final int MAX_COLS = 5;
//		LocalDate ngay = dpNgayDatBan.getValue();
//		String gioStr = cmbGioBatDau.getValue();
//		LocalTime gio = parseGio(gioStr);
//
//		for (Ban_DTO ban : danhSachBan) {
//			String trangThai = getTrangThaiThucTe(ban, ngay, gio);
//			if (!trangThaiChon.equals("Tất cả") && !trangThai.equals(trangThaiChon))
//				continue;
//			Button btnBan = taoNutBan(ban);
//			gridPaneBan.add(btnBan, col, row);
//			col++;
//			if (col >= MAX_COLS) {
//				col = 0;
//				row++;
//			}
//		}
//	}

	private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {
		String backgroundColor = "#ffffff";

		switch (trangThai) {
			case "Đã được đặt":
				backgroundColor = "#ff0000";
				break;
			case "Trống":
				backgroundColor = "#00aa00";
				break;
			case "Đang phục vụ":
				backgroundColor = "#ec9407";
				break;
			default:
				backgroundColor = "#808080";
				break;
		}

		return String.format("""
		-fx-background-color: %s;
		-fx-background-radius: 15;
		-fx-padding: 10;
		-fx-font-size: 18px;
		-fx-font-weight: bold;
		-fx-text-fill: white;
		-fx-font-family: 'Times New Roman';
		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);
	""", backgroundColor);
	}

	private void loadThongTinBan(Ban_DTO ban, DonDatBan_DTO don) {
		try {
			if (don.getNgayGioLapDon() != null) {
				dpNgayDatBan.setValue(don.getNgayGioLapDon().toLocalDate());
			}
			if (don.getGioBatDau() != null) {
				String gio = String.format("%02d:00", don.getGioBatDau().getHour());
				cmbGioBatDau.setValue(gio);
			}
			cmbLoaiBan.setValue("Tất cả");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void moFormThongTinKhachHang() {
		if (donDatBanDuocChon == null) {
			showAlert(Alert.AlertType.WARNING, "Không tìm thấy thông tin đơn đặt bàn!");
			return;
		}

		KhachHang_DTO kh = null;

		Alert dialog = new Alert(Alert.AlertType.NONE);
		dialog.setTitle("Thông tin khách hàng");
		dialog.setHeaderText("Thông tin khách hàng");
		TextField txtSDT = new TextField(kh != null ? kh.getSdt() : "");
		TextField txtTenKH = new TextField(kh != null ? kh.getTenKH() : "");
		txtSDT.textProperty().addListener((obs, oldV, newV) -> {
			if (newV.matches("\\d{9,11}")) {
				KhachHang_DTO khTim = timKhachHangTheoSdt(newV);
				if (khTim != null) {
					txtTenKH.setText(khTim.getTenKH());
				} else {
					txtTenKH.clear();
				}
			} else {
				txtTenKH.clear();
			}
		});
		DatePicker dpNgayDat = new DatePicker(
				donDatBanDuocChon.getNgayGioLapDon() != null ? donDatBanDuocChon.getNgayGioLapDon().toLocalDate()
						: LocalDate.now());
		ComboBox<String> cmbGioDat = new ComboBox<>();
		for (int hour = 8; hour <= 23; hour++) {
			for (int minute = 0; minute < 60; minute += 5) {
				cmbGioDat.getItems().add(String.format("%02d:%02d", hour, minute));
			}
		}
		if (donDatBanDuocChon.getGioBatDau() != null) {
			cmbGioDat.setValue(String.format("%02d:00", donDatBanDuocChon.getGioBatDau().getHour()));
		} else {
			cmbGioDat.setValue("08:00");
		}
		TextField txtSoLuong = new TextField(String.valueOf(donDatBanDuocChon.getSoLuong()));
		txtSoLuong.textProperty().addListener((obs, oldV, newV) -> {
			if (!newV.matches("\\d*")) {
				showAlert(Alert.AlertType.ERROR, "Số lượng khách phải là số!");
				txtSoLuong.setText(oldV);
				return;
			}

			if (!newV.isEmpty()) {
				int sl = Integer.parseInt(newV);
				if (sl <= 0) {
					showAlert(Alert.AlertType.ERROR, "Số lượng khách phải lớn hơn 0!");
				}
			}
		});
		GridPane pane = new GridPane();
		pane.setHgap(20);
		pane.setVgap(20);
		pane.setPadding(new Insets(15));

		pane.addRow(0, new javafx.scene.control.Label("Số điện thoại:"), txtSDT);
		pane.addRow(1, new javafx.scene.control.Label("Tên khách hàng:"), txtTenKH);
		pane.addRow(2, new javafx.scene.control.Label("Ngày đặt:"), dpNgayDat);
		pane.addRow(3, new javafx.scene.control.Label("Giờ đặt:"), cmbGioDat);
		pane.addRow(4, new javafx.scene.control.Label("Số lượng:"), txtSoLuong);

		dialog.getDialogPane().setContent(pane);
		dialog.getDialogPane().getButtonTypes().clear();
		dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK,
				javafx.scene.control.ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(bt -> {
			if (bt == javafx.scene.control.ButtonType.OK) {
				try {
					KhachHang_DTO khUpdate = timKhachHangTheoSdt(txtSDT.getText());

					if (txtSDT.getText().isEmpty() || txtTenKH.getText().isEmpty()) {
						showAlert(Alert.AlertType.WARNING, "Không được để trống SĐT hoặc Tên khách hàng!");
						return;
					}
					LocalDate ngayDat = dpNgayDat.getValue();
					if (ngayDat == null) {
						showAlert(Alert.AlertType.WARNING, "Ngày đặt không hợp lệ!");
						return;
					}
					String gioStr = cmbGioDat.getValue();
					String[] timeParts = gioStr.split(":");
					int hour = Integer.parseInt(timeParts[0]);
					int minute = Integer.parseInt(timeParts[1]);
					LocalTime gioDat = LocalTime.of(hour, minute);
					int soLuong = Integer.parseInt(txtSoLuong.getText());

					if (khUpdate != null) {
						khUpdate.setSdt(txtSDT.getText());
						khUpdate.setTenKH(txtTenKH.getText());
						// cập nhật khách hàng thực hiện phía server/service
					}

					donDatBanDuocChon.setNgayGioLapDon(ngayDat.atTime(gioDat));
					donDatBanDuocChon.setGioBatDau(gioDat);
					donDatBanDuocChon.setSoLuong(soLuong);
					updateDonDatBan(donDatBanDuocChon);

					showAlert(Alert.AlertType.INFORMATION, "Cập nhật thông tin khách hàng thành công!");
				} catch (Exception e) {
					e.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Lỗi khi cập nhật dữ liệu!");
				}
			}
		});
	}

	private String getTrangThaiThucTe(Ban_DTO ban, LocalDate ngay, LocalTime gio,boolean boQuaDonHienTai) {
		if (ngay == null || gio == null)
			return "Trống";
		List<DonDatBan_DTO> dsDon = getDonByBan(ban);
		if (dsDon == null || dsDon.isEmpty())
			return "Trống";
		for (DonDatBan_DTO don : dsDon) {
			if (don == null)
				continue;
			if (don.getNgayGioLapDon() == null || don.getGioBatDau() == null)
				continue;
			if (boQuaDonHienTai && donDatBanDuocChon != null && don.getMaDatBan().equals(donDatBanDuocChon.getMaDatBan())) {
				continue;
			}
			LocalDate ngayDat = don.getNgayGioLapDon().toLocalDate();
			if (!ngayDat.equals(ngay)) {
				continue;
			}
			LocalTime gioBatDau = don.getGioBatDau();
			LocalTime gioKetThuc = gioBatDau.plusHours(2);
			boolean trungGio = !gio.isBefore(gioBatDau) && gio.isBefore(gioKetThuc);

			if (!trungGio)
				continue;
			String trangThai = don.getTrangThai();
			if ("Đang phục vụ".equalsIgnoreCase(trangThai) || "Đã nhận bàn".equalsIgnoreCase(trangThai)) {
				return "Đang phục vụ";
			}
			if ("Chưa nhận bàn".equalsIgnoreCase(trangThai)) {
				return "Đã được đặt";
			}
		}
		return "Trống";
	}

	private LocalTime parseGio(String gioStr) {
		if (gioStr == null)
			return null;
		String[] parts = gioStr.split(":");
		return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}

	private void showAlert(Alert.AlertType type, String msg) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	@SuppressWarnings("unchecked")
	private List<Ban_DTO> getAllBan() {
		try {
			Request request = Request.builder().commandType(CommandType.BAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			return data instanceof List<?> raw ? (List<Ban_DTO>) raw : List.of();
		} catch (Exception e) {
			return List.of();
		}
	}

	@SuppressWarnings("unchecked")
	private List<DonDatBan_DTO> getDonByBan(Ban_DTO ban) {
		try {
			Request request = Request.builder().commandType(CommandType.DONDATBAN_GET_BY_BAN).data(ban).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			return data instanceof List<?> raw ? (List<DonDatBan_DTO>) raw : List.of();
		} catch (Exception e) {
			return List.of();
		}
	}

	@SuppressWarnings("unchecked")
	private KhachHang_DTO timKhachHangTheoSdt(String sdt) {
		try {
			Request request = Request.builder().commandType(CommandType.KHACHHANG_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> raw)) {
				return null;
			}
			List<KhachHang_DTO> ds = (List<KhachHang_DTO>) raw;
			return ds.stream().filter(kh -> kh != null && sdt.equals(kh.getSdt())).findFirst().orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	private void updateDonDatBan(DonDatBan_DTO dto) {
		Request request = Request.builder().commandType(CommandType.DONDATBAN_UPDATE).data(dto).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore, caller handles UI state
			}
		}
	}

	private void updateBan(Ban_DTO dto) {
		Request request = Request.builder().commandType(CommandType.BAN_UPDATE).data(dto).build();
		if (client != null) {
			try {
				client.send(request);
			} catch (Exception e) {
				// ignore, caller handles UI state
			}
		}
	}
}
