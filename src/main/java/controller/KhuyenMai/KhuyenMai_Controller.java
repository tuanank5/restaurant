package controller.KhuyenMai;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import config.RestaurantApplication;
import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
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

public class KhuyenMai_Controller {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnThemKM;

    @FXML
    private Button btnSuaKM;

    @FXML
    private Button btnXoaKM;

    @FXML
    private TableColumn<KhuyenMai, String> colloaiKM;

    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;

    @FXML
    private TableColumn<KhuyenMai, Date> colNgayBatDau;

    @FXML
    private TableColumn<KhuyenMai, Date> colNgayKetThuc;

    @FXML
    private TableColumn<KhuyenMai, Integer> colPhanTramGiamGia;

    @FXML
    private TableColumn<KhuyenMai, String> colSanPhamKM;

    @FXML
    private TableColumn<KhuyenMai, String> colTenKM;

    @FXML
    private TableView<KhuyenMai> tblKM;

    @FXML
    private TextField txtTimKiem;

    private ObservableList<KhuyenMai> danhSachKhuyenMai = FXCollections.observableArrayList();
    private List<KhuyenMai> danhSachKhuyenMaiDB;

    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        timKiem();
    }

    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();

        if (source == btnXoaKM) {
            xoa();
        }if (source == txtTimKiem) {
        	timKiem();
        }
    }

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Object source = event.getSource();
        if (source == tblKM) {
            btnXoaKM.setDisable(false);
        } else if (source == borderPane) {
            huyChonDong();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getSource() == borderPane && event.getCode() == KeyCode.ESCAPE) {
            huyChonDong();
        }
    }

    // Tìm kiếm theo tên, mã, sản phẩm
    private void timKiem() {
        FilteredList<KhuyenMai> filteredData = new FilteredList<>(danhSachKhuyenMai, p -> true);

        txtTimKiem.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(km -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();

                return km.getMaKM().toLowerCase().contains(filter)
                    || km.getTenKM().toLowerCase().contains(filter)
                    || km.getSanPhamKM().toLowerCase().contains(filter);
            });
            tblKM.setItems(filteredData);
        });
    }

    // XÓA
    private void xoa() {
        KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
        if (km != null) {
            Optional<ButtonType> confirm = showAlertConfirm("Bạn có chắc chắn muốn xóa?");
            if (confirm.get().getButtonData() == ButtonBar.ButtonData.NO) return;

            if (confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {

                boolean check = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(KhuyenMai_DAO.class)
                        .capNhat(km);

                if (check) {
                    showAlert("Thông báo", "Xóa thành công!", Alert.AlertType.INFORMATION);
                    danhSachKhuyenMai.remove(km);
                    tblKM.refresh();
                } else {
                    showAlert("Thông báo", "Xóa thất bại!", Alert.AlertType.WARNING);
                }
            }
        }
    }

    private void loadData() {
        Map<String, Object> filter = new HashMap<>();

        danhSachKhuyenMaiDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(KhuyenMai_DAO.class)
                .getDanhSach(KhuyenMai.class, filter);

        danhSachKhuyenMai.addAll(danhSachKhuyenMaiDB);

        tblKM.setItems(danhSachKhuyenMai);
    }

    private void setValueTable() {
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colloaiKM.setCellValueFactory(new PropertyValueFactory<>("loaiKM"));
        colSanPhamKM.setCellValueFactory(new PropertyValueFactory<>("sanPhamKM"));
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colPhanTramGiamGia.setCellValueFactory(new PropertyValueFactory<>("phanTramGiamGia"));
    }

    private void huyChonDong() {
        tblKM.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }

    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);

        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no, cancel);

        return alert.showAndWait();
    }
}
