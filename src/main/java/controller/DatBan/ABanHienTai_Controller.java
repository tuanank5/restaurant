package controller.DatBan;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.net.URL;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ABanHienTai_Controller {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnBan;

	@FXML
	private Button btnChoThanhToan;

	@FXML
	private Button btnDatTruoc;

	@FXML
	private Button btnDatBan;

	@FXML
	private Button btnKhac;

	@FXML
	private Button btnMonAn;

	@FXML
	private Button btnThanhToan;

	@FXML
	private ComboBox<String> cmbLoaiBan;

	@FXML
	private GridPane gridPaneHD;

	@FXML
	private BorderPane paneBan;

	@FXML
	private BorderPane paneEast;

	@FXML
	private BorderPane paneNorth;

	@FXML
	private BorderPane paneTien;

	@FXML
	private BorderPane paneWest;

	@FXML
	private TextField txtTongDatBan;

	private final Ban_DAO banDAO = new Ban_DAOImpl();
	private final HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
	private final DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
	private final ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAOImpl();

	// --- Danh sách và trạng thái ---
	private List<HoaDon> dsHoaDon = new ArrayList<>();
	private List<Ban> dsBan = new ArrayList<Ban>();
	private int tongDatBan;

	public static ABanHienTai_Controller aBHT;
	boolean checkTTbangKo;

	@FXML
	private void controller(ActionEvent event) throws IOException {
		Object source = event.getSource();
		if (source == btnDatBan) {
			MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");
		} else if (source == btnChoThanhToan) {
			loadDanhSachHoaDon();
		} else if (source == btnDatTruoc) {

		} else if (source == btnBan) {
			MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
		} else if (source == btnMonAn) {
			MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
		}
	}

	@FXML
	public void initialize() {
		aBHT = this;
		checkTTbangKo = false;
		refreshData();
		capNhatTrangThaiBanTheoDonDatTruoc();
		khoiTaoComboBoxes();
		loadDanhSachHoaDon();
		cmbLoaiBan.setOnAction(e -> loadDanhSachHoaDon());
		txtTongDatBan.setEditable(false);
		txtTongDatBan.setText(tongDatBan + "");

		Tooltip toolTipLB = new Tooltip("Lọc danh sách theo loại bàn");
		cmbLoaiBan.setTooltip(toolTipLB);

		Tooltip toolTipDB = new Tooltip("Đặt bàn mới");
		btnDatBan.setTooltip(toolTipDB);
	}

	private void capNhatTrangThaiBanMacDinh() {
		// Giữ nguyên trạng thái thực tế trong DB, không reset toàn bộ về "Trống".
	}

	private void capNhatTrangThaiBanTheoDonDatTruoc() {
		LocalDate nowDate = LocalDate.now();
		LocalTime nowHour = LocalTime.now();
		List<DonDatBan> dsDDBnow = new ArrayList<DonDatBan>();

		for (HoaDon hd : dsHoaDon) {
			if (hd.getTrangThai().equals("Đặt trước")) {
				LocalDate ngayGioLapDon = hd.getDonDatBan().getNgayGioLapDon().toLocalDate();
				if (ngayGioLapDon.equals(nowDate)) {
					if (nowHour.isAfter(hd.getDonDatBan().getGioBatDau().minusHours(1))
							&& nowHour.isBefore(hd.getDonDatBan().getGioBatDau().plusHours(1))) {
						dsDDBnow.add(hd.getDonDatBan());
					}
				}
			}
		}

		for (DonDatBan ddb : dsDDBnow) {
			for (Ban ban : dsBan) {
				if (ban.getMaBan().equals(ddb.getBan().getMaBan())) {
					ban.setTrangThai("Đã được đặt");
					updateBan(ban);
				}
			}
		}
	}

	private void khoiTaoComboBoxes() {
		cmbLoaiBan.getItems().clear();
		cmbLoaiBan.getItems().add("Tất cả");
		for (Ban ban : dsBan) {
			String tenLoai = ban.getLoaiBan().getTenLoaiBan();
			if (!cmbLoaiBan.getItems().contains(tenLoai)) {
				cmbLoaiBan.getItems().add(tenLoai);
			}
		}
		cmbLoaiBan.getSelectionModel().select("Tất cả");
	}

	private void loadDanhSachHoaDon() {
		refreshData();
		tongDatBan = 0;
		gridPaneHD.getChildren().clear();

		int col = 0, row = 0;
		final int MAX_COLS = 9;

		String loaiBanLoc = cmbLoaiBan.getValue();
		LocalDate localDate = LocalDate.now();
		Date dateNow = Date.valueOf(localDate);

		for (final HoaDon hoaDon : dsHoaDon) {
			boolean matchType = "Tất cả".equals(loaiBanLoc)
					|| loaiBanLoc.equals(hoaDon.getDonDatBan().getBan().getLoaiBan().getTenLoaiBan());

			if (matchType && hoaDon.getNgayLap().equals(dateNow) && hoaDon.getKieuThanhToan().equals("Chưa thanh toán")
					&& hoaDon.getDonDatBan().getTrangThai().equals("Đã nhận bàn")) {

				BorderPane borderPane = new BorderPane();

				// North
				BorderPane paneNorth = new BorderPane();
				borderPane.setTop(paneNorth);
				paneNorth.setCenter(new Label("Hóa đơn"));
				paneNorth.setStyle(
						"-fx-padding: 10; -fx-background-color: lightblue; -fx-font-size: 20px; -fx-font-weight: bold;");

				// West
				BorderPane paneWest = new BorderPane();
				borderPane.setLeft(paneWest);
				paneWest.setStyle(getBorderStyle()); // Thêm viền cho paneWest

				BorderPane paneBan = new BorderPane();
				paneWest.setTop(paneBan);
				paneBan.setCenter(new Label(hoaDon.getDonDatBan().getBan().getMaBan() + "\n"
						+ hoaDon.getDonDatBan().getBan().getLoaiBan().getTenLoaiBan()));
				paneBan.setStyle(getBorderStyle()); // Thêm viền cho paneBan

				Button btnThanhToan = taoButtonIcon("TT", "/img/pay.png");
				paneWest.setLeft(btnThanhToan);
				BorderPane.setMargin(btnThanhToan, new Insets(5, 10, 5, 10));
				btnThanhToan.setOnMouseClicked(event -> {
					double tongTien = tinhTongTienMon(hoaDon);
					if (tongTien > 0) {
						MenuNV_Controller.aBanHienTai_HD = hoaDon;
						MenuNV_Controller.instance.readyUI("DatBan/aThanhToan");
					} else {
						Dialog<ButtonType> dialog = new Dialog<>();
						dialog.setTitle("Thông báo");
						dialog.setHeaderText("Vui lòng đặt món");
						dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
						Optional<ButtonType> result = dialog.showAndWait();

						if (result.isPresent() && result.get() == ButtonType.OK) {
							checkTTbangKo = true;
							MenuNV_Controller.aBanHienTai_HD = hoaDon;
							MenuNV_Controller.instance.readyUI("DatBan/aDatMon");
						}
					}
				});

				Button btnBan = taoButtonIcon("B", "/img/table.png");
				paneWest.setRight(btnBan);
				BorderPane.setMargin(btnBan, new Insets(5, 10, 5, 10));
				btnBan.setOnMouseClicked(event -> {
//                	MenuNV_Controller.aBanHienTai_HD = hoaDon;
					MenuNV_Controller.donDatBanDangDoi = hoaDon.getDonDatBan();
					MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
				});

				// East
				BorderPane paneEast = new BorderPane();
				borderPane.setRight(paneEast);
				paneEast.setStyle(getBorderStyle()); // Thêm viền cho paneEast

				BorderPane paneTien = new BorderPane();
				paneEast.setTop(paneTien);
				double tongTien = tinhTongTienMon(hoaDon);
				Label lblTien = new Label(String.format(" %,.0f VND \n", tongTien));

				paneTien.setCenter(lblTien);

				Button btnMonAn = taoButtonIcon("MA", "/img/food.png");
				paneEast.setLeft(btnMonAn);
				BorderPane.setMargin(btnMonAn, new Insets(5, 10, 5, 10));
				btnMonAn.setOnMouseClicked(event -> {
					MenuNV_Controller.aBanHienTai_HD = hoaDon;
					MenuNV_Controller.donDatBanDangDoi = hoaDon.getDonDatBan();
					MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
				});

				Button btnKhac = taoButtonIcon("K", "/img/cancel.png");
				paneEast.setRight(btnKhac);
				BorderPane.setMargin(btnKhac, new Insets(5, 10, 5, 10));
				btnKhac.setOnMouseClicked(event -> {

					Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
					confirm.setTitle("Xác nhận hủy đơn");
					confirm.setHeaderText(null);
					confirm.setContentText("Khách hàng xác nhận hủy đơn đặt bàn này?");
					Optional<ButtonType> rs = confirm.showAndWait();

					if (rs.isEmpty() || rs.get() != ButtonType.OK)
						return;
					try {
						DonDatBan ddb = hoaDon.getDonDatBan();
						ddb.setTrangThai("Đã hủy");
						updateDonDatBan(ddb);

						if (hoaDon != null) {
							updateHoaDonStatus(hoaDon.getMaHD(), "Đã hủy");
						}

						Ban ban = ddb.getBan();
						if (ban != null) {
							ban.setTrangThai("Trống");
							updateBan(ban);
						}

						KhachHang kh = getKhachHangTheoMaDatBan(ddb.getMaDatBan());

						showAlert(Alert.AlertType.INFORMATION,
								"Đã hủy đơn đặt bàn của khách " + (kh != null ? kh.getTenKH() : "") + " theo yêu cầu.");
						loadDanhSachHoaDon();
					} catch (Exception e) {
						e.printStackTrace();
						showAlert(Alert.AlertType.ERROR, "Không thể hủy đơn đặt bàn!");
					}
				});

				GridPane.setMargin(borderPane, new Insets(5.0));
				gridPaneHD.add(borderPane, col, row);

				tongDatBan += 1;

				col++;
				if (col >= MAX_COLS) {
					col = 0;
					row++;
				}
			}
		}
		txtTongDatBan.setText(tongDatBan + "");
	}

	private void showAlert(Alert.AlertType type, String msg) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	private String getBorderStyle() {
		return "-fx-border-color: gray; " + "-fx-border-width: 2; " + "-fx-background-color: white; "
				+ "-fx-border-radius: 10; " + "-fx-padding: 10;";
	}

	private String getButtonStyle() {
		return "-fx-background-color: lightgreen; " + "-fx-background-radius: 5; " + "-fx-padding: 10; "
				+ "-fx-font-size: 14px; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);";
	}

	private double tinhTongTienMon(HoaDon hoaDon) {
		if (hoaDon == null) return 0;
		if (hoaDon.getTongTien() > 0) return hoaDon.getTongTien();

		List<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.getChiTietTheoMaHoaDon(hoaDon.getMaHD());
		double tong = dsChiTiet.stream()
				.mapToDouble(ct -> ct.getMonAn().getDonGia() * ct.getSoLuong())
				.sum();

		if (tong > 0) {
			hoaDon.setTongTien(tong);
			hoaDonDAO.capNhat(hoaDon);
		}
		return tong;
	}

	private Button taoButtonIcon(String text, String iconPath) {
		Button btn = new Button();
		URL iconUrl = getClass().getResource(iconPath);
		if (iconUrl != null) {
			ImageView icon = new ImageView(iconUrl.toExternalForm());
			icon.setFitWidth(30);
			icon.setFitHeight(30);
			btn.setGraphic(icon);
		} else {
			// Fallback khi ảnh icon chưa có trong resources.
			btn.setText(text);
		}
		btn.setStyle(getButtonStyle());
		btn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
		btn.setGraphicTextGap(5);

		return btn;
	}

	private void refreshData() {
		dsHoaDon = hoaDonDAO.getDanhSach("HoaDon.list", HoaDon.class);
		dsBan = banDAO.getDanhSach("Ban.list", Ban.class);
	}

	private void updateBan(Ban ban) {
		banDAO.capNhat(ban);
	}

	private void updateHoaDonStatus(String maHD, String trangThai) {
		HoaDon hd = hoaDonDAO.timTheoMa(maHD);
		if (hd != null) {
			hd.setTrangThai(trangThai);
			hoaDonDAO.capNhat(hd);
		}
	}

	private void updateDonDatBan(DonDatBan ddb) {
		donDatBanDAO.capNhat(ddb);
	}

	private KhachHang getKhachHangTheoMaDatBan(String maDatBan) {
		return donDatBanDAO.getKhachHangTheoMaDatBan(maDatBan);
	}

}
