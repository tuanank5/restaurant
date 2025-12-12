package controller.DatBan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.MonAn_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.MonAn_DAOImpl;
import entity.Ban;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import util.AutoIDUitl;

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
    private TextField txtTimKiem;

    @FXML
    private TextField txtTongDatBan;
    
    // --- DAO ---
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private DonDatBan_DAO donDatBanDao = new DonDatBan_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<HoaDon> dsHoaDon = new ArrayList<>();
    private List<Ban> dsBan = new ArrayList<Ban>();
    private List<DonDatBan> dsDDB = new ArrayList<DonDatBan>();
    private Ban hoaDonDangChon = null;
    private Button buttonHoaDonDangChonUI = null;
    private String maHoaDonDangChon;
    private boolean isFromSearch = false;
    
    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnDatBan) {
            MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");
        } else if (source == btnChoThanhToan) {
        	loadDanhSachHoaDon();
        } else if (source == btnDatTruoc) {
        	
        } 
    }

	@FXML
    public void initialize() {
    	dsHoaDon = hoaDonDAO.getDanhSach("HoaDon.list", HoaDon.class);
    	dsBan = banDAO.getDanhSach("Ban.list", Ban.class);
    	dsDDB = donDatBanDao.getDanhSach("DonDatBan.list", DonDatBan.class);
    	capNhatTrangThaiBanMacDinh();
    	capNhatTrangThaiBanTheoDonDatTruoc();
        khoiTaoComboBoxes();
        loadDanhSachHoaDon();
        cmbLoaiBan.setOnAction(e -> loadDanhSachHoaDon());
    }

    private void capNhatTrangThaiBanMacDinh() {
		for (Ban ban : dsBan) {
			if (!ban.getTrangThai().equals("Trống")) {
				ban.setTrangThai("Trống");
				RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .capNhat(ban);
			}
		}
		
	}

	private void capNhatTrangThaiBanTheoDonDatTruoc() {
    	LocalDate nowDate = LocalDate.now();
    	LocalTime nowHour = LocalTime.now();
    	List<DonDatBan> dsDDBnow = new ArrayList<DonDatBan>();
    	
    	for (DonDatBan ddb : dsDDB) {
    		LocalDate ngayGioLapDon = ddb.getNgayGioLapDon().toLocalDate();
    		if (ngayGioLapDon.equals(nowDate)) {
    			if (nowHour.isAfter(ddb.getGioBatDau().minusHours(1)) && nowHour.isBefore(ddb.getGioBatDau().plusHours(1))) {
    				dsDDBnow.add(ddb);
    			}
    		}
    	}
    	
    	for (DonDatBan ddb : dsDDBnow) {
    		for (Ban ban : dsBan) {
    			if (ban.getMaBan().equals(ddb.getBan().getMaBan())) {
    				ban.setTrangThai("Đã được đặt");
    				RestaurantApplication.getInstance()
	                    .getDatabaseContext()
	                    .newEntity_DAO(Ban_DAO.class)
	                    .capNhat(ban);
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
        gridPaneHD.getChildren().clear();
        hoaDonDangChon = null;
        buttonHoaDonDangChonUI = null;

        int col = 0, row = 0;
        final int MAX_COLS = 9;

        String loaiBanLoc = cmbLoaiBan.getValue();
        LocalDate localDate = LocalDate.now();
        Date dateNow = Date.valueOf(localDate);

        for (final HoaDon hoaDon : dsHoaDon) {
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(hoaDon.getDonDatBan().getBan().getLoaiBan().getTenLoaiBan());

            if (matchType && hoaDon.getNgayLap().equals(dateNow) && hoaDon.getTrangThai().equals("Chưa thanh toán")) {
//            	BorderPane borderPane = new BorderPane();
//            	BorderPane paneNorth = new BorderPane();
//            	borderPane.setTop(paneNorth);
//            	paneNorth.setCenter(new Label("Hóa đơn"));
//            	
//            	BorderPane paneWest = new BorderPane();
//            	borderPane.setLeft(paneWest);
//            	BorderPane paneBan = new BorderPane();
//            	paneWest.setTop(paneBan);
//            	paneBan.setCenter(new Label(hoaDon.getBan().getMaBan() +"\n"+ hoaDon.getBan().getLoaiBan().getTenLoaiBan()));
//            	Button btnThanhToan = new Button("TT");
//            	paneWest.setLeft(btnThanhToan);
//            	Button btnBan = new Button("B");
//            	paneWest.setRight(btnBan);
//            	
//            	BorderPane paneEast = new BorderPane();
//            	borderPane.setRight(paneEast);
//            	BorderPane paneTien = new BorderPane();
//            	paneEast.setTop(paneTien);
//            	paneTien.setCenter(new Label("0đ"));
//            	Button btnMonAn = new Button("MA");
//            	paneEast.setLeft(btnMonAn);
//            	Button btnKhac = new Button("K");
//            	paneEast.setRight(btnKhac);
            	
            	BorderPane borderPane = new BorderPane();
                
                // North
                BorderPane paneNorth = new BorderPane();
                borderPane.setTop(paneNorth);
                paneNorth.setCenter(new Label("Hóa đơn"));
                paneNorth.setStyle("-fx-padding: 10; -fx-background-color: lightblue; -fx-font-size: 20px; -fx-font-weight: bold;");

                // West
                BorderPane paneWest = new BorderPane();
                borderPane.setLeft(paneWest);
                paneWest.setStyle(getBorderStyle()); // Thêm viền cho paneWest

                BorderPane paneBan = new BorderPane();
                paneWest.setTop(paneBan);
                paneBan.setCenter(new Label(hoaDon.getDonDatBan().getBan().getMaBan() + "\n" + hoaDon.getDonDatBan().getBan().getLoaiBan().getTenLoaiBan()));
                paneBan.setStyle(getBorderStyle()); // Thêm viền cho paneBan

                Button btnThanhToan = new Button("TT");
                btnThanhToan.setStyle(getButtonStyle()); // Cách định dạng cho nút
                paneWest.setLeft(btnThanhToan);
                btnThanhToan.setOnMouseClicked(event -> {
                	MenuNV_Controller.aBanHienTai_HD = hoaDon;
                	MenuNV_Controller.instance.readyUI("DatBan/aThanhToan");
                });

                Button btnBan = new Button("B");
                btnBan.setStyle(getButtonStyle()); // Cách định dạng cho nút
                paneWest.setRight(btnBan);
                btnBan.setOnMouseClicked(event -> {
                	MenuNV_Controller.aBanHienTai_HD = hoaDon;
                	MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
                });

                // East
                BorderPane paneEast = new BorderPane();
                borderPane.setRight(paneEast);
                paneEast.setStyle(getBorderStyle()); // Thêm viền cho paneEast

                BorderPane paneTien = new BorderPane();
                paneEast.setTop(paneTien);
                paneTien.setCenter(new Label("0đ"));
                paneTien.setStyle(getBorderStyle()); // Thêm viền cho paneTien

                Button btnMonAn = new Button("MA");
                btnMonAn.setStyle(getButtonStyle()); // Cách định dạng cho nút
                paneEast.setLeft(btnMonAn);
                btnMonAn.setOnMouseClicked(event -> {
                	MenuNV_Controller.aBanHienTai_HD = hoaDon;
//                	MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
                });

                Button btnKhac = new Button("K");
                btnKhac.setStyle(getButtonStyle()); // Cách định dạng cho nút
                paneEast.setRight(btnKhac);
                btnKhac.setOnMouseClicked(event -> {
                	
                });
            	

                GridPane.setMargin(borderPane, new Insets(5.0));
                gridPaneHD.add(borderPane, col, row);

                capNhatTrangThaiBanTheoHD(hoaDon);
                
                col++;
                if (col >= MAX_COLS) {
                    col = 0;
                    row++;
                }
            }
        }
    }
    
    private void loadDanhSachHoaDonDatTruoc() {
		
	}
    
    private void capNhatTrangThaiBanTheoHD(HoaDon hd) {
    	for (Ban ban : dsBan) {
        	if (hd.getDonDatBan().getBan().equals(ban)) {
        		ban.setTrangThai("Đang phục vụ");
        		RestaurantApplication.getInstance()
   	                    .getDatabaseContext()
   	                    .newEntity_DAO(Ban_DAO.class)
   	                    .capNhat(ban);
        	}
        }
	}

//    private void handleChonBan(Ban ban, Button btnBan) {
//        Ban banCu = this.banDangChon;
//        Button btnCu = this.buttonBanDangChonUI;
//        
//        // Cập nhật bàn đang chọn mới
//        this.banDangChon = ban;
//        // Xử lý nút Đặt bàn
//        if ("Đã được đặt".equals(ban.getTrangThai()) || "Đang phục vụ".equals(ban.getTrangThai())) {
//            btnDatBan.setDisable(true);
//        } else {
//            btnDatBan.setDisable(false);
//        }
//        // Gán thông tin bàn
//        txtTrangThai.setText(ban.getTrangThai());
//        txtViTri.setText(ban.getViTri());
//        txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());
//        List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
//        int soLuongHienThi = (dsDon != null && !dsDon.isEmpty())
//                ? dsDon.get(dsDon.size() - 1).getSoLuong()
//                : ban.getLoaiBan().getSoLuong();
//        txtSoLuong.setText(String.valueOf(soLuongHienThi));
//        txtSoLuongKH.setText(String.valueOf(soLuongHienThi));
//        // Hoàn nguyên style cho bàn CU
//        if (btnCu != null && banCu != null) {
//            btnCu.setStyle(getStyleByStatusAndType(
//                    banCu.getTrangThai(), banCu.getLoaiBan().getMaLoaiBan()
//            ));
//        }
//        // Đặt style nổi bật cho bàn mới
//        btnBan.setStyle(
//            "-fx-background-color: yellow;" +
//            "-fx-text-fill: black;" +
//            "-fx-font-weight: bold;"
//        );
//        // Lưu lại tham chiếu UI của bàn mới
//        this.buttonBanDangChonUI = btnBan;
//    }

//    private void handleChonBan(Ban ban, Button btnBan) {
//        Ban banCu = this.banDangChon;
//        Button btnCu = this.buttonBanDangChonUI;
//        this.banDangChon = ban;
//        
//        // --- Nếu đã được đặt → vẫn mở DatMon để xem hoặc sửa ---
//        if ("Đã được đặt".equals(ban.getTrangThai()) && !isFromSearch) {
//            List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
//            if (dsDon != null && !dsDon.isEmpty()) {
//                DonDatBan donGan = dsDon.get(dsDon.size() - 1);
//                MenuNV_Controller.banDangChon = ban;
//                MenuNV_Controller.khachHangDangChon = donGan.getKhachHang();
//            }
//            //MenuNV_Controller.instance.readyUI("MonAn/DatMon");
//            return;
//        }
//        //Style chọn bàn
//        if (btnCu != null) {
//            btnCu.getStyleClass().remove("ban-selected");
//        }
//        btnBan.getStyleClass().add("ban-selected");
//
//        this.buttonBanDangChonUI = btnBan;
//
//    }
//
//    private void handleDatBan(ActionEvent event) {
//        if (banDangChon == null) {
//            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn!");
//            return;
//        }
//        if ("Đã được đặt".equals(banDangChon.getTrangThai()) || "Đang phục vụ".equals(banDangChon.getTrangThai())) {
//            showAlert(Alert.AlertType.WARNING, "Bàn này đã được chọn hoặc đang phục vụ, không thể đặt lại!");
//            return;
//        }
//
//        DonDatBan don = new DonDatBan();
//        don.setMaDatBan(AutoIDUitl.sinhMaDonDatBan());
//        don.setBan(banDangChon);
//
//        banDangChon.setTrangThai("Đã được đặt");
//
//        boolean thanhCong = donDatBanDAO.them(don);
//        if (thanhCong) {
//            banDAO.sua(banDangChon);
//            showAlert(Alert.AlertType.INFORMATION, "Đặt bàn thành công!");
//            loadDanhSachBan();          
//        	} else {
//            showAlert(Alert.AlertType.ERROR, "Lỗi khi thêm đơn đặt bàn!");
//        }
//    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
//    ("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
    private String getStyleByStatusAndType() {
    	String backgroundColor = "white"; // Mặc định

        return String.format(
        	    "-fx-background-color: %s;" +
        	    "-fx-background-radius: 15;" +
        	    "-fx-padding: 10;" +
        	    "-fx-font-size: 14px;" +
        	    "-fx-font-weight: bold;" +
        	    "-fx-text-fill: white;" +
        	    "-fx-font-family: 'Times New Roman';" +
        	    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);",
        	    backgroundColor
        	);

    }
    
    private String getBorderStyle() {
        return "-fx-border-color: gray; " +
               "-fx-border-width: 2; " +
               "-fx-background-color: white; " +
               "-fx-border-radius: 10; " +
               "-fx-padding: 10;";
    }

    private String getButtonStyle() {
        return "-fx-background-color: lightgreen; " +
               "-fx-background-radius: 5; " +
               "-fx-padding: 10; " +
               "-fx-font-size: 14px; " +
               "-fx-font-weight: bold; " +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);";
    }

//    @FXML
//    void btnNhanBan(ActionEvent event) {
//    	if (banDangChon == null) {
//            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước!");
//            return;
//        }
//        String trangThai = banDangChon.getTrangThai();
//        if (!"Đã được đặt".equals(trangThai)) {
//            showAlert(Alert.AlertType.WARNING, "Chỉ những bàn đã được đặt mới nhận phục vụ!");
//            return;
//        }
//        // Cập nhật trạng thái bàn
//        banDangChon.setTrangThai("Đang phục vụ");
//        boolean capNhat = banDAO.sua(banDangChon);
//        if (capNhat) {
//            showAlert(Alert.AlertType.INFORMATION, "Bàn đã được nhận, trạng thái: Đang phục vụ");
//            loadDanhSachBan(); // Load lại UI để cập nhật màu sắc, trạng thái
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Cập nhật trạng thái thất bại!");
//        }
//    }

}

