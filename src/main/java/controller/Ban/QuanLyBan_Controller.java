package controller.Ban;

import java.util.List;

import config.RestaurantApplication;
import dao.Ban_DAO;
import entity.Ban;
import entity.LoaiBan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class QuanLyBan_Controller {

    @FXML
    private TableView<Ban> tblBan;

    @FXML
    private TableColumn<Ban, String> colMaBan;

    @FXML
    private TableColumn<Ban, String> colViTri;

    @FXML
    private TableColumn<Ban, String> colTrangThai;

    @FXML
    private TableColumn<Ban, String> colLoaiBan;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnSua;

    @FXML
    private Button btnXoa;

    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setValueTable();
        loadData();
    }

    private void setValueTable() {
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("viTri"));
        colTrangThai.setCellValueFactory(cellData -> {
            String trangThai = cellData.getValue().getTrangThai(); // String
            return new SimpleStringProperty(trangThai != null ? trangThai : "Trống");
        });
        colLoaiBan.setCellValueFactory(cellData -> {
            LoaiBan loaiBan = cellData.getValue().getLoaiBan();
            return new SimpleStringProperty(loaiBan != null ? loaiBan.getTenLoaiBan() : "");
        });
    }

    private void loadData() {
        List<Ban> list = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);
        danhSachBan.setAll(list);
        tblBan.setItems(danhSachBan);
    }

    @FXML
    private void themBan(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chức năng Thêm chưa làm!");
        alert.show();
    }

    @FXML
    private void suaBan(ActionEvent event) {
        Ban selected = tblBan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chức năng Sửa chưa làm!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn bàn để sửa!");
            alert.show();
        }
    }

    @FXML
    private void xoaBan(ActionEvent event) {
        Ban selected = tblBan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean ok = RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(Ban_DAO.class)
                    .capNhat(selected); // hoặc set trạng thái = "Trống"
            if (ok) {
                danhSachBan.remove(selected);
                tblBan.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Xóa bàn thành công!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Xóa bàn thất bại!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn bàn để xóa!");
            alert.show();
        }
    }
}
