package controller.DatMon;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.HoaDon_DAO;
import dao.impl.HoaDon_DAOImpl;
import dao.ChiTietHoaDon_DAO;
import dao.DonDatBan_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.MonAn_DAOImpl;

import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class DoiMonTruoc_Controller implements Initializable {

    @FXML private TextField txtTim;
    @FXML private Button btnTroLai;
    @FXML private ComboBox<String> comBoxPhanLoai;
    @FXML private ScrollPane scrollPaneMon;
    @FXML private GridPane gridPaneMon;

    // TABLE MÓN CŨ
    @FXML private TableView<ChiTietHoaDon> tblMonCu;
    @FXML private TableColumn<ChiTietHoaDon, Integer> colSTT;
    @FXML private TableColumn<ChiTietHoaDon, String> colTenMonCu;
    @FXML private TableColumn<ChiTietHoaDon, Integer> colSoLuongCu;
    @FXML private TableColumn<ChiTietHoaDon, Double> colDonGia;

    private ObservableList<ChiTietHoaDon> danhSachMon = FXCollections.observableArrayList();
    private List<MonAn> dsMonAn;
    private Map<MonAn, Integer> dsMonAnDat = new LinkedHashMap<>();

    public static Ban banChonStatic;
    public static DonDatBan donDatBanDuocChon;

    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private DonDatBan_DAO donDatBanDAO = new DonDatBan_DAOImpl();
    private MonAn_DAO monAnDAO = new MonAn_DAOImpl();
    private ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAOImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dsMonAn = monAnDAO.getDanhSachMonAn();
        khoiTaoComboBoxPhanLoai();

        if (dsMonAn != null && !dsMonAn.isEmpty()) {
            loadMonAnToGrid(dsMonAn);
        }

        txtTim.textProperty().addListener((obs, o, n) -> timMonTheoTen());

        // ===== TABLE MÓN CŨ =====
        tblMonCu.setItems(danhSachMon);

        colSTT.setCellValueFactory(col ->
            new ReadOnlyObjectWrapper<>(tblMonCu.getItems().indexOf(col.getValue()) + 1)
        );

        colTenMonCu.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getMonAn().getTenMon())
        );

        colSoLuongCu.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getSoLuong())
        );

        colDonGia.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getThanhTien())
        );

        colDonGia.setCellFactory(column -> new TableCell<ChiTietHoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : dinhDangTien(item));
            }
        });

        loadMonCu();
    }

    // ===== CHỌN MÓN =====
    private void chonMon(MonAn mon) {

        if (dsMonAnDat.containsKey(mon)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Món đã chọn");
            alert.setHeaderText(mon.getTenMon());
            alert.setContentText("Bạn muốn thao tác gì?");

            ButtonType btnTang = new ButtonType("➕ Tăng");
            ButtonType btnGiam = new ButtonType("➖ Giảm");
            ButtonType btnXoa = new ButtonType("❌ Xoá");
            ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnTang, btnGiam, btnXoa, btnHuy);

            Optional<ButtonType> kq = alert.showAndWait();

            if (!kq.isPresent()) return;

            if (kq.get() == btnTang) {
                dsMonAnDat.put(mon, dsMonAnDat.get(mon) + 1);

            } else if (kq.get() == btnGiam) {
                int sl = dsMonAnDat.get(mon) - 1;
                if (sl <= 0) dsMonAnDat.remove(mon);
                else dsMonAnDat.put(mon, sl);

            } else if (kq.get() == btnXoa) {
                dsMonAnDat.remove(mon);
            }

        } else {
            dsMonAnDat.put(mon, 1);
        }

        hienThiMonMoi();
    }

    // ===== HIỂN THỊ MÓN MỚI =====
    private void hienThiMonMoi() {
        danhSachMon.clear();
        for (Map.Entry<MonAn,Integer> e : dsMonAnDat.entrySet()) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setMonAn(e.getKey());
            ct.setSoLuong(e.getValue());
            ct.setThanhTien(e.getKey().getDonGia() * e.getValue());
            danhSachMon.add(ct);
        }
        tblMonCu.refresh();  // Cập nhật TableView ngay
    }

    // ===== LOAD MÓN CŨ TỪ HÓA ĐƠN =====
    private void loadMonCu() {

        if (donDatBanDuocChon == null) return;

        String maBan = donDatBanDuocChon.getMaDatBan();
        HoaDon hd = hoaDonDAO.getHoaDonTheoMaBan(maBan);

        if (hd == null) {
            danhSachMon.clear();
            return;
        }

        List<ChiTietHoaDon> list = chiTietHoaDonDAO.getChiTietTheoMaHoaDon(hd.getMaHoaDon());
        danhSachMon.setAll(list);
    }

    // ===== TÌM MÓN =====
    private void timMonTheoTen() {
        String tuKhoa = txtTim.getText().trim().toLowerCase();
        List<MonAn> dsLoc = new ArrayList<>();

        if (tuKhoa.isEmpty()) dsLoc = dsMonAn;
        else {
            for (MonAn mon : dsMonAn) {
                if (mon.getTenMon().toLowerCase().contains(tuKhoa))
                    dsLoc.add(mon);
            }
        }

        loadMonAnToGrid(dsLoc);
    }

    // ===== LỌC LOẠI MÓN =====
    private void locMonTheoLoai() {
        String loaiChon = comBoxPhanLoai.getValue();
        List<MonAn> dsLoc = new ArrayList<>();

        if (loaiChon.equals("Tất cả")) dsLoc = dsMonAn;
        else {
            for (MonAn mon : dsMonAn) {
                if (loaiChon.equals(mon.getLoaiMon()))
                    dsLoc.add(mon);
            }
        }

        loadMonAnToGrid(dsLoc);
    }

    private void khoiTaoComboBoxPhanLoai() {
        List<String> dsLoai = new ArrayList<>();
        dsLoai.add("Tất cả");
        for (MonAn m : dsMonAn) {
            if (!dsLoai.contains(m.getLoaiMon())) dsLoai.add(m.getLoaiMon());
        }
        comBoxPhanLoai.getItems().setAll(dsLoai);
        comBoxPhanLoai.setOnAction(e -> locMonTheoLoai());
    }

    // ===== LOAD GRID MÓN ĂN =====
    private void loadMonAnToGrid(List<MonAn> danhSach) {
        gridPaneMon.getChildren().clear();
        gridPaneMon.getColumnConstraints().clear();
        gridPaneMon.getRowConstraints().clear();

        int columns = 4;
        int col = 0, row = 0;

        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPaneMon.getColumnConstraints().add(cc);
        }

        int totalRows = (int) Math.ceil(danhSach.size() / (double) columns);
        for (int i = 0; i < totalRows; i++) {
            gridPaneMon.getRowConstraints().add(new RowConstraints(220));
        }

        for (MonAn mon : danhSach) {
            ImageView img = new ImageView();
            try { img.setImage(new Image("file:" + mon.getDuongDanAnh())); } catch (Exception ex) {}

            img.setFitWidth(120);
            img.setFitHeight(120);

            Label lblTen = new Label(mon.getTenMon());
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

            Label lblGia = new Label(dinhDangTien(mon.getDonGia()));

            Button btnChon = new Button("Chọn");
            btnChon.setOnAction(e -> chonMon(mon));

            VBox box = new VBox(img, lblTen, lblGia, btnChon);
            box.setSpacing(6);
            box.setStyle("-fx-alignment:center; -fx-border-color:#ccc; -fx-background-color:white;");

            gridPaneMon.add(box, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    private String dinhDangTien(double soTien) {
        return String.format("%,.0f", soTien);
    }

    // ===== XÁC NHẬN ĐỔI MÓN =====
    @FXML
    private void btnXacNhan(ActionEvent e) {

        if (donDatBanDuocChon == null) {
            alert("Lỗi", "Chưa chọn đơn đặt bàn!");
            return;
        }
        
        // ===== DEBUG =====
        System.out.println("Bàn chọn: " + donDatBanDuocChon.getMaDatBan());
        HoaDon hd1 = hoaDonDAO.getHoaDonTheoMaBan(donDatBanDuocChon.getMaDatBan());
        System.out.println("HoaDon: " + hd1);
        // =================

        String maBan = donDatBanDuocChon.getMaDatBan();
        HoaDon hd = hoaDonDAO.getHoaDonTheoMaBan(maBan);

        if (hd == null) {
            alert("Lỗi", "Không tìm thấy hóa đơn!");
            return;
        }

        try {
            // Xóa món cũ
            chiTietHoaDonDAO.deleteByMaHoaDon(hd.getMaHoaDon());

            // Ghi món mới
            for (Map.Entry<MonAn,Integer> eMon : dsMonAnDat.entrySet()) {
                MonAn mon = eMon.getKey();
                int sl = eMon.getValue();
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setHoaDon(hd);
                ct.setMonAn(mon);
                ct.setSoLuong(sl);
                ct.setThanhTien(mon.getDonGia() * sl);
                chiTietHoaDonDAO.themChiTiet(ct); 
            }

            alert("Thành công", "Đổi món thành công!");

        } catch (Exception ex) {
            alert("Lỗi", "Không lưu được: " + ex.getMessage());
        }
    }

    private void alert(String t, String m){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
    
}


