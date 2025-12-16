package controller.NhanVien;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.HangKhachHang_DAO;
import dao.NhanVien_DAO;
import entity.HangKhachHang;
import entity.NhanVien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CapNhatNhanVien_Controller implements Initializable{

    @FXML private TextField txtMaNV;
    @FXML private TextField txtTenNV;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDiaChi;

    @FXML private DatePicker txtNamSinh;
    @FXML private DatePicker txtNgayVaoLam;

    @FXML private ComboBox<String> cmbGioiTinh;
    @FXML private ComboBox<String> cmbChucVu;
    @FXML private ComboBox<String> cmbTrangThai;


    @FXML private Button btnLuu;
    @FXML private Button btnHuy;

    private NhanVien nhanVien;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	cmbGioiTinh.setItems(
    	            javafx.collections.FXCollections.observableArrayList("Nam", "Nữ")
    	);
    	cmbChucVu.setItems(
    	            javafx.collections.FXCollections.observableArrayList("Nhân viên", "Quản lý")
    	);
    	cmbTrangThai.setItems(
	            javafx.collections.FXCollections.observableArrayList("Đang Làm", "Đã Nghĩ")
        );
	}
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
        loadDuLieuLenForm();
    }
    
    private void loadDuLieuLenForm() {
        txtMaNV.setText(nhanVien.getMaNV());
        txtTenNV.setText(nhanVien.getTenNV());
        txtEmail.setText(nhanVien.getEmail());
        txtDiaChi.setText(nhanVien.getDiaChi());
        txtNamSinh.setValue(nhanVien.getNamSinh().toLocalDate());
        txtNgayVaoLam.setValue(nhanVien.getNgayVaoLam().toLocalDate());
        cmbGioiTinh.setValue(nhanVien.isGioiTinh() ? "Nữ" : "Nam");
        cmbChucVu.setValue(nhanVien.getChucVu());
        cmbTrangThai.setValue(nhanVien.isTrangThai() ? "Đang Làm" : "Đã Nghĩ");
        txtMaNV.setEditable(false);
    }

    @FXML
    void controller(ActionEvent event) {
        Object src = event.getSource();
        if (src == btnLuu) {
            luuCapNhat();
        } else if (src == btnHuy) {
            quayLai();
        }
    }   

    private void luuCapNhat() {
        if (!validate()) return;
        nhanVien.setTenNV(txtTenNV.getText().trim());
        nhanVien.setEmail(txtEmail.getText().trim());
        nhanVien.setDiaChi(txtDiaChi.getText().trim());
        nhanVien.setNamSinh(Date.valueOf(txtNamSinh.getValue()));
        nhanVien.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getValue()));
        nhanVien.setGioiTinh(cmbGioiTinh.getValue().equals("Nữ"));
        nhanVien.setChucVu(cmbChucVu.getValue());

        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(NhanVien_DAO.class)
                .capNhat(nhanVien);

        if (check) {
            showAlert("Thông báo", "Cập nhật nhân viên thành công!", Alert.AlertType.INFORMATION);
            quayLai();
        } else {
            showAlert("Lỗi", "Cập nhật thất bại!", Alert.AlertType.ERROR);
        }
    }

    private boolean validate() {

        if (txtTenNV.getText().trim().isEmpty()) {
            showAlert("Cảnh báo", "Tên nhân viên không được rỗng!", Alert.AlertType.WARNING);
            return false;
        }

        if (!txtEmail.getText().matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
            showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
            return false;
        }

        if (txtNamSinh.getValue() == null ||
                Period.between(txtNamSinh.getValue(), LocalDate.now()).getYears() < 18) {
            showAlert("Cảnh báo", "Nhân viên phải đủ 18 tuổi!", Alert.AlertType.WARNING);
            return false;
        }

        if (txtNgayVaoLam.getValue() == null) {
            showAlert("Cảnh báo", "Vui lòng chọn ngày vào làm!", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void quayLai() {
        MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
}
