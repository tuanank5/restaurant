package controller.NhanVien;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Menu.MenuNVQL_Controller;
import controller.Menu.MenuNV_Controller;
import entity.NhanVien;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ThongTinChiTietNV_Controller implements Initializable{

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnSuaTT;

    @FXML
    private ComboBox<String> cmbChucVu;

    @FXML
    private ComboBox<String> cmbGioiTinh;

    @FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private Label lblDanhSachNhanVien;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMaNV;

    @FXML
    private DatePicker txtNamSinh;

    @FXML
    private DatePicker txtNgayVaoLam;

    @FXML
    private TextField txtTenNV;

    private NhanVien nhanVien;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
    	Platform.runLater(() -> btnHuy.requestFocus());
        loadComboBox();
	}

    @FXML
    void controller(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnSuaTT) {
            showCapNhat();
        } else if (source == btnHuy) {
            troLai();
        }
    }

    @FXML
    void mouseClicked(MouseEvent event) {
        if (event.getSource() == lblDanhSachNhanVien) {
            troLai();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            troLai();
        }
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
        cmbTrangThai.setValue(nv.isTrangThai() ? "Đang làm việc" : "Ngưng làm việc");
        txtMaNV.setEditable(false);
    }

    private void loadComboBox() {
    	cmbGioiTinh.setItems(
                javafx.collections.FXCollections.observableArrayList("Nam", "Nữ")
        );
        cmbChucVu.setItems(
                javafx.collections.FXCollections.observableArrayList("Nhân viên", "Quản lý")
        );
        cmbTrangThai.setItems(
                javafx.collections.FXCollections.observableArrayList("Đang làm việc", "Ngưng làm việc")
        );
    }

    private void showCapNhat() {
        if (nhanVien != null) {
            CapNhatNhanVien_Controller controller =
                MenuNVQL_Controller.instance
                    .readyUI("NhanVien/CapNhatNhanVien")
                    .getController();
            controller.setNhanVien(nhanVien);
        }
    }

    private void troLai() {
        MenuNV_Controller.instance.readyUI("NhanVien/ThongTinChiTietNV");
    }
}
