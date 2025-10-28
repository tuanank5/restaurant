package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.RestaurantApplication;
import dao.KhachHang_DAO;
import entity.HangKhachHang;
import entity.KhachHang;
import entity.NhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class KhachHang_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private TableView<KhachHang> tableView;

    @FXML
    private TableColumn<KhachHang,String> tblDiaChi;

    @FXML
    private TableColumn<KhachHang,Integer> tblDiemTichLuy;

    @FXML
    private TableColumn<KhachHang, String> tblEmail;

    @FXML
    private TableColumn<KhachHang,String> tblHangKH;

    @FXML
    private TableColumn<KhachHang,String> tblKhachHang;

    @FXML
    private TableColumn<KhachHang,String> tblSoDienThoai;

    @FXML
    private TableColumn<KhachHang,String> tblTenKH;

    @FXML
    private TextField txtTimKiem;
    
    private HBox hBoxPage;
    private ObservableList<KhachHang> danhSachKhachHang = FXCollections.observableArrayList();
    private List<KhachHang> danhSachKhachHangDB;
    private final int LIMIT = 15;
    private String status = "all";
    
//    @FXML
//    private void initialize() {
//        setValueTable();
//        loadData();
//        phanTrang(danhSachHanhKhachDB.size());
//    }
    @FXML
    void btnTatCa(ActionEvent event) {

    }

    @FXML
    void btnThemKhachHang(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/KhachHang/ThemKhachHang.fxml"));
			Pane ketCaPane = loader.load();
			// Hiển thị giao diện Khách Hàng vào vùng center của BorderPane
			borderPane.setCenter(ketCaPane);
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void btnXuatExcel(ActionEvent event) {

    }

    @FXML
    private void controller(ActionEvent event) {
    	
    }
    private void loadData() {
    	Map<String, Object> filter = new HashMap();
    	danhSachKhachHangDB =  RestaurantApplication.getInstance()
    			.getDatabaseContext()
    			.newEntity_DAO(KhachHang_DAO.class)
    			.getDanhSach(KhachHang.class, filter);
    	List<KhachHang> top15KhachHang = danhSachKhachHangDB.subList(0, Math.min(danhSachKhachHangDB.size(), LIMIT));
    	danhSachKhachHang.addAll(top15KhachHang);
    	tableView.setItems(danhSachKhachHang);
    }
    private void loadData(List<KhachHang> khachHang) {
    	danhSachKhachHang.clear();
    	danhSachKhachHang.addAll(khachHang);
    	tableView.setItems(danhSachKhachHang);
    }
    private void setValueTable() {
    	tblKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        tblTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        tblSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        tblDiemTichLuy.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));

        tblHangKH.setCellValueFactory(cellData ->{
        	HangKhachHang Hang = cellData.getValue().getHangKhachHang();
        	String tenHang = (Hang != null) ? Hang.getTenHang() : "";
        	return new SimpleStringProperty(tenHang);
        });
    }
    
    private void huyChonDong() {
    	tableView.getSelectionModel().clearSelection();
    }
    
//    private void showThongTin(int countClick) {
//    	if(countClick == 2) {
//    		KhachHang khachHang = tableView.getSelectionModel().getSelectedItem();
//    		if(khachHang != null) {
//    			
//    		}
//    	}
//    }
}
