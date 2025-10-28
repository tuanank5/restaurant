package controller.Ban;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import config.RestaurantApplication;
import dao.Ban_DAO;
import dao.LoaiBan_DAO;
import entity.Ban;
import entity.LoaiBan;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ThemBan_Controller {

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtViTri;

    @FXML
    private ComboBox<String> comBoxTrangThai;

    @FXML
    private ComboBox<LoaiBan> comBoxLoaiBan;

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnTroLai;

    private Ban banSua; // Nếu sửa thì dùng

    @FXML
    public void initialize() {
        Platform.runLater(() -> txtViTri.requestFocus());
        loadLoaiBan();
        comBoxTrangThai.getItems().addAll("Trống", "Đang sử dụng", "Đang đặt");
    }

    private void loadLoaiBan() {
        List<LoaiBan> list = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(LoaiBan_DAO.class)
                .getDanhSach(LoaiBan.class, null);
        comBoxLoaiBan.getItems().addAll(list);
    }

    public void setBanSua(Ban ban) {
        this.banSua = ban;
        if (ban != null) {
            txtMaBan.setText(ban.getMaBan());
            txtMaBan.setDisable(true); // Mã bàn không sửa
            txtViTri.setText(ban.getViTri());
            comBoxTrangThai.setValue(ban.getTrangThai());
            comBoxLoaiBan.setValue(ban.getLoaiBan());
        }
    }

    @FXML
    void controller(ActionEvent event) {
        Object src = event.getSource();
        if (src == btnLuu) {
            luuBan();
        } else if (src == btnTroLai) {
            troLai();
        }
    }

    private void luuBan() {
        if (txtMaBan.getText().isEmpty() || txtViTri.getText().isEmpty()
                || comBoxTrangThai.getValue() == null || comBoxLoaiBan.getValue() == null) {
            showAlert("Cảnh báo", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        Ban banMoi = new Ban(
                txtMaBan.getText(),
                txtViTri.getText(),
                comBoxTrangThai.getValue(),
                comBoxLoaiBan.getValue()
        );

        boolean ok;
        if (banSua != null) {
            // Sửa bàn
            ok = RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(Ban_DAO.class)
                    .capNhat(banMoi);
        } else {
        	// Thêm bàn
            ok = RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(Ban_DAO.class)
                    .them(banMoi);
        }

        if (ok) {
            showAlert("Thông báo", (banSua != null ? "Sửa" : "Thêm") + " bàn thành công!", Alert.AlertType.INFORMATION);
            resetForm();
        } else {
            showAlert("Thông báo", (banSua != null ? "Sửa" : "Thêm") + " bàn thất bại!", Alert.AlertType.WARNING);
        }
    }

    private void troLai() {
        Optional<ButtonType> confirm = showAlertConfirm("Bạn có muốn quay lại? Nếu chưa lưu dữ liệu sẽ mất.");
        if (confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/Ban/QuanLyBan.fxml"));
                Parent root = loader.load();
                borderPane.setCenter(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetForm() {
        txtMaBan.setText("");
        txtViTri.setText("");
        comBoxTrangThai.setValue(null);
        comBoxLoaiBan.setValue(null);
        txtViTri.requestFocus();
        banSua = null;
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(msg);
        a.show();
    }

    private Optional<ButtonType> showAlertConfirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Xác nhận");
        a.setHeaderText(msg);
        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(yes, no);
        return a.showAndWait();
    }
}
        