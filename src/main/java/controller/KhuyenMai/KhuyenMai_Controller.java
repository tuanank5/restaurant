package controller.KhuyenMai;

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
import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
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
    private TableColumn<KhuyenMai, String> colLoaiKM;

    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;

    @FXML
    private TableColumn<KhuyenMai, Date> colNgayBatDau;

    @FXML
    private TableColumn<KhuyenMai, Date> colNgayKetThuc;

    @FXML
    private TableColumn<KhuyenMai, Integer> colPhanTramKM;

    @FXML
    private TableColumn<KhuyenMai, String> colSanPhamKM;

    @FXML
    private TableColumn<KhuyenMai, String> colTenKM;

    @FXML
    private TableView<KhuyenMai> tblKM;

    @FXML
    private TextField txtTimKiem;

    @FXML
    private HBox hBox;

    private ObservableList<KhuyenMai> danhSachKhuyenMai = FXCollections.observableArrayList();
    private List<KhuyenMai> danhSachKhuyenMaiDB;
    private final int LIMIT = 15;

    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        phanTrang(danhSachKhuyenMaiDB.size());
    }

    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();

        if (source == btnXoaKM) {
            xoa();
        }
    }

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Object source = event.getSource();
        if (source == txtTimKiem) {
            timKiem();
        } else if (source == tblKM) {
            btnXoaKM.setDisable(false);
        } else if (source == borderPane) {
            huyChonDong();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        Object source = event.getSource();
        if (source == borderPane && event.getCode() == KeyCode.ESCAPE) {
            huyChonDong();
        }
    }

    // Tìm kiếm
    private void timKiem() {
        ObservableList<KhuyenMai> newList = FXCollections.observableArrayList();
        newList.addAll(danhSachKhuyenMaiDB);
        hBox.setVisible(false);

        FilteredList<KhuyenMai> filteredData = new FilteredList<>(newList, p -> true);

        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(km -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();

                return km.getMaKM().toLowerCase().contains(filter)
                        || km.getTenKM().toLowerCase().contains(filter)
                        || km.getSanPhamKM().toLowerCase().contains(filter);
            });

            hBox.setVisible(true);
            phanTrang(filteredData);

            if (hBox.getChildren().size() > 0 && hBox.getChildren().get(0) instanceof Button) {
                ((Button) hBox.getChildren().get(0)).fire();
            }
        });

        tblKM.setItems(filteredData);
    }

    // XÓA CỨNG
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

    // PHÂN TRANG
    private void phanTrang(int soLuongBanGhi) {
        hBox.getChildren().clear();
        loadCountPage(soLuongBanGhi);

        hBox.getChildren().forEach(button -> {
            ((Button)button).setOnAction(event -> {
                int page = Integer.parseInt(((Button)button).getText());
                int skip = (page - 1) * LIMIT;
                int endIndex = Math.min(skip + LIMIT, danhSachKhuyenMaiDB.size());

                List<KhuyenMai> sub = danhSachKhuyenMaiDB.subList(skip, endIndex);
                loadData(sub);
            });
        });
    }

    private void phanTrang(FilteredList<KhuyenMai> list) {
        hBox.getChildren().clear();
        loadCountPage(list.size());

        hBox.getChildren().forEach(button -> {
            ((Button) button).setOnAction(event -> {
                int page = Integer.parseInt(((Button) button).getText());
                int skip = (page - 1) * LIMIT;

                int endIndex = Math.min(skip + LIMIT, list.size());
                List<KhuyenMai> sub = list.subList(skip, endIndex);
                loadData(sub);
            });
        });
    }

    private void loadCountPage(int total) {
        int pages = (int) Math.ceil((double) total / LIMIT);
        for (int i = 0; i < pages; i++) {
            Button btn = ComponentUtil.createButton(String.valueOf(i + 1), 14);
            hBox.getChildren().add(btn);
        }
    }

    private void loadData() {
        Map<String, Object> filter = new HashMap<>();

        danhSachKhuyenMaiDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(KhuyenMai_DAO.class)
                .getDanhSach(KhuyenMai.class, filter);

        List<KhuyenMai> top = danhSachKhuyenMaiDB.subList(0, Math.min(danhSachKhuyenMaiDB.size(), LIMIT));
        danhSachKhuyenMai.addAll(top);

        tblKM.setItems(danhSachKhuyenMai);
    }

    private void loadData(List<KhuyenMai> sub) {
        danhSachKhuyenMai.clear();
        danhSachKhuyenMai.addAll(sub);
        tblKM.setItems(danhSachKhuyenMai);
    }

    private void setValueTable() {
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colLoaiKM.setCellValueFactory(new PropertyValueFactory<>("loaiKM"));
        colSanPhamKM.setCellValueFactory(new PropertyValueFactory<>("sanPhamKM"));
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colPhanTramKM.setCellValueFactory(new PropertyValueFactory<>("phanTramGiamGia"));
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
