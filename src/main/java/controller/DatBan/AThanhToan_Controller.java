package controller.DatBan;

import entity.Ban;
import entity.DonDatBan;
import entity.HoaDon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AThanhToan_Controller {
	
	@FXML
    private Button btnDiemTichLuy;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnTamTinh;

    @FXML
    private Button btnThuTien;

    @FXML
    private ComboBox<?> cmbKM;

    @FXML
    private TableColumn<?, ?> colDonGia;

    @FXML
    private TableColumn<?, ?> colDonGia1;

    @FXML
    private TableColumn<?, ?> colSTT;

    @FXML
    private TableColumn<?, ?> colSoLuong;

    @FXML
    private TableColumn<?, ?> colTenMon;

    @FXML
    private Label lblConPhaiThu;

    @FXML
    private Label lblThanhTien;

    @FXML
    private Label lblThue;

    @FXML
    private Label lblTongThanhToan;

    @FXML
    private TableColumn<?, ?> tblBan;

    @FXML
    private TableView<?> tblDS;

    @FXML
    private TableColumn<?, ?> tblKH;

    @FXML
    private TableColumn<?, ?> tblKM;

    @FXML
    private TableColumn<?, ?> tblSTT;

    @FXML
    private TableView<?> tblThongTin;

    @FXML
    private TextField txtBan;

    @FXML
    private TextField txtKH;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSL;

    @FXML
    public void initialize() {
    	tblThongTin.setVisible(false);
    }
}
