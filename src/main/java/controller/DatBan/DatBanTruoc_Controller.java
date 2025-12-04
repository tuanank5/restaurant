package controller.DatBan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import util.AutoIDUitl;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;


public class DatBanTruoc_Controller implements Initializable {
	@FXML
    private Button btnBan;

    @FXML
    private ComboBox<String> cmbGioBatDau;

    @FXML
    private ComboBox<String> cmbLoaiBan;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private GridPane gridPaneBan;

    @FXML
    private TextField txtSoBan;

    @FXML
    private TextField txtSoLuongKH;

    @FXML
    void btnDatBan(ActionEvent event) {

    }

 // DAO
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

    // Danh sách bàn
    private List<Ban> danhSachBan = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDanhSachBan();
    }

    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        banDangChon = null;
        buttonBanDangChonUI = null;

        // Lấy danh sách bàn từ DB
        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);

        int col = 0, row = 0;
        final int MAX_COLS = 5;

        for (Ban ban : danhSachBan) {
            // Lấy số lượng từ loại bàn hoặc đơn đặt gần nhất
            List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
            int soLuongHienThi = (dsDon != null && !dsDon.isEmpty())
                    ? dsDon.get(dsDon.size() - 1).getSoLuong()
                    : ban.getLoaiBan().getSoLuong();

            // Tạo nút đại diện cho bàn
            Button btnBan = new Button(ban.getMaBan() + "\n(" + soLuongHienThi + " chỗ)");
            btnBan.setPrefSize(120, 100);
            btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));

            btnBan.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
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

    private void handleChonBan(Ban ban, Button btnBan) {
        Ban banCu = this.banDangChon;
        Button btnCu = this.buttonBanDangChonUI;

        this.banDangChon = ban;

        // Lấy đơn đặt gần nhất nếu có
        List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
        DonDatBan donGanNhat = (dsDon != null && !dsDon.isEmpty()) ? dsDon.get(dsDon.size() - 1) : null;

        // Load thông tin lên form
        loadThongTinBan(ban, donGanNhat);

        // Reset style bàn cũ
        if (btnCu != null && banCu != null) {
            btnCu.setStyle(getStyleByStatusAndType(banCu.getTrangThai(), banCu.getLoaiBan().getMaLoaiBan()));
        }

        // Style bàn được chọn
        btnBan.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold;");
        this.buttonBanDangChonUI = btnBan;
    }

    private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {
        String backgroundColor = "white";
        String borderColor = "black";

        if (trangThai != null && !trangThai.isEmpty()) {
            switch (trangThai) {
                case "Đã được đặt": borderColor = "red"; break;
                case "Trống": borderColor = "purple"; break;
                case "Đang phục vụ": borderColor = "#257925"; break;
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
 
    private void loadThongTinBan(Ban ban, DonDatBan donGanNhat) {
        if (ban == null) return;

        // Hiển thị mã bàn
        txtSoBan.setText(ban.getMaBan());

        // Hiển thị số lượng khách
        int soLuong = (donGanNhat != null) ? donGanNhat.getSoLuong() : ban.getLoaiBan().getSoLuong();
        txtSoLuongKH.setText(String.valueOf(soLuong));

        // Hiển thị ngày đặt bàn nếu có
        if (donGanNhat != null && donGanNhat.getNgayGioLapDon() != null) {
            dpNgayDatBan.setValue(donGanNhat.getNgayGioLapDon().toLocalDate());
        }

        // Hiển thị loại bàn
        if (ban.getLoaiBan() != null) {
            cmbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());
        }

        // Nếu muốn, có thể hiển thị giờ bắt đầu, giờ kết thúc từ đơn đặt gần nhất
        if (donGanNhat != null) {
            cmbGioBatDau.setValue(donGanNhat.getGioBatDau().toString());
            // cmbGioKetThuc.setValue(donGanNhat.getGioKetThuc().toString()); // nếu có
        }
    }
 
}
