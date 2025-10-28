package controller.Ban;

import java.util.List;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
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
import javafx.scene.layout.BorderPane;

public class QuanLyBan_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnSua;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnXoa;

    @FXML
    private TableColumn<Ban, String> colMaBan;

    @FXML
    private TableColumn<Ban, String> colMaLoaiBan;

    @FXML
    private TableColumn<Ban, String> colTrangThai;

    @FXML
    private TableColumn<Ban, String> colViTri;

    @FXML
    private TableView<Ban> tblBan;

    @FXML
    void controller(ActionEvent event) {

    }

    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Thiết lập các cột TableView
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("viTri"));
        
        colTrangThai.setCellValueFactory(cellData -> {
            String trangThai = cellData.getValue().getTrangThai();
            return new SimpleStringProperty(trangThai != null ? trangThai : "Trống");
        });

        // Chú ý: colMaLoaiBan phải là TableColumn<Ban, String>
        colMaLoaiBan.setCellValueFactory(cellData -> {
            LoaiBan loaiBan = cellData.getValue().getLoaiBan();
            String tenLoai = (loaiBan != null) ? loaiBan.getTenLoaiBan() : "";
            return new SimpleStringProperty(tenLoai);
        });

        // Load dữ liệu từ SQL
        List<Ban> list = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);

        // Đưa dữ liệu vào ObservableList và hiển thị lên TableView
        danhSachBan.setAll(list);
        tblBan.setItems(danhSachBan);
    }

   
}
