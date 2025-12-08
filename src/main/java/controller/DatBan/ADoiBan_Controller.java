package controller.DatBan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.DonDatBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import util.AutoIDUitl;

public class ADoiBan_Controller implements Initializable {
	// --- ComboBox lọc ---
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private ComboBox<String> cmbLoaiBan;
    
    // --- Button và GridPane ---
    @FXML private Button btnQuayLai;
    @FXML private GridPane gridPaneBan;

    // --- DAO ---
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<Ban> danhSachBan = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;
    private boolean isFromSearch = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        khoiTaoComboBoxes();
        loadDanhSachBan();
        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
    }

    private void khoiTaoComboBoxes() {
        cmbTrangThai.getItems().clear();
        cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đang phục vụ", "Đã được đặt");
        cmbTrangThai.getSelectionModel().select("Tất cả");
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

    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        banDangChon = null;
        buttonBanDangChonUI = null;

        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);
        int col = 0, row = 0;
        final int MAX_COLS = 6;

        String trangThaiLoc = cmbTrangThai.getValue();
        String loaiBanLoc = cmbLoaiBan.getValue();

        for (Ban ban : danhSachBan) {
            boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan().getTenLoaiBan());

            if (matchStatus && matchType) {
                // Lấy số lượng từ đơn đặt bàn gần nhất nếu có, hoặc lấy từ LoaiBan
                Button btnBan = new Button(ban.getMaBan() +"\n"+ ban.getLoaiBan().getTenLoaiBan());
                btnBan.setPrefSize(185, 110);
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
                //btnBan.setOnAction(e -> handleChonBan(ban, btnBan));
                btnBan.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                    // Nếu bàn KHÔNG TRỐNG → không cho chọn
                    	if (!"Trống".equals(ban.getTrangThai())) {
                    	    Alert alert = new Alert(Alert.AlertType.WARNING);
                    	    alert.setTitle("Không thể chọn bàn");
                            alert.setHeaderText("Bàn này không được phép chọn!");
                    	    alert.setContentText(
                    	                "Trạng thái hiện tại: " + ban.getTrangThai() +
                    	                "\nChỉ có thể chọn những bàn đang TRỐNG."
                    	            );
                    	    alert.showAndWait();

                    // Reset lại style cũ
                    	    btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
                    	    return;
                    	}

                    // Nếu bàn TRỐNG → hỏi người dùng
                    	int soLuongChoNgoi = ban.getLoaiBan().getSoLuong();

                    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    	alert.setTitle("Xác nhận đổi bàn");
                    	alert.setHeaderText("Bạn muốn đổi bàn này?");
                    	alert.setContentText("Mã bàn: " + ban.getMaBan() +"\nLoại bàn: " + ban.getLoaiBan().getTenLoaiBan()+ "\nSố lượng khách tối đa : " + soLuongChoNgoi + " người");
                    	     
                    	Optional<ButtonType> result = alert.showAndWait();
                    	if (result.isPresent() && result.get() == ButtonType.OK) {
                    	            // Gán bàn sang MenuNV
                    	            MenuNV_Controller.banDangChon = ban;
                    	            MenuNV_Controller.khachHangDangChon = null;
                    	            // Chuyển UI
                    	            MenuNV_Controller.instance.readyUI("MonAn/aDatMon");
                    	} else {
                    	            btnBan.setStyle(
                    	                getStyleByStatusAndType(
                    	                    ban.getTrangThai(),
                    	                    ban.getLoaiBan().getMaLoaiBan()
                    	                )
                    	            );
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

    private void handleChonBan(Ban ban, Button btnBan) {
        Ban banCu = this.banDangChon;
        Button btnCu = this.buttonBanDangChonUI;
        this.banDangChon = ban;
        
        // --- Nếu đã được đặt → vẫn mở DatMon để xem hoặc sửa ---
        if ("Đã được đặt".equals(ban.getTrangThai()) && !isFromSearch) {
            List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
            if (dsDon != null && !dsDon.isEmpty()) {
                DonDatBan donGan = dsDon.get(dsDon.size() - 1);
                MenuNV_Controller.banDangChon = ban;
                MenuNV_Controller.khachHangDangChon = donGan.getKhachHang();
            }
            //MenuNV_Controller.instance.readyUI("MonAn/DatMon");
            return;
        }
        //Style chọn bàn
        if (btnCu != null) {
            btnCu.getStyleClass().remove("ban-selected");
        }
        btnBan.getStyleClass().add("ban-selected");

        this.buttonBanDangChonUI = btnBan;

    }


    private void handleDatBan(ActionEvent event) {
        if (banDangChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn!");
            return;
        }
        if ("Đã được đặt".equals(banDangChon.getTrangThai()) || "Đang phục vụ".equals(banDangChon.getTrangThai())) {
            showAlert(Alert.AlertType.WARNING, "Bàn này đã được chọn hoặc đang phục vụ, không thể đặt lại!");
            return;
        }

        DonDatBan don = new DonDatBan();
        don.setMaDatBan(AutoIDUitl.sinhMaDonDatBan());
        don.setBan(banDangChon);

        banDangChon.setTrangThai("Đã được đặt");

        boolean thanhCong = donDatBanDAO.them(don);
        if (thanhCong) {
            banDAO.sua(banDangChon);
            showAlert(Alert.AlertType.INFORMATION, "Đặt bàn thành công!");
            loadDanhSachBan();          
        	} else {
            showAlert(Alert.AlertType.ERROR, "Lỗi khi thêm đơn đặt bàn!");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
//    ("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
    private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {

        String backgroundColor = "white";
        if (trangThai != null && !trangThai.isEmpty()) {
            switch (trangThai) {
                case "Đã được đặt": 
                    backgroundColor = "red";
                    break;
                case "Trống":
                    backgroundColor = "green";
                    break;
                case "Đang phục vụ":
                	backgroundColor = "#f39c12";
            }
        }

        return String.format(
            "-fx-background-color: %s;" +
            "-fx-background-radius: 15;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Times New Roman';" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);",
            backgroundColor
        );
    }


    @FXML
    void btnNhanBan(ActionEvent event) {
    	if (banDangChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước!");
            return;
        }
        String trangThai = banDangChon.getTrangThai();
        if (!"Đã được đặt".equals(trangThai)) {
            showAlert(Alert.AlertType.WARNING, "Chỉ những bàn đã được đặt mới nhận phục vụ!");
            return;
        }
        // Cập nhật trạng thái bàn
        banDangChon.setTrangThai("Đang phục vụ");
        boolean capNhat = banDAO.sua(banDangChon);
        if (capNhat) {
            showAlert(Alert.AlertType.INFORMATION, "Bàn đã được nhận, trạng thái: Đang phục vụ");
            loadDanhSachBan(); // Load lại UI để cập nhật màu sắc, trạng thái
        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật trạng thái thất bại!");
        }
    }
}
