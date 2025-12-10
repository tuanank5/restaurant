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
import javafx.scene.control.ButtonBar;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

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
        tblView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                DonDatBan don = tblView.getSelectionModel().getSelectedItem();
                if (don != null) {
                    hienThiDialogThongTin(don);
                }
            }
        });
	}
    
    @FXML
    void btnDatBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnHuyDon(ActionEvent event) {

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
    
    private void hienThiDialogThongTin(DonDatBan don) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Thông tin khách hàng");
        dialog.setHeaderText("Chi tiết đơn đặt bàn");
        
        // Nút Xác nhận & Hủy
        ButtonType btnXacNhan = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnXacNhan, ButtonType.CANCEL);
        
        KhachHang kh = don.getKhachHang();
        TextField txtTen = new TextField(kh != null ? kh.getTenKH() : "");
        TextField txtSDTDialog = new TextField(kh != null ? kh.getSdt() : "");
        TextField txtSoLuong = new TextField(String.valueOf(don.getSoLuong()));

        ComboBox<String> cmbTrangThaiDialog = new ComboBox<>();
        cmbTrangThaiDialog.getItems().addAll("Đã Nhận Bàn", "Chưa Nhận Bàn");
        cmbTrangThaiDialog.setValue(
                don.getTrangThai() != null ? don.getTrangThai() : "Chưa Nhận Bàn"
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Tên khách hàng:"), txtTen);
        grid.addRow(1, new Label("Số điện thoại:"), txtSDTDialog);
        grid.addRow(2, new Label("Số lượng:"), txtSoLuong);
        grid.addRow(3, new Label("Trạng thái:"), cmbTrangThaiDialog);
        dialog.getDialogPane().setContent(grid);
        
        // Xử lý khi nhấn XÁC NHẬN
        dialog.setResultConverter(button -> {
            if (button == btnXacNhan) {
                try {
                    String tenMoi = txtTen.getText().trim();
                    String sdtMoi = txtSDTDialog.getText().trim();
                    if (tenMoi.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Tên khách hàng không được trống!");
                        return null;
                    }
                    if (!sdtMoi.matches("\\d{9,11}")) {
                        showAlert(Alert.AlertType.WARNING, "Số điện thoại không hợp lệ (9-11 số)!");
                        return null;
                    }
                    
                    kh.setTenKH(tenMoi);
                    kh.setSdt(sdtMoi);
                    RestaurantApplication.getInstance()
                            .getDatabaseContext()
                            .newEntity_DAO(KhachHang_DAO.class)
                            .capNhat(kh);

                    int soLuongMoi = Integer.parseInt(txtSoLuong.getText());
                    don.setSoLuong(soLuongMoi);
                    don.setTrangThai(cmbTrangThaiDialog.getValue());
                    RestaurantApplication.getInstance()
                            .getDatabaseContext()
                            .newEntity_DAO(DonDatBan_DAO.class)
                            .capNhat(don);
                    tblView.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật đơn đặt bàn thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Không thể lưu thay đổi!");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }
    
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
