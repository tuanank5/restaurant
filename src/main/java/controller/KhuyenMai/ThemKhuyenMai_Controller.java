package controller.KhuyenMai;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

import config.RestaurantApplication;
import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ThemKhuyenMai_Controller {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnTroLai;

    @FXML
    private Label lblDanhSach;

    @FXML
    private TextField txtMaKM;

    @FXML
    private TextField txtTenKM;

    @FXML
    private TextField txtLoaiKM;

    @FXML
    private TextField txtSanPhamKM;

    @FXML
    private TextField txtPhanTram;

    @FXML
    private DatePicker dpNgayBatDau;

    @FXML
    private DatePicker dpNgayKetThuc;

    private String ui;


    @FXML
    public void initialize() {
        Platform.runLater(() -> txtTenKM.requestFocus());
    }

    public FXMLLoader readyUI(String ui) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/view/fxml/" + ui + ".fxml"));
            root = loader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    @FXML
    void controller(ActionEvent event) {
        Object src = event.getSource();

        if (src == btnTroLai) {
            xacNhanLuu("KhuyenMai/KhuyenMai");
        } else if (src == btnLuu) {
            luu();
        }
    }

    @FXML
    void mouseClicked(MouseEvent event) {
        if (event.getSource() == lblDanhSach) {
            xacNhanLuu("KhuyenMai/KhuyenMai");
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            xacNhanLuu("KhuyenMai/KhuyenMai");
        } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
            luu();
        }
    }

    private void xacNhanLuu(String ui) {
        KhuyenMai km = getKhuyenMaiNew();

        Optional<ButtonType> btn = showAlertConfirm("Bạn có muốn lưu thông tin không?");
        if (btn.get().getButtonData() == ButtonBar.ButtonData.YES) {
            if (km != null) {
                boolean check = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(KhuyenMai_DAO.class)
                        .them(km);

                if (check) {
                    showAlert("Thông báo", "Thêm khuyến mãi thành công!", Alert.AlertType.INFORMATION);
                }
            }
        }
        readyUI(ui);
    }

    private void luu() {
        KhuyenMai km = getKhuyenMaiNew();

        if (km != null) {
            boolean check = RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(KhuyenMai_DAO.class).them(km);

            if (check) {
                showAlert("Thông báo", "Thêm thành công!", Alert.AlertType.INFORMATION);
                resetField();
            }
        }
    }

    private KhuyenMai getKhuyenMaiNew() {
        if (txtTenKM.getText().isEmpty()) {
            showAlert("Cảnh báo", "Tên KM không được rỗng!", Alert.AlertType.WARNING);
            return null;
        }

        if (txtPhanTram.getText().isEmpty()) {
            showAlert("Cảnh báo", "Không bỏ trống phần trăm!", Alert.AlertType.WARNING);
            return null;
        }


        int percent;
        try {
            percent = Integer.parseInt(txtPhanTram.getText());
            if (percent < 0 || percent > 100) {
                showAlert("Cảnh báo", "Phần trăm 0 - 100!", Alert.AlertType.WARNING);
                return null;
            }
        } catch (Exception e) {
            showAlert("Cảnh báo", "Phần trăm phải là số!", Alert.AlertType.WARNING);
            return null;
        }

        String ma = "KM" + System.currentTimeMillis();

        return new KhuyenMai(
                ma,
                txtTenKM.getText(),
                txtLoaiKM.getText(),
                txtSanPhamKM.getText(),
                Date.valueOf(dpNgayBatDau.getValue()),
                Date.valueOf(dpNgayKetThuc.getValue()),
                percent
        );
    }

    private void resetField() {
        txtTenKM.setText("");
        txtLoaiKM.setText("");
        txtSanPhamKM.setText("");
        txtPhanTram.setText("");
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(msg);
        a.show();
    }

    private Optional<ButtonType> showAlertConfirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Thông báo");
        a.setHeaderText(msg);
        ButtonType y = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType n = new ButtonType("Không", ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(y, n);
        return a.showAndWait();
    }

    public void setUrl(String name, String ui) {
        lblDanhSach.setText(name);
        this.ui = ui;
    }
}


