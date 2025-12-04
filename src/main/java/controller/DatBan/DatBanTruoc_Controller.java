package controller.DatBan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class DatBanTruoc_Controller {
	@FXML
    private Button btnBan;

    @FXML
    private ComboBox<?> cmbGioBatDau;

    @FXML
    private ComboBox<?> cmbLoaiBan;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private GridPane gridPaneBan;

    @FXML
    private TextField txtSoBan;

    @FXML
    private TextField txtSoLuongKH;

    @FXML
    void btnDatBan(ActionEvent event) {

    }
}
