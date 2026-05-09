package controller.DatBan;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import entity.MonAn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ABanHienTai_Controller {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnBan;

    @FXML
    private Button btnChoThanhToan;

    @FXML
    private Button btnDatTruoc;

    @FXML
    private Button btnDatBan;

    @FXML
    private Button btnKhac;

    @FXML
    private Button btnMonAn;

    @FXML
    private Button btnThanhToan;

    @FXML
    private ComboBox<String> cmbLoaiBan;

    @FXML
    private GridPane gridPaneHD;

    @FXML
    private BorderPane paneBan;

    @FXML
    private BorderPane paneEast;

    @FXML
    private BorderPane paneNorth;

    @FXML
    private BorderPane paneTien;

    @FXML
    private BorderPane paneWest;

    @FXML
    private TextField txtTongDatBan;

    private Client client;

    private List<HoaDon_DTO> dsHoaDon = new ArrayList<>();
    private List<Ban_DTO> dsBan = new ArrayList<>();
    private int tongDatBan;

    public static ABanHienTai_Controller aBHT;
    boolean checkTTbangKo;
    private List<ChiTietHoaDon_DTO> dsCTHD_DB;

    Map<MonAn, Integer> dsMonAnTA = new HashMap<>();

    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnDatBan) {
            MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");
        } else if (source == btnChoThanhToan) {
            loadDanhSachHoaDon();
        } else if (source == btnDatTruoc) {

        } else if (source == btnBan) {
            MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
        } else if (source == btnMonAn) {
            MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
        }
    }

    @FXML
    public void initialize() {
        aBHT = this;
        checkTTbangKo = false;
        try {
            client = new Client();
        } catch (Exception e) {
            e.printStackTrace();
        }
        taiLaiDuLieuTuServer();
        capNhatTrangThaiBanMacDinh();
        capNhatTrangThaiBanTheoDonDatTruoc();
        khoiTaoComboBoxes();
        loadDanhSachHoaDon();
        cmbLoaiBan.setOnAction(e -> loadDanhSachHoaDon());
        txtTongDatBan.setEditable(false);
        txtTongDatBan.setText(tongDatBan + "");

        Tooltip toolTipLB = new Tooltip("Lọc danh sách theo loại bàn");
        cmbLoaiBan.setTooltip(toolTipLB);

        Tooltip toolTipDB = new Tooltip("Đặt bàn mới");
        btnDatBan.setTooltip(toolTipDB);
    }

    private void taiLaiDuLieuTuServer() {
        try {
            Response rHd = client.send(new Request(CommandType.HOADON_GET_ALL, null));
            if (rHd != null && rHd.isSuccess() && rHd.getData() != null) {
                @SuppressWarnings("unchecked")
                List<HoaDon_DTO> listHd = (List<HoaDon_DTO>) rHd.getData();
                dsHoaDon = listHd;
            } else {
                dsHoaDon = new ArrayList<>();
            }
            Response rBan = client.send(new Request(CommandType.BAN_GET_ALL, null));
            if (rBan != null && rBan.isSuccess() && rBan.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Ban_DTO> listBan = (List<Ban_DTO>) rBan.getData();
                dsBan = listBan;
            } else {
                dsBan = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            dsHoaDon = new ArrayList<>();
            dsBan = new ArrayList<>();
        }
    }

    private void capNhatTrangThaiBanMacDinh() {
        for (Ban_DTO ban : dsBan) {
            if (!ban.getTrangThai().equals("Trống")) {
                ban.setTrangThai("Trống");
                try {
                    client.send(new Request(CommandType.BAN_UPDATE, ban));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void capNhatTrangThaiBanTheoDonDatTruoc() {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowHour = LocalTime.now();
        List<DonDatBan_DTO> dsDDBnow = new ArrayList<>();

        for (HoaDon_DTO hd : dsHoaDon) {
            if (hd.getTrangThai().equals("Đặt trước") && hd.getDonDatBan() != null
                    && hd.getDonDatBan().getNgayGioLapDon() != null) {
                LocalDate ngayGioLapDon = hd.getDonDatBan().getNgayGioLapDon().toLocalDate();
                if (ngayGioLapDon.equals(nowDate)) {
                    if (nowHour.isAfter(hd.getDonDatBan().getGioBatDau().minusHours(1))
                            && nowHour.isBefore(hd.getDonDatBan().getGioBatDau().plusHours(1))) {
                        dsDDBnow.add(hd.getDonDatBan());
                    }
                }
            }
        }

        for (DonDatBan_DTO ddb : dsDDBnow) {
            for (Ban_DTO ban : dsBan) {
                if (ddb.getBan() != null && ban.getMaBan().equals(ddb.getBan().getMaBan())) {
                    ban.setTrangThai("Đã được đặt");
                    try {
                        client.send(new Request(CommandType.BAN_UPDATE, ban));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Tên loại bàn: ưu tiên DTO từ hóa đơn (sau khi server enrich), fallback theo {@link #dsBan}.
     */
    private String tenLoaiBanTuHoaDon(HoaDon_DTO hoaDon) {
        if (hoaDon == null || hoaDon.getDonDatBan() == null || hoaDon.getDonDatBan().getBan() == null) {
            return "";
        }
        Ban_DTO b = hoaDon.getDonDatBan().getBan();
        if (b.getTenLoaiBan() != null && !b.getTenLoaiBan().isBlank()) {
            return b.getTenLoaiBan();
        }
        String maBan = b.getMaBan();
        if (maBan == null) {
            return "";
        }
        for (Ban_DTO ban : dsBan) {
            if (maBan.equals(ban.getMaBan()) && ban.getTenLoaiBan() != null && !ban.getTenLoaiBan().isBlank()) {
                return ban.getTenLoaiBan();
            }
        }
        return "";
    }

    private void khoiTaoComboBoxes() {
        cmbLoaiBan.getItems().clear();
        cmbLoaiBan.getItems().add("Tất cả");
        for (Ban_DTO ban : dsBan) {
            String tenLoai = ban.getTenLoaiBan();
            if (tenLoai != null && !cmbLoaiBan.getItems().contains(tenLoai)) {
                cmbLoaiBan.getItems().add(tenLoai);
            }
        }
        cmbLoaiBan.getSelectionModel().select("Tất cả");
    }

    private void loadDanhSachHoaDon() {
        tongDatBan = 0;
        gridPaneHD.getChildren().clear();
        taiLaiDuLieuTuServer();

        int col = 0, row = 0;
        final int MAX_COLS = 9;

        String loaiBanLoc = cmbLoaiBan.getValue();
        LocalDate localDate = LocalDate.now();
        Date dateNow = Date.valueOf(localDate);

        for (final HoaDon_DTO hoaDon : dsHoaDon) {
            String tenLoaiHd = tenLoaiBanTuHoaDon(hoaDon);
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(tenLoaiHd);

            if (matchType && hoaDon.getNgayLap().equals(dateNow) && hoaDon.getKieuThanhToan().equals("Chưa thanh toán")
                    && hoaDon.getDonDatBan() != null
                    && hoaDon.getDonDatBan().getTrangThai().equals("Đã nhận bàn")) {

                BorderPane borderPane = new BorderPane();

                BorderPane paneNorth = new BorderPane();
                borderPane.setTop(paneNorth);
                paneNorth.setCenter(new Label("Hóa đơn"));
                paneNorth.setStyle(
                        "-fx-padding: 10; -fx-background-color: lightblue; -fx-font-size: 20px; -fx-font-weight: bold;");

                BorderPane paneWest = new BorderPane();
                borderPane.setLeft(paneWest);
                paneWest.setStyle(getBorderStyle());

                BorderPane paneBan = new BorderPane();
                paneWest.setTop(paneBan);
                String maBanTxt = hoaDon.getDonDatBan().getBan() != null ? hoaDon.getDonDatBan().getBan().getMaBan() : "";
                String tenLoaiTxt = tenLoaiBanTuHoaDon(hoaDon);
                paneBan.setCenter(new Label(maBanTxt + "\n" + tenLoaiTxt));
                paneBan.setStyle(getBorderStyle());

                Button btnThanhToan = taoButtonIcon("TT", "/img/pay.png");
                paneWest.setLeft(btnThanhToan);
                BorderPane.setMargin(btnThanhToan, new Insets(5, 10, 5, 10));
                btnThanhToan.setOnMouseClicked(event -> {
                    double tongTien = tinhTongTienMon(hoaDon);
                    if (tongTien > 0) {
                        MenuNV_Controller.aBanHienTai_HD = hoaDon;
                        MenuNV_Controller.instance.readyUI("DatBan/aThanhToan");
                    } else {
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Thông báo");
                        dialog.setHeaderText("Vui lòng đặt món");
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                        Optional<ButtonType> result = dialog.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            checkTTbangKo = true;
                            MenuNV_Controller.aBanHienTai_HD = hoaDon;
                            MenuNV_Controller.instance.readyUI("DatBan/aDatMon");
                            try {
                                Response res = client.send(new Request(CommandType.CTHD_GET_BY_MAHD, hoaDon.getMaHD()));
                                if (res != null && res.isSuccess() && res.getData() != null) {
                                    @SuppressWarnings("unchecked")
                                    List<ChiTietHoaDon_DTO> dsCT = (List<ChiTietHoaDon_DTO>) res.getData();
                                    dsCTHD_DB = dsCT;
                                } else {
                                    dsCTHD_DB = new ArrayList<>();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                dsCTHD_DB = new ArrayList<>();
                            }
                            dsMonAnTA.clear();
                            for (ChiTietHoaDon_DTO cthd : dsCTHD_DB) {
                                MonAn mon = new MonAn(cthd.getMaMonAn(), cthd.getTenMon(), cthd.getDonGia(), null, null);
                                dsMonAnTA.put(mon, cthd.getSoLuong());
                            }
                        }
                    }
                });

                Button btnBan = taoButtonIcon("B", "/img/table.png");
                paneWest.setRight(btnBan);
                BorderPane.setMargin(btnBan, new Insets(5, 10, 5, 10));
                btnBan.setOnMouseClicked(event -> {
                    if (hoaDon.getDonDatBan() != null) {
                        MenuNV_Controller.donDatBanDangDoi = hoaDon.getDonDatBan();
                    }
                    MenuNV_Controller.instance.readyUI("DatBan/aDoiBan");
                });

                BorderPane paneEast = new BorderPane();
                borderPane.setRight(paneEast);
                paneEast.setStyle(getBorderStyle());

                BorderPane paneTien = new BorderPane();
                paneEast.setTop(paneTien);
                double tongTien = tinhTongTienMon(hoaDon);
                Label lblTien = new Label(String.format(" %,.0f VND \n", tongTien));

                paneTien.setCenter(lblTien);

                Button btnMonAn = taoButtonIcon("MA", "/img/food.png");
                paneEast.setLeft(btnMonAn);
                BorderPane.setMargin(btnMonAn, new Insets(5, 10, 5, 10));
                btnMonAn.setOnMouseClicked(event -> {
                    MenuNV_Controller.aBanHienTai_HD = hoaDon;
                    if (hoaDon.getDonDatBan() != null) {
                        MenuNV_Controller.donDatBanDangDoi = hoaDon.getDonDatBan();
                    }
                    MenuNV_Controller.instance.readyUI("DatBan/aDoiMon");
                });

                Button btnKhac = taoButtonIcon("K", "/img/cancel.png");
                paneEast.setRight(btnKhac);
                BorderPane.setMargin(btnKhac, new Insets(5, 10, 5, 10));
                btnKhac.setOnMouseClicked(event -> {

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận hủy đơn");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Khách hàng xác nhận hủy đơn đặt bàn này?");
                    Optional<ButtonType> rs = confirm.showAndWait();

                    if (rs.isEmpty() || rs.get() != ButtonType.OK)
                        return;
                    try {
                        DonDatBan_DTO ddb = hoaDon.getDonDatBan();
                        if (ddb == null)
                            return;
                        ddb.setTrangThai("Đã hủy");
                        Response r1 = client.send(new Request(CommandType.DONDATBAN_UPDATE, ddb));
                        if (r1 == null || !r1.isSuccess()) {
                            showAlert(Alert.AlertType.ERROR, "Không thể hủy đơn đặt bàn!");
                            return;
                        }

                        hoaDon.setTrangThai("Đã hủy");
                        Response r2 = client.send(new Request(CommandType.HOADON_UPDATE, hoaDon));
                        if (r2 == null || !r2.isSuccess()) {
                            showAlert(Alert.AlertType.ERROR, "Không thể cập nhật hóa đơn!");
                            return;
                        }

                        Ban_DTO ban = ddb.getBan();
                        if (ban != null) {
                            ban.setTrangThai("Trống");
                            Response r3 = client.send(new Request(CommandType.BAN_UPDATE, ban));
                            if (r3 == null || !r3.isSuccess()) {
                                showAlert(Alert.AlertType.ERROR, "Không thể cập nhật trạng thái bàn!");
                                return;
                            }
                        }

                        Response rKh = client.send(new Request(CommandType.DONDATBAN_GET_KH_BY_MADATBAN, ddb.getMaDatBan()));
                        KhachHang_DTO kh = null;
                        if (rKh != null && rKh.isSuccess()) {
                            kh = (KhachHang_DTO) rKh.getData();
                        }

                        showAlert(Alert.AlertType.INFORMATION,
                                "Đã hủy đơn đặt bàn của khách " + (kh != null ? kh.getTenKH() : "") + " theo yêu cầu.");
                        loadDanhSachHoaDon();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Không thể hủy đơn đặt bàn!");
                    }
                });

                GridPane.setMargin(borderPane, new Insets(5.0));
                gridPaneHD.add(borderPane, col, row);

                capNhatTrangThaiBanTheoHD(hoaDon);
                tongDatBan += 1;

                col++;
                if (col >= MAX_COLS) {
                    col = 0;
                    row++;
                }
            }
        }
        txtTongDatBan.setText(tongDatBan + "");
    }

    private void capNhatTrangThaiBanTheoHD(HoaDon_DTO hd) {
        if (hd.getDonDatBan() == null || hd.getDonDatBan().getBan() == null) {
            return;
        }
        String maBanHd = hd.getDonDatBan().getBan().getMaBan();
        for (Ban_DTO ban : dsBan) {
            if (maBanHd != null && maBanHd.equals(ban.getMaBan())) {
                ban.setTrangThai("Đang phục vụ");
                try {
                    client.send(new Request(CommandType.BAN_UPDATE, ban));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String getBorderStyle() {
        return "-fx-border-color: gray; " + "-fx-border-width: 2; " + "-fx-background-color: white; "
                + "-fx-border-radius: 10; " + "-fx-padding: 10;";
    }

    private String getButtonStyle() {
        return "-fx-background-color: lightgreen; " + "-fx-background-radius: 5; " + "-fx-padding: 10; "
                + "-fx-font-size: 14px; " + "-fx-font-weight: bold; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);";
    }

    private double tinhTongTienMon(HoaDon_DTO hoaDon) {
        try {
            Response res = client.send(new Request(CommandType.CTHD_GET_BY_MAHD, hoaDon.getMaHD()));
            if (res == null || !res.isSuccess() || res.getData() == null) {
                return 0;
            }
            @SuppressWarnings("unchecked")
            List<ChiTietHoaDon_DTO> dsCT = (List<ChiTietHoaDon_DTO>) res.getData();
            double tong = 0;
            for (ChiTietHoaDon_DTO ct : dsCT) {
                tong += ct.getSoLuong() * ct.getDonGia();
            }
            return tong;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Button taoButtonIcon(String text, String iconPath) {
        Button btn = new Button();
        btn.setStyle(getButtonStyle());
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);

        var resource = getClass().getResource(iconPath);
        if (resource != null) {
            ImageView icon = new ImageView(resource.toExternalForm());
            icon.setFitWidth(30);
            icon.setFitHeight(30);
            btn.setGraphic(icon);
        } else {
            btn.setText(text);
            btn.setContentDisplay(javafx.scene.control.ContentDisplay.TEXT_ONLY);
        }

        return btn;
    }

}
