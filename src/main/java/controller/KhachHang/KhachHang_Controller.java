package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.KhachHang_DAO;
import entity.HangKhachHang;
import entity.KhachHang;
import entity.NhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.ExportExcelUtil;

public class KhachHang_Controller {
	@FXML
    private BorderPane borderPane;
	
	@FXML
    private Button btnTatCa;

    @FXML
    private Button btnThemKhachHang;

    @FXML
    private Button btnXuatExcel;

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
    
    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        phanTrang(danhSachKhachHangDB.size());
    }
    @FXML
    void btnTatCa(ActionEvent event) {

    }

    @FXML
    void btnThemKhachHang(ActionEvent event) {
    	
    }
    
    @FXML
    void btnXuatExcel(ActionEvent event) {

    }

    @FXML
    private void controller(ActionEvent event) {
    	Object src = event.getSource();
        if (src == btnThemKhachHang) {
            btnThemKhachHang(event); // Gọi phương thức mở form thêm khách hàng
        } else if (src == btnXuatExcel) {
            btnXuatExcel(event); // Gọi phương thức xuất Excel
        } else {
            int soLuongBanGhi = 0;
            if (src == btnTatCa) {
                status = "all";
                hBoxPage.setVisible(true);
                soLuongBanGhi = locDuLieuTheoTrangThai(status);
            }
            phanTrang(soLuongBanGhi);
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) {
        Object source = event.getSource();
        if (source == borderPane) {
            if (event.getCode() == KeyCode.ESCAPE) {
                huyChonDong();
            }
        }
    }
    
    @FXML
    void mouseClicked(MouseEvent event) {
        Object source = event.getSource();
        if (source == txtTimKiem) {
            timKiem();
        } else if (source == tableView) {
            showThongTin(event.getClickCount());
        } else if (source == borderPane) {
            huyChonDong();
        }
    }
    
    private void xuatExcel() throws IOException {
        Stage stage = (Stage) tableView.getScene().getWindow();
        danhSachKhachHang.clear();
        danhSachKhachHang.addAll(danhSachKhachHangDB);
        tableView.setItems(danhSachKhachHang);
        ExportExcelUtil.exportTableViewToExcel(tableView, stage);
        danhSachKhachHang.clear();
        loadData();
    }
    
    // Tìm kiếm khách hàng
    private void timKiem() {
        // Tạo danh sách tạm thời dựa trên dữ liệu gốc
        ObservableList<KhachHang> newDanhSachKhachHang = FXCollections.observableArrayList();
        newDanhSachKhachHang.addAll(danhSachKhachHangDB);
        // Ẩn phân trang khi filter đang hoạt động
        hBoxPage.setVisible(false);
        // Tạo FilteredList để lọc dữ liệu
        FilteredList<KhachHang> filteredData = new FilteredList<>(newDanhSachKhachHang, p -> true);
        // Thêm listener cho ô tìm kiếm
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(kh -> {
                // Nếu ô tìm kiếm rỗng, hiển thị tất cả dữ liệu
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Kiểm tra từng trường của KhachHang
                if (kh.getMaKH().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (kh.getTenKH().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (kh.getSdt().contains(lowerCaseFilter)) {
                    return true;
                } else if (kh.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (kh.getDiaChi().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (kh.getDiemTichLuy() != 0 && String.valueOf(kh.getDiemTichLuy()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Kiểm tra Hạng khách hàng
                HangKhachHang hang = kh.getHangKhachHang();
                if (hang != null && hang.getTenHang().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Không khớp
            });
            // Hiển thị phân trang
            hBoxPage.setVisible(true);
            phanTrang(filteredData); // Gọi phân trang với FilteredList
            // Tự động hiển thị trang 1
            if (!hBoxPage.getChildren().isEmpty()) {
                if (hBoxPage.getChildren().get(0) instanceof Button) {
                    Button firstButton = (Button) hBoxPage.getChildren().get(0);
                    firstButton.fire(); // Kích hoạt sự kiện click trang 1
                }
            }
        });
        // Gán dữ liệu lọc vào TableView
        tableView.setItems(filteredData);
    }
    
    public void showThemHanhKhach() {
        MenuNV_Controller.instance.readyUI("KhachHang/ThemKhachHang");
    }
    
    private void phanTrang(int soLuongBanGhi) {
        hBoxPage.getChildren().clear();  // Xóa các nút cũ
        loadCountPage(soLuongBanGhi);    // Tạo nút phân trang
        // Gán sự kiện cho từng nút
        hBoxPage.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                String value = ((Button) button).getText();
                int skip = (Integer.parseInt(value) - 1) * LIMIT;
                // Giới hạn phần tử cuối cùng để tránh vượt quá kích thước danh sách
                if (status.equalsIgnoreCase("all")) {
                    int endIndex = Math.min(skip + LIMIT, danhSachKhachHangDB.size());
                    List<KhachHang> khachHangs = danhSachKhachHangDB.subList(skip, endIndex);
                    loadData(khachHangs); // Cập nhật dữ liệu hiển thị trong TableView
                }
            });
        });
    }
    
    /**
     * @Param Hàm phân trang, nhận vào danh sách đã lọc (cho tìm kiếm)
     */
    private void phanTrang(FilteredList<KhachHang> danhSachPhanTrang) {
        hBoxPage.getChildren().clear(); // Xóa hết nút phân trang cũ
        loadCountPage(danhSachPhanTrang.size()); // Tạo nút phân trang mới dựa trên tổng số bản ghi

        // Gán sự kiện cho từng nút phân trang
        hBoxPage.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                String value = ((Button) button).getText();
                int skip = (Integer.parseInt(value) - 1) * LIMIT;

                // Giới hạn phần tử cuối cùng để tránh vượt quá kích thước danh sách
                int endIndex = Math.min(skip + LIMIT, danhSachPhanTrang.size());

                // Lấy danh sách con để hiển thị trong TableView
                List<KhachHang> khachHangs = danhSachPhanTrang.subList(skip, endIndex);
                loadData(khachHangs); // Cập nhật dữ liệu TableView
            });
        });
    }
    
    private int locDuLieuTheoTrangThai(String status) {
        danhSachKhachHang.clear();
        List<KhachHang> khachHangs = null;
        int soLuongBanGhi = 0;
        if (status.equalsIgnoreCase("all")) {
            khachHangs = danhSachKhachHangDB.subList(0, Math.min(LIMIT, danhSachKhachHangDB.size()));
            soLuongBanGhi = danhSachKhachHangDB.size();
        }
        if (khachHangs != null) {
            danhSachKhachHang.addAll(khachHangs);
        }
        tableView.refresh();
        tableView.setItems(danhSachKhachHang);
        return soLuongBanGhi;
    }
    
    /**
     * Tạo nút phân trang dựa trên tổng số bản ghi
     * @param soLuongBanGhi Tổng số bản ghi cần phân trang
     */
    private void loadCountPage(int soLuongBanGhi) {
        hBoxPage.getChildren().clear(); // Xóa các nút cũ trước khi tạo mới
        int soLuongTrang = (int) Math.ceil((double) soLuongBanGhi / LIMIT); // Số trang cần tạo
        for (int i = 0; i < soLuongTrang; i++) {
            Button button = new Button(String.valueOf(i + 1));
            button.setStyle("-fx-font-size: 14px;"); // Có thể chỉnh style giống ComponentUtil
            hBoxPage.getChildren().add(button);
        }
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
    
    private void showThongTin(int countClick) {
    	if(countClick == 2) {
    		KhachHang khachHang = tableView.getSelectionModel().getSelectedItem();
    		if(khachHang != null) {
    			ThongTinKhachHang_Controller thongTinKhachHang = MenuNV_Controller.instance.readyUI("KhachHang/ThongTinChiTietKhachHang").getController();
    			thongTinKhachHang.setKhachHang(khachHang);
    		}
    	}
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);
        ButtonType buttonLuu = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonKhongLuu = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType buttonHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonLuu, buttonKhongLuu, buttonHuy);
        return alert.showAndWait();
    }
}
