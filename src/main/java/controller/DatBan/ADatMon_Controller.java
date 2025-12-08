package controller.DatBan;

import dao.KhuyenMai_DAO;
import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.KhuyenMai_DAOImpl;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.DonDatBan;
import entity.HoaDon;
import entity.LoaiBan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import util.AutoIDUitl;
import dao.impl.DonDatBan_DAOImpl;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import config.RestaurantApplication;
import controller.HoaDon.ChiTietHoaDon_Controller;
import controller.Menu.MenuNV_Controller;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.impl.MonAn_DAOImpl;
import dao.impl.NhanVien_DAOImpl;
import entity.MonAn;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.NumberFormat;
import java.util.Locale;


public class ADatMon_Controller implements Initializable {


    @FXML
    private Button btnGiam;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnQuayLai;

    @FXML
    private Button btnTang;

    @FXML
    private Button btnXacNhan;

    @FXML
    private ComboBox<String> cmbLoaiMon;

    @FXML
    private TableColumn<MonAn, Double> colDonGia;

    @FXML
    private TableColumn<MonAn, Integer> colSTT;

    @FXML
    private TableColumn<MonAn, Integer> colSoLuong;

    @FXML
    private TableColumn<MonAn, String> colTenMon;

    @FXML
    private GridPane gridPaneMon;

    @FXML
    private Label lblTongTien;

    @FXML
    private ScrollPane scrollPaneMon;

    @FXML
    private TableView<MonAn> tblDS;

    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtNhanVien;

    @FXML
    private TextField txtSoLuong;
    
    //-----Bàn---------
    private Ban banDangChon;

    
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAOImpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private List<MonAn> dsMonAn;
    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
    
    public void setBanDangChon(Ban ban) {
        this.banDangChon = ban;
        loadMonCuaBan();
    }
    
    private DonDatBan donDatBanHienTai;

    public void setDonDatBanHienTai(DonDatBan don) {
        this.donDatBanHienTai = don;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	 // Lấy danh sách món ăn từ DB
        dsMonAn = monAnDAO.getDanhSachMonAn();
        loadTenNhanVien();
        
        // Khởi tạo ComboBox phân loại (dùng dsMonAn)
        khoiTaoComboBoxPhanLoai();

        // Load toàn bộ món lên giao diện
        loadMonAnToGrid(dsMonAn);

        colSTT.setCellValueFactory(col -> {
            int index = tblDS.getItems().indexOf(col.getValue());
            return new ReadOnlyObjectWrapper<>(index >= 0 ? index + 1 : 0);
        });
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
        colSoLuong.setCellValueFactory(col -> {
            Integer soLuong = dsMonAnDat.get(col.getValue());
            return new ReadOnlyObjectWrapper<>(soLuong != null ? soLuong : 0);
        });
        tblDS.setItems(FXCollections.observableArrayList());
        // --- Nhận dữ liệu bàn và KH từ MenuNV_Controller ---
        if (MenuNV_Controller.banDangChon != null) {
            this.banDangChon = MenuNV_Controller.banDangChon;
            txtMaBan.setText(banDangChon.getMaBan());
            loadMonCuaBan();
        }
        tblDS.setOnMouseClicked(e -> capNhatTongTien());

    }
    public void loadTenNhanVien() {
        if (MenuNV_Controller.taiKhoan != null) {
            String ten = MenuNV_Controller.taiKhoan.getNhanVien().getTenNV();
            txtNhanVien.setText(ten);
        }
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
        cmbLoaiMon.getItems().setAll(danhSachLoai);

        // Thêm sự kiện chọn
        cmbLoaiMon.setOnAction(e -> locMonTheoLoai());
    }

    private void locMonTheoLoai() {
        String loaiChon = cmbLoaiMon.getValue();
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

    @FXML
    void hanhleThanhToan(ActionEvent event) {
        try {
        	// Gán dữ liệu cho hóa đơn như bạn đang làm
            MenuNV_Controller.banDangChon = banDangChon;
            MenuNV_Controller.dsMonAnDangChon = dsMonAnDat;
            //Cập nhật trạng thái bàn về TRỐNG trong database
            Ban_DAO banDAO = new Ban_DAOImpl();
            banDangChon.setTrangThai("Trống");
            banDAO.capNhat(banDangChon);
            //Xóa món ăn đã lưu tạm cho bàn này
            MenuNV_Controller.dsMonTheoBan.remove(banDangChon.getMaBan());
            //Cập nhật giao diện danh sách bàn trong MenuNV
            MenuNV_Controller.instance.refreshBanUI();
            //Mở UI hóa đơn
//------------------Chuyển qua gdien tứng em ------------------------------
            MenuNV_Controller.instance.readyUI("HoaDon/ChiTiet");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    void hanhleXacNhan(ActionEvent event) {
        // Lưu danh sách món theo bàn
    	HoaDon hd = new HoaDon();
    	hd.setMaHoaDon(AutoIDUitl.sinhMaHoaDon());
    	LocalDate localDate = LocalDate.now();
        Date dateNow = Date.valueOf(localDate);
    	hd.setNgayLap(dateNow);
    	hd.setTrangThai("Chưa thanh toán");
    	hd.setKieuThanhToan("kieuThanhToan");
    	hd.setNhanVien(MenuNV_Controller.taiKhoan.getNhanVien());
    	hd.setBan(MenuNV_Controller.banDangChon);
    	
    	try {
   	     if (hd != null) {
   	        boolean check = RestaurantApplication.getInstance()
   	                    .getDatabaseContext()
   	                    .newEntity_DAO(HoaDon_DAO.class)
   	                    .them(hd);
   	            //Kiểm tra kết quả thêm
   	            if (check) {
   	                showAlert("Thông báo", "Lưu hóa đơn tạm thành công!", Alert.AlertType.INFORMATION);
   	                themChiTietHoaDon(hd.getMaHoaDon(), null);
   	            } else {
   	                showAlert("Thông báo", "Lưu hóa đơn tạm thất bại!", Alert.AlertType.WARNING);
   	            }
   	        }
   	    } catch (Exception e) {
   	        e.printStackTrace();
   	        showAlert("Lỗi", "ADatMon_Controller lỗi", Alert.AlertType.ERROR);
   	    }
    	

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText("Đã lưu món tạm cho bàn " + banDangChon.getMaBan());
        alert.showAndWait();
        try {
            MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void themChiTietHoaDon(String maHD, List<MonAn> dsMonAn) {
        for (int i = 0; i < dsMonAn.size(); i++) {
            MonAn monAn = dsMonAn.get(i);
        }
	}

	private void loadMonAnToGrid(List<MonAn> danhSach) {
        gridPaneMon.getChildren().clear();
        gridPaneMon.getColumnConstraints().clear();
        gridPaneMon.getRowConstraints().clear();
        gridPaneMon.setHgap(15);
        gridPaneMon.setVgap(30);
        gridPaneMon.setPadding(new Insets(15));

        scrollPaneMon.setFitToWidth(true);
        gridPaneMon.prefWidthProperty().bind(scrollPaneMon.widthProperty());

        int columns = 4;
        int col = 0;
        int row = 0;

        // Chia đều cột
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPaneMon.getColumnConstraints().add(cc);
        }

        // TÍNH SỐ ROW THEO DANH SÁCH HIỆN TẠI
        int totalRows = (int) Math.ceil(danhSach.size() / (double) columns);
        for (int i = 0; i < totalRows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(220);
            rc.setVgrow(Priority.NEVER);
            gridPaneMon.getRowConstraints().add(rc);
        }

        // HIỂN THỊ MÓN ĂN ĐÚNG DANH SÁCH ĐANG LOAD
        for (MonAn mon : danhSach) {
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

            Label lblTen = new Label(mon.getTenMon());
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label lblGia = new Label(dinhDangTien(mon.getDonGia()));

            Button btnChon = new Button("Chọn");
            btnChon.setOnAction(e -> chonMon(mon));

            VBox box = new VBox(img, lblTen, lblGia, btnChon);
            box.setSpacing(6);
            box.setPadding(new Insets(8));
            box.setPrefWidth(150);
            box.setStyle("-fx-border-color: #CFCFCF; -fx-background-color:#FFFFFF; " + "-fx-alignment:center; -fx-border-radius:10; -fx-background-radius:10;");
            gridPaneMon.add(box, col, row);
            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        gridPaneMon.requestLayout();
    }
    
    private void loadMonCuaBan() {
        if (banDangChon == null) return;

        // Nếu bàn đã từng đặt món → lấy lại
        if (MenuNV_Controller.dsMonTheoBan.containsKey(banDangChon.getMaBan())) {
            dsMonAnDat = MenuNV_Controller.dsMonTheoBan.get(banDangChon.getMaBan());
        }

        // Cập nhật bảng
        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
        tblDS.refresh();
        capNhatTongTien();
    }

    

// -------------------------Tăng giảm số lượng-----------------------    
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
            // Thêm món mới
            dsMonAnDat.put(mon, 1);
        }
        // Cập nhật bảng
        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
        tblDS.refresh();
        capNhatTongTien();
    }
    
    private void capNhatTongTien() {
        double tongTruocVAT = dsMonAnDat.entrySet().stream().mapToDouble(e -> e.getKey().getDonGia() * e.getValue()).sum();
        lblTongTien.setText(dinhDangTien(tongTruocVAT));
    }
    
    
    private String dinhDangTien(double soTien) {
    	NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(soTien);
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
}
   