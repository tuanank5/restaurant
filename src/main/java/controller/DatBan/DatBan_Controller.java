package controller.DatBan;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import entity.KhachHang;
import entity.Ban;
import entity.LoaiBan;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import dao.DonDatBan_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.DonDatBan;



public class DatBan_Controller implements Initializable {
	
	// --- TextField thông tin bàn ---
    @FXML private TextField txtTrangThai;
    @FXML private TextField txtViTri;
    @FXML private TextField txtSoLuong;
    @FXML private TextField txtLoaiBan;
    
    // --- ComboBox lọc ---
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private ComboBox<String> cmbLoaiBan;
    @FXML private ComboBox<String> cmbGioBatDau;
    @FXML private ComboBox<String> cmbGioKetThuc; 
    
    // --- TextField thông tin khách hàng ---
    @FXML
    private Button btnTimKiem;
    @FXML
    private TextField txtTenKH, txtDiemTichLuy, txtSDT;
    
    // --- Button và GridPane ---
    @FXML private Button btnDatBan;
    @FXML private GridPane gridPaneBan;
    
    @FXML
    private DatePicker dpNgayDatBan;

    // --- DAO ---
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private Ban_DAO banDAO = new Ban_DAOImpl();
    
    // --- Danh sách và trạng thái ---
    private List<Ban> danhSachBan = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;
    @FXML
    private TextField txtSoLuongKH;
    
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        khoiTaoComboBoxes();
        khoiTaoGio();
        loadDanhSachBan();

        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
        cmbGioBatDau.setOnAction(e -> loadDanhSachBan());
        cmbGioKetThuc.setOnAction(e -> loadDanhSachBan());
        
        btnDatBan.setOnAction(this::handleDatBan);
        btnDatBan.setDisable(true);
        
        btnTimKiem.setOnAction(this::timKiemKhachHang);
    }

    private void khoiTaoComboBoxes() {
    	 // ComboBox trạng thái
        cmbTrangThai.getItems().clear();
        cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
        cmbTrangThai.getSelectionModel().select("Tất cả");

        // Lấy danh sách loại bàn từ danh sách bàn
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
        for (int h = 0; h < 24; h++) {
            String gio = String.format("%02d:00", h);
            cmbGioBatDau.getItems().add(gio);
            cmbGioKetThuc.getItems().add(gio);
        }
        cmbGioBatDau.getSelectionModel().select("08:00");
        cmbGioKetThuc.getSelectionModel().select("22:00");
    }

    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        banDangChon = null;
        buttonBanDangChonUI = null;
        resetThongTinBan();

        // Lấy danh sách thực từ database
        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);

        int col = 0, row = 0;
        final int MAX_COLS = 5;

        String trangThaiLoc = cmbTrangThai.getValue();
        String loaiBanLoc = cmbLoaiBan.getValue();

        for (Ban ban : danhSachBan) {

            // Kiểm tra trạng thái và loại bàn
            boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan().getTenLoaiBan());

            if (matchStatus && matchType) {

                // Hiển thị số lượng chỗ (soLuong) thay vì soChoToiDa
                Button btnBan = new Button(ban.getMaBan() + "\n(" + ban.getSoLuong() + " chỗ)");
                btnBan.setPrefSize(120, 100);

                // Set style theo trạng thái và loại bàn
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getTenLoaiBan()));

                // Sự kiện chọn bàn
                btnBan.setOnAction(e -> handleChonBan(ban, btnBan));

                GridPane.setMargin(btnBan, new javafx.geometry.Insets(5.0));
                gridPaneBan.add(btnBan, col, row);

                col++;
                if (col >= MAX_COLS) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    
 // --- Lấy thông tin khách hàng ---
    private void layThongTinKhachHang() {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại khách hàng!");
            return;
        }

        KhachHang kh = khachHangDAO.timTheoSDT(sdt);

        if (kh != null) {
            txtTenKH.setText(kh.getTenKH());
            txtDiemTichLuy.setText(String.valueOf(kh.getDiemTichLuy()));

            //Lấy danh sách các đơn đặt bàn của khách hàng
            List<DonDatBan> dsDon = donDatBanDAO.timTheoKhachHang(kh);

        } else {
            txtTenKH.clear();
            txtDiemTichLuy.clear();
            showAlert(Alert.AlertType.WARNING, "Không tìm thấy khách hàng với số điện thoại này!");
        }
    }


    private String getStyleByStatusAndType(String trangThai, String loaiBan) {
        String colorLoai;
        switch (loaiBan) {
            case "Nhỏ": colorLoai = "#7FFF7F"; break;
            case "Thường": colorLoai = "#0066cc"; break;
            case "Lớn": colorLoai = "#33cc66"; break;
            default: colorLoai = "#888888";
        }

        String borderColor;
        switch (trangThai) {
            case "Đã được đặt":
                borderColor = "red";
                break;
            case "Đang phục vụ":
                borderColor = "yellow";
                break;
            default:
                borderColor = "black";
                break;
        }

        return String.format(
                "-fx-background-color:%s; -fx-border-color:%s; -fx-border-radius:5; -fx-background-radius:5; -fx-text-fill:white; -fx-font-weight:bold;",
                colorLoai, borderColor
        );
    }

    private void handleChonBan(Ban ban, Button btnBan) {
        banDangChon = ban;
        btnDatBan.setDisable(false);

        txtTrangThai.setText(ban.getTrangThai());
        txtViTri.setText(ban.getViTri());
        txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());
        txtSoLuong.setText(String.valueOf(ban.getSoLuong()));

        if (buttonBanDangChonUI != null)
            buttonBanDangChonUI.setStyle(getStyleByStatusAndType(banDangChon.getTrangThai(), banDangChon.getLoaiBan().getTenLoaiBan()));

        btnBan.setStyle("-fx-background-color:orange; -fx-text-fill:black; -fx-font-weight:bold;");
        buttonBanDangChonUI = btnBan;
    }

    private void handleDatBan(ActionEvent event) {
        if (banDangChon == null) {
            showAlert(AlertType.WARNING, "Vui lòng chọn bàn!");
            return;
        }

        String sdt = txtSDT.getText().trim();
        KhachHang kh = khachHangDAO.timTheoSDT(sdt);

        if (kh == null) {
            showAlert(AlertType.WARNING, "Vui lòng nhập số điện thoại khách hàng hợp lệ trước khi đặt bàn!");
            return;
        }
     // Lấy ngày đặt từ DatePicker
        LocalDate ngayChon = dpNgayDatBan.getValue();
        if (ngayChon == null) {
            showAlert(AlertType.WARNING, "Vui lòng chọn ngày đặt bàn!");
            return;
        }

        // Tạo đơn đặt bàn mới
        DonDatBan don = new DonDatBan();
        don.setMaDatBan("DDB" + System.currentTimeMillis());
        don.setKhachHang(kh);
        don.setBan(banDangChon);
        don.setNgayGioLapDon(java.sql.Date.valueOf(ngayChon));
        don.setSoLuong(banDangChon.getSoLuong());
        don.setGioBatDau(java.time.LocalTime.parse(cmbGioBatDau.getValue()));
        don.setGioKetThuc(java.time.LocalTime.parse(cmbGioKetThuc.getValue()));

        // Cập nhật trạng thái bàn
        banDangChon.setTrangThai("Đã được đặt");

        boolean thanhCong = donDatBanDAO.them(don);
        if (thanhCong) {
            banDAO.sua(banDangChon);
            showAlert(AlertType.INFORMATION, "Đặt bàn thành công!");
            loadDanhSachBan();
        } else {
            showAlert(AlertType.ERROR, "Lỗi khi thêm đơn đặt bàn!");
        }
    }
    //Tìm kiếm đơn đặt bàn của Khách Hàng
    @FXML
    private void timKiemKhachHang(ActionEvent event) {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            showAlert(AlertType.WARNING, "Vui lòng nhập số điện thoại khách hàng!");
            return;
        }
        
        KhachHang kh = khachHangDAO.timTheoSDT(sdt);
        if (kh == null) {
            showAlert(AlertType.INFORMATION, "Không tìm thấy khách hàng với số điện thoại này!");
            clearThongTinKhachHang();
            resetThongTinBan();
            return;
        }

        txtTenKH.setText(kh.getTenKH());
        txtDiemTichLuy.setText(String.valueOf(kh.getDiemTichLuy()));

        List<DonDatBan> danhSachDon = donDatBanDAO.timTheoKhachHang(kh);

        if (danhSachDon == null || danhSachDon.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Khách hàng này chưa từng đặt bàn!");
            resetThongTinBan();
            return;
        }
        // Lấy đơn đặt bàn mới nhất (nếu bạn muốn theo ngày thì sort/ORDER BY trong DAO)
        DonDatBan donGanNhat = danhSachDon.get(danhSachDon.size() - 1);
        Ban ban = donGanNhat.getBan();
        
        if (ban != null) {
            // Cập nhật TextField thông tin bàn
            txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());
            txtSoLuong.setText(String.valueOf(ban.getSoLuong()));
            txtSoLuongKH.setText(String.valueOf(ban.getSoLuong()));
            txtViTri.setText(ban.getViTri());
            txtTrangThai.setText(ban.getTrangThai());
            cmbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());
            dpNgayDatBan.setValue(donGanNhat.getNgayGioLapDon().toLocalDate());
            cmbGioBatDau.setValue(donGanNhat.getGioBatDau().toString());
            cmbGioKetThuc.setValue(donGanNhat.getGioKetThuc().toString());

            // Highlight nút bàn tương ứng trong GridPane
            for (javafx.scene.Node node : gridPaneBan.getChildren()) {
                if (node instanceof Button btn) {
                    if (btn.getText().contains(ban.getMaBan())) {
                        handleChonBan(ban, btn); // đánh dấu bàn đang chọn
                        break; // chỉ highlight 1 bàn
                    }
                }
            }
        } else {
            // Nếu khách chưa đặt bàn nào
            resetThongTinBan();
        }
        
        // --- Thông tin bàn ---
        if (donGanNhat.getBan() != null) {
            txtLoaiBan.setText(donGanNhat.getBan().getLoaiBan().getTenLoaiBan());
            txtSoLuongKH.setText(String.valueOf(donGanNhat.getBan().getSoLuong()));
            txtViTri.setText(donGanNhat.getBan().getViTri());
            txtTrangThai.setText(donGanNhat.getBan().getTrangThai());
        } else {
            txtLoaiBan.clear();
            txtSoLuong.clear();
            txtViTri.clear();
            txtTrangThai.clear();
        }
        // --- Loại bàn (ComboBox<String>) ---
        if (donGanNhat.getBan() != null && donGanNhat.getBan().getLoaiBan() != null) {
            cmbLoaiBan.setValue(donGanNhat.getBan().getLoaiBan().getTenLoaiBan());
        	txtLoaiBan.setText(donGanNhat.getBan().getLoaiBan().getTenLoaiBan());
        } else {
            cmbLoaiBan.setValue(null);
            txtLoaiBan.setText(null);
        }

        // --- Ngày đặt (DatePicker) ---
        if (donGanNhat.getNgayGioLapDon() != null) {
            // ngayGioLapDon là java.sql.Date theo entity bạn gửi
        	dpNgayDatBan.setValue(donGanNhat.getNgayGioLapDon().toLocalDate());
        } else {
        	dpNgayDatBan.setValue(null);
        }

        // --- Giờ bắt đầu / kết thúc (LocalTime) ---
        if (donGanNhat.getGioBatDau() != null) {
            cmbGioBatDau.setValue(donGanNhat.getGioBatDau().toString()); // hoặc format nếu cần
        } else {
            cmbGioBatDau.setValue(null);
        }

        if (donGanNhat.getGioKetThuc() != null) {
            cmbGioKetThuc.setValue(donGanNhat.getGioKetThuc().toString());
        } else {
            cmbGioKetThuc.setValue(null);
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
    
    private void showAlert(AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    



}
