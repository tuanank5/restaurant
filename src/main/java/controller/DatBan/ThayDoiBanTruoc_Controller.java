package controller.DatBan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.LoaiBan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.LoaiBan_DAOImpl;
import entity.Ban;
import entity.DonDatBan;
import entity.LoaiBan;
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
import java.net.URL;
import java.util.ResourceBundle;

public class ThayDoiBanTruoc_Controller implements Initializable {

    @FXML private Button btnBan;
    @FXML private Button btnTroLai;
    @FXML private ComboBox<String> cmbGioBatDau;
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private ComboBox<String> cmbLoaiBan;
    @FXML private DatePicker dpNgayDatBan;
    @FXML private GridPane gridPaneBan;
    @FXML private TextField txtSoBan;

    public static DonDatBan donDatBanDuocChon;
    private String loaiBanCu;

    private Ban_DAO banDAO = new Ban_DAOImpl();
    private DonDatBan_DAO donDAO = new DonDatBan_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private LoaiBan_DAO loaiBanDAO = new LoaiBan_DAOImpl();

    private List<Ban> danhSachBan = new ArrayList<>();
    private List<LoaiBan> dsLoaiBan;
    private List<Ban> danhSachBanDangChon = new ArrayList<>();
    private List<Button> danhSachButtonDangChonUI = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
        cmbTrangThai.setValue("Tất cả");
        cmbTrangThai.setOnAction(e -> filterBanTheoTrangThai());
        loadLoaiBan();
        loadDanhSachBan();
        loadGioBatDau();
        // nếu đang đổi bàn → đánh dấu bàn cũ
        if (donDatBanDuocChon != null) {
            Ban banDaDat = donDatBanDuocChon.getBan();
            loaiBanCu = banDaDat.getLoaiBan().getMaLoaiBan();
            danhDauBanDangDat(banDaDat);
            loadThongTinBan(banDaDat, donDatBanDuocChon);
        }
        btnTroLai.setOnAction(event -> onTroLai(event));
        dpNgayDatBan.valueProperty().addListener((obs, oldV, newV) -> locBanTheoNgayGio());
        cmbGioBatDau.valueProperty().addListener((obs, oldV, newV) -> locBanTheoNgayGio());
    }

    private void loadLoaiBan() {
        dsLoaiBan = loaiBanDAO.getAll();
        cmbLoaiBan.getItems().clear();
        for (LoaiBan lb : dsLoaiBan) {
            cmbLoaiBan.getItems().add(lb.getTenLoaiBan());
        }
    }

    private void loadGioBatDau() {
        cmbGioBatDau.getItems().clear();
        for (int gio = 8; gio <= 23; gio++) {
            cmbGioBatDau.getItems().add(String.format("%02d:00", gio));
        }
        cmbGioBatDau.setValue("08:00");
    }

    @FXML
    void btnDatBan(ActionEvent event) {
        if (danhSachBanDangChon.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước khi đổi!");
            return;
        }
        Ban banMoi = danhSachBanDangChon.get(0);
        LocalDate ngayDat = dpNgayDatBan.getValue();
        if (ngayDat == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ngày đổi bàn!");
            return;
        }
        if (!ngayDat.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Ngày đổi bàn phải lớn hơn ngày hiện tại!");
            return;
        }
        String gioDat = cmbGioBatDau.getValue();
        if (gioDat == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn giờ đổi bàn!");
            return;
        }
        try {
            DonDatBan don = donDatBanDuocChon;
            Ban banCu = don.getBan();
            banCu.setTrangThai("Trống");
            banDAO.capNhat(banCu);
            banMoi.setTrangThai("Đã được đặt");
            banDAO.capNhat(banMoi);
            
            int hour = Integer.parseInt(gioDat.substring(0, 2));
            don.setBan(banMoi);
            don.setNgayGioLapDon(ngayDat.atTime(hour, 0));
            don.setGioBatDau(LocalTime.of(hour, 0));
            don.setTrangThai("Chưa nhận bàn");
            donDAO.capNhat(don);
            
            showAlert(Alert.AlertType.INFORMATION, "Đổi bàn thành công!");
            MenuNV_Controller.instance.readyUI("DatBan/DonDatBan");

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

    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);
        int col = 0, row = 0;
        final int MAX_COLS = 5;
        for (Ban ban : danhSachBan) {
            Button btnBan = taoNutBan(ban);
            gridPaneBan.add(btnBan, col, row);
            col++;
            if (col >= MAX_COLS) {
                col = 0;
                row++;
            }
        }
    }

    private Button taoNutBan(Ban ban) {
        Button btnBan = new Button(ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
        btnBan.setPrefSize(170, 110);
        btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
        btnBan.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleChonBan(ban, btnBan);
            } 
            else if (event.getClickCount() == 2) {
                moFormThongTinKhachHang();
            }
        });

        GridPane.setMargin(btnBan, new Insets(5.0));
        return btnBan;
    }

    private void handleChonBan(Ban ban, Button btnBan) {
        if (!"Trống".equals(ban.getTrangThai())) {
            showAlert(Alert.AlertType.ERROR, "Chỉ được đổi sang bàn trống!");
            return;
        }
        if (loaiBanCu != null &&
                !ban.getLoaiBan().getMaLoaiBan().equals(loaiBanCu)) {
            showAlert(Alert.AlertType.ERROR,
                    "Không được đổi sang loại bàn khác!\nLoại bàn cũ: " + loaiBanCu);
            return;
        }
        if (!danhSachBanDangChon.isEmpty()) {
            Ban banCu = danhSachBanDangChon.get(0);
            Button btnCu = danhSachButtonDangChonUI.get(0);
            btnCu.setText(banCu.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
            btnCu.setStyle(getStyleByStatusAndType(banCu.getTrangThai(), banCu.getLoaiBan().getMaLoaiBan()));
            danhSachBanDangChon.clear();
            danhSachButtonDangChonUI.clear();
        }
        danhSachBanDangChon.add(ban);
        danhSachButtonDangChonUI.add(btnBan);

        btnBan.setText("✔ " + ban.getMaBan() + "\n(" + ban.getLoaiBan().getSoLuong() + " chỗ)");
        btnBan.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");
        txtSoBan.setText(String.valueOf(danhSachBanDangChon.size()));
    }

    private void danhDauBanDangDat(Ban banDaDat) {
    	for (javafx.scene.Node node : gridPaneBan.getChildren()) {

    	    if (node instanceof Button) {
    	        
    	        Button btn = (Button) node;

    	        String ma = btn.getText().replace("✔", "").trim();

    	        if (ma.equals(banDaDat.getMaBan())) {
    	            danhSachBanDangChon.add(banDaDat);
    	            danhSachButtonDangChonUI.add(btn);

    	            btn.setText("✔ " + banDaDat.getMaBan());
    	            btn.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold;");

    	            txtSoBan.setText("1");
    	            break;
    	        }
            }
        }
    }

    private void filterBanTheoTrangThai() {
        String trangThaiChon = cmbTrangThai.getValue();
        gridPaneBan.getChildren().clear();
        if (trangThaiChon.equals("Tất cả")) {
            loadDanhSachBan();
            return;
        }
        int col = 0, row = 0;
        final int MAX_COLS = 5;

        for (Ban ban : danhSachBan) {
            if (!ban.getTrangThai().equals(trangThaiChon)) continue;
            Button btnBan = taoNutBan(ban);
            gridPaneBan.add(btnBan, col, row);
            col++;
            if (col >= MAX_COLS) {
                col = 0;
                row++;
            }
        }
    }

    private void locBanTheoNgayGio() {
        LocalDate ngay = dpNgayDatBan.getValue();
        String gioStr = cmbGioBatDau.getValue();
        if (ngay == null || gioStr == null) return;
        int gio = Integer.parseInt(gioStr.substring(0, 2));
        LocalTime gioBatDau = LocalTime.of(gio, 0);
        gridPaneBan.getChildren().clear();

        int col = 0, row = 0;
        final int MAX_COLS = 5;
        for (Ban ban : danhSachBan) {
            boolean trung = coTrungLich(ban, ngay, gioBatDau);
            Button btnBan = new Button(ban.getMaBan());
            btnBan.setPrefSize(170, 110);
            
            btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
            if (trung) {
                // hiển thị màu đỏ khi đã được đặt
                btnBan.setStyle(getStyleByStatusAndType("Đã được đặt", ban.getLoaiBan().getMaLoaiBan()));
            } else {
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
                btnBan.setOnMouseClicked(e -> handleChonBan(ban, btnBan));
            }

            GridPane.setMargin(btnBan, new Insets(5));
            gridPaneBan.add(btnBan, col, row);
            col++;
            if (col >= MAX_COLS) {
                col = 0;
                row++;
            }
        }
    }

    private boolean coTrungLich(Ban ban, LocalDate ngay, LocalTime gioMoi) {
        List<DonDatBan> dsDon = donDatBanDAO.timTheoBan(ban);
        if (dsDon == null || dsDon.isEmpty()) return false;
        for (DonDatBan don : dsDon) {
            if (donDatBanDuocChon != null &&
                don.getMaDatBan().equals(donDatBanDuocChon.getMaDatBan()))
                continue;

            LocalDate ngayDat = don.getNgayGioLapDon().toLocalDate();
            LocalTime gioDat = don.getGioBatDau();

            if (!ngayDat.equals(ngay)) continue;
            LocalTime gioKetThuc = gioDat.plusHours(2);
            if (!gioMoi.isBefore(gioDat) && !gioMoi.isAfter(gioKetThuc)) {
                return true;
            }
        }
        return false;
    }

    private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {
        String backgroundColor = "white";

        switch (trangThai) {
            case "Đã được đặt": backgroundColor = "#ff0000"; break;
            case "Trống": backgroundColor = "#00aa00"; break;
            case "Đang phục vụ": backgroundColor = "#ec9407"; break;
        }

        return "-fx-background-color: " + backgroundColor + ";" +
               "-fx-background-radius: 15;" +
               "-fx-padding: 10;" +
               "-fx-font-size: 18px;" +
               "-fx-font-weight: bold;" +
               "-fx-text-fill: white;" +
               "-fx-font-family: 'Times New Roman';" +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);";
    }
    
    private void loadThongTinBan(Ban ban, DonDatBan don) {
        try {
            if (don.getNgayGioLapDon() != null) {
                dpNgayDatBan.setValue(don.getNgayGioLapDon().toLocalDate());
            }
            if (don.getGioBatDau() != null) {
                String gio = String.format("%02d:00", don.getGioBatDau().getHour());
                cmbGioBatDau.setValue(gio);
            }
            if (ban.getLoaiBan() != null) {
                cmbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());
            }
            if (ban.getTrangThai() != null) {
                cmbTrangThai.setValue(ban.getTrangThai());
            }
            txtSoBan.setText("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    private void moFormThongTinKhachHang() {
        if (donDatBanDuocChon == null) {
            showAlert(Alert.AlertType.WARNING, "Không tìm thấy thông tin đơn đặt bàn!");
            return;
        }
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setTitle("Thông tin khách hàng");
        dialog.setHeaderText("Thông tin khách hàng");
        TextField txtSDT = new TextField(donDatBanDuocChon.getKhachHang().getSdt());
        TextField txtTenKH = new TextField(donDatBanDuocChon.getKhachHang().getTenKH());
        DatePicker dpNgayDat = new DatePicker(
                donDatBanDuocChon.getNgayGioLapDon() != null ?
                        donDatBanDuocChon.getNgayGioLapDon().toLocalDate() : LocalDate.now()
        );        
        ComboBox<String> cmbGioDat = new ComboBox<>();
        for (int gio = 8; gio <= 23; gio++) {
            cmbGioDat.getItems().add(String.format("%02d:00", gio));
        }
        if (donDatBanDuocChon.getGioBatDau() != null) {
            cmbGioDat.setValue(String.format("%02d:00", donDatBanDuocChon.getGioBatDau().getHour()));
        } else {
            cmbGioDat.setValue("08:00");
        }
        TextField txtSoLuong = new TextField(String.valueOf(donDatBanDuocChon.getSoLuong()));

        GridPane pane = new GridPane();
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setPadding(new Insets(15));

        pane.addRow(0, new javafx.scene.control.Label("Số điện thoại:"), txtSDT);
        pane.addRow(1, new javafx.scene.control.Label("Tên khách hàng:"), txtTenKH);
        pane.addRow(2, new javafx.scene.control.Label("Ngày đặt:"), dpNgayDat);
        pane.addRow(3, new javafx.scene.control.Label("Giờ đặt:"), cmbGioDat);
        pane.addRow(4, new javafx.scene.control.Label("Số lượng:"), txtSoLuong);

        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().clear();
        dialog.getDialogPane().getButtonTypes().addAll(
                javafx.scene.control.ButtonType.OK,
                javafx.scene.control.ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == javafx.scene.control.ButtonType.OK) {
                try {
                    if (txtSDT.getText().isEmpty() || txtTenKH.getText().isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Không được để trống SĐT hoặc Tên khách hàng!");
                        return;
                    }
                    LocalDate ngayDat = dpNgayDat.getValue();
                    if (ngayDat == null) {
                        showAlert(Alert.AlertType.WARNING, "Ngày đặt không hợp lệ!");
                        return;
                    }
                    String gioStr = cmbGioDat.getValue();
                    int gio = Integer.parseInt(gioStr.substring(0, 2));
                    LocalTime gioDat = LocalTime.of(gio, 0);
                    int soLuong = Integer.parseInt(txtSoLuong.getText());
                    
                    donDatBanDuocChon.getKhachHang().setSdt(txtSDT.getText());
                    donDatBanDuocChon.getKhachHang().setTenKH(txtTenKH.getText());
                    donDatBanDuocChon.setNgayGioLapDon(ngayDat.atTime(gioDat));
                    donDatBanDuocChon.setGioBatDau(gioDat);
                    donDatBanDuocChon.setSoLuong(soLuong);
                    donDAO.capNhat(donDatBanDuocChon);
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật thông tin khách hàng thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi khi cập nhật dữ liệu!");
                }
            }
        });
    }


    
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
