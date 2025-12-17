package controller.DatBan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.ButtonBar;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.LoaiBan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.LoaiBan_DAOImpl;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ADoiBan_Controller implements Initializable {

    @FXML
    private Button btnBan;

    @FXML
    private Button btnTroLai;

    @FXML
    private Button btnXacNhan;

    @FXML
    private ComboBox<String> cmbLoaiBan;

    @FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private GridPane gridPaneBan;


    @FXML
    void btnXacNhan(ActionEvent event) {

        if (banMoiDuocChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn mới!");
            return;
        }

        int soChoToiDa = banMoiDuocChon.getLoaiBan().getSoLuong();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Xác nhận đổi bàn");
        dialog.setHeaderText("Nhập số lượng khách");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label lblBanMoi = new Label(
                "Bàn mới: " + banMoiDuocChon.getMaBan() +
                " (" + banMoiDuocChon.getLoaiBan().getTenLoaiBan() + ")"
        );

        TextField txtSoLuong = new TextField();
        txtSoLuong.setPromptText("Tối đa " + soChoToiDa + " khách");

        grid.add(lblBanMoi, 0, 0, 2, 1);
        grid.add(new Label("Số lượng khách:"), 0, 1);
        grid.add(txtSoLuong, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get().getButtonData() != ButtonBar.ButtonData.OK_DONE) {
            return;
        }

        // Validate số lượng
        int soLuongMoi;
        try {
            soLuongMoi = Integer.parseInt(txtSoLuong.getText());
            if (soLuongMoi <= 0 || soLuongMoi > soChoToiDa) {
                showAlert(Alert.AlertType.ERROR,
                        "Số lượng phải từ 1 đến " + soChoToiDa);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Số lượng không hợp lệ!");
            return;
        }

        // GỌI HÀM ĐỔI BÀN
        thucHienDoiBan(soLuongMoi);
    }
    
    private void thucHienDoiBan(int soLuongMoi) {

        // 1. Kiểm tra đơn đặt bàn
        DonDatBan don = MenuNV_Controller.donDatBanDangDoi;
        if (don == null) {
            showAlert(Alert.AlertType.ERROR, "Không tìm thấy đơn đặt bàn cần đổi!");
            return;
        }

        // 2. Kiểm tra bàn cũ
        Ban banCu = don.getBan();
        if (banCu == null) {
            showAlert(Alert.AlertType.ERROR, "Đơn đặt bàn chưa có bàn cũ!");
            return;
        }

        // 3. Kiểm tra bàn mới
        if (banMoiDuocChon == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn mới trước khi xác nhận!");
            return;
        }

        // 4. Kiểm tra số lượng chỗ
        if (soLuongMoi <= 0) {
            showAlert(Alert.AlertType.WARNING, "Số lượng chỗ không hợp lệ!");
            return;
        }

        try {
            // 5. Cập nhật trạng thái bàn cũ
            banCu.setTrangThai("Trống");
            banDAO.capNhat(banCu);

            // 6. Cập nhật trạng thái bàn mới
            banMoiDuocChon.setTrangThai("Đã được đặt");
            banDAO.capNhat(banMoiDuocChon);

            // 7. Cập nhật đơn đặt bàn
            don.setBan(banMoiDuocChon);
            don.setSoLuong(soLuongMoi); // ⭐ CÀI LẠI SỐ LƯỢNG CHỖ
            don.setTrangThai("Chưa nhận bàn");

            donDAO.capNhat(don);

            showAlert(Alert.AlertType.INFORMATION, "Đổi bàn thành công!");

            // 8. Quay về màn hình hiện tại
            MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Đổi bàn thất bại! Vui lòng thử lại.");
        }
    }


    @FXML
    void btnQuayLai(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
    }
	
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
	public void initialize(URL location, ResourceBundle resources) {
        khoiTaoComboBoxes();
        loadDanhSachBan();
        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
		
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
    
    private Ban banMoiDuocChon = null; // đổi bàn
    private Button btnBanDangChonUI = null;

    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);

        int col = 0, row = 0;
        final int MAX_COLS = 6;

        String trangThaiLoc = cmbTrangThai.getValue();
        String loaiBanLoc = cmbLoaiBan.getValue();

        for (Ban ban : danhSachBan) {
            boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan().getTenLoaiBan());

            if (matchStatus && matchType) {

                Button btnBan = new Button(
                        ban.getMaBan() + "\n" + ban.getLoaiBan().getTenLoaiBan()
                );
                btnBan.setPrefSize(185, 110);
                btnBan.setStyle(
                        getStyleByStatusAndType(
                                ban.getTrangThai(),
                                ban.getLoaiBan().getMaLoaiBan()
                        )
                );

                btnBan.setOnMouseClicked(event -> {

                    // ❌ Chỉ cho chọn bàn trống
                    if (!"Trống".equals(ban.getTrangThai())) {
                        showAlert(Alert.AlertType.WARNING,
                                "Chỉ được chọn bàn trống để đổi!");
                        return;
                    }

                    // Hoàn nguyên style bàn cũ
                    if (btnBanDangChonUI != null && banMoiDuocChon != null) {
                        btnBanDangChonUI.setStyle(
                                getStyleByStatusAndType(
                                        banMoiDuocChon.getTrangThai(),
                                        banMoiDuocChon.getLoaiBan().getMaLoaiBan()
                                )
                        );
                    }

                    // Lưu bàn mới
                    banMoiDuocChon = ban;
                    btnBanDangChonUI = btnBan;

                    // Highlight bàn đang chọn
                    btnBan.setStyle(
                            "-fx-background-color: yellow;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-weight: bold;"
                    );
                });

                GridPane.setMargin(btnBan, new Insets(5));
                gridPaneBan.add(btnBan, col, row);

                col++;
                if (col >= MAX_COLS) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    
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

        private void showAlert(Alert.AlertType type, String msg) {
            Alert alert = new Alert(type);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }

}
