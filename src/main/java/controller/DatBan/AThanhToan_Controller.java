package controller.DatBan;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.MonAn_DAOImpl;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.MonAn;
import entity.NhanVien;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AThanhToan_Controller {
	
	@FXML
    private Button btnDiemTichLuy;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnTamTinh;

    @FXML
    private Button btnThuTien;

    @FXML
    private ComboBox<String> cmbKM;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colDonGia;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSTT;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colSoLuong;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colTenMon;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colThanhTien;

    @FXML
    private Label lblConPhaiThu;

    @FXML
    private Label lblThanhTien;

    @FXML
    private Label lblThue;

    @FXML
    private Label lblTongThanhToan;

    @FXML
    private TableView<ChiTietHoaDon> tblDS;

    @FXML
    private TextField txtBan;

    @FXML
    private TextField txtKH;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSL;
    
    // --- DAO ---
    private Ban_DAO banDAO = new Ban_DAOImpl();
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private DonDatBan_DAO donDatBanDao = new DonDatBan_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<HoaDon> dsHoaDon = new ArrayList<>();
    private List<Ban> dsBan = new ArrayList<Ban>();
    private List<DonDatBan> dsDDB = new ArrayList<DonDatBan>();
    private Ban hoaDonDangChon = null;
    private Button buttonHoaDonDangChonUI = null;
    private String maHoaDonDangChon;
    private boolean isFromSearch = false;
    
    private ObservableList<ChiTietHoaDon> dsCTHD = FXCollections.observableArrayList();
    private List<ChiTietHoaDon> dsCTHD_DB;
    
    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();

    @FXML
    public void initialize() {
    	String maHD = MenuNV_Controller.instance.aBanHienTai_HD.getMaHoaDon();
    	dsCTHD_DB = cthdDAO.getChiTietTheoMaHoaDon(maHD);
    
    	setValueTbl();
        loadData(dsCTHD_DB);
    }
    
    private void setValueTbl() {
    	colSTT.setCellValueFactory(col -> {
            int index = tblDS.getItems().indexOf(col.getValue());
            return new ReadOnlyObjectWrapper<>(index >= 0 ? index + 1 : 0);
        });
    	NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
	    colTenMon.setCellValueFactory(cell -> {
	        String tenMon = cell.getValue().getMonAn().getTenMon();
	        return new SimpleStringProperty(tenMon);
	    });
	    colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
	    colDonGia.setCellValueFactory(cell -> {
	        double donGia = cell.getValue().getMonAn().getDonGia();
	        return new SimpleStringProperty(formatTienVN(donGia));
	    });

	    // Thành tiền
	    colThanhTien.setCellValueFactory(cell -> {
	        MonAn mon = cell.getValue().getMonAn();
	        int sl = cell.getValue().getSoLuong();
	        double thanhTien = mon.getDonGia() * sl;
	        return new SimpleStringProperty(formatTienVN(thanhTien));
	    });
	    // Tổng thanh toán
//	    try {
//	        double tong = Double.parseDouble(tongTienSauVAT);
//	        lblTongThanhToan.setText(formatTienVN(tong));
//	    } catch (NumberFormatException e) {
//	        lblTongThanhToan.setText(tongTienSauVAT);
//	    }
	    // Tính tiền trả
//	    txtTien.textProperty().addListener((obs, oldVal, newVal) -> calculateTienTra());
	}
    
    private void loadData(List<ChiTietHoaDon> chitietHDs) {
    	dsCTHD.clear();
    	dsCTHD.addAll(chitietHDs);
        tblDS.setItems(dsCTHD);
    }
    
    private String formatTienVN(double tien) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
	    nf.setGroupingUsed(true);
	    //nf.setMaximumFractionDigits(0); // không hiển thị phần thập phân
	    nf.setMinimumFractionDigits(3); // hiển thị ít nhất 3 số sau dấu thập phân
	    nf.setMaximumFractionDigits(3);
	    return nf.format(tien) + " VND";
	}
}
