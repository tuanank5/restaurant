package controller.HoaDon;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
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
	private static int soHoaDonCuoi = 0;
	
	@FXML
    private Button btnTroLai;

    @FXML
    private Button btnXuatHD;
	
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
    	Object source = event.getSource();
        if (source == btnTroLai) {
        	MenuNV_Controller.instance.readyUI("MonAn/DatMon");
        } else if (source == btnXuatHD) {
            themHD();
        } 
    }
    
    private void themHD() {
		
	}

	@FXML
    private void initialize() {
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
	    // Mã & Tên
	    tblMaMonAn.setCellValueFactory(new PropertyValueFactory<>("maMon"));
	    tblTenMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
	    // Số lượng
	    tblSoLuong.setCellValueFactory(cell -> {
	        Integer sl = dsMonAnDangChon.get(cell.getValue());
	        return new SimpleStringProperty(String.valueOf(sl != null ? sl : 0));
	    });
	    // Đơn giá
	    tblDonGia.setCellValueFactory(cell -> {
	        double donGia = cell.getValue().getDonGia();
	        return new SimpleStringProperty(formatTienVN(donGia));
	    });

	    // Thành tiền
	    tblThanhTien.setCellValueFactory(cell -> {
	        MonAn mon = cell.getValue();
	        int sl = dsMonAnDangChon.getOrDefault(mon, 0);
	        double thanhTien = mon.getDonGia() * sl;
	        return new SimpleStringProperty(formatTienVN(thanhTien));
	    });
	    // Load dữ liệu
	    tblDanhSachMon.setItems(FXCollections.observableArrayList(dsMonAnDangChon.keySet()));
	    // Thông tin khách, NV, ngày
	    txtMaHoaDon.setText(taoMaHoaDon());
	    txtTenKH.setText(khachHangDangChon.getTenKH());
	    txtSDT.setText(khachHangDangChon.getSdt());
	    txtNV.setText(taiKhoan.getNhanVien().getTenNV());
	    txtNgay.setText(LocalDate.now().toString());
	    // Tổng thanh toán
	    try {
	        double tong = Double.parseDouble(tongTienSauVAT);
	        lblTongThanhToan.setText(formatTienVN(tong));
	    } catch (NumberFormatException e) {
	        lblTongThanhToan.setText(tongTienSauVAT);
	    }
	    // Tính tiền trả
	    txtTien.textProperty().addListener((obs, oldVal, newVal) -> calculateTienTra());
    }

    

    private void calculateTienTra() {
    	NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    	try {
            double tienNhap = Double.parseDouble(txtTien.getText());
            double tong = Double.parseDouble(tongTienSauVAT);
            double tienTra = tienNhap - tong;
            lblTienTra.setText(tienTra < 0 
                ?formatTienVN(-tienTra) 
                :formatTienVN(tienTra));
        } catch (NumberFormatException e) {
            lblTienTra.setText("Vui lòng nhập số hợp lệ!");
        }
	}

	private void loadDataToTable() {
    	ObservableList<MonAn> data = FXCollections.observableArrayList(dsMonAnDangChon.keySet());
        tblDanhSachMon.setItems(data);
        
        // Format cột giá và thành tiền sang VNĐ
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tblDonGia.setCellFactory(column -> new TableCell<MonAn, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        double donGia = Double.parseDouble(item);
                        setText(currencyVN.format(donGia));
                    } catch (NumberFormatException e) {
                        setText(item);
                    }
                }
            }
        });

        tblThanhTien.setCellFactory(column -> new TableCell<MonAn, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        double thanhTien = Double.parseDouble(item);
                        setText(currencyVN.format(thanhTien));
                    } catch (NumberFormatException e) {
                        setText(item);
                    }
                }
            }
        });
	}
	private String formatTienVN(double tien) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
	    nf.setGroupingUsed(true);
	    //nf.setMaximumFractionDigits(0); // không hiển thị phần thập phân
	    return nf.format(tien) + " VND";
	}
	
	private String taoMaHoaDon() {
	    soHoaDonCuoi++; // tăng số hóa đơn
	    return String.format("HD%04d", soHoaDonCuoi);
	}
}
