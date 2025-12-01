package controller.NhanVien;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.stage.Stage;
import util.ComponentUtil;
import util.ExportExcelUtil;

public class NhanVien_Controller {
	
	@FXML
    private BorderPane borderPane;
	
	@FXML
    private Button btnDaNghi;

    @FXML
    private Button btnDangLamViec;

    @FXML
    private Button btnTatCa;

    @FXML
    private Button btnThemNV;

    @FXML
    private Button btnUpdateNV;

    @FXML
    private Button btnXoa;

    @FXML
    private Button btnXuatPDF;
	
	@FXML
    private TableView<NhanVien> tblNV;

    @FXML
    private TableColumn<NhanVien, String> tblChucVu;

    @FXML
    private TableColumn<NhanVien, Date> tblNgayVaoLam;

    @FXML
    private TableColumn<NhanVien, String> tblDiaChi;

    @FXML
    private TableColumn<NhanVien, String> tblEmail;

    @FXML
    private TableColumn<NhanVien, String> tblGioiTinh;

    @FXML
    private TableColumn<NhanVien, String> tblMaNV;

    @FXML
    private TableColumn<NhanVien, Date> tblNamSinh;

    @FXML
    private TableColumn<NhanVien, String> tblTenNV;

    @FXML
    private TableColumn<NhanVien, String> tblTrangThai;
    
    @FXML
    private TextField txtTimKiem;
    
    @FXML
    private HBox hBox;
    private ObservableList<NhanVien> danhSachNhanVien = FXCollections.observableArrayList();
    private List<NhanVien> danhSachNhanVienDB;
    private final int LIMIT = 15;
    private String status = "all";
    
    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        phanTrang(danhSachNhanVienDB.size());
    }
    
    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnThemNV) {
            showThemNhanVien();
        } else if (source == btnXuatPDF) {
            xuatExcel();
        } else if (source == btnXoa) {
            xoa();
        } else if (source == btnUpdateNV) {
        	updateNV();
        } else {
            int soLuongBanGhi = 0;
            if (source == btnTatCa) {
                status = "all";
                soLuongBanGhi = locDuLieuTheoTrangThai(status);
            } else if (source == btnDangLamViec) {
                status = "active";
                soLuongBanGhi = locDuLieuTheoTrangThai(status);
            } else if (source == btnDaNghi) {
                status = "inactive";
                soLuongBanGhi = locDuLieuTheoTrangThai(status);
            }
            hBox.setVisible(true);
            phanTrang(soLuongBanGhi);
        }

    }
    
    private void updateNV() {
    	MenuNVQL_Controller.instance.readyUI("NhanVien/CapNhatNhanVien");
	}

	@FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Object source = event.getSource();
        if (source == txtTimKiem) {
            timKiem();
        } else if (source == tblNV) {
            btnXoa.setDisable(false);
//            showThongTin(event.getClickCount());
        } else if (source == borderPane) {
            huyChonDong();
        }
    }
    
//    private void showThongTin(int countClick) {
//        if (countClick == 2) {
//            NhanVien nhanVien = tblNV.getSelectionModel().getSelectedItem();
//            if (nhanVien != null) {
//                ThongTinNhanVien_Controller thongTinNhanVienController =
//                        MenuNVQL_Controller.instance.readyUI("NhanVien/ThongTinNhanVien").getController();
//                thongTinNhanVienController.setNhanVien(nhanVien);
//            }
//        }
//    }
    
    @FXML
    void keyPressed(KeyEvent event) {
        Object source = event.getSource();
        if (source == borderPane) {
            if (event.getCode() == KeyCode.ESCAPE) {
                huyChonDong();
            }
        }
    }

	private int locDuLieuTheoTrangThai(String status) {
        danhSachNhanVien.clear();
        List<NhanVien> nhanViens = null;
        int soLuongBanGhi = 0;
        switch (status)  {
            case "all": {
                nhanViens = danhSachNhanVienDB.subList(0, Math.min(LIMIT, danhSachNhanVienDB.size()));
                soLuongBanGhi = danhSachNhanVienDB.size();
                break;
            }
            case "active":
            case "inactive": {
                // Lọc nhân viên theo trạng thái
                boolean isActive = status.equalsIgnoreCase("active");
                List<NhanVien> filteredList = danhSachNhanVienDB.stream()
                        .filter(nhanVien -> nhanVien.isTrangThai() == isActive)
                        .collect(Collectors.toList());

                // Kiểm tra kích thước trước khi gọi subList
                soLuongBanGhi = filteredList.size();
                if (soLuongBanGhi > 0) {
                    nhanViens = filteredList.subList(0, Math.min(LIMIT, soLuongBanGhi));
                }
                break;
            }
        }
        danhSachNhanVien.addAll(Objects.requireNonNull(nhanViens));
        tblNV.refresh();
        tblNV.setItems(danhSachNhanVien);
        return soLuongBanGhi;
    }
	
    // Tìm kiếm
    private void timKiem() {
        // Tạo một FilteredList dựa trên danh sách gốc
        // Thực hiện chổ này giúp nếu field rỗng thì nó sẽ load lại tất cả
        ObservableList<NhanVien> newDanhSachNhanVien = FXCollections.observableArrayList();
        newDanhSachNhanVien.addAll(danhSachNhanVienDB);
        hBox.setVisible(false);
        FilteredList<NhanVien> filteredData = new FilteredList<>(newDanhSachNhanVien, p -> true);
        // Thêm bộ lọc cho ô tìm kiếm
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(nhanVien -> {
                // Nếu ô tìm kiếm rỗng, hiển thị tất cả dữ liệu
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Kiểm tra từng cột xem có chứa giá trị tìm kiếm không
                if (nhanVien.getMaNV().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (nhanVien.getTenNV().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (nhanVien.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } 
                return false; // Không tìm thấy
            });
            // Cập nhật phân trang với danh sách đã lọc
            hBox.setVisible(true);
            phanTrang(filteredData);
            if (hBox.getChildren().size() > 0) {
                if (hBox.getChildren().get(0) instanceof Button) {
                    Button firstButton = (Button) hBox.getChildren().get(0);
                    firstButton.fire(); // Kích hoạt sự kiện click
                }
            }
            phanTrang(filteredData);
        });
        // Đặt dữ liệu vào TableView
        tblNV.setItems(filteredData);
    }
	
	private void xoa() {
        NhanVien nhanVien = tblNV.getSelectionModel().getSelectedItem();
        if (nhanVien != null) {
            Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc chắn xóa?");
            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
                return;
            }
            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
                nhanVien.setTrangThai(false);
                boolean check = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(NhanVien_DAO.class)
                        .capNhat(nhanVien);

                if (check) {
                    showAlert("Thông báo", "Xóa thành công!", Alert.AlertType.INFORMATION);
                    danhSachNhanVien.removeIf(NhanVien::isTrangThai);
                    tblNV.refresh();
                } else  {
                    showAlert("Thông báo", "Xóa thất bại", Alert.AlertType.WARNING);
                }
            }
        }
    }
	
	private void xuatExcel() throws IOException {
        Stage stage = (Stage) tblNV.getScene().getWindow();
        danhSachNhanVien.clear();
        danhSachNhanVien.addAll(danhSachNhanVienDB);
        tblNV.setItems(danhSachNhanVien);
        ExportExcelUtil.exportTableViewToExcel(tblNV, stage);
        danhSachNhanVien.clear();
        loadData();
    }
	
	/**
     * @Param Hàm phân trang cho traạng thái và tác vụ thường
     * */
    private void phanTrang(int soLuongBanGhi) {
    	hBox.getChildren().clear();
        loadCountPage(soLuongBanGhi);
        // Code bắt sự kiện
        hBox.getChildren().forEach(button -> {
            ((Button)button).setOnAction(event -> {
                String value = ((Button) button).getText();
                int skip = (Integer.parseInt(value) - 1) * LIMIT;
                // Giới hạn phần tử cuối cùng để tránh vượt quá kích thước của danh sách
                if (status.equalsIgnoreCase("all")) {
                    int endIndex = Math.min(skip + LIMIT, danhSachNhanVienDB.size());
                    List<NhanVien> nhanViens = danhSachNhanVienDB.subList(skip, endIndex);
                    loadData(nhanViens);
                } else {
                    List<NhanVien> nhanViens = danhSachNhanVienDB.stream()
                            .filter(nhanVien -> nhanVien.isTrangThai() == (status.equalsIgnoreCase("active")))
                            .collect(Collectors.toCollection(ArrayList::new));
                    int endIndex = Math.min(skip + LIMIT, nhanViens.size());
                    nhanViens = nhanViens.subList(skip, endIndex);
                    loadData(nhanViens);
                }
            });
        });
    }
    
    /**
     * @Param Hàm phân trang, nhận vào danh sách đã lọc (Cho tìm kiếm)
     */
	private void phanTrang(FilteredList<NhanVien> danhSachPhanTrang) {
		hBox.getChildren().clear();
        loadCountPage(danhSachPhanTrang.size());

        hBox.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                String value = ((Button) button).getText();
                int skip = (Integer.parseInt(value) - 1) * LIMIT;

                // Giới hạn phần tử cuối cùng để tránh vượt quá kích thước của danh sách
                int endIndex = Math.min(skip + LIMIT, danhSachPhanTrang.size());
                List<NhanVien> nhanViens = danhSachPhanTrang.subList(skip, endIndex);
                loadData(nhanViens); // Cập nhật dữ liệu hiển thị trong TableView
            });
        });
    }
	
	private void loadCountPage(int soLuongBanGhi) {
        int soLuongTrang = (int) Math.ceil((double) soLuongBanGhi / LIMIT);
        for (int i = 0; i < soLuongTrang; i++) {
            Button button = ComponentUtil.createButton(String.valueOf(i + 1), 14);
            hBox.getChildren().add(button);
        }
    }
	
	private void loadData() {
        Map<String, Object> filter = new HashMap<>();
//        filter.put("trangThai", true);
        danhSachNhanVienDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(NhanVien_DAO.class)
                .getDanhSach(NhanVien.class, filter);
        List<NhanVien> top15NhanVien = danhSachNhanVienDB.subList(0, Math.min(danhSachNhanVienDB.size(), LIMIT));
        danhSachNhanVien.addAll(top15NhanVien);
        tblNV.setItems(danhSachNhanVien);
    }
	
	private void loadData(List<NhanVien> nhanViens) {
        danhSachNhanVien.clear();
        danhSachNhanVien.addAll(nhanViens);
        tblNV.setItems(danhSachNhanVien);
    }
	
	public void showThemNhanVien() {
        MenuNVQL_Controller.instance.readyUI("NhanVien/ThemNhanVien");
    }
	
	private void setValueTable() {
        tblMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        tblTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        tblChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblNamSinh.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        tblDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        tblGioiTinh.setCellValueFactory(cellData -> {
            NhanVien nhanVien = cellData.getValue();
            String gioiTinh = nhanVien.isGioiTinh() ? "Nữ" : "Nam";
            return new SimpleStringProperty(gioiTinh);
        });
        tblNgayVaoLam.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
        tblTrangThai.setCellValueFactory(cellData -> {
            NhanVien nhanVien = cellData.getValue();
            String trangThai = nhanVien.isTrangThai() ? "Đang làm việc" : "Ngừng làm việc";
            return new SimpleStringProperty(trangThai);
        });
    }
	
    private void huyChonDong() {
        tblNV.getSelectionModel().clearSelection();
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
