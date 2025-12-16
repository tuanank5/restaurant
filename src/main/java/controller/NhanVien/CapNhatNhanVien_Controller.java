package controller.NhanVien;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class CapNhatNhanVien_Controller implements Initializable {
	
	@FXML private BorderPane borderPane;
    @FXML private TextField txtMaNV;
    @FXML private TextField txtTenNV;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDiaChi;
    @FXML private DatePicker txtNamSinh;
    @FXML private DatePicker txtNgayVaoLam;
    @FXML private Label lblDanhSachNhanVien;
    @FXML private ComboBox<String> cmbGioiTinh;
    @FXML private ComboBox<String> cmbChucVu;
    @FXML private ComboBox<String> cmbTrangThai;
    @FXML private Button btnLuu;
    @FXML private Button btnHuy;
    @FXML private Button btnQuayLai;

    private NhanVien nhanVien; // object gốc

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
        hienThiThongTin(nhanVien);
    }

    private void hienThiThongTin(NhanVien nv) {
        txtMaNV.setText(nv.getMaNV());
        txtTenNV.setText(nv.getTenNV());
        txtEmail.setText(nv.getEmail());
        txtDiaChi.setText(nv.getDiaChi());
        txtNamSinh.setValue(nv.getNamSinh().toLocalDate());
        txtNgayVaoLam.setValue(nv.getNgayVaoLam().toLocalDate());
        cmbGioiTinh.setValue(nv.isGioiTinh() ? "Nữ" : "Nam");
        cmbChucVu.setValue(nv.getChucVu());
        cmbTrangThai.setValue(nv.isTrangThai() ? "Đang Làm" : "Đã Nghĩ");
        txtMaNV.setEditable(false);
    }

    @FXML
    void controller(ActionEvent event) {
        if (event.getSource() == btnLuu) {
            luuCapNhat();
        } else if (event.getSource() == btnQuayLai) {
            xacNhanQuayLai();
        } else if(event.getSource() == btnHuy) {
        	hoanTac();
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) throws IOException {
        Object src = event.getSource();
        if (src == borderPane) {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                luuCapNhat();
            } else if (event.getCode() == KeyCode.ESCAPE) {
            	if (event.getSource() == lblDanhSachNhanVien) {
                    MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
                }
            }
        }
    }

    private void luuCapNhat() {
        NhanVien nhanVienNew = getNhanVienNew();
        if (nhanVienNew == null) return;

        Optional<ButtonType> option = showAlertConfirm("Bạn có chắc chắn muốn lưu thay đổi?");
        if (option.get().getButtonData() == ButtonBar.ButtonData.NO) return;

        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(NhanVien_DAO.class)
                .capNhat(nhanVienNew);

        if (check) {
            showAlert("Thông báo", "Cập nhật nhân viên thành công!", Alert.AlertType.INFORMATION);
            this.nhanVien = nhanVienNew;
            quayLai();
        } else {
            showAlert("Lỗi", "Cập nhật thất bại!", Alert.AlertType.ERROR);
        }
    }

    private NhanVien getNhanVienNew() {
        if (!validate()) return null;

        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText().trim());
        nv.setTenNV(txtTenNV.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setNamSinh(Date.valueOf(txtNamSinh.getValue()));
        nv.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getValue()));
        nv.setGioiTinh(cmbGioiTinh.getValue().equals("Nữ"));
        nv.setChucVu(cmbChucVu.getValue());
        nv.setTrangThai(cmbTrangThai.getValue().equals("Đang Làm"));
        return nv;
    }

    private boolean validate() {

        if (txtTenNV.getText().trim().isEmpty()) {
            showAlert("Cảnh báo", "Tên nhân viên không được rỗng!", Alert.AlertType.WARNING);
            return false;
        }

        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
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

        if (cmbTrangThai.getValue() == null) {
            showAlert("Cảnh báo", "Vui lòng chọn trạng thái!", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void xacNhanQuayLai() {
        Optional<ButtonType> option = showAlertConfirm("Bạn có muốn lưu thay đổi?");
        if (option.get().getButtonData() == ButtonBar.ButtonData.YES) {
            luuCapNhat();
        } else {
            quayLai();
        }
    }
    
    private void hoanTac(){
        Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc chắn muốn hoàn tác?");
        if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
            return;
        }
        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(NhanVien_DAO.class)
                .capNhat(nhanVien);
        if (check) {
            showAlert("Thông báo", "Hoàn tác thành công!", Alert.AlertType.INFORMATION);
            hienThiThongTin(nhanVien);
        } else {
            showAlert("Thông báo", "Hoàn tác thất bại!", Alert.AlertType.WARNING);
        }
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

    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);
        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yes, no);
        return alert.showAndWait();
    }
}
