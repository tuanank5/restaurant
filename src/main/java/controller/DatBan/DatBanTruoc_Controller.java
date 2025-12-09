package controller.DatBan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.DatMon.DatMonTruoc_Controller;
import controller.DatMon.DatMon_Controller;
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
    private ComboBox<String> cmbTrangThai;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private GridPane gridPaneBan;
   
    // DAO
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

    // Danh sách bàn
    private List<Ban> dsBanChon = new ArrayList<>();
    private List<Ban> danhSachBan = new ArrayList<>();
    private List<Ban> danhSachBanDangChon = new ArrayList<>();
    private List<Button> danhSachButtonDangChonUI = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;
    public static LocalDate ngayDatBanStatic;
    public static String gioBatDauStatic;
    @FXML
    void btnDatBan(ActionEvent event) {
        //Kiểm tra đã chọn bàn chưa
        if (danhSachBanDangChon.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước khi đặt món!");
            alert.showAndWait();
            return;
        }

        //Kiểm tra đã chọn ngày chưa
        LocalDate ngayDat = dpNgayDatBan.getValue();
        if (ngayDat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đặt bàn!");
            alert.showAndWait();
            return;
        }
        //Kiểm tra đã chọn giờ chưa
        String gioDat = cmbGioBatDau.getValue(); // Lấy giá trị chọn từ ComboBox
        if (gioDat == null || gioDat.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn giờ đặt bàn!");
            alert.showAndWait();
            return;
        }
        //Nếu tất cả hợp lệ, truyền bàn sang DatMonNew
        try {
        	DatBanTruoc_Controller.ngayDatBanStatic = ngayDat;
        	DatBanTruoc_Controller.gioBatDauStatic = gioDat;
            DatMonTruoc_Controller.danhSachBanChonStatic = new ArrayList<>(danhSachBanDangChon);
            for (Ban ban : danhSachBanDangChon) {
                ban.setTrangThai("Đã được đặt");
                banDAO.capNhat(ban);
            }
            loadDanhSachBan();
            MenuNV_Controller.instance.readyUI("MonAn/DatMonTruoc");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDanhSachBan();
        loadGioBatDau(); // Khởi tạo ComboBox giờ bắt đầu
    }

    private void loadGioBatDau() {
        cmbGioBatDau.getItems().clear();
        for (int gio = 0; gio < 24; gio++) { // từ 00 đến 23
            String gioStr = String.format("%02d:00", gio);
            cmbGioBatDau.getItems().add(gioStr);
        }
        cmbGioBatDau.setValue("08:00"); // giá trị mặc định (có thể tùy chỉnh)
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
        //Không cho chọn bàn đã được đặt
        if ("Đã được đặt".equals(ban.getTrangThai())) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Bàn này đã được đặt!\nVui lòng chọn bàn khác.");
            alert.showAndWait();
            return;
        }
        // Một khách hàng chỉ được đặt 1 bàn
        if (!danhSachBanDangChon.isEmpty() && !danhSachBanDangChon.contains(ban)) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Một khách hàng chỉ được đặt 1 bàn!\nHãy bỏ chọn bàn đã chọn để chọn bàn khác.");
            alert.showAndWait();
            return;
        }
        // Nếu bàn đang được chọn → bỏ chọn
        if (danhSachBanDangChon.contains(ban)) {
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
            btnBan.setText(ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
            btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
        } else {
            // Chọn bàn
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
            danhSachBanDangChon.add(ban);
            danhSachButtonDangChonUI.add(btnBan);
            btnBan.setText("✔ " + ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
            btnBan.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");
        }
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

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
