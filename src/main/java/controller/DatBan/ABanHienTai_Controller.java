package controller.DatBan;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import util.AutoIDUitl;

public class ABanHienTai_Controller {

	@FXML
    private Button btnBan;

    @FXML
    private Button btnChoThanhToan;

    @FXML
    private Button btnDatTruoc;

    @FXML
    private Button btnGoiMon;

    @FXML
    private ComboBox<String> cmbLoaiBan;

    @FXML
    private GridPane gridPaneBan;

    @FXML
    private TextField txtTimKiem;

    @FXML
    private TextField txtTongDatBan;
    
 // --- DAO ---
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<HoaDon> dsHoaDon = new ArrayList<>();
    private Ban hoaDonDangChon = null;
    private Button buttonHoaDonDangChonUI = null;
    private String maHoaDonDangChon;
    private boolean isFromSearch = false;

    @FXML
    public void initialize() {
        khoiTaoComboBoxes();
        loadDanhSachHoaDon();
        cmbLoaiBan.setOnAction(e -> loadDanhSachHoaDon());
    }

    private void khoiTaoComboBoxes() {
        cmbLoaiBan.getItems().clear();
        cmbLoaiBan.getItems().add("Tất cả");
        List<Ban> danhSachBanTemp = banDAO.getDanhSach("Ban.list", Ban.class);
        for (Ban ban : danhSachBanTemp) {
            String tenLoai = ban.getLoaiBan().getTenLoaiBan();
            if (!cmbLoaiBan.getItems().contains(tenLoai)) {
                cmbLoaiBan.getItems().add(tenLoai);
            }
        }
        cmbLoaiBan.getSelectionModel().select("Tất cả");
    }

    private void loadDanhSachHoaDon() {
        gridPaneBan.getChildren().clear();
        hoaDonDangChon = null;
        buttonHoaDonDangChonUI = null;

        dsHoaDon = hoaDonDAO.getDanhSach("HoaDon.list", HoaDon.class);
        int col = 0, row = 0;
        final int MAX_COLS = 9;

        String loaiBanLoc = cmbLoaiBan.getValue();
        LocalDate localDate = LocalDate.now();
        Date dateNow = Date.valueOf(localDate);

        for (HoaDon hoaDon : dsHoaDon) {
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(hoaDon.getBan().getLoaiBan().getTenLoaiBan());

            if (matchType && hoaDon.getNgayLap().equals(dateNow)) {
                // Lấy số lượng từ đơn đặt bàn gần nhất nếu có, hoặc lấy từ LoaiBan
                
                Button btnBan = new Button(hoaDon.getBan().getMaBan() +"\n"+ hoaDon.getBan().getLoaiBan().getTenLoaiBan());
                btnBan.setPrefSize(120, 100);
                btnBan.setStyle(getStyleByStatusAndType());
                //btnBan.setOnAction(e -> handleChonBan(ban, btnBan));
                btnBan.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                    	
                    	 // Hiện hộp thoại xác nhận
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Xác nhận chọn bàn");
                        alert.setHeaderText("Bạn muốn chọn bàn này?");
                        alert.setContentText(
                            "Mã bàn: " + hoaDon.getBan().getMaBan() +
                            "\n Loại bàn: " + hoaDon.getBan().getLoaiBan().getTenLoaiBan()
                        );

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {

//                            // Gán bàn đã chọn sang MenuNV
//                            MenuNV_Controller.banDangChon = ban;
//                            MenuNV_Controller.khachHangDangChon = null;
//
//                            // Chuyển sang giao diện đặt món
//                            MenuNV_Controller.instance.readyUI("MonAn/DatMon");

                        } else {
//                            // Người dùng bấm Cancel → reset style bàn
//                            btnBan.setStyle(
//                                getStyleByStatusAndType(
//                                    ban.getTrangThai(),
//                                    ban.getLoaiBan().getMaLoaiBan()
//                                )
//                            );
                        }
                    }
                });


                GridPane.setMargin(btnBan, new Insets(5.0));
                gridPaneBan.add(btnBan, col, row);

                col++;
                if (col >= MAX_COLS) {
                    col = 0;
                    row++;
                }
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

