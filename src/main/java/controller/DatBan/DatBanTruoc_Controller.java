package controller.DatBan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import controller.DatMon.DatMonTruoc_Controller;
import controller.Menu.MenuNV_Controller;
import javafx.scene.control.ButtonBar;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.LoaiBan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.LoaiBan_DAOImpl;
import entity.Ban;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DatBanTruoc_Controller implements Initializable {

    @FXML private ComboBox<String> cmbGioBatDau;
    @FXML private ComboBox<String> cmbLoaiBan;
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private DatePicker dpNgayDatBan;
    @FXML private GridPane gridPaneBan;
    @FXML private Button btnTroLai;

    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private LoaiBan_DAO loaiBanDAO = new LoaiBan_DAOImpl();
    private List<LoaiBan> dsLoaiBan;
    private List<Ban> danhSachBan = new ArrayList<>();
    private List<Ban> danhSachBanDangChon = new ArrayList<>();
    private List<Button> danhSachButtonDangChonUI = new ArrayList<>();
    private KhachHang khachHangDaChon; 
    private int slKhach = 1;

    public static LocalDate ngayDatBanStatic;
    public static String gioBatDauStatic;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	dpNgayDatBan.setValue(LocalDate.now());
    	loadLoaiBan();
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
    }
    
    private void loadLoaiBan() {
        dsLoaiBan = loaiBanDAO.getAll();
        cmbLoaiBan.getItems().clear();
        for (LoaiBan lb : dsLoaiBan) {
            cmbLoaiBan.getItems().add(lb.getTenLoaiBan());
        }
    }
    
    private void loadGioBatDau() {
        cmbGioBatDau.getItems().clear();
        for (int gio = 8; gio <= 23; gio++) {
            cmbGioBatDau.getItems().add(String.format("%02d:00", gio));
        }
        cmbGioBatDau.setValue("08:00");
    }

    // LOAD DANH SÁCH BÀN — CHUẨN HÓA LOGIC
    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);
        int col = 0, row = 0;
        final int MAX_COLS = 5;
        LocalDate ngayChon = dpNgayDatBan.getValue();
        String gioStr = cmbGioBatDau.getValue();
        LocalTime gioChon = (gioStr != null ? LocalTime.parse(gioStr) : null);
        for (Ban ban : danhSachBan) {
            boolean banBan = (ngayChon != null && gioChon != null)
                    && isBanDangBan(ban, ngayChon, gioChon);
            String bgColor = banBan ? "#ff0000" : "#00aa00"; // đỏ / xanh
            if (ban.getTrangThai().equals("Đang phục vụ"))
                bgColor = "#ec9407"; // cam
            Button btn = new Button(ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
            btn.setPrefSize(170, 110);
            btn.setStyle(buildStyle(bgColor));
            final boolean isBanBanFinal = banBan;
            btn.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // Double click -> mở form nhập thông tin khách hàng
                    hienThiFormThongTinKhachHang(ban);
                    return;
                }
                if (isBanBanFinal) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "Bàn này đang được đặt trong 2 giờ.\nKhông thể chọn!");
                    alert.showAndWait();
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
    private boolean isBanDangBan(Ban ban, LocalDate ngayChon, LocalTime gioChon) {
        List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
        if (dsDon == null) return false;
        LocalDateTime tDat = LocalDateTime.of(ngayChon, gioChon);
        LocalDateTime tDatKetThuc = tDat.plusHours(2);
        for (DonDatBan don : dsDon) {
            LocalDate ngayDon = don.getNgayGioLapDon().toLocalDate();
            LocalTime gioDon = don.getGioBatDau();
            if (!ngayDon.equals(ngayChon)) continue;
            LocalDateTime tDaDat = LocalDateTime.of(ngayDon, gioDon);
            LocalDateTime tDaKetThuc = tDaDat.plusHours(2);
            // KIỂM TRA GIAO NHAU
            boolean giaoNhau = tDat.isBefore(tDaKetThuc) && tDaDat.isBefore(tDatKetThuc);
            if (giaoNhau) return true;
        }
        return false;
    }

    
    // CHỌN BÀN — CHỈ CHO CHỌN 1 BÀN
    private void handleChonBan(Ban ban, Button btnBan) {
        if (!danhSachBanDangChon.isEmpty() && !danhSachBanDangChon.contains(ban)) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Bạn chỉ được chọn 1 bàn.\nHãy bỏ chọn bàn hiện tại!");
            alert.showAndWait();
            return;
        }
        
        if (danhSachBanDangChon.contains(ban)) {
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
            btnBan.setStyle(buildStyle("#00aa00"));
            btnBan.setText(ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
        } else {
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
            danhSachBanDangChon.add(ban);
            danhSachButtonDangChonUI.add(btnBan);
            btnBan.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");
            btnBan.setText("✔ " + ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
        }
    }

    private String buildStyle(String color) {
        return String.format(
                "-fx-background-color: %s;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Times New Roman';" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);",
                color);
    }

    // LỌC BÀN THEO TRẠNG THÁI MENU
    private void filterBanTheoTrangThai() {

        String trangThaiChon = cmbTrangThai.getValue();
        if (trangThaiChon.equals("Tất cả")) {
            loadDanhSachBan();
            return;
        }
        gridPaneBan.getChildren().clear();
        int col = 0, row = 0;
        final int MAX_COLS = 5;
        for (Ban ban : danhSachBan) {
            if (!ban.getTrangThai().equals(trangThaiChon))
                continue;
            Button btn = new Button(ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
            btn.setPrefSize(120, 100);
            btn.setStyle(buildStyle("#ec9407"));
            btn.setOnMouseClicked(event -> handleChonBan(ban, btn));
            GridPane.setMargin(btn, new Insets(5));
            gridPaneBan.add(btn, col, row);
            col++;
            if (col >= MAX_COLS) {
                col = 0;
                row++;
            }
        }
    }
    
    private void hienThiFormThongTinKhachHang(Ban ban) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Thông tin đặt bàn");
        dialog.setHeaderText("Nhập thông tin khách hàng cho bàn " + ban.getMaBan());

        ButtonType btnXacNhan = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnChonMon = new ButtonType("Chọn món ăn", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(btnXacNhan, btnChonMon, ButtonType.CANCEL);

        // --- UI ---
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(15));

        TextField txtSDT = new TextField();
        TextField txtTenKH = new TextField();
        txtTenKH.setEditable(false);
        TextField txtSoLuong = new TextField();

        DatePicker dpNgay = new DatePicker(dpNgayDatBan.getValue());
        ComboBox<String> cmbGio = new ComboBox<>();
        cmbGio.getItems().addAll(cmbGioBatDau.getItems());
        cmbGio.setValue(cmbGioBatDau.getValue());

        // --- Khi nhập số điện thoại thì load thông tin khách hàng ---
        txtSDT.textProperty().addListener((obs, oldV, newV) -> {
            if (newV.trim().length() >= 10) {
                try {
                    var kh = new dao.impl.KhachHang_DAOlmpl().timTheoSDT(newV.trim()); 
                    if (kh != null) {
                        txtTenKH.setText(kh.getTenKH());
                        khachHangDaChon = kh;          // ⭐ LƯU KH ngay khi tìm được
                    } else {
                        txtTenKH.setText("Không tìm thấy");
                        khachHangDaChon = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        txtSoLuong.textProperty().addListener((o, oldV, newV) -> {
            try {
                slKhach = Integer.parseInt(newV.trim());
                if (slKhach <= 0) slKhach = 1;
            } catch (Exception e) {
                slKhach = 1;
            }
        });

        grid.addRow(0, new Label("Số điện thoại:"), txtSDT);
        grid.addRow(1, new Label("Tên khách hàng:"), txtTenKH);
        grid.addRow(2, new Label("Ngày đặt:"), dpNgay);
        grid.addRow(3, new Label("Giờ đặt:"), cmbGio);
        grid.addRow(4, new Label("Số lượng khách:"), txtSoLuong);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == btnXacNhan) {
                xuLyXacNhanThongTin(ban, txtSDT.getText(), txtTenKH.getText(),
                        dpNgay.getValue(), cmbGio.getValue(), txtSoLuong.getText());
            } else if (button == btnChonMon) {
            	 DatBanTruoc_Controller.ngayDatBanStatic = dpNgay.getValue();
                 DatBanTruoc_Controller.gioBatDauStatic = cmbGio.getValue();
                xuLyChonMon(ban);
            }
            return null;
        });

        dialog.showAndWait();
    }
    
    private void xuLyXacNhanThongTin(Ban ban, String sdtInput, String tenKHInput,
            LocalDate ngayInput, String gioInput, String soLuongInput) {		
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
			KhachHang kh = null;
			try {
				kh = new dao.impl.KhachHang_DAOlmpl().timTheoSDT(sdt);
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
			// Số lượng khách
			int soLuongKH_final = 1;
			try {
				soLuongKH = Integer.parseInt(soLuongInput.trim());
			if (soLuongKH <= 0) soLuongKH = 1;
			} catch (Exception e) {
				soLuongKH = 1;
			}
			
			DonDatBan_DAO ddbDAO = new DonDatBan_DAOImpl();
			HoaDon_DAOImpl hdDAO = new HoaDon_DAOImpl();
			
			// Danh sách DonDatBan đã tạo
			List<DonDatBan> danhSachDatBanDaTao = new ArrayList<>();
			
			//TẠO ĐƠN ĐẶT BÀN
			try {
			DonDatBan ddb = new DonDatBan();
			ddb.setMaDatBan(util.AutoIDUitl.sinhMaDonDatBan());		
			// Ngày – giờ đặt (ưu tiên static của màn trước)
			LocalDate ngay = DatBanTruoc_Controller.ngayDatBanStatic != null
			        ? DatBanTruoc_Controller.ngayDatBanStatic
			        : ngayInput;
			String gio = DatBanTruoc_Controller.gioBatDauStatic != null
			        ? DatBanTruoc_Controller.gioBatDauStatic
			        : gioInput;
			LocalTime gioBatDau = LocalTime.parse(gio);
			ddb.setNgayGioLapDon(LocalDateTime.of(ngay, gioBatDau));
			ddb.setGioBatDau(gioBatDau);
			
			ddb.setSoLuong(soLuongKH);
			ddb.setKhachHang(kh);
			ddb.setBan(ban);
			ddb.setTrangThai("Chưa Nhận Bàn");
			
			boolean ok = ddbDAO.them(ddb);
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
				new Ban_DAOImpl().capNhat(ban);
			} catch (Exception e) {
				showAlert(Alert.AlertType.ERROR, "Không cập nhật được trạng thái bàn.");
				return;
				}				
				//Tạo hóa đơn
				if (danhSachDatBanDaTao.isEmpty()) {
				showAlert(Alert.AlertType.ERROR, "Không có đơn đặt bàn để tạo hóa đơn.");
				return;
				}
				
				DonDatBan donDau = danhSachDatBanDaTao.get(0);				
				try {
					HoaDon hd = new HoaDon();
					hd.setMaHoaDon(util.AutoIDUitl.sinhMaHoaDon());
					hd.setNgayLap(java.sql.Date.valueOf(LocalDate.now()));
					hd.setTongTien(0.0);
					hd.setThue(0.0);
					hd.setTrangThai("Chưa Thanh Toán");
					hd.setKieuThanhToan("");
					hd.setTienNhan(0.0);
					hd.setTienThua(0.0);
					hd.setKhachHang(kh);
					hd.setKhuyenMai(null);
					hd.setNhanVien(MenuNV_Controller.taiKhoan.getNhanVien());
					hd.setDonDatBan(donDau);				
					boolean okHD = hdDAO.them(hd);
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
    }

    
    private void xuLyChonMon(Ban ban) {
        try {
            DatMonTruoc_Controller.danhSachBanChonStatic = List.of(ban);
            if (khachHangDaChon == null) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại hợp lệ!");
                return;
            }
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
}
