package controller.DatBan;

import dao.KhuyenMai_DAO;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.KhuyenMai_DAOImpl;
import dao.DonDatBan_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.DonDatBan;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.AutoIDUitl;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.DatMon.DatMon_Controller;
import controller.Menu.MenuNV_Controller;


public class DatBan_Controller implements Initializable {

    // --- TextField thông tin bàn
    @FXML private TextField txtTrangThai;
    @FXML private TextField txtViTri;
    @FXML private TextField txtSoLuong;
    @FXML private TextField txtLoaiBan;
    @FXML private TextField txtSoLuongKH;

    // --- ComboBox lọc ---
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private ComboBox<String> cmbLoaiBan;
    @FXML private ComboBox<String> cmbGioBatDau;
    @FXML private ComboBox<String> cmbGioKetThuc; 

    // --- TextField thông tin khách hàng ---
    @FXML private TextField txtTenKH, txtDiemTichLuy, txtSDT;
    @FXML private Button btnTimKiem;
    @FXML private ScrollPane scrollPaneBan;
 // --- thông tin khuyến mãi ---
    @FXML
    private ComboBox<KhuyenMai> cmbKM;

    // --- Button và GridPane ---
    @FXML private Button btnDatBan;
    @FXML private GridPane gridPaneBan;
    @FXML private DatePicker dpNgayDatBan;

    // --- DAO ---
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<Ban> danhSachBan = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;
    
    private String maBanDangChon;
    private String maKHDangChon;
    private boolean isFromSearch = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
//    	 scrollPaneBan.setContent(gridPaneBan); // Gắn gridPaneBan vào scrollPaneBan có trong FXML
//    	    scrollPaneBan.setFitToWidth(true);
//    	    scrollPaneBan.setFitToHeight(true);
        khoiTaoComboBoxes();
        khoiTaoGio();
//        khoiTaoComboBoxKhuyenMai();
        loadDanhSachBan();

        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
        cmbGioBatDau.setOnAction(e -> loadDanhSachBan());
        cmbGioKetThuc.setOnAction(e -> loadDanhSachBan());
        
        btnDatBan.setOnAction(this::handleDatBan);
        btnDatBan.setDisable(true);

        btnTimKiem.setOnAction(this::timKiemKhachHang);
        
        if (MenuNV_Controller.banDangChon != null) {
        	maBanDangChon = MenuNV_Controller.banDangChon.getMaBan();
        }

        if (MenuNV_Controller.khachHangDangChon != null) {
        	maKHDangChon = MenuNV_Controller.khachHangDangChon.getMaKH();
            txtTenKH.setText(MenuNV_Controller.khachHangDangChon.getTenKH());
        }
    }
    
    private void khoiTaoComboBoxes() {
        cmbTrangThai.getItems().clear();
        cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
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
    private void khoiTaoGio() {
        cmbGioBatDau.getItems().clear();
        cmbGioKetThuc.getItems().clear();
        // Lấy giờ - phút hiện tại
        LocalTime now = LocalTime.now();
        String gioPhutHienTai = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        // Tạo danh sách giờ/phút theo từng phút
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m++) {
                String gio = String.format("%02d:%02d", h, m);
                cmbGioBatDau.getItems().add(gio);
                cmbGioKetThuc.getItems().add(gio);
            }
        }
        // Chọn giờ bắt đầu = giờ phút hiện tại
        cmbGioBatDau.getSelectionModel().select(gioPhutHienTai);

        // Giờ kết thúc mặc định thêm 1 giờ
        String gioKetThucMacDinh = now.plusHours(1)
                .format(DateTimeFormatter.ofPattern("HH:mm"));
        cmbGioKetThuc.getSelectionModel().select(gioKetThucMacDinh);
    }


    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        banDangChon = null;
        buttonBanDangChonUI = null;
        resetThongTinBan();

        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);
        int col = 0, row = 0;
        final int MAX_COLS = 5;

        String trangThaiLoc = cmbTrangThai.getValue();
        String loaiBanLoc = cmbLoaiBan.getValue();

        for (Ban ban : danhSachBan) {
            boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan().getTenLoaiBan());

            if (matchStatus && matchType) {
                // Lấy số lượng từ đơn đặt bàn gần nhất nếu có, hoặc lấy từ LoaiBan
                List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
                int soLuongHienThi = (dsDon != null && !dsDon.isEmpty()) ? dsDon.get(dsDon.size() - 1).getSoLuong() : ban.getLoaiBan().getSoLuong();

                Button btnBan = new Button(ban.getMaBan() + "\n(" + soLuongHienThi + " chỗ)");
                btnBan.setPrefSize(120, 100);
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
                //btnBan.setOnAction(e -> handleChonBan(ban, btnBan));
                btnBan.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                    	// BÀN TRỐNG mở DatMon để đặt món trước
                        if ("Trống".equals(ban.getTrangThai())) {
                            MenuNV_Controller.banDangChon = ban;
                            MenuNV_Controller.khachHangDangChon = null;
                            MenuNV_Controller.instance.readyUI("MonAn/DatMon");
                            return;
                        }
                        
                        if("Đang phục vụ".equals(ban.getTrangThai())) {
                        	MenuNV_Controller.banDangChon = ban;
                            MenuNV_Controller.khachHangDangChon = null;
                            MenuNV_Controller.instance.readyUI("MonAn/DatMon");
                            return;
                        }
                        // Double click: nếu bàn đã được đặt thì mở giao diện DatMon
                        if ("Đã được đặt".equals(ban.getTrangThai())) {
                            if (dsDon != null && !dsDon.isEmpty()) {
                                DonDatBan donGanNhat = dsDon.get(dsDon.size() - 1);
                                KhachHang khachHang = donGanNhat.getKhachHang();
                                
                                // Gán dữ liệu sang MenuNV_Controller để dùng ở giao diện DatMon
                                MenuNV_Controller.banDangChon = ban;
                                MenuNV_Controller.khachHangDangChon = khachHang;
                            }
                            MenuNV_Controller.instance.readyUI("MonAn/DatMon");
                        }
                    } else if (event.getClickCount() == 1) {
                        // Single click: chỉ chọn bàn
                        handleChonBan(ban, btnBan);
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

        // Gán thông tin bàn lên UI
        txtTrangThai.setText(ban.getTrangThai());
        txtViTri.setText(ban.getViTri());
        txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());
        txtSoLuong.setText(String.valueOf(ban.getLoaiBan().getSoLuong()));
        txtSoLuongKH.setText(String.valueOf(ban.getLoaiBan().getSoLuong()));
        
        if ("Trống".equals(ban.getTrangThai())) {
            btnDatBan.setDisable(false);
        } else {
            btnDatBan.setDisable(true);
        }
        
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

        // --- Nếu đang phục vụ → không cho chỉnh, chỉ hiển thị ---
        if ("Đang phục vụ".equals(ban.getTrangThai())) {
            btnDatBan.setDisable(true);
        }

        //Style chọn bàn
        if (btnCu != null) {
            btnCu.setStyle(getStyleByStatusAndType(banCu.getTrangThai(), banCu.getLoaiBan().getMaLoaiBan()));
        }
        btnBan.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold;");
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

        String sdt = txtSDT.getText().trim();
        KhachHang kh = khachHangDAO.timTheoSDT(sdt);

        if (kh == null) {
            if (sdt.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại của khách hàng!");
                return;
            }
        }
        
        LocalDate ngayChon = dpNgayDatBan.getValue();
        if (ngayChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đặt bàn!");
            return;
        }
        
        DonDatBan don = new DonDatBan();
        don.setMaDatBan(AutoIDUitl.sinhMaDonDatBan());
        don.setKhachHang(kh);
        don.setBan(banDangChon);
        don.setNgayGioLapDon(ngayChon.atTime(LocalTime.now()));
        don.setSoLuong(Integer.parseInt(txtSoLuongKH.getText().trim()));
        don.setGioBatDau(java.time.LocalTime.parse(cmbGioBatDau.getValue()));
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

    @FXML
    private void timKiemKhachHang(ActionEvent event) {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại khách hàng!");
            return;
        }

        KhachHang kh = khachHangDAO.timTheoSDT(sdt);
        if (kh == null) {
            showAlert(Alert.AlertType.INFORMATION, "Không tìm thấy khách hàng với số điện thoại này!");
            clearThongTinKhachHang();
            resetThongTinBan();
            return;
        }

        txtTenKH.setText(kh.getTenKH());
        txtDiemTichLuy.setText(String.valueOf(kh.getDiemTichLuy()));

        List<DonDatBan> danhSachDon = donDatBanDAO.timTheoKhachHang(kh);

        if (danhSachDon == null || danhSachDon.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Khách hàng này chưa từng đặt bàn!");
            resetThongTinBan();
            return;
        }

        DonDatBan donGanNhat = danhSachDon.get(danhSachDon.size() - 1);
        Ban ban = donGanNhat.getBan();

        if (ban != null) {
            txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());
            List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
            int soLuongHienThi = (dsDon != null && !dsDon.isEmpty()) ? dsDon.get(dsDon.size() - 1).getSoLuong() : ban.getLoaiBan().getSoLuong();
            txtSoLuong.setText(String.valueOf(soLuongHienThi));
            txtSoLuongKH.setText(String.valueOf(soLuongHienThi));
            txtViTri.setText(ban.getViTri());
            txtTrangThai.setText(ban.getTrangThai());
            cmbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());
            dpNgayDatBan.setValue(donGanNhat.getNgayGioLapDon().toLocalDate());
            cmbGioBatDau.setValue(donGanNhat.getGioBatDau().toString());

            for (javafx.scene.Node node : gridPaneBan.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node; // Ép kiểu thủ công
                    if (btn.getText().contains(ban.getMaBan())) {
                    	isFromSearch = true;
                    	handleChonBan(ban, btn);
                    	isFromSearch = false;
                        break;
                    }
                }
            }
        }
    }

    

    private void resetThongTinBan() {
        txtTrangThai.clear();
        txtViTri.clear();
        txtLoaiBan.clear();
        txtSoLuong.clear();
        btnDatBan.setDisable(true);
    }

    private void clearThongTinKhachHang() {
        txtTenKH.clear();
        txtDiemTichLuy.clear();
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
        String borderColor = "black";

        if (trangThai != null && !trangThai.isEmpty()) {
            switch (trangThai) {
                case "Đã được đặt":
                    borderColor = "red";
                    break;
                case "Trống":
                    borderColor = "purple";
                    break;
                case "Đang phục vụ":
                	borderColor = "#257925";
            }
        }

        switch (maLoaiBan) {
            case "LB01": backgroundColor = "#66cccc"; break;
            case "LB02": backgroundColor = "#FFEB3B"; break;
            case "LB03": backgroundColor = "#FF6F61"; break;
        }

        return String.format(
            "-fx-background-color: %s;" +
            "-fx-border-color: %s;" +
            "-fx-border-width: 6;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 15;" +
            "-fx-min-width: 60px;" +
            "-fx-min-height: 40px;",
            backgroundColor, borderColor
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

