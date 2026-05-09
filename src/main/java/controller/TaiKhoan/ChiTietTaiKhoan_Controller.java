package controller.TaiKhoan;

import controller.Menu.MenuNVQL_Controller;
import dto.TaiKhoan_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChiTietTaiKhoan_Controller implements Initializable {
    @FXML
    private Label lblDanhSachTaiKhoan;

    @FXML
    private TextField txtChucVu;

    @FXML
    private TextField txtMaNV;

    @FXML
    private TextField txtMaTK;

    @FXML
    private TextField txtNgayDN;

    @FXML
    private TextField txtNgayDX;

    @FXML
    private TextField txtNgaySuaDoi;

    @FXML
    private TextField txtTenNV;

    @FXML
    private TextField txtTenTK;

    private TaiKhoan_DTO taiKhoan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void mouseClicked(MouseEvent event) {
        Object source = event.getSource();
        if (source == lblDanhSachTaiKhoan) {
            troLai();
        }
    }

    public void troLai() {
        MenuNVQL_Controller.instance.readyUI("TaiKhoan/TaiKhoanTA");
    }

    public void btnQuayLai(ActionEvent event) {
        MenuNVQL_Controller.instance.readyUI("TaiKhoan/TaiKhoanTA");
    }

    public void setTaiKhoan(TaiKhoan_DTO taiKhoan) {
        this.taiKhoan = taiKhoan;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (taiKhoan == null)
            return;
        txtMaTK.setText(taiKhoan.getMaTaiKhoan());
        txtTenTK.setText(taiKhoan.getTenTaiKhoan());

        txtNgayDN.setText(taiKhoan.getNgayDangNhap() != null ? taiKhoan.getNgayDangNhap().toString() : "");

        txtNgayDX.setText(taiKhoan.getNgayDangXuat() != null ? taiKhoan.getNgayDangXuat().toString() : "");

        txtNgaySuaDoi.setText(taiKhoan.getNgaySuaDoi() != null ? taiKhoan.getNgaySuaDoi().toString() : "");

        txtMaNV.setText(taiKhoan.getMaNhanVien());
        txtTenNV.setText(taiKhoan.getTenNhanVien());
        txtChucVu.setText(taiKhoan.getChucVu() != null ? taiKhoan.getChucVu() : "");
    }
}
