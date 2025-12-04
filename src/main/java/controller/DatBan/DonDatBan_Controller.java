package controller.DatBan;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.DonDatBan_DAO;
import dao.KhachHang_DAO;
import entity.Ban;
import entity.DonDatBan;
import entity.HangKhachHang;
import entity.KhachHang;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DonDatBan_Controller implements Initializable{
	@FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private TableColumn<DonDatBan, String> tblGioDen;

    @FXML
    private TableColumn<DonDatBan, String> tblKhachHang;

    @FXML
    private TableColumn<DonDatBan, String> tblSoBan;

    @FXML
    private TableColumn<DonDatBan, String> tblSoNguoi;

    @FXML
    private TableColumn<DonDatBan, String> tblTienCoc;

    @FXML
    private TableColumn<DonDatBan, String> tblTrangThai;

    @FXML
    private TableView<DonDatBan> tblView;

    @FXML
    private TextField txtSDT;
    
    @FXML
    private TextField txtTongDonDat;
    
    private ObservableList<DonDatBan> danhSachDonDatBan = FXCollections.observableArrayList();
    private List<DonDatBan> danhSachDonDatBanDB;
    private final int LIMIT = 15;
    private String status = "all";
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		khoiTaoComboBoxes();
		setValueTable();
        loadData();
	}
    
    @FXML
    void btnDatBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnDoiBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnHuyBan(ActionEvent event) {

    }

    @FXML
    void btnThayDoi(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnTimKiem(ActionEvent event) {

    }
    
    private void khoiTaoComboBoxes() {
        cmbTrangThai.getItems().clear();
        cmbTrangThai.getItems().addAll("Tất cả","Đã Nhận Bàn","Chưa Nhận Bàn");
        cmbTrangThai.getSelectionModel().select("Tất cả");
    }
    
    private void loadData() {
        Map<String, Object> filter = new HashMap<>();

        danhSachDonDatBanDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(DonDatBan_DAO.class)
                .getDanhSach(DonDatBan.class, filter);

        danhSachDonDatBan.clear();
        danhSachDonDatBan.addAll(danhSachDonDatBanDB);

        tblView.setItems(danhSachDonDatBan);
    }

    private void loadData(List<DonDatBan> list) {
        danhSachDonDatBan.clear();
        danhSachDonDatBan.addAll(list);
        tblView.setItems(danhSachDonDatBan);
    }

    private void setValueTable() {
        tblKhachHang.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKhachHang() 
                		!= null ?cellData.getValue().getKhachHang().getTenKH() : "")
        );

        tblSoBan.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getBan() 
        		!= null ?cellData.getValue().getBan().getMaBan() : "")
        );

        tblSoNguoi.setCellValueFactory(cellData ->new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong()))
        );

        tblGioDen.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getGioBatDau() 
        		!= null ?cellData.getValue().getGioBatDau().toString() : "")
        );

        tblTienCoc.setCellValueFactory(cellData ->new SimpleStringProperty("0")); // nếu có tiền cọc thực tế thì thay

        tblTrangThai.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getTrangThai() != 
        		null ?cellData.getValue().getTrangThai() : ""));
    }

}
