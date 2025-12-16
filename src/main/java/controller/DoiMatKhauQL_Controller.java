package controller;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;

public class DoiMatKhauQL_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnLuuLai;

    @FXML
    private Button btnQuayLai;

    @FXML
    private PasswordField mkCuTextField;

    @FXML
    private PasswordField mkMoiTextField;

    @FXML
    private PasswordField mkNhapLaiTextField;

    @FXML
    void controller(ActionEvent event) {

    }
    @FXML
    void btnLuuLai(ActionEvent event) {
        TaiKhoan taiKhoan = MenuNVQL_Controller.taiKhoan;
        String mkCu = mkCuTextField.getText();
        String mkMoi = mkMoiTextField.getText();
        String mkNhapLai = mkNhapLaiTextField.getText();

        if (mkCu.isEmpty() || mkMoi.isEmpty() || mkNhapLai.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!taiKhoan.getMatKhau().equals(mkCu)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu cũ không đúng");
            lamMoi();
            return;
        }

        if (!mkMoi.equals(mkNhapLai)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu mới không khớp");
            lamMoi();
            return;
        }

        if (mkMoi.equals(mkCu)) {
            showAlert(Alert.AlertType.WARNING, "Mật khẩu mới phải khác mật khẩu cũ");
            lamMoi();
            return;
        }

        taiKhoan.setMatKhau(mkMoi);
        RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(TaiKhoan_DAO.class)
                .capNhat(taiKhoan);

        showAlert(Alert.AlertType.INFORMATION, "Đổi mật khẩu thành công");
        lamMoi();
    }

    @FXML
    void btnLamMoi(ActionEvent event) {
        lamMoi();
    }

    private void lamMoi() {
        mkCuTextField.clear();
        mkMoiTextField.clear();
        mkNhapLaiTextField.clear();
    }

    @FXML
    void btnQuayLai(ActionEvent event) {
        MenuNVQL_Controller.instance.dashBoard();
    }

    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
