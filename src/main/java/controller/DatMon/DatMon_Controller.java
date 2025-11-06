package controller.DatMon;

import dao.KhuyenMai_DAO;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.KhuyenMai_DAOImpl;
import dao.DonDatBan_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.DonDatBan;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatMon_Controller {


    @FXML
    private Button btnThanhToan;

    @FXML
    private ComboBox<?> cmbKM;

    @FXML
    private TableColumn<?, ?> colDonGia;

    @FXML
    private TableColumn<?, ?> colSTT;

    @FXML
    private TableColumn<?, ?> colSoLuong;

    @FXML
    private TableColumn<?, ?> colTenMon;

    @FXML
    private ComboBox<?> comBoxPhanLoai;

    @FXML
    private ComboBox<?> conBoxTrangThai;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private GridPane gridPaneBan;

    @FXML
    private TableView<?> tblDS;

    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

    private Ban banDangChon;

    public void setBanDangChon(Ban ban) {
        this.banDangChon = ban;
    }
    
    
}
