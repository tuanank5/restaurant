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
import javafx.scene.layout.VBox;
import dao.impl.DonDatBan_DAOImpl;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import dao.MonAn_DAO;
import dao.impl.MonAn_DAOImpl;
import entity.MonAn;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DatMon_Controller implements Initializable {


    @FXML
    private Button btnThanhToan;

    // --- thông tin khuyến mãi ---
    @FXML
    private ComboBox<KhuyenMai> cmbKM;

    @FXML
    private TableView<MonAn> tblDS;

    @FXML
    private TableColumn<MonAn, Integer> colSTT;
    @FXML
    private TableColumn<MonAn, String> colTenMon;
    @FXML
    private TableColumn<MonAn, Double> colDonGia;
    @FXML
    private TableColumn<MonAn, Integer> colSoLuong;

    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private GridPane gridPaneMon;


    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;
    
    //-----Bàn---------
    private Ban banDangChon;

    public void setBanDangChon(Ban ban) {
        this.banDangChon = ban;
        loadThongTinKhachHang();
    }
    
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private List<MonAn> dsMonAn;

    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMonAnToGrid();
        dpNgayDatBan.setValue(LocalDate.now());

        // Setup TableView
        colSTT.setCellValueFactory(col -> 
            new ReadOnlyObjectWrapper<>(tblDS.getItems().indexOf(col.getValue()) + 1));
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colSoLuong.setCellValueFactory(col -> 
        new ReadOnlyObjectWrapper<>(dsMonAnDat.get(col.getValue())));
        tblDS.setItems(FXCollections.observableArrayList());
        
        loadThongTinKhachHang();
    }

    
    private void loadMonAnToGrid() {
        dsMonAn = monAnDAO.getDanhSachMonAn();
        gridPaneMon.getChildren().clear();
        gridPaneMon.setHgap(10);
        gridPaneMon.setVgap(10);
        gridPaneMon.setPadding(new Insets(10));

        int columns = 5;
        int col = 0;
        int row = 0;

        for (MonAn mon : dsMonAn) {
            ImageView img = new ImageView();
            String path = mon.getDuongDanAnh();
            if (path != null && !path.isEmpty()) {
                try {
                    Image image = new Image("file:" + path);
                    img.setImage(image);
                } catch (Exception e) {
                    System.out.println("Không load được ảnh: " + path);
                }
            }
            img.setFitWidth(90);
            img.setFitHeight(90);
            img.setPreserveRatio(true);

            Label lblTen = new Label(mon.getTenMon());
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label lblGia = new Label(mon.getDonGia() + " VND");

            Button btnChon = new Button("Chọn");
            btnChon.setOnAction(e -> chonMon(mon));

            VBox box = new VBox(img, lblTen, lblGia, btnChon);
            box.setSpacing(6);
            box.setPadding(new Insets(8));
            box.setPrefSize(130, 160);
            box.setStyle("-fx-border-color: #CFCFCF; -fx-background-color:#FFFFFF; -fx-alignment:center; -fx-border-radius:8; -fx-background-radius:8;");

            gridPaneMon.add(box, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    private void loadThongTinKhachHang() {
        if (banDangChon != null) {
            // Giả sử Bàn đã đặt có DonDatBan
            DonDatBan donDat = donDatBanDAO.layDonDatTheoBan(banDangChon.getMaBan());
            if (donDat != null) {
                KhachHang kh = donDat.getKhachHang();
                if (kh != null) {
                    txtTenKH.setText(kh.getTenKH());
                    txtSDT.setText(kh.getSdt());
                    txtDiemTichLuy.setText(String.valueOf(kh.getDiemTichLuy()));
                }
            }
        }
    }

    private void chonMon(MonAn mon) {
        if (dsMonAnDat.containsKey(mon)) {
            // Nếu đã chọn món, tăng số lượng
            dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
        } else {
            // Thêm món mới với số lượng = 1
            dsMonAnDat.put(mon, 1);
        }

        // Cập nhật TableView
        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
        tblDS.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chọn món");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã chọn món: " + mon.getTenMon());
        alert.showAndWait();
    }


    
}
