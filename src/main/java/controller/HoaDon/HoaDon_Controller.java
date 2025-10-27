package controller.HoaDon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class HoaDon_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnTimKiem;

    @FXML
    private Button btnXacNhan;

    @FXML
    private Button btnXemChiTiet;

    @FXML
    private Button btnXuatExcel;

    @FXML
    private ComboBox<?> comboKhuyenMai;

    @FXML
    private ComboBox<?> comboTT;

    @FXML
    private ComboBox<?> comboTrangThai;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField tienThua;

    @FXML
    private TextField txtBan;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtKhachHang;

    @FXML
    private TextField txtMaHoaDon;

    @FXML
    private TextField txtNhanVien;

    @FXML
    private TextField txtTienCoc;

    @FXML
    private TextField txtTienNhan;

    @FXML
    private TextField txtTongCong;

    @FXML
    private TextField txtTongTien;

    @FXML
    private TextField txtVAT;
}
