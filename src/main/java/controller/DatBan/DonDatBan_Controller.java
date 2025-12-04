package controller.DatBan;

import controller.Menu.MenuNV_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DonDatBan_Controller {
	@FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private DatePicker dpNgayDatBan;
    
    @FXML
    private TableColumn<?, ?> tblDatMon;

    @FXML
    private TableColumn<?, ?> tblGioDen;

    @FXML
    private TableColumn<?, ?> tblKhachHang;

    @FXML
    private TableColumn<?, ?> tblSoBan;

    @FXML
    private TableColumn<?, ?> tblSoNguoi;

    @FXML
    private TableColumn<?, ?> tblTienCoc;

    @FXML
    private TableColumn<?, ?> tblTrangThai;

    @FXML
    private TableView<?> tblView;

    @FXML
    private TextField txtSDT;

    @FXML
    void btnDatBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnHuyDoiBan(ActionEvent event) {

    }

    @FXML
    void btnThayDoi(ActionEvent event) {

    }

    @FXML
    void btnTimKiem(ActionEvent event) {

    }
}
