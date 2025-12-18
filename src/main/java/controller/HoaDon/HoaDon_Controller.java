package controller.HoaDon;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import java.time.LocalDate;
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
    private ObservableList<HoaDon> danhSachHoaDonDB = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		cauHinhCot();
	    cauHinhLoc();
	    loadData();
	    xuLyTimKiem();
	    
	    BeforDay.setTooltip(new Tooltip("Lọc hoá đơn từ ngày!"));
	    AfterDay.setTooltip(new Tooltip("Lọc hoá đơn đến ngày!"));
	    cmbLoc.setTooltip(new Tooltip("Lọc theo trạng thái hoá đơn!"));
	    txtTimKiem.setTooltip(new Tooltip("Tìm kiếm hoá đơn theo tên Khách hàng!"));
	}
	
	private void loadData() {
	    HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
	    List<HoaDon> list = hoaDonDAO.getAllHoaDons();
	    danhSachHoaDonDB.setAll(list);   // danh sách gốc
	    danhSachHoaDon.setAll(list);     // danh sách hiển thị
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
	
	private void cauHinhLoc() {
	    cmbLoc.setItems(FXCollections.observableArrayList(
	        "Tất cả",
	        "Đã thanh toán",
	        "Chưa thanh toán"
	    ));
	    cmbLoc.setValue("Tất cả");

	    cmbLoc.setOnAction(e -> locDuLieu());
	    AfterDay.setOnAction(e -> locDuLieu());
	    BeforDay.setOnAction(e -> locDuLieu());
	}
	
	private void locDuLieu() {
	    danhSachHoaDon.setAll(
	        danhSachHoaDonDB.stream()
	            .filter(this::locTheoNgay)
	            .filter(this::locTheoTrangThai)
	            .collect(java.util.stream.Collectors.toList())
	    );
	}
	
	private boolean locTheoNgay(HoaDon hd) {
	    if (hd.getNgayLap() == null)
	        return false;
	    LocalDate from = AfterDay.getValue();
	    LocalDate to   = BeforDay.getValue();
	    LocalDate ngayLap = hd.getNgayLap().toLocalDate();
	    
	    if (from == null && to == null)
	        return true;

	    if (from != null && to == null)
	        return !ngayLap.isBefore(from);
	    
	    if (from == null && to != null)
	        return !ngayLap.isAfter(to);

	    if (from.isAfter(to)) {
	        LocalDate temp = from;
	        from = to;
	        to = temp;
	    }
	    return !ngayLap.isBefore(from) && !ngayLap.isAfter(to);
	}
	
	private boolean locTheoTrangThai(HoaDon hd) {
	    String loc = cmbLoc.getValue();

	    if (loc == null || loc.equals("Tất cả"))
	        return true;

	    return hd.getTrangThai() != null &&
	           hd.getTrangThai().equalsIgnoreCase(loc);
	}
	
	private void xuLyTimKiem() {
	    txtTimKiem.textProperty().addListener((obs, oldText, newText) -> {
	        String keyword = newText.toLowerCase().trim();

	        danhSachHoaDon.setAll(
	            danhSachHoaDonDB.stream()
	                .filter(hd ->
	                    hd.getMaHoaDon().toLowerCase().contains(keyword)
	                    || (hd.getKhachHang() != null
	                        && hd.getKhachHang().getTenKH().toLowerCase().contains(keyword))
	                )
	                .filter(this::locTheoNgay)
	                .filter(this::locTheoTrangThai)
	                .collect(java.util.stream.Collectors.toList())
	        );
	    });
	}

}
