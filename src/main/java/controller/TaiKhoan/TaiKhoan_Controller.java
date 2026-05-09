package controller.TaiKhoan;

import controller.Menu.MenuNVQL_Controller;
import dto.TaiKhoan_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TaiKhoan_Controller implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnTatCa;
    @FXML
    private TableView<TaiKhoan_DTO> tableView;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colMaTK;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colTenTK;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colHoTen;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colNgayDN;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colNDX;
    @FXML
    private TableColumn<TaiKhoan_DTO, String> colNgaySD;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private DatePicker txtDate;

    private final ObservableList<TaiKhoan_DTO> danhSachTaiKhoan = FXCollections.observableArrayList();
    private FilteredList<TaiKhoan_DTO> filteredList;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cauHinhTable();

        try {
            client = new Client();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client = Client.tryCreate();

        loadData();
        xuLyTimKiem();
        xuLyLocTheoNgay();
        xuLyNutTatCa();

        txtDate.setTooltip(new Tooltip("Lọc tài khoản theo thời gian"));
        btnTatCa.setTooltip(new Tooltip("Hiện tất cả tài khoản"));
        txtTimKiem.setTooltip(new Tooltip("Nhập từ khoá để tìm kiếm tài khoản"));
    }

    @FXML
    void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            showThongTin();
        }
    }

    private void cauHinhTable() {
        colMaTK.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaTaiKhoan()));

        colTenTK.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenTaiKhoan()));

        colHoTen.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTenNhanVien() != null ? c.getValue().getTenNhanVien() : ""));

        colNgayDN.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNgayDangNhap() != null ? c.getValue().getNgayDangNhap().toString() : ""));

        colNDX.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNgayDangXuat() != null ? c.getValue().getNgayDangXuat().toString() : ""));

        colNgaySD.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNgaySuaDoi() != null ? c.getValue().getNgaySuaDoi().toString() : ""));
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            Request request = new Request();
            request.setCommandType(CommandType.TAIKHOAN_GET_ALL);

            Response response = client.send(request);

            List<TaiKhoan_DTO> list = (List<TaiKhoan_DTO>) response.getData();

            danhSachTaiKhoan.setAll(list);
            filteredList = new FilteredList<>(danhSachTaiKhoan, p -> true);
            tableView.setItems(filteredList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xuLyTimKiem() {
        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
            String f = newV.toLowerCase().trim();

            filteredList.setPredicate(tk -> {
                if (f.isEmpty())
                    return true;

                return tk.getMaTaiKhoan().toLowerCase().contains(f) || tk.getTenTaiKhoan().toLowerCase().contains(f)
                        || (tk.getTenNhanVien() != null && tk.getTenNhanVien().toLowerCase().contains(f));
            });
        });
    }

    private void xuLyLocTheoNgay() {
        txtDate.valueProperty().addListener((obs, oldD, newD) -> {
            if (newD == null) {
                filteredList.setPredicate(p -> true);
                return;
            }

            filteredList.setPredicate(tk -> {
                Date ngayDN = tk.getNgayDangNhap();
                return ngayDN != null && ngayDN.toLocalDate().equals(newD);
            });
        });
    }

    private void showThongTin() {
        TaiKhoan_DTO taiKhoan = tableView.getSelectionModel().getSelectedItem();

        if (taiKhoan != null) {
            ChiTietTaiKhoan_Controller chiTiet = MenuNVQL_Controller.instance.readyUI("TaiKhoan/ChiTietTaiKhoanTA")
                    .getController();

            chiTiet.setTaiKhoan(taiKhoan);
        }
    }

    private void xuLyNutTatCa() {
        btnTatCa.setOnAction(e -> {
            txtTimKiem.clear();
            txtDate.setValue(null);
            filteredList.setPredicate(p -> true);
        });
    }
}
