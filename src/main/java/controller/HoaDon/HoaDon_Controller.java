package controller.HoaDon;

import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import controller.Menu.MenuNV_Controller;

public class HoaDon_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }
	  @FXML
	    private BorderPane borderPane;


@FXML
private ComboBox<String> cmbLoc;
	    @FXML
	    private TableColumn<?, ?> colKH;

	    @FXML
	    private TableColumn<?, ?> colKM;

	    @FXML
	    private TableColumn<?, ?> colKieuThanhToan;

	    @FXML
	    private TableColumn<?, ?> colMaHD;

	    @FXML
	    private TableColumn<?, ?> colNV;

	    @FXML
	    private TableColumn<?, ?> colNgayLap;

	    @FXML
	    private TableColumn<?, ?> colTrangThai;

	    @FXML
	    private DatePicker dpDenNgay;

	    @FXML
	    private DatePicker dpTuNgay;

	    @FXML
	    private TableView<?> tableView;

	    @FXML
	    private TextField txtTimKiem;
	    

}
