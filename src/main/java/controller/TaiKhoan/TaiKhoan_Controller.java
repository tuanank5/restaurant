package controller.TaiKhoan;

import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TaiKhoan_Controller implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private Button btnTatCa;

    @FXML private TableView<TaiKhoan> tableView;
    @FXML private TableColumn<TaiKhoan, String> colMaTK;
    @FXML private TableColumn<TaiKhoan, String> colTenTK;
    @FXML private TableColumn<TaiKhoan, String> colHoTen;
    @FXML private TableColumn<TaiKhoan, String> colNgayDN;
    @FXML private TableColumn<TaiKhoan, String> colNDX;
    @FXML private TableColumn<TaiKhoan, String> colNgaySD;

    @FXML private TextField txtTimKiem;
    @FXML private DatePicker txtDate;

    private ObservableList<TaiKhoan> danhSachTaiKhoan = FXCollections.observableArrayList();
    private List<TaiKhoan> danhSachTaiKhoanDB;
    private FilteredList<TaiKhoan> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	 cauHinhTable();   // chỉ set column
    	 loadData();       // load DB
    	 xuLyTimKiem();
    	 xuLyLocTheoNgay();
    	 xuLyNutTatCa();
    	 tableView.setRowFactory(tv -> {
    	        TableRow<TaiKhoan> row = new TableRow<>();

    	        row.setOnMouseClicked(event -> {
    	            if (!row.isEmpty() && event.getClickCount() == 2) {
    	                TaiKhoan tk = row.getItem();
    	                moManHinhChiTiet(tk);
    	            }
    	        });

    	        return row;
    	    });
    }

    private void cauHinhTable() {
        colMaTK.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getMaTaiKhoan()));

        colTenTK.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getTenTaiKhoan()));

        colHoTen.setCellValueFactory(c ->
            new SimpleStringProperty(
                c.getValue().getNhanVien() != null
                    ? c.getValue().getNhanVien().getTenNV()
                    : ""
            ));

        colNgayDN.setCellValueFactory(c ->
            new SimpleStringProperty(
                c.getValue().getNgayDangNhap() != null
                    ? c.getValue().getNgayDangNhap().toString()
                    : ""
            ));
        
        colNDX.setCellValueFactory(c ->
        new SimpleStringProperty(
        		c.getValue().getNgayDangXuat() != null
        		? c.getValue().getNgayDangXuat().toString()
        		: ""
        	));
        
        colNgaySD.setCellValueFactory(c ->
            new SimpleStringProperty(
                c.getValue().getNgaySuaDoi() != null
                    ? c.getValue().getNgaySuaDoi().toString()
                    : ""
            ));
    }

    private void loadData() {
        danhSachTaiKhoanDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(TaiKhoan_DAO.class)
                .getDanhSach(TaiKhoan.class, new HashMap<>());

        danhSachTaiKhoan.setAll(danhSachTaiKhoanDB);
        filteredList = new FilteredList<>(danhSachTaiKhoan, p -> true);
        tableView.setItems(filteredList);
    }

    private void xuLyTimKiem() {
        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
            String f = newV.toLowerCase().trim();

            filteredList.setPredicate(tk -> {
                if (f.isEmpty()) return true;

                return tk.getMaTaiKhoan().toLowerCase().contains(f)
                        || tk.getTenTaiKhoan().toLowerCase().contains(f)
                        || tk.getNhanVien().getTenNV().toLowerCase().contains(f);
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
    
    private void moManHinhChiTiet(TaiKhoan taiKhoan) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/fxml/TaiKhoan/ChiTietTaiKhoanTA.fxml")
            );
            Parent root = loader.load();

            ChiTietTaiKhoan_Controller controller = loader.getController();
            controller.setTaiKhoan(taiKhoan);

            Stage stage = new Stage();
            stage.setTitle("Chi tiết tài khoản");
            stage.setScene(new Scene(root));
            stage.initOwner(borderPane.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
