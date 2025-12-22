package controller.Dashboard;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import controller.Menu.MenuNV_Controller;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import entity.DonDatBan;
import entity.HoaDon;
import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class DashboardNV_Controller {
	
	@FXML
    private AnchorPane anchorPane_main;

    @FXML
    private AreaChart<String, Number> areaChart_DoanhThu;

    @FXML
    private BarChart<String, Number> barChart_DoanhThuNam;

    @FXML
    private BarChart<String, Number> barChart_DonDatBan;

    private DatePicker txtDateEnd = new DatePicker();

    private DatePicker txtDateStart = new DatePicker();
    
    @FXML
    private CategoryAxis axis;
    
    private final HoaDon_DAOImpl hoaDon_DAO;
    private final DonDatBan_DAOImpl donDatBan_DAO;
    
    private NhanVien nhanVien = MenuNV_Controller.instance.taiKhoan.getNhanVien();
    
    public DashboardNV_Controller() {
    	this.hoaDon_DAO = new HoaDon_DAOImpl();
    	this.donDatBan_DAO = new DonDatBan_DAOImpl();
    }
    
    DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");
    
    @FXML
    public void initialize() {
    	txtDateEnd.setValue(LocalDate.now());
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
        
        //UpDate 4 Ô TOP
        List<HoaDon> dsHD = hoaDon_DAO.getHoaDonNVTheoNgayCuThe(dateStart, dateEnd, nhanVien.getMaNV());
        
        List<DonDatBan> dsDon = donDatBan_DAO.getAllDonDatBanTheoNgayNVCuThe(dateStart, dateEnd, nhanVien.getMaNV());
        //END - UpDate 4 Ô TOP
        
        //UpDate Chart
        updateChartDoanhThu(dsHD);
        updateChartDonDatBan(dsDon);
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
        areaChart_DoanhThu.setTitle("DOANH THU TUẦN TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

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
        barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO TUẦN TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

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
	    int maxSoDDB = 0;
	    int minSoDDB = Integer.MAX_VALUE;
	    int minSoDDB2 = Integer.MAX_VALUE;
	    List<String> daysWithMaxRevenue = new ArrayList<>();
	    List<String> daysWithMinRevenue = new ArrayList<>();
	    List<String> daysWithMinKhac0 = new ArrayList<>();
	
	    int daysWithRevenue = 0; // Biến đếm số ngày có doanh thu khác 0
	
	    // Duyệt qua từng ngày để tính các thông số
	    for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
	        String ngay = entry.getKey();
	        double soDDB = entry.getValue();
	
	        // Kiểm tra và cập nhật mức doanh thu cao nhất
	        if (soDDB > maxSoDDB) {
	            maxSoDDB = (int) soDDB;
	            daysWithMaxRevenue.clear(); // Xóa danh sách cũ nếu tìm thấy doanh thu cao hơn
	            daysWithMaxRevenue.add(ngay); // Thêm ngày hiện tại vào danh sách
	        } else if (soDDB == maxSoDDB) {
	            daysWithMaxRevenue.add(ngay); // Thêm ngày vào danh sách nếu doanh thu bằng mức cao nhất
	        }
	
	        // Kiểm tra và cập nhật mức doanh thu thấp nhất
	        if (soDDB < minSoDDB) {
	            minSoDDB = (int) soDDB;
	            daysWithMinRevenue.clear();
	            daysWithMinRevenue.add(ngay);
	        } else if (soDDB == minSoDDB) {
	            daysWithMinRevenue.add(ngay);
	        }
	
	        // Kiểm tra và cập nhật mức doanh thu thấp nhất khác 0
	        if (soDDB < minSoDDB2 && soDDB != 0) {
	            minSoDDB2 = (int) soDDB;
	            daysWithMinKhac0.clear();
	            daysWithMinKhac0.add(ngay);
	        } else if (soDDB == minSoDDB2) {
	            daysWithMinKhac0.add(ngay);
	        }
	
	        // Đếm số ngày có doanh thu khác 0
	        if (soDDB > 0) {
	            daysWithRevenue++;
	        }
	    }
	    
	    yAxis.setUpperBound(maxSoDDB + 1);
    }
}

