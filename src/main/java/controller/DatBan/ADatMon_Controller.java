package controller.DatBan;

import dao.KhachHang_DAO;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.impl.DonDatBan_DAOImpl;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.KhachHang;
import entity.DonDatBan;
import entity.HoaDon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import util.AutoIDUitl;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import config.RestaurantApplication;
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


public class ADatMon_Controller implements Initializable {


    @FXML
    private Button btnGiam;

    @FXML
    private TextField txtKhachHang;
    
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
    private TextField txtSoLuong;
    
    //-----Bàn---------
    private Ban banDangChon;
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
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
    
    private boolean checkTTbangKo = ABanHienTai_Controller.aBHT.checkTTbangKo;
    private Map<MonAn, Integer> dsMonAnTA = ABanHienTai_Controller.aBHT.dsMonAnTA;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	 // Lấy danh sách món ăn từ DB
        dsMonAn = monAnDAO.getDanhSachMonAn();
        loadTenKhachHang();
        // lấy sluong khách
        if (checkTTbangKo == true) {
            txtSoLuong.setText(MenuNV_Controller.aBanHienTai_HD.getDonDatBan().getSoLuong() + "");
        } else {
            txtSoLuong.setText(String.valueOf(MenuNV_Controller.soLuongKhach));       	
        }
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
        if (checkTTbangKo == true) {
        	txtMaBan.setText(MenuNV_Controller.aBanHienTai_HD.getDonDatBan().getBan().getMaBan());	
        } else if (MenuNV_Controller.banDangChon != null) {
            this.banDangChon = MenuNV_Controller.banDangChon;
            txtMaBan.setText(banDangChon.getMaBan());
            loadMonCuaBan();
        }
        tblDS.setOnMouseClicked(e -> capNhatTongTien());
        
        if (checkTTbangKo == false) {
            Tooltip toolTipQL = new Tooltip("Quay lại danh sách bàn");
    		btnQuayLai.setTooltip(toolTipQL);
    		
    		Tooltip toolTipLM = new Tooltip("Lọc danh sách theo loại món");
    		cmbLoaiMon.setTooltip(toolTipLM);

    		Tooltip toolTipHuy = new Tooltip("Hủy đặt bàn và đặt món");
    		btnHuy.setTooltip(toolTipHuy);

    		Tooltip toolTipXN = new Tooltip("Xác nhận đặt bàn và đặt món");
    		btnXacNhan.setTooltip(toolTipXN);
		} else {
			Tooltip toolTipQL2 = new Tooltip("Quay lại danh sách bàn");
    		btnQuayLai.setTooltip(toolTipQL2);
			
			Tooltip toolTipHuy2 = new Tooltip("Hủy đặt món");
			btnHuy.setTooltip(toolTipHuy2);
			
			Tooltip toolTipXN = new Tooltip("Xác nhận đặt món");
    		btnXacNhan.setTooltip(toolTipXN);
		}
		
		btnHuy.setOnAction(e -> quayLaiGD());
    }
    private void quayLaiGD() {
		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	@FXML
    void btnQuayLai(ActionEvent event) {
		if (checkTTbangKo == true) {
			MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
		} else {
	    	MenuNV_Controller.instance.readyUI("DatBan/aDatBanHienTai");			
		}
    }
    
    // cài khách hàng
    private KhachHang khachHangDangChon;

    public void setKhachHang(KhachHang kh) {
        this.khachHangDangChon = kh;
        if (kh != null) {
            txtKhachHang.setText(kh.getTenKH());
        }
    }
    
    private void loadTenKhachHang() {
        if (checkTTbangKo == false) {
            txtKhachHang.setText(MenuNV_Controller.khachHangDangChon.getTenKH());
        } else {
        	txtKhachHang.setText(MenuNV_Controller.aBanHienTai_HD.getKhachHang().getTenKH());
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
    void handleThanhToan(ActionEvent event) {
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
            MenuNV_Controller.instance.readyUI("HoaDon/ChiTiet");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleXacNhan(ActionEvent event) {
    	if (dsMonAnDat.isEmpty()) {
           	showAlert("Thông báo", "Vui lòng chọn món ăn", Alert.AlertType.INFORMATION);
        } else {
        	if (checkTTbangKo == true) {
        		showAlert("Thông báo", "Lưu hóa đơn tạm thành công!", Alert.AlertType.INFORMATION);
        		themChiTietHoaDon(MenuNV_Controller.aBanHienTai_HD, dsMonAnDat);
                MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
        	} else {
        		DonDatBan ddb = themDDB();
            	themHD(ddb);
        	}
    	}
    }
    
    private DonDatBan themDDB() {
    	LocalDateTime nowDate = LocalDateTime.now();
    	LocalTime nowHour = LocalTime.now();
    	
    	DonDatBan ddb = new DonDatBan();
    	ddb.setMaDatBan(AutoIDUitl.sinhMaDonDatBan());
    	ddb.setNgayGioLapDon(nowDate);
    	ddb.setSoLuong(MenuNV_Controller.soLuongKhach);
    	ddb.setBan(MenuNV_Controller.banDangChon);
    	ddb.setGioBatDau(nowHour);
    	ddb.setTrangThai("Đã nhận bàn");
    	
    	if (RestaurantApplication.getInstance()
   	                    .getDatabaseContext()
   	                    .newEntity_DAO(DonDatBan_DAO.class)
   	                    .them(ddb)) {
    		return ddb;
    	}

		return null;
	}

    private void themHD(DonDatBan ddb) {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(AutoIDUitl.sinhMaHoaDon());
        LocalDate localDate = LocalDate.now();
        Date dateNow = Date.valueOf(localDate);
        hd.setNgayLap(dateNow);

        hd.setTrangThai("Hiện tại");
        hd.setKieuThanhToan("Chưa thanh toán");

        // --- Gán khách hàng ---
        KhachHang kh = MenuNV_Controller.khachHangDangChon;
        hd.setKhachHang(kh);

        hd.setKhuyenMai(null);
        hd.setNhanVien(MenuNV_Controller.taiKhoan.getNhanVien());
        hd.setDonDatBan(ddb);
        hd.setCoc(null);

        try {
            boolean check = RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(HoaDon_DAO.class)
                    .them(hd);

            if (check) {
                showAlert("Thông báo", "Lưu hóa đơn tạm thành công!", Alert.AlertType.INFORMATION);
                if (!dsMonAnDat.isEmpty()) {
                    themChiTietHoaDon(hd, dsMonAnDat);
                    MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
                }
            } else {
                showAlert("Thông báo", "Lưu hóa đơn tạm thất bại!", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "ADatMon_Controller lỗi", Alert.AlertType.ERROR);
        }
    }

    
    private void themChiTietHoaDon(HoaDon hd, Map<MonAn, Integer> dsMonAn) {
    	ChiTietHoaDon cthd = new ChiTietHoaDon();
    	for (Map.Entry<MonAn, Integer> entry : dsMonAn.entrySet()) {
            MonAn monAn = entry.getKey(); // Lấy món ăn
            Integer soLuong = entry.getValue(); // Lấy số lượng tương ứng
            cthd.setHoaDon(hd);
            cthd.setMonAn(monAn);
            cthd.setSoLuong(soLuong);
            RestaurantApplication.getInstance()
               .getDatabaseContext()
               .newEntity_DAO(ChiTietHoaDon_DAO.class)
               .them(cthd);
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
   