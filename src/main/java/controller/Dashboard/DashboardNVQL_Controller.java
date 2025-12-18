package controller.Dashboard;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import entity.DonDatBan;
import entity.HoaDon;
import entity.LoaiBan;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class DashboardNVQL_Controller {
	
	@FXML
    private AnchorPane anchorPane_main;

    @FXML
    private AreaChart<String, Number> areaChart_DoanhThu;

    @FXML
    private BarChart<String, Number> barChart_DoanhThuNam;

    @FXML
    private BarChart<String, Number> barChart_DonDatBan;

    @FXML
    private Label lblThongTinDoanhThu;

    @FXML
    private Label lblThongTinDonDatBan;

    @FXML
    private PieChart pieChart_LoaiBan;
    
    @FXML
    private CategoryAxis axis;
    
    private final HoaDon_DAOImpl hoaDon_DAO;
    private final DonDatBan_DAOImpl donDatBan_DAO;
    
    private DatePicker txtDateStart = new DatePicker();

    private DatePicker txtDateEnd = new DatePicker();
    
    public DashboardNVQL_Controller() {
    	this.hoaDon_DAO = new HoaDon_DAOImpl();
    	this.donDatBan_DAO = new DonDatBan_DAOImpl();
    }
    
    DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");
    
    @FXML
    public void initialize() {
    	txtDateEnd.setValue(LocalDate.now());

        // Đặt ngày bắt đầu là 6 ngày trước
        txtDateStart.setValue(LocalDate.now().minusDays(6));
        
        displayData();
        barChart_DoanhThuNam.setVisible(false);
        anchorPane_main.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if(newScene != null){
                newScene.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.F5)
                    	displayData();
                });
            }
        });
    }
    
    private void displayData() {
            showCustomDateRangeData();
    }
    
    private void showCustomDateRangeData() {
    	barChart_DoanhThuNam.setVisible(false);
        areaChart_DoanhThu.setVisible(true);
        LocalDate dateStart = txtDateStart.getValue();
        LocalDate dateEnd = txtDateEnd.getValue();
//        ============ UpDate 4 Ô TOP ==============
        List<HoaDon> dsHD = hoaDon_DAO.getHoaDonTheoNgayCuThe(dateStart, dateEnd);
        List<DonDatBan> dsDon = donDatBan_DAO.getAllDonDatBanTheoNgayCuThe(dateStart, dateEnd);
        List<String> dsKH = donDatBan_DAO.getKhachHangTheoNgayCuThe(dateStart, dateEnd);
        Double tongDoanhThu = hoaDon_DAO.getTongDoanhThuTheoNgayCuThe(dateStart, dateEnd);
        String formattedDoanhThu = decimalFormat.format(tongDoanhThu);

//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
        Map<LoaiBan, Integer> countLoaiGhe = donDatBan_DAO.countLoaiBanTheoNgayCuThe(dateStart, dateEnd);

        updateChartDoanhThu(dsHD);
        updateChartDonDatBan(dsDon);
        updateChartBan(countLoaiGhe);
    }
    
    private void updateChartDoanhThu(List<HoaDon> dsHD) {
        areaChart_DoanhThu.getData().clear();
        axis.setAutoRanging(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        LocalDate dateStart = txtDateStart.getValue();
        LocalDate dateEnd = txtDateEnd.getValue();
        // Tạo danh sách các ngày trong khoảng thời gian đã chọn
        Map<String, Double> doanhThuTheoNgay = new LinkedHashMap<>();
        while (!dateStart.isAfter(dateEnd)) {
            doanhThuTheoNgay.put(dateStart.format(formatter), 0.0); // Set doanh thu là 0 cho mọi ngày trước tiên
            dateStart = dateStart.plusDays(1); // Chuyển đến ngày tiếp theo
        }

        // Cập nhật doanh thu cụ thể trong Map
        for (HoaDon hoaDon : dsHD) {
//            LocalDate ngayLap = hoaDon.getNgayLap().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
        	Instant instant = hoaDon.getNgayLap().toLocalDate()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();
        	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        	LocalDate ngayLap = zonedDateTime.toLocalDate();
            
            String ngay = ngayLap.format(formatter);
            Double tongTien = hoaDon.getTongTien();
            if (doanhThuTheoNgay.containsKey(ngay)) {
                doanhThuTheoNgay.put(ngay, doanhThuTheoNgay.get(ngay) + tongTien);
            }
        }
        // Tạo Series cho biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo ngày");
        areaChart_DoanhThu.setTitle("DOANH THU THEO NGÀY TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

        for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
            // Lắng nghe sự kiện khi Node được tạo
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) { // Khi Node được tạo
                    Tooltip tooltip = new Tooltip("Doanh Thu: " + decimalFormat.format(entry.getValue()));
                    tooltip.setShowDelay(Duration.millis(100));
                    tooltip.setHideDelay(Duration.millis(200));
                    Tooltip.install(newNode, tooltip); // Gắn Tooltip vào Node
                }
            });
        }
        // Cập nhật biểu đồ
        areaChart_DoanhThu.getData().add(series);

        // ========== Cập nhật thông tin Doanh Thu =============
        double maxDoanhThu = 0;
        double minDoanhThu = Double.MAX_VALUE;
        double minDoanhThu2 = Double.MAX_VALUE;
        double totalDoanhThu = 0;
        int countDays = doanhThuTheoNgay.size();
        List<String> daysWithMaxRevenue = new ArrayList<>();
        List<String> daysWithMinRevenue = new ArrayList<>();
        List<String> daysWithMinKhac0 = new ArrayList<>();

        int daysWithRevenue = 0; // Biến đếm số ngày có doanh thu khác 0

        // Duyệt qua từng ngày để tính các thông số
        for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
            String ngay = entry.getKey();
            double doanhThu = entry.getValue();

            // Cập nhật tổng doanh thu
            totalDoanhThu += doanhThu;

            // Kiểm tra và cập nhật mức doanh thu cao nhất
            if (doanhThu > maxDoanhThu) {
                maxDoanhThu = doanhThu;
                daysWithMaxRevenue.clear(); // Xóa danh sách cũ nếu tìm thấy doanh thu cao hơn
                daysWithMaxRevenue.add(ngay); // Thêm ngày hiện tại vào danh sách
            } else if (doanhThu == maxDoanhThu) {
                daysWithMaxRevenue.add(ngay); // Thêm ngày vào danh sách nếu doanh thu bằng mức cao nhất
            }

            // Kiểm tra và cập nhật mức doanh thu thấp nhất
            if (doanhThu < minDoanhThu) {
                minDoanhThu = doanhThu;
                daysWithMinRevenue.clear();
                daysWithMinRevenue.add(ngay);
            } else if (doanhThu == minDoanhThu) {
                daysWithMinRevenue.add(ngay);
            }

            // Kiểm tra và cập nhật mức doanh thu thấp nhất khác 0
            if (doanhThu < minDoanhThu2 && doanhThu != 0) {
                minDoanhThu2 = doanhThu;
                daysWithMinKhac0.clear();
                daysWithMinKhac0.add(ngay);
            } else if (doanhThu == minDoanhThu2) {
                daysWithMinKhac0.add(ngay);
            }

            // Đếm số ngày có doanh thu khác 0
            if (doanhThu > 0) {
                daysWithRevenue++;
            }
        }

        // Tính doanh thu trung bình
        double averageDoanhThu = totalDoanhThu / countDays;

        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
        String daysWithMaxText = String.join(", ", daysWithMaxRevenue);
        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
        String daysWithMinText = String.join(", ", daysWithMinRevenue);
        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất khác 0
        String daysWithMinText2 = String.join(", ", daysWithMinKhac0);

        // Cập nhật thông tin cho label
        String infoText = String.format(
                "Doanh thu cao nhất: %s (Ngày: %s)\nDoanh thu thấp nhất: %s (Ngày: %s)\nDoanh thu thấp nhất khác 0: %s (Ngày: %s)\nDoanh thu trung bình: %s\nSố ngày có doanh thu: %d/%d",
                decimalFormat.format(maxDoanhThu), daysWithMaxText,
                decimalFormat.format(minDoanhThu), daysWithMinText,
                decimalFormat.format(minDoanhThu2), daysWithMinText2,
                decimalFormat.format(averageDoanhThu),
                daysWithRevenue, countDays
        );
        lblThongTinDoanhThu.setText(infoText);
    }
    
    //---
    
    private void updateChartDonDatBan(List<DonDatBan> dsDon) {
    	barChart_DonDatBan.getData().clear();
        NumberAxis yAxis = (NumberAxis) barChart_DonDatBan.getYAxis();
        axis.setAutoRanging(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate dateStart = txtDateStart.getValue();
        LocalDate dateEnd = txtDateEnd.getValue();

        // Tạo danh sách các ngày trong khoảng thời gian đã chọn
        Map<String, Integer> donDatTheoNgay = new LinkedHashMap<>();
        while (!dateStart.isAfter(dateEnd)) {
        	donDatTheoNgay.put(dateStart.format(formatter), 0); // Set số lượng đơn là 0 cho mọi ngày trước tiên
            dateStart = dateStart.plusDays(1); // Chuyển đến ngày tiếp theo
        }

        // Cập nhật số lượng đơn bán cụ thể trong Map
        for (DonDatBan don : dsDon) {
//            LocalDate ngayLap = don.getNgayGioLapDon().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            Instant instant = don.getNgayGioLapDon().toLocalDate()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();
        	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        	LocalDate ngayGioLapDon = zonedDateTime.toLocalDate();
            
            String ngay = ngayGioLapDon.format(formatter);
            if (donDatTheoNgay.containsKey(ngay)) {
            	donDatTheoNgay.put(ngay, donDatTheoNgay.get(ngay) + 1); // Tăng số lượng đơn bán
            }
        }

        // Tạo Series cho biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng đơn đặt bàn theo ngày");
        barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO NGÀY TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

        for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
            // Lắng nghe sự kiện khi Node được tạo
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) { // Khi Node được tạo
                    Tooltip tooltip = new Tooltip("Số đơn đặt bàn: " + entry.getValue());
                    tooltip.setShowDelay(Duration.millis(100));
                    tooltip.setHideDelay(Duration.millis(200));
                    Tooltip.install(newNode, tooltip); // Gắn Tooltip vào Node
                }
            });
        }

        // Cập nhật biểu đồ
        barChart_DonDatBan.getData().add(series);

        // ========== Cập nhật thông tin Doanh Thu =============
        int maxSoVe = 0;
        int minSoVe = Integer.MAX_VALUE;
        int minSoVe2 = Integer.MAX_VALUE;
        int totalSoVe = dsDon.size();
        int countDays = donDatTheoNgay.size();
        List<String> daysWithMaxRevenue = new ArrayList<>();
        List<String> daysWithMinRevenue = new ArrayList<>();
        List<String> daysWithMinKhac0 = new ArrayList<>();

        int daysWithRevenue = 0; // Biến đếm số ngày có doanh thu khác 0

        // Duyệt qua từng ngày để tính các thông số
        for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
            String ngay = entry.getKey();
            double soVe = entry.getValue();

            // Kiểm tra và cập nhật mức doanh thu cao nhất
            if (soVe > maxSoVe) {
                maxSoVe = (int) soVe;
                daysWithMaxRevenue.clear(); // Xóa danh sách cũ nếu tìm thấy doanh thu cao hơn
                daysWithMaxRevenue.add(ngay); // Thêm ngày hiện tại vào danh sách
            } else if (soVe == maxSoVe) {
                daysWithMaxRevenue.add(ngay); // Thêm ngày vào danh sách nếu doanh thu bằng mức cao nhất
            }

            // Kiểm tra và cập nhật mức doanh thu thấp nhất
            if (soVe < minSoVe) {
                minSoVe = (int) soVe;
                daysWithMinRevenue.clear();
                daysWithMinRevenue.add(ngay);
            } else if (soVe == minSoVe) {
                daysWithMinRevenue.add(ngay);
            }

            // Kiểm tra và cập nhật mức doanh thu thấp nhất khác 0
            if (soVe < minSoVe2 && soVe != 0) {
                minSoVe2 = (int) soVe;
                daysWithMinKhac0.clear();
                daysWithMinKhac0.add(ngay);
            } else if (soVe == minSoVe2) {
                daysWithMinKhac0.add(ngay);
            }

            // Đếm số ngày có doanh thu khác 0
            if (soVe > 0) {
                daysWithRevenue++;
            }
        }

        // Tính doanh thu trung bình
        double averageDoanhThu = (double) totalSoVe / countDays;

        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
        String daysWithMaxText = String.join(", ", daysWithMaxRevenue);
        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
        String daysWithMinText = String.join(", ", daysWithMinRevenue);
        // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất khác 0
        String daysWithMinText2 = String.join(", ", daysWithMinKhac0);

        // Cập nhật thông tin cho label
        String infoText = String.format(
                "Số đơn đặt bàn cao nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất khác 0: %s (Ngày: %s)\nSố đơn đặt bàn bán được trung bình: %.1f\nSố ngày có đơn đặt bàn: %d/%d",
                maxSoVe, daysWithMaxText,
                minSoVe, daysWithMinText,
                minSoVe2, daysWithMinText2,
                averageDoanhThu,
                daysWithRevenue, countDays
        );
        lblThongTinDonDatBan.setText(infoText);
        yAxis.setUpperBound(maxSoVe + 1);
    }
    
    private void updateChartBan(Map<LoaiBan, Integer> dsBan) {
        pieChart_LoaiBan.getData().clear();

        // Tính tổng giá trị của tất cả các phần trong biểu đồ
        double total = dsBan.values().stream().mapToInt(Integer::intValue).sum();

        // Lặp qua các mục trong danh sách và thêm vào biểu đồ
        for (Map.Entry<LoaiBan, Integer> entry : dsBan.entrySet()) {
            // Tạo phần của Pie chart
            PieChart.Data slice = new PieChart.Data(entry.getKey().getTenLoaiBan(), entry.getValue());

            // Tính phần trăm của phần này
            double percentage = (entry.getValue() / total) * 100;

            // Cập nhật tên của phần để hiển thị phần trăm
            slice.setName(String.format("%s (%.1f%%)", entry.getKey().getTenLoaiBan(), percentage));

            // Thêm phần vào biểu đồ
            pieChart_LoaiBan.getData().add(slice);

            // Thêm Tooltip cho phần pie chart
            Tooltip tooltip = new Tooltip(String.format("%s: %.1f%% \nSố lượng: %d", entry.getKey().getTenLoaiBan(), percentage, entry.getValue()));

            // Đặt thời gian trễ khi hiển thị Tooltip (0 ms để không có delay)
            tooltip.setShowDelay(Duration.millis(100));
            tooltip.setHideDelay(Duration.millis(200));  // Vẫn giữ thời gian ẩn Tooltip

            // Đảm bảo Tooltip được áp dụng đúng vào mỗi phần của Pie chart
            Tooltip.install(slice.getNode(), tooltip);
        }
    }
}
