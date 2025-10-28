package controller.NhanVien;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import config.DatabaseContext;
import config.RestaurantApplication;
import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.EmployeeCodeGeneratorUtil;

public class ThemNhanVien_Controller {
	@FXML
    private Button btnHuy;

    @FXML
    private Button btnLuu;

    @FXML
    private TextField txtMaNV;

    @FXML
    private Button btnQuayLai;

    @FXML
    private Button btnThem;

    @FXML
    private ComboBox<String> cmbChucVu;

    @FXML
    private ComboBox<String> cmbGioiTinh;

    @FXML
    private TableColumn<NhanVien, String> tblChucVu;

    @FXML
    private TableColumn<NhanVien, String> tblDiaChi;

    @FXML
    private TableColumn<NhanVien, String> tblEmail;

    @FXML
    private TableColumn<NhanVien, String> tblGioiTinh;

    @FXML
    private TableColumn<NhanVien, String> tblMaNV;

    @FXML
    private TableColumn<NhanVien, Date> tblNamSinh;

    @FXML
    private TableColumn<NhanVien, String> tblTenNV;

    @FXML
    private TableView<NhanVien> tblThemNV;

    @FXML
    private TableColumn<NhanVien, String> tblTrangThai;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtNamSinh;

    @FXML
    private TextField txtTenNV;
    
    private NhanVien getNhanVienNew() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String email = txtEmail.getText().trim();
        LocalDate namSinh = txtNamSinh.getValue();
        String diaChi = txtDiaChi.getText().trim();
        
        DatabaseContext databaseContext = RestaurantApplication
                .getInstance()
                .getDatabaseContext();
        
        if (tenNV.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập họ tên!", Alert.AlertType.WARNING);
            txtTenNV.requestFocus();
            return null;
        }
        
        if (email.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập email!", Alert.AlertType.WARNING);
            txtDiaChi.requestFocus();
            return null;
        } else {
            if (!email.matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
                showAlert("Cảnh Báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
                txtDiaChi.requestFocus();
                return null;
            }
        }

        if (namSinh == null) {
            showAlert("Cảnh Báo","Vui lòng chọn ngày sinh!", Alert.AlertType.WARNING);
            txtNamSinh.requestFocus();
            return null;
        } else {
            LocalDate today = LocalDate.now();
            int age = Period.between(namSinh, today).getYears();
            if (age < 18) {
                showAlert("Cảnh Báo", "Nhân viên chưa đủ 18 tuổi!", Alert.AlertType.WARNING);
                return null;
            }
        }
        
        if (diaChi.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
            txtDiaChi.requestFocus();
            return null;
        }

        maNV = EmployeeCodeGeneratorUtil.generateEmployeeCode();
        
        boolean gioiTinh = cmbGioiTinh.getValue().contentEquals("Nữ");
//        String chucVu = cmbChucVu.getValue().contentEquals() ? : ;
//        boolean trangThai = trangThaiCombobox.getValue().contentEquals("Đang làm việc");
        boolean trangThai = true;
        
        return new NhanVien();
    }
    
    private void resetAllFiled() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        cmbChucVu.setValue(cmbChucVu.getItems().get(0));
        txtEmail.setText("");
        txtNamSinh.setValue(null);
        txtDiaChi.setText("");
        cmbGioiTinh.setValue(cmbGioiTinh.getItems().get(0));
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);
        ButtonType buttonLuu = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonKhongLuu = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType buttonHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonLuu, buttonKhongLuu, buttonHuy);
        return alert.showAndWait();
    }
}
