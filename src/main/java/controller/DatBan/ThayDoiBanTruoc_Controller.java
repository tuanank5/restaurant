package controller.DatBan;

import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.AutoIDUitl;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;


public class ThayDoiBanTruoc_Controller implements Initializable {
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
    private Button btnTroLai;

    public static DonDatBan donDatBanDuocChon;

    // DAO
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDAO = new DonDatBan_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();

    // Danh sách bàn
    private List<Ban> dsBanChon = new ArrayList<>();
    private List<Ban> danhSachBan = new ArrayList<>();
    private List<Ban> danhSachBanDangChon = new ArrayList<>();
    private List<Button> danhSachButtonDangChonUI = new ArrayList<>();
    private Ban banDangChon = null;
    private Button buttonBanDangChonUI = null;

    @FXML
    void btnDatBan(ActionEvent event) {
        
        // 1) kiểm tra đã chọn bàn chưa
        if (danhSachBanDangChon.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước khi đổi!").showAndWait();
            return;
        }

        // 2) chọn 1 bàn
        Ban banMoi = danhSachBanDangChon.get(0);

        // 3) kiểm tra ngày
        LocalDate ngayDat = dpNgayDatBan.getValue();
        if (ngayDat == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đổi bàn!").showAndWait();
            return;
        }
        
        if (!ngayDat.isAfter(LocalDate.now())) {
            new Alert(Alert.AlertType.ERROR, "Ngày đổi bàn phải lớn hơn ngày hiện tại!").showAndWait();
            return;
        }
        
        // 4) kiểm tra giờ
        String gioDat = cmbGioBatDau.getValue();
        if (gioDat == null || gioDat.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn giờ đổi bàn!").showAndWait();
            return;
        }


        try {
            // ======= LẤY ĐƠN CŨ =========
            DonDatBan don = ThayDoiBanTruoc_Controller.donDatBanDuocChon;

            // ======= BÀN CŨ =========
            Ban banCu = don.getBan();

            // ======= TRẢ BÀN CŨ VỀ TRỐNG =========
            banCu.setTrangThai("Trống");
            banDAO.capNhat(banCu);

            // ======= CHỌN BÀN MỚI =========
            banMoi.setTrangThai("Đã được đặt");
            banDAO.capNhat(banMoi);

            // ======= CẬP NHẬT ĐƠN =========
            int hour = Integer.parseInt(gioDat.substring(0,2));

            don.setBan(banMoi);
            don.setNgayGioLapDon( ngayDat.atTime(hour,0) );
            don.setGioBatDau( java.time.LocalTime.of(hour,0) );
            don.setTrangThai("Chưa nhận bàn");

            //chỉ dùng them() vì capNhat() không tồn tại hợp lệ
            donDAO.capNhat(don);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Đổi bàn thành công!");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    @FXML
    private void onTroLai(ActionEvent event) {
        try {
            MenuNV_Controller.instance.readyUI("DatBan/DonDatBan");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDanhSachBan();
        loadGioBatDau(); // Khởi tạo ComboBox giờ bắt đầu
     // 2. SAU KHI ĐÃ CÓ BUTTON BÀN → MỚI ĐÁNH DẤU
        if (donDatBanDuocChon != null) {

            System.out.println("Đang đổi bàn cho đơn: " + donDatBanDuocChon.getMaDatBan());

            Ban banDaDat = donDatBanDuocChon.getBan();

            // Tick bàn đã đặt
            danhDauBanDangDat(banDaDat);

            // Load thông tin ngày – giờ – loại bàn
            loadThongTinBan(banDaDat, donDatBanDuocChon);
        }
        btnTroLai.setOnAction(event -> onTroLai(event));
    }

    private void danhDauBanDangDat(Ban banDaDat) {
        if (banDaDat == null) return;

        for (javafx.scene.Node node : gridPaneBan.getChildren()) {
        	if (node instanceof Button) {
        	    Button btn = (Button) node;
                String maBanUI = btn.getText().split("\n")[0].replace("✔ ", "").trim();

                if (maBanUI.equals(banDaDat.getMaBan())) {
                    // thêm vào danh sách chọn
                    danhSachBanDangChon.add(banDaDat);
                    danhSachButtonDangChonUI.add(btn);

                    // cập nhật giao diện
                    btn.setText("✔ " + banDaDat.getMaBan());
                    btn.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");

                    txtSoBan.setText("1");
                    break;
                }
            }
        }
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
            Button btnBan = new Button(ban.getMaBan());
            btnBan.setPrefSize(170, 110);
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
    	// Nếu đã chọn 1 bàn rồi → bỏ chọn bàn cũ
        if (!danhSachBanDangChon.isEmpty()) {
            Ban banCu = danhSachBanDangChon.get(0);
            Button btnCu = danhSachButtonDangChonUI.get(0);
            // Bỏ tick bàn cũ
            btnCu.setText(banCu.getMaBan());
            btnCu.setStyle(getStyleByStatusAndType(banCu.getTrangThai(), banCu.getLoaiBan().getMaLoaiBan()));
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
        }
        // Chọn bàn mới
        danhSachBanDangChon.add(ban);
        danhSachButtonDangChonUI.add(btnBan);
        btnBan.setText("✔ " + ban.getMaBan());
        btnBan.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");

        // Cập nhật tổng số bàn được chọn lên txtSoBan
        txtSoBan.setText(String.valueOf(danhSachBanDangChon.size()));
    }

    
    private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {
        String backgroundColor = "white"; // default
        if (trangThai != null && !trangThai.isEmpty()) {
            switch (trangThai) {
                case "Đã được đặt":
                    backgroundColor = "#ff0000";
                    break;
                case "Trống":
                    backgroundColor = "#00aa00"; 
                    break;
                case "Đang phục vụ":
                    backgroundColor = "#ec9407";
                    break;
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
 
    private void loadThongTinBan(Ban ban, DonDatBan donGanNhat) {
        if (ban == null) return;
        // Hiển thị mã bàn
        txtSoBan.setText(ban.getMaBan());
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
