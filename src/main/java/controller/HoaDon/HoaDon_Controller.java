package controller.HoaDon;

import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import controller.Menu.MenuNV_Controller;
import entity.HoaDon;

public class HoaDon_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }
    @FXML
    private DatePicker AfterDay;

    @FXML
    private DatePicker BeforDay;

    @FXML
    private ComboBox<String> cmbLoc;

    @FXML
    private TableColumn<HoaDon,String> colKH;

    @FXML
    private TableColumn<HoaDon,String> colKM;

    @FXML
    private TableColumn<HoaDon,String> colKieuThanhToan;

    @FXML
    private TableColumn<HoaDon,String> colMaHD;

    @FXML
    private TableColumn<HoaDon,String> colNV;

    @FXML
    private TableColumn<HoaDon,String> colNgayLap;

    @FXML
    private TableColumn<HoaDon,String> colTrangThai;

    @FXML
    private TableView<HoaDon> tableView;

    @FXML
    private TextField txtTimKiem;
	    

}
