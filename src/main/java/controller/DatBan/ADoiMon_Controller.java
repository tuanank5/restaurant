package controller.DatBan;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.MonAn_DAOImpl;
import dao.impl.NhanVien_DAOImpl;
import config.RestaurantApplication;
import dao.ChiTietHoaDon_DAO;
import entity.ChiTietHoaDon;
import entity.Ban;
import entity.DonDatBan;
import entity.HoaDon;
import entity.MonAn;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class ADoiMon_Controller implements Initializable{

    @FXML
    private Button btnQuayLai;

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
    private TableColumn<MonAn, Integer> colSoLuong1;

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
    private TextField txtKhachHang;

    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtSoLuongKhach;
    
    //-----Bàn---------
    private Ban banDangChon;

    
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAOImpl();
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private List<MonAn> dsMonAn;
    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();
    private DonDatBan donDatBanHienTai;
    private HoaDon hoaDonHienTai;


    @FXML
    void btnQuayLai(ActionEvent event) {
    	MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
    }

    @FXML
    void btnXacNhan(ActionEvent event) {
        if (hoaDonHienTai == null) {
            showAlert("Lỗi", "Không tìm thấy hóa đơn!", Alert.AlertType.ERROR);
            return;
        }

        ChiTietHoaDon_DAO ctDAO = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(ChiTietHoaDon_DAO.class);

        // 1️⃣ Xóa chi tiết cũ
        ctDAO.deleteByMaHoaDon(hoaDonHienTai.getMaHoaDon());

        // 2️⃣ Lưu chi tiết mới
        for (Map.Entry<MonAn, Integer> entry : dsMonAnDat.entrySet()) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setHoaDon(hoaDonHienTai);
            ct.setMonAn(entry.getKey());
            ct.setSoLuong(entry.getValue());
            ctDAO.them(ct);
        }

        showAlert("Thành công", "Cập nhật món ăn thành công!", Alert.AlertType.INFORMATION);

        MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
    }

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hoaDonHienTai = MenuNV_Controller.aBanHienTai_HD;
		donDatBanHienTai = MenuNV_Controller.donDatBanDangDoi;
        dsMonAn = monAnDAO.getDanhSachMonAn();
        // Khởi tạo ComboBox phân loại (dùng dsMonAn)
        khoiTaoComboBoxPhanLoai();
        loadThongTinKhachVaSoLuong();
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
        this.donDatBanHienTai = MenuNV_Controller.donDatBanDangDoi;
        loadMonDaDatTuHoaDon();

		
	}
	
    public void setBanDangChon(Ban ban) {
        this.banDangChon = ban;
        loadMonCuaBan();
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

        // FIX NULL
        if (loaiChon == null || loaiChon.equals("Tất cả")) {
            loadMonAnToGrid(dsMonAn);
            return;
        }

        List<MonAn> dsLoc = new ArrayList<>();
        for (MonAn mon : dsMonAn) {
            if (loaiChon.equals(mon.getLoaiMon())) {
                dsLoc.add(mon);
            }
        }

        loadMonAnToGrid(dsLoc);
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

    
    private void chonMon(MonAn mon) {
    	if (dsMonAnDat.containsKey(mon)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Món đã chọn");
            alert.setHeaderText("Món '" + mon.getTenMon() + "' đã có trong danh sách.");
            alert.setContentText("Bạn muốn làm gì?");

            ButtonType btnTang = new ButtonType("➕ Tăng số lượng");
            ButtonType btnGiam = new ButtonType("➖ Giảm số lượng");
            ButtonType btnXoa = new ButtonType("❌ Xóa món");
            ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnTang, btnGiam, btnXoa, btnHuy);

            Button btnTangNode = (Button) alert.getDialogPane().lookupButton(btnTang);
            Button btnGiamNode = (Button) alert.getDialogPane().lookupButton(btnGiam);
            Button btnXoaNode  = (Button) alert.getDialogPane().lookupButton(btnXoa);

            btnTangNode.addEventFilter(ActionEvent.ACTION, e -> {
                dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);
                tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
                tblDS.refresh();
                e.consume();
            });

            btnGiamNode.addEventFilter(ActionEvent.ACTION, e -> {
                int soLuong = dsMonAnDat.get(mon) - 1;
                if (soLuong <= 0) {
                    dsMonAnDat.remove(mon);
                    alert.close();
                } else {
                    dsMonAnDat.put(mon, soLuong);
                }
                tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
                tblDS.refresh();
                e.consume();
            });
         
            btnXoaNode.addEventHandler(ActionEvent.ACTION, e -> {
                dsMonAnDat.remove(mon);
                tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
                tblDS.refresh();
            });
            alert.show();
    	}else {
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
    
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();

    private String dinhDangTien(double soTien) {
    	NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(soTien);
    }
    private void loadMonDaDatTuHoaDon() {
        if (hoaDonHienTai == null) {
            System.out.println("❌ hoaDonHienTai = null");
            return;
        }

        dsMonAnDat.clear();

        List<ChiTietHoaDon> dsCT =
                RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(ChiTietHoaDon_DAO.class)
                        .getChiTietTheoMaHoaDon(hoaDonHienTai.getMaHoaDon());

        for (ChiTietHoaDon ct : dsCT) {
            dsMonAnDat.put(ct.getMonAn(), ct.getSoLuong());
        }

        tblDS.setItems(FXCollections.observableArrayList(dsMonAnDat.keySet()));
        tblDS.refresh();
        capNhatTongTien();
    }


    public void setDonDatBanHienTai(DonDatBan don) {
        this.donDatBanHienTai = don;
        loadMonDaDatTuHoaDon();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
    private void loadThongTinKhachVaSoLuong() {

        // ===== TÊN KHÁCH =====
        if (hoaDonHienTai != null && hoaDonHienTai.getKhachHang() != null) {
            txtKhachHang.setText(hoaDonHienTai.getKhachHang().getTenKH());
        } else {
            txtKhachHang.setText("Khách lẻ");
        }

        // ===== SỐ LƯỢNG KHÁCH =====
        if (donDatBanHienTai != null) {
            txtSoLuongKhach.setText(
                    String.valueOf(donDatBanHienTai.getSoLuong())
            );
        }

        // ===== MÃ BÀN =====
            txtMaBan.setText(
                    donDatBanHienTai.getBan().getMaBan()
            );
        
    }


    }


    
    


