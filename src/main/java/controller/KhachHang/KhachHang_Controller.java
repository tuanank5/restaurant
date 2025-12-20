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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ExportExcelUtil;

public class KhachHang_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox hBoxPage;

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
    private Button btnTC;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnXuat;

    @FXML
    private TextField txtTimKiem;
    private ObservableList<KhachHang> danhSachKhachHang = FXCollections.observableArrayList();
    private List<KhachHang> danhSachKhachHangDB;
    private final int LIMIT = 15;
    private String status = "all";
    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        System.out.println("TableView items: " + tableView.getItems().size());
        phanTrang(danhSachKhachHangDB.size());
        borderPane.requestFocus();
        btnXuat.setTooltip(new Tooltip("Thông báo cho nút Xuất Excel!"));
        btnTC.setTooltip(new Tooltip("Thông báo cho nút Tất cả!"));
        btnThem.setTooltip(new Tooltip("Thông báo cho nút Thêm khách hàng!"));
        txtTimKiem.setTooltip(new Tooltip("Tìm kiếm theo tên Khách hàng!"));
        
    }
    
    @FXML
    void btnTatCa(ActionEvent event) {
    	status = "all";
        hBoxPage.setVisible(true);
        int soLuongBanGhi = locDuLieuTheoTrangThai(status);
        phanTrang(soLuongBanGhi);
    }

    @FXML
    void btnThemKhachHang(ActionEvent event) {
        moDialogThemKhachHang();
    }

    private void moDialogThemKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/fxml/KhachHang/ThemKhachHang.fxml")
            );
            Parent root = loader.load();

            // Lấy controller nếu cần truyền dữ liệu
            ThemKhachHang_Controller controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Khách Hàng");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // 🔴 khóa màn hình chính
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait(); // chờ đóng dialog

            // Sau khi đóng dialog → reload danh sách
            loadData();
            phanTrang(danhSachKhachHangDB.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    void btnXuatExcel(ActionEvent event) throws IOException{
    	Stage stage = (Stage) tableView.getScene().getWindow();
        danhSachKhachHang.clear();
        danhSachKhachHang.addAll(danhSachKhachHangDB);
        tableView.setItems(danhSachKhachHang);
        ExportExcelUtil.exportTableViewToExcel(tableView, stage);
        danhSachKhachHang.clear();
        loadData();
    }

    @FXML
    void keyPressed(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ESCAPE) {
            huyChonDong();
        } else if (event.getCode() == KeyCode.F1) {
            moDialogThemKhachHang();
        } else if (event.getCode() == KeyCode.F2) {
            btnXuatExcel(new ActionEvent());
        } else if (event.getCode() == KeyCode.F3) {
            status = "all";
            hBoxPage.setVisible(true);
            loadData();
            phanTrang(danhSachKhachHangDB.size());
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
    
    private void timKiem() {
        // Tạo một danh sách mới để tránh ảnh hưởng danh sách gốc
        ObservableList<KhachHang> newDanhSachKhachHang = FXCollections.observableArrayList();
        newDanhSachKhachHang.addAll(danhSachKhachHangDB);
        hBoxPage.setVisible(false);
        FilteredList<KhachHang> filteredData = new FilteredList<>(newDanhSachKhachHang, p -> true);
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(kh -> {
                // Nếu ô tìm kiếm rỗng thì hiển thị tất cả
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (kh.getMaKH() != null && kh.getMaKH().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getTenKH() != null && kh.getTenKH().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getSdt() != null && kh.getSdt().contains(lowerCaseFilter)) return true;
                if (kh.getEmail() != null && kh.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getDiaChi() != null && kh.getDiaChi().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getDiemTichLuy() != 0 && String.valueOf(kh.getDiemTichLuy()).contains(lowerCaseFilter)) return true;
                HangKhachHang hang = kh.getHangKhachHang();
                if (hang != null && hang.getTenHang() != null && hang.getTenHang().toLowerCase().contains(lowerCaseFilter)) return true;
                return false;
            });
            // Hiển thị phân trang
            hBoxPage.setVisible(true);
            phanTrang(filteredData);
            if (!hBoxPage.getChildren().isEmpty() && hBoxPage.getChildren().get(0) instanceof Button) {
                ((Button) hBoxPage.getChildren().get(0)).fire();
            }
        });
        tableView.setItems(filteredData);
    }

    
    private void phanTrang(int soLuongBanGhi) {
        hBoxPage.getChildren().clear();
        loadCountPage(soLuongBanGhi);
        hBoxPage.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                int skip = (Integer.parseInt(((Button) button).getText()) - 1) * LIMIT;
                if (status.equalsIgnoreCase("all")) {
                    int endIndex = Math.min(skip + LIMIT, danhSachKhachHangDB.size());
                    loadData(danhSachKhachHangDB.subList(skip, endIndex));
                }
            });
        });
    }

    private void phanTrang(FilteredList<KhachHang> danhSachPhanTrang) {
        hBoxPage.getChildren().clear();
        loadCountPage(danhSachPhanTrang.size());
        hBoxPage.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                int skip = (Integer.parseInt(((Button) button).getText()) - 1) * LIMIT;
                int endIndex = Math.min(skip + LIMIT, danhSachPhanTrang.size());
                loadData(danhSachPhanTrang.subList(skip, endIndex));
            });
        });
    }

    private int locDuLieuTheoTrangThai(String status) {
        danhSachKhachHang.clear();
        int soLuongBanGhi = danhSachKhachHangDB.size();
        danhSachKhachHang.addAll(danhSachKhachHangDB.subList(0, Math.min(LIMIT, danhSachKhachHangDB.size())));
        tableView.refresh();
        tableView.setItems(danhSachKhachHang);
        return soLuongBanGhi;
    }

    private void loadCountPage(int soLuongBanGhi) {
        hBoxPage.getChildren().clear();
        int soLuongTrang = (int) Math.ceil((double) soLuongBanGhi / LIMIT);
        for (int i = 0; i < soLuongTrang; i++) {
            Button button = new Button(String.valueOf(i + 1));
            button.setStyle("-fx-font-size: 14px;");
            hBoxPage.getChildren().add(button);
        }
    }

    private void loadData() {
        Map<String, Object> filter = new HashMap<>();
        danhSachKhachHangDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(KhachHang_DAO.class)
                .getDanhSach(KhachHang.class, filter);
        danhSachKhachHang.clear();
        danhSachKhachHang.addAll(danhSachKhachHangDB);
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
        tblHangKH.setCellValueFactory(cellData -> {
            HangKhachHang hang = cellData.getValue().getHangKhachHang();
            return new SimpleStringProperty(hang != null ? hang.getTenHang() : "");
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
