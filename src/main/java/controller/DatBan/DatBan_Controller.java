package controller.DatBan;

import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.DonDatBan_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.KhachHang;
import entity.DonDatBan;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatBan_Controller implements Initializable {

    // --- TextField thông tin bàn ---
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

    // --- Button và GridPane ---
    @FXML private Button btnDatBan;
    @FXML private GridPane gridPaneBan;
    @FXML private DatePicker dpNgayDatBan;

    // --- DAO ---
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

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

        btnTimKiem.setOnAction(this::timKiemKhachHang);
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
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getTenLoaiBan()));
                btnBan.setOnAction(e -> handleChonBan(ban, btnBan));

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

    private void handleChonBan(Ban ban, Button btnBan) {
        banDangChon = ban;
        if ("Đã được đặt".equals(ban.getTrangThai()) || "Đang phục vụ".equals(ban.getTrangThai())) {
            btnDatBan.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Bàn này đã được đặt hoặc đang phục vụ, không thể đặt lại!");
        } else {
            btnDatBan.setDisable(false);
        }

        txtTrangThai.setText(ban.getTrangThai());
        txtViTri.setText(ban.getViTri());
        txtLoaiBan.setText(ban.getLoaiBan().getTenLoaiBan());

        List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
        int soLuongHienThi = (dsDon != null && !dsDon.isEmpty()) ? dsDon.get(dsDon.size() - 1).getSoLuong() : ban.getLoaiBan().getSoLuong();
        txtSoLuong.setText(String.valueOf(soLuongHienThi));
        txtSoLuongKH.setText(String.valueOf(soLuongHienThi));

        if (buttonBanDangChonUI != null)
            buttonBanDangChonUI.setStyle(getStyleByStatusAndType(banDangChon.getTrangThai(), banDangChon.getLoaiBan().getTenLoaiBan()));

        btnBan.setStyle("-fx-background-color:orange; -fx-text-fill:black; -fx-font-weight:bold;");
        buttonBanDangChonUI = btnBan;
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
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập số điện thoại khách hàng hợp lệ trước khi đặt bàn!");
            return;
        }

        LocalDate ngayChon = dpNgayDatBan.getValue();
        if (ngayChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đặt bàn!");
            return;
        }

        DonDatBan don = new DonDatBan();
        don.setMaDatBan("DDB" + System.currentTimeMillis());
        don.setKhachHang(kh);
        don.setBan(banDangChon);
        don.setNgayGioLapDon(java.sql.Date.valueOf(ngayChon));
        don.setSoLuong(Integer.parseInt(txtSoLuongKH.getText().trim()));
        don.setGioBatDau(java.time.LocalTime.parse(cmbGioBatDau.getValue()));
        don.setGioKetThuc(java.time.LocalTime.parse(cmbGioKetThuc.getValue()));

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
            cmbGioKetThuc.setValue(donGanNhat.getGioKetThuc().toString());

            for (javafx.scene.Node node : gridPaneBan.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node; // Ép kiểu thủ công
                    if (btn.getText().contains(ban.getMaBan())) {
                        handleChonBan(ban, btn);
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
            case "Đã được đặt": borderColor = "red"; break;
            case "Đang phục vụ": borderColor = "yellow"; break;
            default: borderColor = "black"; break;
        }

        return String.format(
                "-fx-background-color:%s; -fx-border-color:%s; -fx-border-radius:5; -fx-background-radius:5; -fx-text-fill:white; -fx-font-weight:bold;",
                colorLoai, borderColor
        );
    }
}
