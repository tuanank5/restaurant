package controller.DatBan;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.RestaurantApplication;
import dao.LoaiBan_DAO;
import dao.NhanVien_DAO;
import entity.Ban;
import entity.LoaiBan;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DatBan_Controller {
	@FXML
    private Button btnDatBan;

    @FXML
    private Button btnHienTai;

    @FXML
    private Button btnTimKiem;

    @FXML
    private ComboBox<String> cmbGioBD;

    @FXML
    private ComboBox<String> cmbGioKT;

    @FXML
    private ComboBox<String> cmbKM;

    @FXML
    private ComboBox<LoaiBan> cmbLoaiBan;

    @FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtLoaiBan;

    @FXML
    private DatePicker txtNgayDatBan;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSoLuong;

    @FXML
    private TextField txtSoLuongKH;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtTrangThai;

    @FXML
    private TextField txtViTri;
    
    private ObservableList<NhanVien> danhSachNhanVien = FXCollections.observableArrayList();
    private List<LoaiBan> danhSachLoaiBanDB;
    
    @FXML
    private void initialize() {
        loadData();
    }
    
//    @FXML
//    private void controller(ActionEvent event) throws IOException {
//        Object source = event.getSource();
//        if (source == btnThemNV) {
//            showThemNhanVien();
//        } else if (source == btnXuatPDF) {
//            xuatExcel();
//        } else if (source == btnXoa) {
//            xoa();
//        } else {
//           
//        }
//
//    }

    private void loadData() {
        Map<String, Object> filter = new HashMap<>();
        
        danhSachLoaiBanDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(LoaiBan_DAO.class)
                .getDanhSach(LoaiBan.class, filter);
        
        cmbLoaiBan.getItems().addAll(danhSachLoaiBanDB);
    }
}
