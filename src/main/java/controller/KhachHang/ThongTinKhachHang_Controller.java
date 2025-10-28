package controller.KhachHang;

import java.io.IOException;

import controller.Menu.MenuNV_Controller;
import entity.HangKhachHang;
import entity.KhachHang;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ThongTinKhachHang_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnSuaTT;

    @FXML
    private Button btnTroLai;

    @FXML
    private ComboBox<HangKhachHang> comBoxHangKH;
    
    @FXML
    private Label lblDanhSachKhachHang;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMaKH;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

	private KhachHang khachHang;
    
    public void initialize() {
        Platform.runLater(() -> btnTroLai.requestFocus());
    }
    @FXML
    private void controller(ActionEvent event) {
    	Object source = event.getSource();
        if (source == btnSuaTT) {
            showThongTin();
        } else if (source == btnTroLai) {
            troLai();
        }
    }
    @FXML
    void mouseClicked(MouseEvent event) {
        Object source = event.getSource();
        if (source == lblDanhSachKhachHang) {
            troLai();
        }
    }
    @FXML
    void keyPressed(KeyEvent event) {
        Object source = event.getSource();
        if (source == borderPane) {
            if (event.getCode() == KeyCode.ESCAPE) {
                troLai();
            }
        }
    }
    
    private void showThongTin() {
    	if (khachHang != null) {
            CapNhatKhachHang_Controller capNhatHanhKhachController =
                    MenuNV_Controller.instance.readyUI("KhachHang/CapNhatKhachHang").getController();
            capNhatHanhKhachController.setKhachHang(khachHang);
        }
    }
    
    public void hienThiThongTin(KhachHang khachHang) {
        if (khachHang != null) {
        	txtMaKH.setText(khachHang.getMaKH());
        	txtTenKH.setText(khachHang.getTenKH());
        	txtSDT.setText(khachHang.getSdt());
        	txtEmail.setText(khachHang.getEmail());
        	txtDiaChi.setText(khachHang.getDiaChi());
        	HangKhachHang hang = khachHang.getHangKhachHang();
        	if(hang != null) {
        		comBoxHangKH.setValue(hang);
        	}
        	String diemTichLuy = txtDiemTichLuy.getText();
        }
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        hienThiThongTin(khachHang);
    }
    
    public void troLai() {
    	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
    }
}
