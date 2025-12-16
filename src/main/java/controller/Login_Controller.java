package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import controller.Menu.MenuNV_Controller;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Timestamp;

public class Login_Controller implements Initializable{
	
	private TaiKhoan taiKhoan;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    
    @FXML
    public void login(ActionEvent event) {
        try {
            // Lấy ra thông tin Tài Khoàn
            String user = txtUserName.getText();
            String pass = txtPassword.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                showAlert("Cảnh báo", "Vui lòng nhập đầy đủ tài khoản và mật khẩu", Alert.AlertType.WARNING);
            } if (!user.matches("^(QL|NV)\\d{4}$")) {
                showAlert("Cảnh báo", "Tài khoản phải bắt đầu bằng 'QL' hoặc 'NV' theo sau là 4 chữ số.", Alert.AlertType.WARNING);
                return;
            } if(pass.length() < 6) {
            	showAlert("Cảnh báo", "Độ dài mật khẩu không phù hợp", Alert.AlertType.WARNING);
            }
            else {
                Map<String, Object> filter = new HashMap<>();
                filter.put("tenTaiKhoan", user);
                List<TaiKhoan> taiKhoans = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(TaiKhoan_DAO.class)
                        .getDanhSach(TaiKhoan.class, filter);

                if (!taiKhoans.isEmpty()) {
                    this.taiKhoan = taiKhoans.get(0);

                    if (taiKhoan.kiemTraDangNhap(user, pass)) {
                        // Cập nhật lại ngày giờ đăng nhập
                        LocalDate localDate = LocalDate.now();
                        Date dateNow = Date.valueOf(localDate);
                        this.taiKhoan.setNgayDangNhap(dateNow);
                        RestaurantApplication.getInstance()
                                .getDatabaseContext()
                                .newEntity_DAO(TaiKhoan_DAO.class)
                                .capNhat(taiKhoan);

                        //PHÂN QUYỀN GIAO DIỆN
                        // Lấy chức vụ từ nhân viên liên kết
                        String chucVu = taiKhoan.getNhanVien().getChucVu();

                        FXMLLoader fxmlLoader;
                        Parent root;
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene;

                        if (chucVu != null && chucVu.equalsIgnoreCase("QL")) {
                            //Giao diện nhân viên quản lý
                            fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/Menu/MenuNVQL.fxml"));
                            root = fxmlLoader.load();
                            scene = new Scene(root);

                            MenuNVQL_Controller menuNVQLController = fxmlLoader.getController();
                            menuNVQLController.setThongTin(taiKhoan);
                        } else {
                            //Giao diện nhân viên
                            fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/Menu/MenuNV.fxml"));
                            root = fxmlLoader.load();
                            scene = new Scene(root);

                            MenuNV_Controller menuNhanVienController = fxmlLoader.getController();
                            menuNhanVienController.setThongTin(taiKhoan);
                        }

                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();

                        showAlert("Thông báo", "Đăng nhập thành công!", Alert.AlertType.INFORMATION);

                    } else {
                        showAlert("Cảnh báo", "Tên đăng nhập hoặc mật khẩu không chính xác! Vui lòng thử lại",
                                Alert.AlertType.WARNING);
                    }
                } else {
                    showAlert("Cảnh báo", "Tên đăng nhập hoặc mật khẩu không chính xác! Vui lòng thử lại",
                            Alert.AlertType.WARNING);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		btnLogin.setDefaultButton(true);
	}

}
