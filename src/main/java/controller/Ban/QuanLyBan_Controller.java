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
    private TableColumn<Ban, LoaiBan> colMaLoaiBan;

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

   
}
