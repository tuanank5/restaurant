package controller.DatBan;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import entity.KhachHang;
import entity.Ban;
import entity.LoaiBan;
import java.net.URL;
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

    // --- DAO ---
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private Ban_DAO banDAO = new Ban_DAOImpl();
    
    // --- Danh sách và trạng thái ---
    private List<Ban> danhSachBan = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;

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
        
        btnTimKiem.setOnAction(e -> layThongTinKhachHang());
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
        txtSoLuong.setText("Không có số lượng trong Entity");

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

        banDangChon.setTrangThai("Đã được đặt");

        if (banDAO.sua(banDangChon)) {
            showAlert(AlertType.INFORMATION, "Đặt bàn thành công!");
            loadDanhSachBan();
        } else {
            showAlert(AlertType.ERROR, "Lỗi khi cập nhật trạng thái bàn!");
        }
    }

    private void resetThongTinBan() {
        txtTrangThai.clear();
        txtViTri.clear();
        txtLoaiBan.clear();
        txtSoLuong.clear();
        btnDatBan.setDisable(true);
    }

    private void showAlert(AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    



}
