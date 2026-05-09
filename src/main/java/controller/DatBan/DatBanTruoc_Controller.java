package controller.DatBan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.DatMon.DatMonTruoc_Controller;
import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import dto.LoaiBan_DTO;
import entity.Ban;
import entity.KhachHang;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class DatBanTruoc_Controller implements Initializable {

	@FXML
	private ComboBox<String> cmbGioBatDau;
	@FXML
	private ComboBox<String> cmbLoaiBan;
	@FXML
	private ComboBox<String> cmbTrangThai;
	@FXML
	private DatePicker dpNgayDatBan;
	@FXML
	private GridPane gridPaneBan;
	@FXML
	private Button btnTroLai;

	private List<Ban_DTO> danhSachBan = new ArrayList<>();
	private List<Ban_DTO> danhSachBanDangChon = new ArrayList<>();
	private List<Button> danhSachButtonDangChonUI = new ArrayList<>();
	private KhachHang_DTO khachHangDaChon;
	private int slKhach = 1;
	private Client client;

	public static DonDatBan_DTO donDatBanDuocChon;

	public static LocalDate ngayDatBanStatic;
	public static String gioBatDauStatic;
	private String loaiBanChon = "Tất cả";

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		client = Client.tryCreate();
		dpNgayDatBan.setValue(LocalDate.now());
		khoiTaoLoaiBan();
		loadGioBatDau();
		loadDanhSachBan();
		cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
		cmbTrangThai.setValue("Tất cả");
		cmbTrangThai.setOnAction(e -> filterBanTheoTrangThai());
		dpNgayDatBan.setDayCellFactory(datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (date.isBefore(LocalDate.now())) {
					setDisable(true);
					setStyle("-fx-background-color: #ffc0cb;");
				}
			}
		});
		btnTroLai.setOnAction(event -> onTroLai(event));
		dpNgayDatBan.valueProperty().addListener((obs, oldVal, newVal) -> loadDanhSachBan());
		cmbGioBatDau.setOnAction(e -> loadDanhSachBan());

		cmbLoaiBan.setTooltip(new Tooltip("Hiển thị danh sách theo loại bàn"));
		dpNgayDatBan.setTooltip(new Tooltip("Hiển thị danh sách theo ngày đặt bàn"));
		cmbGioBatDau.setTooltip(new Tooltip("Hiển thị danh sách theo giờ sẽ đến"));
		cmbTrangThai.setTooltip(new Tooltip("Hiển thị danh sách theo trạng thái"));
		btnTroLai.setTooltip(new Tooltip("Quay lại danh sách đơn đặt bàn"));
	}

	private void khoiTaoLoaiBan() {
		cmbLoaiBan.getItems().clear();
		cmbLoaiBan.getItems().add("Tất cả");

		List<LoaiBan_DTO> dsLoaiBan = getAllLoaiBan();
		for (LoaiBan_DTO lb : dsLoaiBan) {
			cmbLoaiBan.getItems().add(lb.getTenLoaiBan());
		}
		cmbLoaiBan.setValue("Tất cả");

		cmbLoaiBan.setOnAction(e -> {
			loaiBanChon = cmbLoaiBan.getValue();
			loadDanhSachBan();
		});

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

	// LOAD DANH SÁCH BÀN
	private void loadDanhSachBan() {
		gridPaneBan.getChildren().clear();
		danhSachBan = getAllBan();
		int col = 0, row = 0;
		final int MAX_COLS = 5;
		LocalDate ngayChon = dpNgayDatBan.getValue();
		String gioStr = cmbGioBatDau.getValue();
		LocalTime gioChon = (gioStr != null ? LocalTime.parse(gioStr) : null);
		for (Ban_DTO ban : danhSachBan) {
			if (loaiBanChon != null && !loaiBanChon.equals("Tất cả")) {
				if (!loaiBanChon.equalsIgnoreCase(getTenLoaiBan(ban.getTenLoaiBan()))) {
					continue;
				}
			}

			boolean banBan = (ngayChon != null && gioChon != null) && isBanDangBan(ban, ngayChon, gioChon);
			String bgColor = banBan ? "#ff0000" : "#00aa00"; // đỏ / xanh
			String trangThai = getTrangThaiThucTe(ban, ngayChon, gioChon);
			switch (trangThai) {
			case "Đang phục vụ":
				bgColor = "#ec9407"; // cam
				break;
			case "Đã được đặt":
				bgColor = "#ff0000"; // đỏ
				break;
			default:
				bgColor = "#00aa00"; // xanh
			}
			Button btn = new Button(ban.getMaBan() + "\n(" + getTenLoaiBan(ban.getTenLoaiBan()) + ")");
			btn.setPrefSize(170, 110);
			btn.setStyle(buildStyle(bgColor));
			final boolean isDangPhucVu = "Đang phục vụ".equals(trangThai);
			btn.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					// Double click -> mở form nhập thông tin khách hàng
					hienThiFormThongTinKhachHang(ban);
					return;
				}
				if (isDangPhucVu) {
					showAlert(Alert.AlertType.WARNING, "Bàn này đang phục vụ khách.\nKhông thể chọn!");
					return;
				}
				if (banBan) {
					showAlert(Alert.AlertType.WARNING, "Bàn này đã được đặt trước.\nKhông thể chọn!");
					return;
				}
				handleChonBan(ban, btn);
			});

			GridPane.setMargin(btn, new Insets(5));
			gridPaneBan.add(btn, col, row);
			col++;
			if (col >= MAX_COLS) {
				col = 0;
				row++;
			}
		}
	}

	// KIỂM TRA BÀN ĐÃ ĐƯỢC ĐẶT TRONG NGÀY — GIỜ ĐẶT KHÔNG
	private boolean isBanDangBan(Ban_DTO ban, LocalDate ngayChon, LocalTime gioChon) {
		List<DonDatBan_DTO> dsDon = getDonByBan(ban);
		if (dsDon == null)
			return false;
		LocalDateTime tDat = LocalDateTime.of(ngayChon, gioChon);
		for (DonDatBan_DTO don : dsDon) {
			LocalDate ngayDon = don.getNgayGioLapDon().toLocalDate();
			if (!ngayDon.equals(ngayChon))
				continue;
			LocalTime gioDon = don.getGioBatDau();
			LocalDateTime tDaDat = LocalDateTime.of(ngayChon, gioDon);
			LocalDateTime tBatDauChan = tDaDat.minusHours(1);
			LocalDateTime tDaKetThuc = tDaDat.plusHours(1);
			if (!tDat.isBefore(tBatDauChan) && !tDat.isAfter(tDaKetThuc)) {
				return true;
			}
		}
		return false;
	}

	private void handleChonBan(Ban_DTO ban, Button btnBan) {
		if (!danhSachBanDangChon.isEmpty() && !danhSachBanDangChon.contains(ban)) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chỉ được chọn 1 bàn.\nHãy bỏ chọn bàn hiện tại!");
			alert.showAndWait();
			return;
		}

		if (danhSachBanDangChon.contains(ban)) {
			danhSachBanDangChon.clear();
			danhSachButtonDangChonUI.clear();
			btnBan.setStyle(buildStyle("#00aa00"));
			btnBan.setText(ban.getMaBan() + "\n(" + ban.getTenLoaiBan() + ")");
		} else {
			danhSachBanDangChon.clear();
			danhSachButtonDangChonUI.clear();
			danhSachBanDangChon.add(ban);
			danhSachButtonDangChonUI.add(btnBan);
			btnBan.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");
			btnBan.setText("✔ " + ban.getMaBan() + "\n(" + ban.getTenLoaiBan() + ")");
		}
	}

	private String buildStyle(String color) {
		return String.format(
				"-fx-background-color: %s;" + "-fx-background-radius: 15;" + "-fx-padding: 10;" + "-fx-font-size: 18px;"
						+ "-fx-font-weight: bold;" + "-fx-text-fill: white;" + "-fx-font-family: 'Times New Roman';"
						+ "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);",
				color);
	}

	// LỌC BÀN THEO TRẠNG THÁI
	private void filterBanTheoTrangThai() {
		String trangThaiChon = cmbTrangThai.getValue();
		gridPaneBan.getChildren().clear();

		int col = 0, row = 0;
		final int MAX_COLS = 5;
		LocalDate ngay = dpNgayDatBan.getValue();
		String gioStr = cmbGioBatDau.getValue();
		LocalTime gio = gioStr != null ? LocalTime.parse(gioStr) : null;

		for (Ban_DTO ban : danhSachBan) {
			String trangThai = getTrangThaiThucTe(ban, ngay, gio);
			if (!trangThaiChon.equals("Tất cả") && !trangThai.equals(trangThaiChon))
				continue;
			Button btnBan = taoNutBan(ban);
			gridPaneBan.add(btnBan, col, row);
			col++;
			if (col >= MAX_COLS) {
				col = 0;
				row++;
			}
		}
	}

	private Button taoNutBan(Ban_DTO ban) {
		LocalDate ngayChon = dpNgayDatBan.getValue();
		String gioStr = cmbGioBatDau.getValue();
		LocalTime gioChon = gioStr != null ? LocalTime.parse(gioStr) : null;

		boolean banBan = (ngayChon != null && gioChon != null) && isBanDangBan(ban, ngayChon, gioChon);

		String bgColor = banBan ? "#ff0000" : "#00aa00"; // đỏ / xanh
		String trangThai = getTrangThaiThucTe(ban, ngayChon, gioChon);
		switch (trangThai) {
		case "Đang phục vụ":
			bgColor = "#ec9407"; // cam
			break;
		case "Đã được đặt":
			bgColor = "#ff0000"; // đỏ
			break;
		default:
			bgColor = "#00aa00"; // xanh
		}

		Button btn = new Button(ban.getMaBan() + "\n(" + ban.getTenLoaiBan() + ")");
		btn.setPrefSize(170, 110);
		btn.setStyle(buildStyle(bgColor));

		final boolean isBanBanFinal = banBan;

		btn.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				hienThiFormThongTinKhachHang(ban);
				return;
			}
			if (isBanBanFinal) {
				showAlert(Alert.AlertType.WARNING, "Bàn này đang được đặt trong 2 giờ.\nKhông thể chọn!");
				return;
			}
			handleChonBan(ban, btn);
		});

		GridPane.setMargin(btn, new Insets(5));

		return btn;
	}

	private String getTrangThaiThucTe(Ban_DTO ban, LocalDate ngay, LocalTime gio) {
		if (ngay == null || gio == null)
			return "Trống";

		List<DonDatBan_DTO> dsDon = getDonByBan(ban);
		if (dsDon == null || dsDon.isEmpty())
			return "Trống";

		for (DonDatBan_DTO don : dsDon) {
			LocalDate ngayDat = don.getNgayGioLapDon().toLocalDate();
			if (!ngayDat.equals(ngay))
				continue;

			LocalTime gioBatDau = don.getGioBatDau();
			LocalTime gioChan = gioBatDau.minusHours(1);
			LocalTime gioKetThuc = gioBatDau.plusHours(1);
			boolean trongKhoang;
			// xử lý qua ngày
			if (gioKetThuc.isAfter(gioChan)) {
				trongKhoang = !gio.isBefore(gioChan) && !gio.isAfter(gioKetThuc);
			} else {
				trongKhoang = !gio.isBefore(gioChan) || !gio.isAfter(gioKetThuc);
			}
			if (!trongKhoang)
				continue;

			String trangThaiDon = don.getTrangThai();

			if ("Đang phục vụ".equalsIgnoreCase(trangThaiDon) || "Đã nhận bàn".equalsIgnoreCase(trangThaiDon)) {
				return "Đang phục vụ";
			}
			if ("Chưa nhận bàn".equalsIgnoreCase(trangThaiDon)) {
				return "Đã được đặt";
			}
		}
		return "Trống";
	}

	private void hienThiFormThongTinKhachHang(Ban_DTO ban) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Thông tin đặt bàn");
		dialog.setHeaderText("Nhập thông tin khách hàng cho bàn " + ban.getMaBan());

		ButtonType btnXacNhan = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
		ButtonType btnChonMon = new ButtonType("Chọn món ăn", ButtonBar.ButtonData.LEFT);
		dialog.getDialogPane().getButtonTypes().addAll(btnXacNhan, btnChonMon, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(15));

		TextField txtSDT = new TextField();
		TextField txtTenKH = new TextField();
		txtTenKH.setEditable(false);

		TextField txtSoLuong = new TextField();
		DatePicker dpNgay = new DatePicker(dpNgayDatBan.getValue());
		dpNgay.setDayCellFactory(p -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (date.isBefore(LocalDate.now())) {
					setDisable(true);
					setStyle("-fx-background-color: #ffc0cb;");
				}
			}
		});

		ComboBox<String> cmbGio = new ComboBox<>();
		cmbGio.getItems().addAll(cmbGioBatDau.getItems());
		cmbGio.setValue(cmbGioBatDau.getValue());

		txtSDT.textProperty().addListener((obs, oldV, newV) -> {
			if (!newV.matches("\\d*")) {
				showAlert(Alert.AlertType.ERROR, "Số điện thoại chỉ được chứa số!");
				txtSDT.setText(oldV);
				return;
			}
			if (newV.length() > 10) {
				showAlert(Alert.AlertType.ERROR, "SĐT không được vượt quá 10 số!");
				txtSDT.setText(oldV);
				return;
			}
			if (newV.length() == 10) {
				var kh = timKhachHangTheoSdt(newV);
				if (kh == null) {
					showAlert(Alert.AlertType.WARNING, "Không tìm thấy khách hàng!");
					txtTenKH.setText("Không tìm thấy");
					khachHangDaChon = null;
				} else {
					txtTenKH.setText(kh.getTenKH());
					khachHangDaChon = kh;
				}
			} else {
				txtTenKH.setText("");
				khachHangDaChon = null;
			}
		});

		txtSoLuong.textProperty().addListener((obs, oldV, newV) -> {
			try {
				int so = Integer.parseInt(newV.trim());
				int sucChuaBan = getSucChuaBan(ban);
				if (so <= 0) {
					showAlert(Alert.AlertType.ERROR, "Số lượng khách phải > 0");
					txtSoLuong.setText("1");
					return;
				}
				if (sucChuaBan > 0 && so > sucChuaBan) {
					showAlert(Alert.AlertType.ERROR,
							"Số lượng khách vượt quá sức chứa (" + sucChuaBan + ")");
					txtSoLuong.setText(String.valueOf(sucChuaBan));
					return;
				}
				slKhach = so;
			} catch (Exception e) {
				txtSoLuong.setText("1");
			}
		});

		grid.addRow(0, new Label("Số điện thoại:"), txtSDT);
		grid.addRow(1, new Label("Tên khách hàng:"), txtTenKH);
		grid.addRow(2, new Label("Ngày đặt:"), dpNgay);
		grid.addRow(3, new Label("Giờ đặt:"), cmbGio);
		grid.addRow(4, new Label("Số lượng khách:"), txtSoLuong);

		dialog.getDialogPane().setContent(grid);
		dialog.setResultConverter(button -> {

			if (button == btnXacNhan || button == btnChonMon) {
				if (txtSDT.getText().trim().isEmpty()) {
					showAlert(Alert.AlertType.ERROR, "Số điện thoại không được để trống!");
					return null;
				}
				if (khachHangDaChon == null) {
					showAlert(Alert.AlertType.ERROR, "Không tìm thấy khách hàng hợp lệ!");
					return null;
				}
				if (dpNgay.getValue().isBefore(LocalDate.now())) {
					showAlert(Alert.AlertType.ERROR, "Ngày đặt không được nhỏ hơn ngày hiện tại!");
					return null;
				}
				int sucChuaBan = getSucChuaBan(ban);
				if (sucChuaBan > 0 && slKhach > sucChuaBan) {
					showAlert(Alert.AlertType.ERROR, "Số lượng khách vượt quá số lượng ghế của bàn!");
					return null;
				}
			}
			if (button == btnXacNhan) {
				xuLyXacNhanThongTin(ban, txtSDT.getText(), txtTenKH.getText(), dpNgay.getValue(), cmbGio.getValue(),
						txtSoLuong.getText());
			} else if (button == btnChonMon) {
				DatBanTruoc_Controller.ngayDatBanStatic = dpNgay.getValue();
				DatBanTruoc_Controller.gioBatDauStatic = cmbGio.getValue();
				xuLyChonMon(ban);
			}
			return null;
		});
		dialog.showAndWait();
	}

	private void xuLyXacNhanThongTin(Ban_DTO ban, String sdtInput, String tenKHInput, LocalDate ngayInput, String gioInput,
			String soLuongInput) {
		// Kiểm tra bàn đã truyền vào
		if (ban == null) {
			showAlert(Alert.AlertType.ERROR, "Chưa chọn bàn.");
			return;
		}
		// Kiểm tra số điện thoại
		String sdt = sdtInput.trim();
		if (sdt.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Vui lòng nhập số điện thoại khách hàng.");
			return;
		}
		// Tìm khách hàng theo SĐT
		KhachHang_DTO kh = null;
		try {
			kh = timKhachHangTheoSdt(sdt);
		} catch (Exception ex) {
			kh = null;
		}
		if (kh == null) {
			showAlert(Alert.AlertType.ERROR, "Không tìm thấy khách hàng theo số điện thoại.");
			return;
		}
		this.khachHangDaChon = kh;
		int soLuongKH = Integer.parseInt(soLuongInput.trim());
		this.slKhach = soLuongKH;
		try {
			soLuongKH = Integer.parseInt(soLuongInput.trim());
			if (soLuongKH <= 0)
				soLuongKH = 1;
		} catch (Exception e) {
			soLuongKH = 1;
		}

		List<DonDatBan_DTO> danhSachDatBanDaTao = new ArrayList<>();

		// TẠO ĐƠN ĐẶT BÀN
		try {
			DonDatBan_DTO ddb = DonDatBan_DTO.builder().maDatBan(util.AutoIDUitl.sinhMaDonDatBan()).build();
			// Ngày – giờ đặt (ưu tiên static của màn trước)
			LocalDate ngay = DatBanTruoc_Controller.ngayDatBanStatic != null ? DatBanTruoc_Controller.ngayDatBanStatic
					: ngayInput;
			String gio = DatBanTruoc_Controller.gioBatDauStatic != null ? DatBanTruoc_Controller.gioBatDauStatic
					: gioInput;
			LocalTime gioBatDau = LocalTime.parse(gio);
			ddb.setNgayGioLapDon(LocalDateTime.of(ngay, gioBatDau));
			ddb.setGioBatDau(gioBatDau);

			ddb.setSoLuong(soLuongKH);
			ddb.setBan(ban);
			ddb.setTrangThai("Chưa nhận bàn");

			boolean ok = addDonDatBan(ddb);
			if (!ok) {
				showAlert(Alert.AlertType.ERROR, "Không thể lưu đơn đặt bàn cho bàn " + ban.getMaBan());
				return;
			}

			danhSachDatBanDaTao.add(ddb);

		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Lỗi khi tạo đơn đặt bàn.");
			return;
		}

		// --- Cập nhật trạng thái bàn ---
		try {
			ban.setTrangThai("Đã được đặt");
			updateBan(ban);
		} catch (Exception e) {
			showAlert(Alert.AlertType.ERROR, "Không cập nhật được trạng thái bàn.");
			return;
		}
		// Tạo hóa đơn
		if (danhSachDatBanDaTao.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Không có đơn đặt bàn để tạo hóa đơn.");
			return;
		}

		DonDatBan_DTO donDau = danhSachDatBanDaTao.get(0);
		try {
			HoaDon_DTO hd = HoaDon_DTO.builder().maHD(util.AutoIDUitl.sinhMaHoaDon())
					.ngayLap(java.sql.Date.valueOf(LocalDate.now())).tongTien(0.0).thue(0.0).trangThai("Đặt trước")
					.kieuThanhToan("Chưa thanh toán").tienNhan(0.0).tienThua(0.0)
					.maKhachHang(kh == null ? null : kh.getMaKH())
					.maNhanVien(MenuNV_Controller.taiKhoan == null || MenuNV_Controller.taiKhoan.getMaNhanVien() == null
							? null
							: MenuNV_Controller.taiKhoan.getMaNhanVien())
					.maDonDatBan(donDau.getMaDatBan()).build();
			boolean okHD = addHoaDon(hd);
			if (!okHD) {
				showAlert(Alert.AlertType.ERROR, "Không thể lưu hóa đơn.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Lỗi khi tạo hóa đơn.");
			return;
		}
		showAlert(Alert.AlertType.INFORMATION, "Đặt bàn thành công!");
		MenuNV_Controller.instance.readyUI("DatBan/DonDatBan");
	}

	private void xuLyChonMon(Ban_DTO ban) {
		try {
			DatMonTruoc_Controller.danhSachBanChonStatic =
					ban == null ? List.of() : List.of(ban);
			if (khachHangDaChon == null) {
				showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại hợp lệ!");
				return;

			}
			KhachHang khEntity = khachHangDaChon == null ? null : util.MapperUtil.map(khachHangDaChon, KhachHang.class);
			DatMonTruoc_Controller.khachHangStatic = khachHangDaChon;
			DatMonTruoc_Controller.soLuongKHStatic = slKhach > 0 ? slKhach : 1;
			MenuNV_Controller.instance.readyUI("MonAn/DatMonTruoc");
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
	private List<LoaiBan_DTO> getAllLoaiBan() {
		try {
			Request request = Request.builder().commandType(CommandType.LOAIBAN_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();
			return data instanceof List<?> raw ? (List<LoaiBan_DTO>) raw : List.of();
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

	private int getSucChuaBan(Ban_DTO ban) {
		if (ban == null || ban.getMaLoaiBan() == null) {
			return -1;
		}
		return getAllLoaiBan().stream().filter(lb -> lb != null && ban.getMaLoaiBan().equals(lb.getMaLoaiBan()))
				.map(LoaiBan_DTO::getSoLuong).findFirst().orElse(-1);
	}

	private boolean addDonDatBan(DonDatBan_DTO ddb) {
		try {
			Request request = Request.builder().commandType(CommandType.DONDATBAN_ADD).data(ddb).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean updateBan(Ban_DTO ban) {
		try {
			Request request = Request.builder().commandType(CommandType.BAN_UPDATE).data(ban).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean addHoaDon(HoaDon_DTO hd) {
		try {
			Request request = Request.builder().commandType(CommandType.HOADON_ADD).data(hd).build();
			Response response = client == null ? null : client.send(request);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}

	private String getTenLoaiBan(String maLoaiBan) {
		return getAllLoaiBan().stream()
				.filter(lb -> lb.getMaLoaiBan().equals(maLoaiBan))
				.map(LoaiBan_DTO::getTenLoaiBan)
				.findFirst()
				.orElse(maLoaiBan);
	}
}
