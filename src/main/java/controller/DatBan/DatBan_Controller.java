package controller.DatBan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

/**
 * Lớp Controller xử lý giao diện Đặt Bàn (DatBan-test.fxml).
 */
public class DatBan_Controller implements Initializable {

    // --- Khai báo FXML Controls từ file DatBan-test.fxml ---
    // Thông tin Bàn
    @FXML private TextField txtTrangThai; 
    @FXML private TextField txtViTri;     
    @FXML private TextField txtSoLuong;   
    @FXML private TextField txtLoaiBan;   
    
    // Thông tin Khách hàng và Đặt bàn
    @FXML private TextField txtSDT;          
    @FXML private TextField txtDiemTichLuy;  
    @FXML private TextField txtTenKH;        
    @FXML private TextField txtSoLuongKH;    
    @FXML private ComboBox<String> cmbKM;        
    @FXML private DatePicker txtNgayDatBan;  
    @FXML private ComboBox<String> cmbGioBD;     
    @FXML private ComboBox<String> cmbGioKT;     
    @FXML private ComboBox<String> cmbTrangThai; 
    @FXML private ComboBox<String> cmbLoaiBan;   
    
    // Nút chức năng
    @FXML private Button btnTimKiem;
    @FXML private Button btnDatBan;
    @FXML private Button btnHienTai;
    
    // Khu vực hiển thị Bàn
    @FXML private GridPane gridPaneBan;

    // --- Mô hình dữ liệu giả lập ---
    public static class Ban {
        private String maBan;
        private String loaiBan; 
        private int soChoToiDa;
        private String viTri;
        private String trangThai; 
        private Button uiButton; 

        public Ban(String maBan, String loaiBan, int soChoToiDa, String viTri, String trangThai) {
            this.maBan = maBan;
            this.loaiBan = loaiBan;
            this.soChoToiDa = soChoToiDa;
            this.viTri = viTri;
            this.trangThai = trangThai;
        }

        public String getMaBan() { return maBan; }
        public String getLoaiBan() { return loaiBan; }
        public int getSoChoToiDa() { return soChoToiDa; }
        public String getViTri() { return viTri; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
        public Button getUIButton() { return uiButton; }
        public void setUIButton(Button uiButton) { this.uiButton = uiButton; }
    }
    
    private List<Ban> danhSachBan;
    private Ban banDangChon = null; 
    private Button buttonBanDangChonUI = null; 

// --------------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        khoiTaoDuLieuGiaLap();
        khoiTaoComboBoxes();
        
        loadDanhSachBan();
        
        btnDatBan.setOnAction(this::handleDatBan);
        btnTimKiem.setOnAction(this::handleTimKiemKhachHang);
        btnHienTai.setOnAction(this::handleHienThiThoiGianHienTai);

        cmbTrangThai.setOnAction(e -> loadDanhSachBan());
        cmbLoaiBan.setOnAction(e -> loadDanhSachBan());
        
        btnDatBan.setDisable(true);
    }

// --------------------------------------------------------------------------------
// --- NHÓM HÀM KHỞI TẠO VÀ TẢI DỮ LIỆU ---
// --------------------------------------------------------------------------------

    private void khoiTaoDuLieuGiaLap() {
        danhSachBan = new ArrayList<>();
        // Giả lập dữ liệu bàn
        danhSachBan.add(new Ban("B01", "Lớn", 10, "Tầng 1 - Phòng VIP", "Trống"));
        danhSachBan.add(new Ban("B02", "Thường", 4, "Tầng 1 - Cửa Sổ", "Trống"));
        danhSachBan.add(new Ban("B03", "Thường", 4, "Tầng 1 - Giữa", "Đã được đặt"));
        danhSachBan.add(new Ban("B04", "Nhỏ", 2, "Tầng 2 - Góc", "Trống"));
        danhSachBan.add(new Ban("B05", "Lớn", 8, "Tầng 2 - Ban Công", "Đang phục vụ"));
        danhSachBan.add(new Ban("B06", "Nhỏ", 2, "Tầng 2 - Giữa", "Trống"));
        danhSachBan.add(new Ban("B07", "Thường", 6, "Tầng 1 - Giữa", "Trống"));
        danhSachBan.add(new Ban("B08", "Thường", 4, "Tầng 2 - Giữa", "Trống"));
    }
    
    private void khoiTaoComboBoxes() {
        cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
        cmbTrangThai.getSelectionModel().select("Tất cả");

        cmbLoaiBan.getItems().addAll("Tất cả", "Nhỏ", "Thường", "Lớn");
        cmbLoaiBan.getSelectionModel().select("Tất cả");
        
        List<String> hours = new ArrayList<>();
        for (int h = 8; h <= 22; h += 2) {
            hours.add(String.format("%02d:00", h));
        }
        cmbGioBD.getItems().addAll(hours);
        cmbGioKT.getItems().addAll(hours.subList(1, hours.size()));
        
        cmbKM.getItems().addAll("Không", "KM10%", "VIP20%");
        cmbKM.getSelectionModel().selectFirst();
    }


    /**
     * Tải và hiển thị danh sách các bàn vào gridPaneBan dựa trên bộ lọc.
     */
    private void loadDanhSachBan() {
        gridPaneBan.getChildren().clear();
        banDangChon = null; 
        buttonBanDangChonUI = null;
        resetThongTinBan();
        
        int col = 0;
        int row = 0;
        final int MAX_COLS = 5; 
        
        String trangThaiLoc = cmbTrangThai.getValue();
        String loaiBanLoc = cmbLoaiBan.getValue();

        for (Ban ban : danhSachBan) {
            boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
            boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan());

            if (matchStatus && matchType) {
                Button btnBan = new Button();
                btnBan.setText(ban.getMaBan() + "\n(" + ban.getSoChoToiDa() + " chỗ)");
                btnBan.setPrefSize(120, 100);
                
                btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan()));
                
                ban.setUIButton(btnBan);
                
                btnBan.setOnAction(e -> handleChonBan(ban, btnBan));
                
                // Thêm 5.0 padding/margin giữa các bàn
                GridPane.setMargin(btnBan, new javafx.geometry.Insets(5.0));

                gridPaneBan.add(btnBan, col, row);

                col++;
                if (col >= MAX_COLS) { 
                    col = 0;
                    row++;
                }
            }
        }
    }
    
    /**
     * Trả về CSS Style cho Button dựa trên trạng thái và loại bàn, đồng bộ với hình ảnh UI.
     */
    private String getStyleByStatusAndType(String trangThai, String loaiBan) {
        String baseStyle = "-fx-border-color: #333333; -fx-border-radius: 5; -fx-background-radius: 5; -fx-alignment: center; -fx-font-weight: bold; -fx-text-fill: white;";
        String colorStyle;
        
        // Màu nền dựa trên Loại bàn (Điều chỉnh theo ảnh và FXML Legend)
        switch (loaiBan) {
            case "Nhỏ":
                // Màu xanh lá nhạt (Tương đương B04 trong ảnh)
                colorStyle = "-fx-background-color: #7FFF7F;"; 
                break;
            case "Thường":
                // Màu Xanh dương đậm (Tương đương B02 trong ảnh)
                colorStyle = "-fx-background-color: #0066cc;"; 
                break;
            case "Lớn":
                // Màu Xanh lá đậm (Tương đương B05 trong ảnh)
                colorStyle = "-fx-background-color: #33cc66;"; 
                break;
            default:
                colorStyle = "-fx-background-color: #f0f0f0; -fx-text-fill: black;";
        }
        
        // Màu Border (Viền) dựa trên Trạng thái
        switch (trangThai) {
            case "Đã được đặt":
                // Viền ĐỎ (như B03 trong ảnh)
                return baseStyle + colorStyle + " -fx-border-color: RED; -fx-border-width: 3;";
            case "Đang phục vụ":
                // Viền VÀNG/CAM (Giả định trạng thái khác Đã được đặt)
                return baseStyle + colorStyle + " -fx-border-color: ORANGE; -fx-border-width: 3;";
            case "Trống":
                // Viền mỏng, mặc định
                return baseStyle + colorStyle + " -fx-border-width: 1; -fx-border-color: #333333;"; 
            default: 
                return baseStyle + colorStyle + " -fx-border-width: 1;";
        }
    }

// --------------------------------------------------------------------------------
// --- NHÓM HÀM XỬ LÝ SỰ KIỆN ---
// --------------------------------------------------------------------------------
    
    /**
     * Xử lý khi người dùng click chọn một bàn.
     */
    private void handleChonBan(Ban ban, Button btnUI) {
        // 1. Reset border của Button đang chọn trước đó (An toàn, đã fix NullPointerException)
        if (banDangChon != null && buttonBanDangChonUI != null && buttonBanDangChonUI != btnUI) {
             // Đặt lại style mặc định của bàn cũ
             buttonBanDangChonUI.setStyle(getStyleByStatusAndType(banDangChon.getTrangThai(), banDangChon.getLoaiBan())); 
        }
        
        // 2. Cập nhật bàn đang chọn mới
        this.banDangChon = ban;
        this.buttonBanDangChonUI = btnUI;
        
        // 3. Highlight bàn đang chọn (dùng viền màu #2bbaba từ Legend)
        btnUI.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan()) + " -fx-border-color: #2bbaba; -fx-border-width: 3;");

        // 4. Hiển thị thông tin bàn lên cột bên trái
        txtLoaiBan.setText(ban.getLoaiBan());
        txtSoLuong.setText(String.valueOf(ban.getSoChoToiDa()));
        txtViTri.setText(ban.getViTri());
        txtTrangThai.setText(ban.getTrangThai());
        
        // 5. Kích hoạt nút Đặt bàn nếu bàn "Trống"
        btnDatBan.setDisable(!"Trống".equals(ban.getTrangThai()));
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Tìm kiếm" khách hàng bằng Số điện thoại.
     * (Đã sửa lỗi không hiển thị thông tin tìm được)
     */
    private void handleTimKiemKhachHang(ActionEvent event) {
        String sdt = txtSDT.getText().trim();
        
        if (sdt.isEmpty()) {
            hienThiThongBao(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số điện thoại.");
            return;
        }

        String tenKH = "Khách hàng mới (Chưa có trong hệ thống)";
        String diemTichLuy = "0";

        // --- Logic Tìm kiếm giả lập ---
        if ("0901234567".equals(sdt) || "0901000178".equals(sdt)) { 
            tenKH = "Nguyễn Văn A (Thành viên)";
            diemTichLuy = "500";
            hienThiThongBao(AlertType.INFORMATION, "Thông báo", "Đã tìm thấy thông tin khách hàng: Nguyễn Văn A (500 điểm).");
        } else {
            hienThiThongBao(AlertType.INFORMATION, "Thông báo", "Không tìm thấy khách hàng. Vui lòng đặt bàn với tư cách khách mới.");
        }
        
        // Cập nhật UI (Sẽ hiển thị ngay cả khi trường là editable="false")
        txtTenKH.setText(tenKH);
        txtDiemTichLuy.setText(diemTichLuy);
    }
    
    private void handleHienThiThoiGianHienTai(ActionEvent event) {
        txtNgayDatBan.setValue(LocalDate.now());
        
        LocalTime currentTime = LocalTime.now();
        String currentHour = String.format("%02d:00", currentTime.getHour());
        String nextHour = String.format("%02d:00", (currentTime.getHour() + 2) % 24);
        
        cmbGioBD.getSelectionModel().select(currentHour);
        cmbGioKT.getSelectionModel().select(nextHour);
        
        loadDanhSachBan();
    }

    /**
     * Phương thức chính xử lý logic khi nhấn nút "Đặt bàn" (btnDatBan).
     */
    private void handleDatBan(ActionEvent event) {
        if (banDangChon == null) {
            hienThiThongBao(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một bàn trước khi đặt.");
            return;
        }
        
        // 1. Xác thực dữ liệu
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String soLuongKH = txtSoLuongKH.getText().trim();
        LocalDate ngayDat = txtNgayDatBan.getValue();
        
        if (tenKH.isEmpty() || sdt.isEmpty() || soLuongKH.isEmpty() || ngayDat == null || cmbGioBD.getValue() == null || cmbGioKT.getValue() == null) {
            hienThiThongBao(AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin: Tên KH, SĐT, Số lượng khách, Ngày và Giờ đặt.");
            return;
        }
        
        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongKH);
            if (soLuong <= 0) {
                 hienThiThongBao(AlertType.ERROR, "Lỗi", "Số lượng khách phải là số dương.");
                 return;
            }
            if (soLuong > banDangChon.getSoChoToiDa()) {
                hienThiThongBao(AlertType.WARNING, "Cảnh báo", "Số lượng khách vượt quá số chỗ tối đa (" + banDangChon.getSoChoToiDa() + "). Vẫn tiến hành đặt bàn.");
            }
        } catch (NumberFormatException e) {
             hienThiThongBao(AlertType.ERROR, "Lỗi", "Số lượng khách phải là số nguyên hợp lệ.");
             return;
        }

        // Kiểm tra logic thời gian 
        String gioBDStr = cmbGioBD.getValue().replace(":", "");
        String gioKTStr = cmbGioKT.getValue().replace(":", "");
        if (Integer.parseInt(gioBDStr) >= Integer.parseInt(gioKTStr)) {
            hienThiThongBao(AlertType.ERROR, "Lỗi", "Giờ kết thúc phải lớn hơn Giờ bắt đầu.");
            return;
        }
        
        // 2. Thực hiện nghiệp vụ Đặt bàn
        try {
            // Cập nhật trạng thái bàn trong Model giả lập
            banDangChon.setTrangThai("Đã được đặt");
            
            // Cập nhật giao diện của bàn vừa đặt
            if (buttonBanDangChonUI != null) {
                // Áp dụng style mới (có viền ĐỎ)
                buttonBanDangChonUI.setStyle(getStyleByStatusAndType("Đã được đặt", banDangChon.getLoaiBan()));
            }
            
            hienThiThongBao(AlertType.INFORMATION, "Thành công", 
                "Đã đặt bàn " + banDangChon.getMaBan() + " thành công cho khách hàng: " + tenKH);
            
            // 3. Reset trạng thái sau khi đặt bàn thành công
            this.banDangChon = null; 
            this.buttonBanDangChonUI = null;
            resetForm(); 
            btnDatBan.setDisable(true); 
            
        } catch (Exception e) {
            hienThiThongBao(AlertType.ERROR, "Lỗi hệ thống", "Có lỗi xảy ra khi thực hiện đặt bàn: " + e.getMessage());
            e.printStackTrace();
        }
    }

// --------------------------------------------------------------------------------
// --- NHÓM HÀM TIỆN ÍCH ---
// --------------------------------------------------------------------------------

    private void resetThongTinBan() {
        txtLoaiBan.clear();
        txtSoLuong.clear();
        txtViTri.clear();
        txtTrangThai.clear();
        btnDatBan.setDisable(true);
    }
    
    private void resetForm() {
        txtSDT.clear();
        txtTenKH.clear();
        txtDiemTichLuy.clear();
        txtSoLuongKH.clear();
        resetThongTinBan();
    }

    private void hienThiThongBao(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}