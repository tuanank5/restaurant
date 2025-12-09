package controller.DatBan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import javafx.scene.control.Alert;
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
    
    private DonDatBan donDangChon;

    
    private ObservableList<DonDatBan> danhSachDonDatBan = FXCollections.observableArrayList();
    private List<DonDatBan> danhSachDonDatBanDB;
    private final int LIMIT = 15;
    private String status = "all";
    
    private void capNhatTongDon() {
        txtTongDonDat.setText(String.valueOf(tblView.getItems().size()));
    }

    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		khoiTaoComboBoxes();
		setValueTable();
        loadData();        
        tblView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            donDangChon = newVal;
        });        
        tblView.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends DonDatBan> change) -> {
            capNhatTongDon();
        });
        // cập nhật lần đầu
        capNhatTongDon();
	}
    
    @FXML
    void btnDatBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnHuyBan(ActionEvent event) {

    }

   
    @FXML
    void btnThayDoi(ActionEvent event) {
        if (donDangChon == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một đơn đặt bàn trước khi thay đổi!");
            alert.showAndWait();

            return;
        }

        // Lưu tạm dữ liệu chuyển màn hình
        ThayDoiBanTruoc_Controller.donDatBanDuocChon = donDangChon;

        MenuNV_Controller.instance.readyUI("DatBan/ThayDoiBanTruoc");
    }

    @FXML
    void btnTimKiem(ActionEvent event) {
        String sdt = txtSDT.getText() != null ? txtSDT.getText().trim() : "";
        LocalDate ngayChon = dpNgayDatBan.getValue();
        FilteredList<DonDatBan> filtered = new FilteredList<>(danhSachDonDatBan, d -> true);
        filtered.setPredicate(don -> {
            if (don == null) return false;
            if (!sdt.isEmpty()) {
                if (don.getKhachHang() == null || don.getKhachHang().getSdt() == null) return false;
                if (!don.getKhachHang().getSdt().contains(sdt)) return false;
            }
            LocalDateTime ngayGio = don.getNgayGioLapDon();
            if (ngayGio == null) return false;
            LocalDate ngayDon = ngayGio.toLocalDate();
            if (ngayChon == null) {
                return ngayDon.isAfter(LocalDate.now());
            }
            return !ngayDon.isBefore(ngayChon);
        });
        tblView.setItems(filtered);
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
        capNhatTongDon();
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
