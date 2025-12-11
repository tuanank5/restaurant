package controller.DatBan;

import dao.KhuyenMai_DAO;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.HoaDon_DAO;
import dao.impl.HoaDon_DAOImpl;
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
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;


public class ADatBanHienTai_Controller implements Initializable {

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
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();

    private List<DonDatBan> dsDDB = new ArrayList<DonDatBan>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        khoiTaoComboBoxes();
        loadDanhSachBan();
        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
    }
    @FXML
    void btnQuayLai(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
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

                Button btnBan = new Button(ban.getMaBan() + "\n" + ban.getLoaiBan().getTenLoaiBan());
                btnBan.setPrefSize(185, 110);
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));

                btnBan.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {

                        // ❌ Bàn không trống → cảnh báo
                        if (!"Trống".equals(ban.getTrangThai())) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Không thể chọn bàn");
                            alert.setHeaderText("Bàn này không được phép chọn!");
                            alert.setContentText(
                                    "Trạng thái hiện tại: " + ban.getTrangThai()
                                            + "\nChỉ có thể chọn những bàn đang trống!"
                            );
                            alert.showAndWait();

                            btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
                            return;
                        }

                        // ✔ Bàn trống → mở dialog nhập số lượng khách
                        int soLuongChoNgoi = ban.getLoaiBan().getSoLuong();

                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Xác nhận chọn bàn");
                        dialog.setHeaderText("Bạn muốn chọn bàn này?");

                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);

                        Label lblMaBan = new Label("Mã bàn: " + ban.getMaBan());
                        Label lblLoaiBan = new Label("Loại bàn: " + ban.getLoaiBan().getTenLoaiBan());

                        TextField txtSoLuong = new TextField();
                        txtSoLuong.setPromptText("số lượng khách (tối đa " + soLuongChoNgoi + ")");

                        grid.add(lblMaBan, 0, 0);
                        grid.add(lblLoaiBan, 0, 1);
                        grid.add(new Label("Số lượng khách:"), 0, 2);
                        grid.add(txtSoLuong, 1, 2);

                        dialog.getDialogPane().setContent(grid);
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                        Optional<ButtonType> result = dialog.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            try {
                                int soLuongNhap = Integer.parseInt(txtSoLuong.getText());

                                if (soLuongNhap <= 0 || soLuongNhap > soLuongChoNgoi) {
                                    Alert err = new Alert(Alert.AlertType.ERROR);
                                    err.setHeaderText("Số lượng không hợp lệ!");
                                    err.setContentText("Vui lòng nhập số lượng từ 1 đến " + soLuongChoNgoi);
                                    err.show();
                                    return;
                                }

                                MenuNV_Controller.banDangChon = ban;
                                MenuNV_Controller.soLuongKhach = soLuongNhap;
                                MenuNV_Controller.khachHangDangChon = null;

                                MenuNV_Controller.instance.readyUI("DatBan/aDatMon");

                            } catch (NumberFormatException ex) {
                                Alert err = new Alert(Alert.AlertType.ERROR);
                                err.setHeaderText("Dữ liệu không hợp lệ");
                                err.setContentText("Vui lòng nhập số nguyên!");
                                err.show();
                            }

                        } else {
                            // Bấm Hủy
                            btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
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
    
    private boolean checkDonDatBan(Ban ban) {
		dsDDB = donDatBanDAO.getDanhSach("DonDatBan.list", DonDatBan.class);
		LocalTime now = LocalTime.now();
    	
		for (DonDatBan ddb : dsDDB) {
			if (ddb.getBan().getMaBan().equals(ban.getMaBan())) {
				if (now.isAfter(ddb.getGioBatDau().minusHours(1)) && now.isBefore(ddb.getGioBatDau().plusHours(1))) {
					return false;
				}
			}
		}
		return true;
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
                KhachHang kh = hoaDonDAO.getKhachHangTheoMaDatBan(donGan.getMaDatBan());
                MenuNV_Controller.khachHangDangChon = kh;
            }
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


//        switch (trangThai != null && !trangThai.is) {
//            case "Trống":
//                backgroundColor = "GREEN";  // Xanh lá
//                break;
//            case "Đã được đặt":
//                backgroundColor = "red";  // Đỏ
//                break;
//            case "Đang phục vụ":
//                backgroundColor = "#f39c12";  // Cam
//                break;
//            default:
//                backgroundColor = "#bdc3c7";  // Xám – trạng thái lạ → dễ nhận biết
//                break;
//        }

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