package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.HangKhachHang_DAO;
import entity.HangKhachHang;
import entity.KhachHang;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ComboBox<String> comBoxHangKH;
    
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
	
	private List<HangKhachHang> danhSachHangKhachHangDB;
    
    public void initialize() {
        Platform.runLater(() -> btnTroLai.requestFocus());
        loadData();
    }
    @FXML
    private void controller(ActionEvent event) {
    	Object source = event.getSource();
        if (source == btnSuaTT) {
            showThongTin();
        } else if (source == btnTroLai) {
            troLai();
        } else if(source == btnHuy) {
        	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
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
        if (event.getCode() == KeyCode.ESCAPE) {
            troLai();
        }else if (event.getCode() == KeyCode.X) {
            btnHuy.fire();
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
        	txtDiemTichLuy.setText(String.valueOf(khachHang.getDiemTichLuy()));
        	HangKhachHang hang = khachHang.getHangKhachHang();
        	if(hang != null) {
        		comBoxHangKH.setValue(hang.getTenHang());
        	}
        }
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        hienThiThongTin(khachHang);
    }
    
    private void loadData() {
        Map<String,Object> filter = new HashMap<>();
        danhSachHangKhachHangDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(HangKhachHang_DAO.class)
                .getDanhSach(HangKhachHang.class, filter);
        comBoxHangKH.getItems().clear();
        //comBoxHangKH.getItems().add("Tất cả");
        for(HangKhachHang hang : danhSachHangKhachHangDB) {
            comBoxHangKH.getItems().add(hang.getTenHang());
        }
        comBoxHangKH.getSelectionModel().selectFirst();
    }    

    public void troLai() {
    	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
    }
}
