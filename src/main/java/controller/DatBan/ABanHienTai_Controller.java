package controller.DatBan;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
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
import javafx.util.Duration;

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
    private int tongDatBan;
    
    public static ABanHienTai_Controller aBHT;
    boolean checkTTbangKo;
    private List<ChiTietHoaDon> dsCTHD_DB;
    
    Map<MonAn, Integer> dsMonAnTA = new HashMap<>();
    
    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnDatBan) {
            MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");
        } else if (source == btnChoThanhToan) {
        	loadDanhSachHoaDon();
        } else if (source == btnDatTruoc) {
        	
        } else if(source == btnBan) {
        	MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
        } else if(source == btnMonAn) {
        	MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
        }
    }

	@FXML
    public void initialize() {
		aBHT = this;
		checkTTbangKo = false;
    	dsHoaDon = hoaDonDAO.getDanhSach("HoaDon.list", HoaDon.class);
    	dsBan = banDAO.getDanhSach("Ban.list", Ban.class);
    	dsDDB = donDatBanDao.getDanhSach("DonDatBan.list", DonDatBan.class);
    	capNhatTrangThaiBanMacDinh();
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

    	for (HoaDon hd : dsHoaDon) {
    		if (hd.getTrangThai().equals("Đặt trước")) {
    			LocalDate ngayGioLapDon = hd.getDonDatBan().getNgayGioLapDon().toLocalDate();
        		if (ngayGioLapDon.equals(nowDate)) {
        			if (nowHour.isAfter(hd.getDonDatBan().getGioBatDau().minusHours(1)) && nowHour.isBefore(hd.getDonDatBan().getGioBatDau().plusHours(1))) {
        				dsDDBnow.add(hd.getDonDatBan());
        			}
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
    	tongDatBan = 0;
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

            if (matchType && hoaDon.getNgayLap().equals(dateNow) 
            		&& hoaDon.getKieuThanhToan().equals("Chưa thanh toán")
            		&& hoaDon.getDonDatBan().getTrangThai().equals("Đã nhận bàn")) {
            	
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
                    		dsCTHD_DB = cthdDAO.getChiTietTheoMaHoaDon(hoaDon.getMaHoaDon());
                    		dsMonAnTA.clear();
                    		for (ChiTietHoaDon cthd : dsCTHD_DB) {
                        		dsMonAnTA.put(cthd.getMonAn(), cthd.getSoLuong());
                        	}
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
                    	donDatBanDao.capNhat(ddb);

                        if (hoaDon != null) {
                            hoaDon.setTrangThai("Đã hủy");
                            RestaurantApplication.getInstance()
                                    .getDatabaseContext()
                                    .newEntity_DAO(HoaDon_DAO.class)
                                    .capNhat(hoaDon);
                        }
                        
                        Ban ban = ddb.getBan();
                        if (ban != null) {
                            ban.setTrangThai("Trống");
                            RestaurantApplication.getInstance()
                                    .getDatabaseContext()
                                    .newEntity_DAO(Ban_DAO.class)
                                    .capNhat(ban);
                        }
                        
                        KhachHang kh = donDatBanDao.getKhachHangTheoMaDatBan(ddb.getMaDatBan());

                        showAlert(
                            Alert.AlertType.INFORMATION,
                            "Đã hủy đơn đặt bàn của khách "
                            + (kh != null ? kh.getTenKH() : "")
                            + " theo yêu cầu."
                        );
                        loadDanhSachHoaDon();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Không thể hủy đơn đặt bàn!");
                    }
                });
            	

                GridPane.setMargin(borderPane, new Insets(5.0));
                gridPaneHD.add(borderPane, col, row);

                capNhatTrangThaiBanTheoHD(hoaDon);
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
    
    private double tinhTongTienMon(HoaDon hoaDon) {
        List<ChiTietHoaDon> dsCT =
                RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(ChiTietHoaDon_DAO.class)
                        .getChiTietTheoMaHoaDon(hoaDon.getMaHoaDon());

        double tong = 0;
        for (ChiTietHoaDon ct : dsCT) {
            tong += ct.getSoLuong() * ct.getMonAn().getDonGia(); 
        }
        return tong;
    }
    
    private Button taoButtonIcon(String text, String iconPath) {
        ImageView icon = new ImageView(
                getClass().getResource(iconPath).toExternalForm()
        );
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        Button btn = new Button();
        btn.setGraphic(icon); 
        btn.setStyle(getButtonStyle());
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        btn.setGraphicTextGap(5);

        return btn;
    }
    
}

