
package controller.DatMon;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.impl.Ban_DAOImpl;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import controller.DatBan.DatBanTruoc_Controller;
import controller.Menu.MenuNV_Controller;
import dao.ChiTietHoaDon_DAO;
import dao.DonDatBan_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.MonAn_DAOImpl;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import entity.NhanVien;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
 

public class DoiMonTruoc_Controller implements Initializable{
    @FXML
    private TextField txtTim;
    
    @FXML
    private Button btnTroLai;
    
    @FXML
    private TableColumn<MonAn, Integer> colSTT;
    
    @FXML
    private TableColumn<MonAn, String> colTenMon;
    
    @FXML
    private TableColumn<MonAn, Double> colDonGia;
    
    @FXML
    private TableColumn<MonAn, Integer> colSoLuong;
    
    @FXML
    private ComboBox<String> comBoxPhanLoai;
    
    @FXML
    private TableView<MonAn> tblDS;

  
    @FXML
    private ScrollPane scrollPaneMon;
    
    @FXML
    private GridPane gridPaneMon;
    public static Ban banChonStatic;
    public static List<Ban> danhSachBanChonStatic = new ArrayList<>();
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private List<MonAn> dsMonAn;

    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) { 
    	if (danhSachBanChonStatic != null && !danhSachBanChonStatic.isEmpty()) {
            System.out.println("Số bàn được chọn: " + danhSachBanChonStatic.size());
            
            // Ví dụ hiển thị lên UI
            for (Ban b : danhSachBanChonStatic) {
                System.out.println("Bàn: " + b.getMaBan());
            }
        }
    	 //Lấy danh sách món ăn từ database
        dsMonAn = monAnDAO.getDanhSachMonAn();
        //Khởi tạo ComboBox phân loại dựa trên dsMonAn
        khoiTaoComboBoxPhanLoai();        
        //Hiển thị món ăn lên GridPane
        if (dsMonAn != null && !dsMonAn.isEmpty()) {
            loadMonAnToGrid(dsMonAn);
        }
        txtTim.textProperty().addListener((observable, oldValue, newValue) -> {
            timMonTheoTen();
        });
        colSTT.setCellValueFactory(col -> 
        new ReadOnlyObjectWrapper<>(tblDS.getItems().indexOf(col.getValue()) + 1));
        	colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
	        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
	        colDonGia.setCellFactory(col -> new TableCell<MonAn, Double>() {
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(dinhDangTien(item));
            }
        }
    });  
	    // Số lượng
	    colSoLuong.setCellValueFactory(col -> {
	        Integer soLuong = dsMonAnDat.get(col.getValue());
	        return new ReadOnlyObjectWrapper<>(soLuong != null ? soLuong : 0);
	    });
	    // Khởi tạo TableView rỗng
	    tblDS.setItems(FXCollections.observableArrayList());
    }
    
    private void timMonTheoTen() {
        String tuKhoa = txtTim.getText().trim().toLowerCase(); // chuyển về chữ thường
        List<MonAn> dsLoc = new ArrayList<>();
        
        if (tuKhoa.isEmpty()) {
            dsLoc = dsMonAn; // nếu rỗng thì hiển thị tất cả
        } else {
            for (MonAn mon : dsMonAn) {
                if (mon.getTenMon().toLowerCase().contains(tuKhoa)) {
                    dsLoc.add(mon);
                }
            }
        }
        
        loadMonAnToGrid(dsLoc); // cập nhật GridPane
    }

    private void locMonTheoLoai() {
        String loaiChon = comBoxPhanLoai.getValue();
        List<MonAn> dsLoc = new ArrayList<>();
        
        if (loaiChon.equals("Tất cả")) {
            dsLoc = dsMonAn;
        } else {
            for (MonAn mon : dsMonAn) {
                if (loaiChon.equals(mon.getLoaiMon())) {
                    dsLoc.add(mon);
                }
            }
        }
        
        loadMonAnToGrid(dsLoc);
    }
    
    private void khoiTaoComboBoxPhanLoai() {
        // Lấy danh sách loại món duy nhất
        List<String> danhSachLoai = new ArrayList<>();
        danhSachLoai.add("Tất cả"); // để hiển thị toàn bộ món
        for (MonAn mon : dsMonAn) {
            String loai = mon.getLoaiMon();
            if (loai != null && !danhSachLoai.contains(loai)) {
                danhSachLoai.add(loai);
            }
        }
        comBoxPhanLoai.getItems().setAll(danhSachLoai);
        comBoxPhanLoai.setPromptText("Phân loại");

        // Thêm sự kiện chọn
        comBoxPhanLoai.setOnAction(e -> locMonTheoLoai());
    }
    
    private void loadMonAnToGrid(List<MonAn> danhSach) {
        gridPaneMon.getChildren().clear();
        gridPaneMon.getColumnConstraints().clear();
        gridPaneMon.getRowConstraints().clear();
        gridPaneMon.setHgap(15);
        gridPaneMon.setVgap(30);

        int columns = 4; // số cột muốn hiển thị
        int col = 0;
        int row = 0;

        // Chia đều cột
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPaneMon.getColumnConstraints().add(cc);
        }
        // Tính số row theo danh sách
        int totalRows = (int) Math.ceil(danhSach.size() / (double) columns);
        for (int i = 0; i < totalRows; i++) {
            gridPaneMon.getRowConstraints().add(new RowConstraints(220));
        }
        for (MonAn mon : danhSach) {
            // Ảnh
            ImageView img = new ImageView();
            String path = mon.getDuongDanAnh();
            if (path != null && !path.isEmpty()) {
                try {
                    img.setImage(new Image("file:" + path));
                } catch (Exception e) {
                    System.out.println("Không load được ảnh: " + path);
                }
            }
            img.setFitWidth(120);
            img.setFitHeight(120);
            img.setPreserveRatio(true);
            // Tên món
            Label lblTen = new Label(mon.getTenMon());
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            // Giá
            Label lblGia = new Label(dinhDangTien(mon.getDonGia()));
            // Nút chọn
            Button btnChon = new Button("Chọn");
            btnChon.setOnAction(e -> chonMon(mon));
            VBox box = new VBox(img, lblTen, lblGia, btnChon);
            box.setSpacing(6);
            box.setPrefWidth(150);
            box.setStyle("-fx-border-color: #CFCFCF; -fx-background-color:#FFFFFF; "
                       + "-fx-alignment:center; -fx-border-radius:10; -fx-background-radius:10;");
            gridPaneMon.add(box, col, row);
            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
        gridPaneMon.requestLayout();
    }
    
    // Hàm định dạng tiền
    private String dinhDangTien(double soTien) {
        return String.format("%,.0f", soTien); // định dạng kiểu 1,000,000
    }

    private void chonMon(MonAn mon) {
        if (dsMonAnDat.containsKey(mon)) {
            // Nếu đã có món này hỏi người dùng
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Món đã chọn");
            alert.setHeaderText("Món '" + mon.getTenMon() + "' đã có trong danh sách.");
            alert.setContentText("Bạn muốn làm gì?");
            ButtonType btnTang = new ButtonType("➕ Tăng số lượng");
            ButtonType btnGiam = new ButtonType("➖ Giảm số lượng");
            ButtonType btnXoa = new ButtonType("❌ Xóa món");
            ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnTang, btnGiam, btnXoa, btnHuy);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == btnTang) {
                    dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
                } else if (result.get() == btnGiam) {
                    int soLuong = dsMonAnDat.get(mon) - 1;
                    if (soLuong <= 0) dsMonAnDat.remove(mon);
                    else dsMonAnDat.put(mon, soLuong);
                } else if (result.get() == btnXoa) {
                    dsMonAnDat.remove(mon);
                }
            }
        } else {
            //Thêm món mới
            dsMonAnDat.put(mon, 1);
        }

        //CẬP NHẬT TABLEVIEW
        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
        tblDS.refresh();
    }

  

    @FXML
    private void btnXacNhan(ActionEvent event) {
       
    }

   

}
