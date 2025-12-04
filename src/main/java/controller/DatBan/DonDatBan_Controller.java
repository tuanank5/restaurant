package controller.DatBan;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import entity.Ban;
import entity.DonDatBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DonDatBan_Controller implements Initializable{
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
    private TableView<DonDatBan> tblView;

    @FXML
    private TextField txtSDT;
    
    @FXML
    private TextField txtTongDonDat;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		khoiTaoComboBoxes();
	}
    
    @FXML
    void btnDatBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnDoiBan(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnHuyBan(ActionEvent event) {

    }

    @FXML
    void btnThayDoi(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/DatBanTruoc");
    }

    @FXML
    void btnTimKiem(ActionEvent event) {

    }
    
    private void khoiTaoComboBoxes() {
        cmbTrangThai.getItems().clear();
        cmbTrangThai.getItems().addAll("Tất cả","Đã Nhận Bàn","Chưa Nhận Bàn");
        cmbTrangThai.getSelectionModel().select("Tất cả");
    }
}
