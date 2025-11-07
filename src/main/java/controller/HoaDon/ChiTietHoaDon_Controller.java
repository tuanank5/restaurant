package controller.HoaDon;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import entity.Ban;
import entity.KhachHang;
import entity.MonAn;
import entity.TaiKhoan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class ChiTietHoaDon_Controller {
	
	@FXML
    private Button btnTroLai;
	
    @FXML
    private Label lblTienTra;

    @FXML
    private Label lblTongThanhToan;

    @FXML
    private TableView<MonAn> tblDanhSachMon;

    @FXML
    private TableColumn<MonAn, String> tblDonGia;

    @FXML
    private TableColumn<MonAn, String> tblMaMonAn;

    @FXML
    private TableColumn<MonAn, String> tblSoLuong;

    @FXML
    private TableColumn<MonAn, String> tblTenMonAn;

    @FXML
    private TableColumn<MonAn, String> tblThanhTien;

    @FXML
    private TextField txtMaHoaDon;

    @FXML
    private TextField txtNV;

    @FXML
    private TextField txtNgay;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtTien;
    
    private String tongTienSauVAT = MenuNV_Controller.tongTienSauVAT;
    private Ban banDangChon = MenuNV_Controller.banDangChon;
    private KhachHang khachHangDangChon = MenuNV_Controller.khachHangDangChon;
    private Map<MonAn, Integer> dsMonAnDangChon = MenuNV_Controller.dsMonAnDangChon;
    private TaiKhoan taiKhoan = MenuNV_Controller.taiKhoan;
    
    private ObservableList<MonAn> danhSachMonAn = FXCollections.observableArrayList();

    @FXML
    void controller(ActionEvent event) {

    }
    
    @FXML
    private void initialize() {
    	tblMaMonAn.setCellValueFactory(new PropertyValueFactory<>("maMon"));
        tblTenMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        tblSoLuong.setCellValueFactory(cellData -> {
            Integer soLuong = dsMonAnDangChon.get(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(soLuong != null ? soLuong : 0));
        });
        tblDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        tblThanhTien.setCellValueFactory(cellData -> {
            MonAn monAn = cellData.getValue();
            Integer soLuong = dsMonAnDangChon.get(monAn); // Lấy số lượng từ map
            double thanhTien = monAn.getDonGia() * soLuong; // Tính thành tiền
            return new SimpleStringProperty(String.valueOf(thanhTien)); // Chuyển đổi thành chuỗi
        });
        
        loadDataToTable();
        
        txtTenKH.setText(khachHangDangChon.getTenKH());
        txtSDT.setText(khachHangDangChon.getSdt());
        txtNV.setText(taiKhoan.getNhanVien().getTenNV());
        txtNgay.setText(LocalDate.now() + "");
        lblTongThanhToan.setText(tongTienSauVAT);
        
    }

    

    private void loadDataToTable() {
    	ObservableList<MonAn> data = FXCollections.observableArrayList(dsMonAnDangChon.keySet());
        tblDanhSachMon.setItems(data);
		
	}
}
