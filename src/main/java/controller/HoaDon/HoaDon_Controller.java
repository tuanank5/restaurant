package controller.HoaDon;

import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import entity.DonDatBan;
import entity.HoaDon;
import dao.impl.HoaDon_DAOImpl;

public class HoaDon_Controller implements Initializable{

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }
    @FXML
    private DatePicker AfterDay;

    @FXML
    private DatePicker BeforDay;

    @FXML
    private ComboBox<String> cmbLoc;

    @FXML
    private TableColumn<HoaDon,String> colKH;

    @FXML
    private TableColumn<HoaDon,String> colKM;

    @FXML
    private TableColumn<HoaDon,String> colKieuThanhToan;

    @FXML
    private TableColumn<HoaDon,String> colMaHD;

    @FXML
    private TableColumn<HoaDon,String> colNV;

    @FXML
    private TableColumn<HoaDon,String> colNgayLap;

    @FXML
    private TableColumn<HoaDon,String> colTongTienThu;
    
    @FXML
    private TableColumn<HoaDon,String> colTrangThai;

    @FXML
    private TableView<HoaDon> tableView;

    @FXML
    private TextField txtTimKiem;
    private ObservableList<HoaDon> danhSachHoaDon = FXCollections.observableArrayList();
    private List<HoaDon> danhSachHoaDonDB;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		cauHinhCot();
		loadData();
	}
	
	private void loadData() {
	    HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
	    List<HoaDon> list = hoaDonDAO.getAllHoaDons();
	    danhSachHoaDon.setAll(list);
	    tableView.setItems(danhSachHoaDon);
	}
	
	private void cauHinhCot() {
	    colMaHD.setCellValueFactory(data ->
	        new SimpleStringProperty(data.getValue().getMaHoaDon())
	    );

	    colNgayLap.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getNgayLap() != null
	                ? data.getValue().getNgayLap().toString()
	                : ""
	        )
	    );

	    colKH.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getKhachHang() != null
	                ? data.getValue().getKhachHang().getTenKH()
	                : ""
	        )
	    );

	    colNV.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getNhanVien() != null
	                ? data.getValue().getNhanVien().getTenNV()
	                : ""
	        )
	    );

	    colKM.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getKhuyenMai() != null
	                ? data.getValue().getKhuyenMai().getTenKM()
	                : "Không có"
	        )
	    );

	    colTrangThai.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getTrangThai() != null
	                ? data.getValue().getTrangThai()
	                : ""
	        )
	    );

	    colKieuThanhToan.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            data.getValue().getKieuThanhToan() != null
	                ? data.getValue().getKieuThanhToan()
	                : ""
	        )
	    );
	    
	    colTongTienThu.setCellValueFactory(data ->
	        new SimpleStringProperty(
	            String.format("%,.0f", data.getValue().getTongTien())
	        )
	    );
	}
	
}
