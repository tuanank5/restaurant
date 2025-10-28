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
    	MenuNV_Controller.instance.readyUI("KhachHang/ThemKhachHang");
    }
    
    @FXML
    void txtTimKiem(ActionEvent event) {
    	timKiem();
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
    void keyPressed(KeyEvent event) {
        if (event.getSource() == borderPane) {
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

    private void timKiem() {
        ObservableList<KhachHang> newDanhSachKhachHang = FXCollections.observableArrayList();
        newDanhSachKhachHang.addAll(danhSachKhachHangDB);
        hBoxPage.setVisible(false);
        FilteredList<KhachHang> filteredData = new FilteredList<>(newDanhSachKhachHang, p -> true);

        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(kh -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (kh.getMaKH().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getTenKH().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getSdt().contains(lowerCaseFilter)) return true;
                if (kh.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getDiaChi().toLowerCase().contains(lowerCaseFilter)) return true;
                if (kh.getDiemTichLuy() != 0 && String.valueOf(kh.getDiemTichLuy()).contains(lowerCaseFilter)) return true;
                HangKhachHang hang = kh.getHangKhachHang();
                if (hang != null && hang.getTenHang().toLowerCase().contains(lowerCaseFilter)) return true;
                return false;
            });
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
        if (countClick == 2) {
            KhachHang khachHang = tableView.getSelectionModel().getSelectedItem();
            if (khachHang != null) {
                if (menuController != null) {
                    FXMLLoader loader = menuController.readyUI("KhachHang/ThongTinChiTietKhachHang");
                    if (loader != null) {
                        ThongTinKhachHang_Controller thongTin = loader.getController();
                        if (thongTin != null) {
                            thongTin.setKhachHang(khachHang);
                        } else {
                            System.err.println("ThongTinKhachHang_Controller null");
                        }
                    } else {
                        System.err.println("FXMLLoader null");
                    }
                } else {
                    System.err.println("MenuNV_Controller chưa được khởi tạo!");
                }
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
