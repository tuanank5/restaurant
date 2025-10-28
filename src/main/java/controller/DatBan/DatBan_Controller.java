package controller.DatBan;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import config.RestaurantApplication;
import dao.Ban_DAO;
import dao.LoaiBan_DAO;
import dao.NhanVien_DAO;
import entity.Ban;
//import entity.Ghe;
import entity.LoaiBan;
import entity.NhanVien;
//import entity.ToaTau;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DatBan_Controller {
	@FXML
    private Button btnDatBan;

    @FXML
    private Button btnHienTai;

    @FXML
    private Button btnTimKiem;

    @FXML
    private ComboBox<String> cmbGioBD;

    @FXML
    private ComboBox<String> cmbGioKT;

    @FXML
    private ComboBox<String> cmbKM;

    @FXML
    private ComboBox<String> cmbLoaiBan;

    @FXML
    private ComboBox<String> cmbTrangThai;

    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtLoaiBan;

    @FXML
    private DatePicker txtNgayDatBan;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSoLuong;

    @FXML
    private TextField txtSoLuongKH;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtTrangThai;

    @FXML
    private TextField txtViTri;
    
    private ObservableList<NhanVien> danhSachNhanVien = FXCollections.observableArrayList();
    private List<LoaiBan> danhSachLoaiBanDB;
    private List<Ban> danhSachBanDB;
    
    @FXML
    private void initialize() {
        loadData();
    }
    
    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == txtSoLuongKH) {
//            showThemNhanVien();
        } else if (source == cmbLoaiBan) {
//            xuatExcel();
        } else if (source == cmbTrangThai) {
        	
        } else if (source == txtNgayDatBan) {
        	
        } else if (source == cmbGioBD) {
        	
        } else if (source == cmbGioKT) {
        	
        } else if (source == btnHienTai) {
        	
        }
    }

    private void loadData() {
        Map<String, Object> filter = new HashMap<>();
        
        danhSachLoaiBanDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(LoaiBan_DAO.class)
                .getDanhSach(LoaiBan.class, filter);
        cmbLoaiBan.getItems().add("Tất cả");
        for(LoaiBan loaiBan : danhSachLoaiBanDB) {
        	cmbLoaiBan.getItems().add(loaiBan.getTenLoaiBan());
        }
        
        Map<String, Object> filter1 = new HashMap<>();
        cmbLoaiBan.getSelectionModel().selectFirst();
        
        danhSachBanDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, filter1);
        cmbTrangThai.getItems().add("Tất cả");
        cmbTrangThai.getItems().add("Đã đặt");
        cmbTrangThai.getItems().add("Trống");
        cmbTrangThai.getSelectionModel().selectFirst();
        
        txtNgayDatBan.setValue(LocalDate.now());
        
        LocalTime currentTime = LocalTime.now();
        int currentHour = currentTime.getHour();
        
        for (int i = 0; i < 24; i++) {
            cmbGioBD.getItems().add(i + "");
        }
        
        cmbGioBD.setValue(currentHour + "");
        setGioKT(currentHour);
        
    }
    
    private void setGioKT(int currentHour) {
    	cmbGioKT.getItems().clear();
    	for (int i = currentHour + 1; i < 24; i++) {
            cmbGioKT.getItems().add(i +  "");
        }
    	cmbGioKT.getSelectionModel().selectFirst();
    }
    
//    private void hienThiBan() {
//        gridPaneGheTrai.getChildren().clear();
//        gridPaneGheTrai.getRowConstraints().clear();
//        gridPaneGheTrai.getColumnConstraints().clear();
//
//        gridPaneGheTrai.setHgap(10); // Khoảng cách giữa các cột
//        gridPaneGheTrai.setVgap(10); // Khoảng cách giữa các hàng
//
//        // Cấu hình ScrollPane nếu cần
//        if (!(gridPaneGheTrai.getParent() instanceof ScrollPane)) {
//            ScrollPane scrollPaneLeft = new ScrollPane(gridPaneGheTrai);
//            scrollPaneLeft.setFitToWidth(true);
//            scrollPaneLeft.setPrefHeight(400); // Điều chỉnh chiều cao phù hợp
//        }
//
//        Map<ToaTau, Set<Ghe>> gheDaChonMap = isDi ? gheDaChonMapDi : gheDaChonMapVe;
//        List<Ghe> danhSachGhe = gheDAO.layDanhSachGheTrongToa(toaTau.getMaTt());
//        int totalSeats = danhSachGhe.size();
//        List<Ghe> gheDaMua = gheDAO.layDanhSachGheTrongLichTrinh(
//                toaTau.getTauByMaTau().getMaTau(),
//                Date.from(lichTrinh.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        Set<String> gheDaMuaSet = new HashSet<>();
//        for (Ghe ghe : gheDaMua) {
//            gheDaMuaSet.add(ghe.getMaGhe());
//        }
//
//        gheDaChonMap.putIfAbsent(toaTau, new HashSet<>());
//
//        for (int i = 0; i < totalSeats; i++) {
//            Ghe ghe = danhSachGhe.get(i);
//            String maGhe = ghe.getMaGhe();
//            VBox content = new VBox();
//            Label soGheLabel = new Label("" + ghe.getSoGhe());
//            soGheLabel.setStyle("-fx-font-size: 14px;"); // Tăng kích thước chữ
//            Label giaGheLabel = new Label(String.format("%.0fK", ghe.getGiaGhe() / 1000));
//            giaGheLabel.setStyle("-fx-font-size: 12px;"); // Tăng kích thước chữ
//
//            content.getChildren().addAll(soGheLabel, giaGheLabel);
//            content.setAlignment(Pos.CENTER);
//
//            ToggleButton gheButton = new ToggleButton();
//            gheButton.setGraphic(content);
//            gheButton.setPrefSize(100, 100); // Tăng kích thước ghế (rộng và cao)
//            gheButton.setMaxSize(100, 100);
//
//            // Thiết lập trạng thái ghế
//            if (gheDaMuaSet.contains(maGhe)) {
//                gheButton.setDisable(true);
//                gheButton.setStyle("-fx-border-color: red;");
//            } else if (gheDaChonMap.get(toaTau).contains(ghe)) {
//                gheButton.setSelected(true);
//                gheButton.setStyle(
//                        "-fx-border-color: #2bbaba; -fx-border-width: 3; -fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");
//            } else {
//                gheButton.setStyle("-fx-background-color: white; -fx-border-color: transparent;");
//            }
//
//            // Xử lý sự kiện khi nhấn vào ghế
//            gheButton.setOnAction(event -> {
//                if (!gheDaMuaSet.contains(maGhe)) {
//                    if (gheButton.isSelected()) {
//                        // Thêm viền bo góc và thay đổi màu viền khi ghế được chọn
//                        gheButton.setStyle(
//                                "-fx-border-color: #2bbaba; -fx-border-width: 3; -fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");
//                        gheDaChonMap.get(toaTau).add(ghe);
//                        themVe(ghe, toaTau, isDi);
//                        System.out.println(veList);
//                    } else {
//                        // Bỏ viền và màu khi ghế không được chọn
//                        gheButton.setStyle(
//                                "-fx-background-color: white; -fx-border-color: transparent; -fx-border-radius: 10; -fx-background-radius: 10;");
//                        gheDaChonMap.get(toaTau).remove(ghe);
//                        xoaVe(ghe, isDi);
//                        System.out.println(veList);
//                    }
//                }
//            });
//
//            int row = i / 4; // Số hàng
//            int column = i % 4; // Số cột
//
//            if (column >= 2) {
//                column++;
//            }
//            dsGhelb.setVisible(false);
//            gridPaneGheTrai.add(gheButton, column, row);
//        }
//
//        // Điều chỉnh phần trăm chiều rộng cho mỗi cột
//        for (int i = 0; i < 5; i++) {
//            ColumnConstraints columnConstraints = new ColumnConstraints();
//            columnConstraints.setPercentWidth(20); // Điều chỉnh lại chiều rộng các cột để có kích thước mong muốn
//            gridPaneGheTrai.getColumnConstraints().add(columnConstraints);
//        }
//
//        for (int i = 0; i < (totalSeats / 4) + 1; i++) {
//            RowConstraints rowConstraints = new RowConstraints();
//            rowConstraints.setPrefHeight(100); // Tăng chiều cao hàng để ghế lớn hơn
//            gridPaneGheTrai.getRowConstraints().add(rowConstraints);
//        }
//    }
}
