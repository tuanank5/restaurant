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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import dao.impl.DonDatBan_DAOImpl;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.HoaDon.ChiTietHoaDon_Controller;
import controller.Menu.MenuNV_Controller;
import dao.MonAn_DAO;
import dao.impl.MonAn_DAOImpl;
import entity.MonAn;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.NumberFormat;
import java.util.Locale;


public class DatMon_Controller implements Initializable {


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
    private ComboBox<String> comBoxPhanLoai;
    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private ScrollPane scrollPaneMon;
    
    @FXML
    private GridPane gridPaneMon;

    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtMaKH;
    
    @FXML
    private Label lblTongTien;

    @FXML
    private Label lblVat;
    
    @FXML
    private Label lblTongTienVAT;
    
    private double Vat_Rate = 0.1;
    
    //-----Bàn---------
    private Ban banDangChon;

    public void setBanDangChon(Ban ban) {
        this.banDangChon = ban;
        loadThongTinKhachHang();
    }
    
    private DonDatBan donDatBanHienTai;

    public void setDonDatBanHienTai(DonDatBan don) {
        this.donDatBanHienTai = don;
        loadThongTinKhachHang(); // load ngay khi nhận được
    }

    
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAOImpl();
    
    private List<MonAn> dsMonAn;

    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	loadMonAnToGrid();
        dpNgayDatBan.setValue(LocalDate.now());
        // Setup TableView
//        colSTT.setCellValueFactory(col ->
//            new ReadOnlyObjectWrapper<>(tblDS.getItems().indexOf(col.getValue()) + 1));
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
//        colSoLuong.setCellValueFactory(col ->
//            new ReadOnlyObjectWrapper<>(dsMonAnDat.get(col.getValue())));
        colSoLuong.setCellValueFactory(col -> {
            Integer soLuong = dsMonAnDat.get(col.getValue());
            return new ReadOnlyObjectWrapper<>(soLuong != null ? soLuong : 0);
        });
        tblDS.setItems(FXCollections.observableArrayList());
        khoiTaoComboBoxKhuyenMai();
        // --- Nhận dữ liệu bàn và KH từ MenuNV_Controller ---
        if (MenuNV_Controller.banDangChon != null) {
            this.banDangChon = MenuNV_Controller.banDangChon;
            txtMaBan.setText(banDangChon.getMaBan());
        }

        if (MenuNV_Controller.khachHangDangChon != null) {
            txtMaKH.setText(MenuNV_Controller.khachHangDangChon.getMaKH());
        }

        loadThongTinKhachHang();
    }
    
    @FXML
    void hanhleThanhToan(ActionEvent event) {
    	try {
            // Gán dữ liệu cần thiết vào MenuNV_Controller (giống cách bạn truyền ở DatMon)
            MenuNV_Controller.banDangChon = banDangChon;
            MenuNV_Controller.dsMonAnDangChon = dsMonAnDat; // bạn cần tạo biến này trong MenuNV_Controller
            MenuNV_Controller.tongTienSauVAT = lblTongTienVAT.getText();

            // Mở giao diện Chi tiết hóa đơn trong BorderPane chính
            MenuNV_Controller.instance.readyUI("HoaDon/ChiTiet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // --- Khởi tạo ComboBox Khuyến mãi ---
    private void khoiTaoComboBoxKhuyenMai() {
        List<KhuyenMai> danhSachKM = khuyenMaiDAO.getDanhSach("KhuyenMai.list", KhuyenMai.class);
        if (danhSachKM != null && !danhSachKM.isEmpty()) {
            cmbKM.getItems().setAll(danhSachKM);
            // Hiển thị tên KM + %
            cmbKM.setCellFactory(lv -> new ListCell<KhuyenMai>() {
                @Override
                protected void updateItem(KhuyenMai item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTenKM() + " - " + item.getPhanTramGiamGia() + "%");
                }
            });
            cmbKM.setButtonCell(new ListCell<KhuyenMai>() {
                @Override
                protected void updateItem(KhuyenMai item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTenKM() + " - " + item.getPhanTramGiamGia() + "%");
                }
            });
        }
    }
    
    private void loadMonAnToGrid() {
        dsMonAn = monAnDAO.getDanhSachMonAn();
        gridPaneMon.getChildren().clear();
        gridPaneMon.getColumnConstraints().clear();
        gridPaneMon.getRowConstraints().clear();
        
        gridPaneMon.setHgap(15);
        gridPaneMon.setVgap(30);
        gridPaneMon.setPadding(new Insets(15));
        
        scrollPaneMon.setFitToWidth(true);
        gridPaneMon.prefWidthProperty().bind(scrollPaneMon.widthProperty());

        int columns = 4;

        // ColumnConstraints chia đều cột
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPaneMon.getColumnConstraints().add(cc);
        }

        // RowConstraints để tránh chồng hàng
        int totalRows = (int) Math.ceil(dsMonAn.size() / (double) columns);
        for (int i = 0; i < totalRows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(220); // phù hợp với kích thước VBox + khoảng cách
            rc.setVgrow(Priority.NEVER);
            gridPaneMon.getRowConstraints().add(rc);
        }

        int col = 0;
        int row = 0;

        for (MonAn mon : dsMonAn) {
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
            box.setMaxHeight(Region.USE_COMPUTED_SIZE);
            box.setStyle("-fx-border-color: #CFCFCF; -fx-background-color:#FFFFFF; -fx-alignment:center; -fx-border-radius:10; -fx-background-radius:10;");

            gridPaneMon.add(box, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        gridPaneMon.requestLayout();
    }



    // -------------------- LOAD THÔNG TIN KHÁCH HÀNG --------------------
    private void loadThongTinKhachHang() {
        if (donDatBanHienTai != null) {
            Ban ban = donDatBanHienTai.getBan();
            KhachHang kh = donDatBanHienTai.getKhachHang();
            if (ban != null) {
                txtMaBan.setText(ban.getMaBan());
            }
            if (kh != null) {
                txtMaKH.setText(kh.getMaKH());
            }
            dpNgayDatBan.setValue(donDatBanHienTai.getNgayGioLapDon().toLocalDate());
        } else if (banDangChon != null) {
        	txtMaBan.setText(banDangChon.getMaBan()); // Tránh việc chỉ hiển thị mã bàn không thấy mã KH
            // fallback lấy bàn nếu chưa set donDatBanHienTai
            DonDatBan donDat = donDatBanDAO.layDonDatTheoBan(banDangChon.getMaBan());
            if (donDat != null && donDat.getKhachHang() != null) {
                KhachHang kh = donDat.getKhachHang();
                txtMaKH.setText(kh.getMaKH());
                dpNgayDatBan.setValue(donDat.getNgayGioLapDon().toLocalDate());
            }
        }
    }

//    private void chonMon(MonAn mon) {
//        if (dsMonAnDat.containsKey(mon)) {
//            // Nếu đã chọn món, tăng số lượng
//            dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
//        } else {
//            // Thêm món mới với số lượng = 1
//            dsMonAnDat.put(mon, 1);
//        }
//
//        // Cập nhật TableView
//        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
//        tblDS.refresh();
//
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Chọn món");
//        alert.setHeaderText(null);
//        alert.setContentText("Bạn đã chọn món: " + mon.getTenMon());
//        alert.showAndWait();
//    }
    private void chonMon(MonAn mon) {
        if (dsMonAnDat.containsKey(mon)) {
            // Nếu đã có món này hỏi người dùng
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Món đã chọn");
            alert.setHeaderText("Món '" + mon.getTenMon() + "đã có trong danh sách.");
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
        double tongTruocVAT = dsMonAnDat.entrySet().stream()
            .mapToDouble(e -> e.getKey().getDonGia() * e.getValue())
            .sum();

        double tienVAT = tongTruocVAT * Vat_Rate;
        double tongSauVAT = tongTruocVAT + tienVAT;

        lblVat.setText(dinhDangTien(tienVAT));
        lblTongTien.setText(dinhDangTien(tongTruocVAT));
        lblTongTienVAT.setText(dinhDangTien(tongSauVAT));
    }
    
    private String dinhDangTien(double soTien) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(soTien);
    }
    
}
