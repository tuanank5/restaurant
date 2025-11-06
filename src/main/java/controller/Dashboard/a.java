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

public class a {
	
	@FXML
    private AnchorPane anchorPane_main;

    @FXML
    private AreaChart<String, Number> areaChart_DoanhThu;

    @FXML
    private BarChart<String, Number> barChart_DoanhThuNam;

    @FXML
    private BarChart<String, Number> barChart_DonDatBan;

    @FXML
    private JFXButton btnLoc;

    @FXML
    private JFXComboBox<Integer> cmbNam;

    @FXML
    private JFXComboBox<Integer> cmbThang;

    @FXML
    private Label lblDoanhThu;

    @FXML
    private Label lblDonDatBan;

    @FXML
    private Label lblKhachHang;

    @FXML
    private Label lblHoaDon;

    @FXML
    private Label lblThongTinDoanhThu;

    @FXML
    private Label lblThongTinDonDatBan;

    @FXML
    private PieChart pieChart_LoaiBan;

    @FXML
    private JFXRadioButton radNgay;

    @FXML
    private JFXRadioButton radThangNam;

    @FXML
    private DatePicker txtDateEnd;

    @FXML
    private DatePicker txtDateStart;
    
    @FXML
    private CategoryAxis axis;
    
    private final HoaDon_DAOImpl hoaDon_DAO;
    private final DonDatBan_DAOImpl donDatBan_DAO;
    
    public a() {
    	this.hoaDon_DAO = new HoaDon_DAOImpl();
    	this.donDatBan_DAO = new DonDatBan_DAOImpl();
    }
    
    DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");
    
    @FXML
    public void initialize() {
    	btnGroup();
    	setCmb();
        displayData();
        barChart_DoanhThuNam.setVisible(false);
        anchorPane_main.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if(newScene != null){
                newScene.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.F5)
                        btnLoc.fire();
                });
            }
        });
        btnLoc.setOnAction(actionEvent -> {
            displayData();
            if(radNgay.isSelected()) {
            	checkDate();
            }

        });
    }
    
    private void btnGroup(){
        radThangNam.setSelected(true);
        radThangNam.setOnAction(actionEvent -> radNgay.setSelected(!radThangNam.isSelected()));
        radNgay.setOnAction(actionEvent -> radThangNam.setSelected(!radNgay.isSelected()));
    }
    
    private void setCmb() {
        for (int i = 0; i <= 12; i++) {
            cmbThang.getItems().add(i);
        }
        int currentYear = LocalDate.now().getYear();
        for (int i = 2021; i <= currentYear; i++) {
            cmbNam.getItems().add(i);
        }
        
        cmbThang.setValue(LocalDate.now().getMonthValue() - 1);
        cmbNam.setValue(currentYear);

        txtDateStart.setValue(LocalDate.now());
        txtDateEnd.setValue(LocalDate.now());
    }
    
    private void displayData() {
        if (radThangNam.isSelected()) {
            Integer nam = cmbNam.getValue();
            if(cmbThang.getValue() != 0){
                showMonthlyData(nam);
            }
            else {
//                showYearlyData(nam);
            }
        }
        else if(radNgay.isSelected()){
//            showCustomDateRangeData();
        }
    }
    
    private void checkDate(){
        if(txtDateStart.getValue().isAfter(LocalDate.now()) ||  txtDateEnd.getValue().isAfter(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR); // Loại thông báo là ERROR
            alert.setTitle("");
            alert.setHeaderText(null); // Không cần tiêu đề phụ
            alert.setContentText("Chưa có dữ liệu"); // Nội dung thông báo
            // Hiển thị dialog
            alert.showAndWait();
            txtDateEnd.setValue(LocalDate.now());
            txtDateStart.setValue(LocalDate.now());
        }
        if(txtDateEnd.getValue() == null) {
            txtDateEnd.setValue(txtDateStart.getValue());
        }
        if (txtDateEnd.getValue().isBefore(txtDateStart.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR); // Loại thông báo là ERROR
            alert.setTitle("Lỗi");
            alert.setHeaderText(null); // Không cần tiêu đề phụ
            alert.setContentText("Ngày kết thúc không thể trước ngày bắt đầu!"); // Nội dung thông báo
            // Hiển thị dialog
            alert.showAndWait();
            txtDateEnd.setValue(txtDateStart.getValue());
        }
    }
    
    private void showMonthlyData(Integer nam){
    	barChart_DoanhThuNam.setVisible(false);
    	areaChart_DoanhThu.setVisible(true);

        Integer thang = cmbThang.getValue();
//        ============ UpDate 4 Ô TOP ==============
        List<HoaDon> dsHD = hoaDon_DAO.getAllHoaDonTheoThang(thang, nam);
        
        List<DonDatBan> dsDon = donDatBan_DAO.getAllDonDatBanTheoThang(thang, nam);
        
        List<String> dsKH = donDatBan_DAO.getKhachHangTheoThang(thang, nam);
        Double tongDoanhThu = hoaDon_DAO.getTongDoanhThuTheoThang(thang, nam);
        
        String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
        lblDoanhThu.setText(formattedDoanhThu);
        updateHDInfo(dsHD);
        updateDDBInfo(dsDon);
        updateKHInfo(dsKH);
//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
//        Map<Double, String> doanhThuTauTheoThang = vedao.doanhThuTauTheoThang(selectedThang, selectedNam);
//        Map<LoaiHanhKhach, Integer> countLoaiHK = vedao.countLoaiKhachHangTheoThang(selectedThang, selectedNam);
//        Map<LoaiGhe, Integer> countLoaiGhe = vedao.countLoaiGheTheoThang(selectedThang, selectedNam);
//        Map<String, Integer> countSoTuyenMoiTau = ltdao.countTuyenMoiTauTheoThang(selectedThang, selectedNam);
//        Map<String, Integer> countSoTuyen = vedao.countSoTuyenTheoThang(selectedThang, selectedNam);
//        Map<String, Integer> countSoGioDi = vedao.countThoiGianDiTheoThang(selectedThang, selectedNam);

        updateChartDoanhThu(dsHD);
        updateChartDonDatBan(dsDon);
//        updateChartKhachHang(countLoaiHK);
//        updateChartGhe(countLoaiGhe);
//        updateChartDoanhThuTau(doanhThuTauTheoThang);
//        updateChartSoChuyenTau(countSoTuyenMoiTau);
//        updateChartSoTuyen(countSoTuyen);
//        updateChartGioDi(countSoGioDi);
    }
    
    private void showYearlyData(Integer nam) {
        barChart_DoanhThuNam.setVisible(true);
        areaChart_DoanhThu.setVisible(false);

        Map<String, Double> doanhThuTungThang = hoaDon_DAO.getDoanhThuTheoNam(nam);
//        ============ UpDate 4 Ô TOP ==============
        List<HoaDon> dsHD = hoaDon_DAO.getHoaDonTheoNam(nam);
        List<DonDatBan> dsVe = donDatBan_DAO.getAllDonDatBanTheoNam(nam);
        List<String> dsKH = donDatBan_DAO.getKhachHangTheoNam(nam);
        Double tongDoanhThu = hoaDon_DAO.getTongDoanhThuTheoNam(nam);
        String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
        lblDoanhThu.setText(formattedDoanhThu);
        updateHDInfo(dsHD);
        updateDDBInfo(dsVe);
        updateKHInfo(dsKH);
//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
//        Map<Double, String> doanhThuTauTheoNam= vedao.doanhThuTauTheoNam(selectedNam);
//        Map<LoaiHanhKhach, Integer> countLoaiHK = vedao.countLoaiKhachHangTheoNam(selectedNam);
//        Map<LoaiGhe, Integer> countLoaiGhe = vedao.countLoaiGheTheoNam(selectedNam);
//        Map<String, Integer> countSoVe = vedao.countSoVeTheoNam(selectedNam);
//        Map<String, Integer> countSoTuyenMoiTau = ltdao.countTuyenMoiTauTheoNam(selectedNam);
//        Map<String, Integer> countSoTuyen = vedao.countSoTuyenTheoNam(selectedNam);
//        Map<String, Integer> countSoGioDi = vedao.countThoiGianDiTheoNam(selectedNam);

//        updateChartDoanhThuTheoNam(doanhThuTungThang);
//        updateChartSoVeTheoNam(countSoVe);
//        updateChartKhachHang(countLoaiHK);
//        updateChartGhe(countLoaiGhe);
//        updateChartDoanhThuTau(doanhThuTauTheoNam);
//        updateChartSoChuyenTau(countSoTuyenMoiTau);
//        updateChartSoTuyen(countSoTuyen);
//        updateChartGioDi(countSoGioDi);
    }
    
    private void showCustomDateRangeData() {
    	barChart_DoanhThuNam.setVisible(false);
        areaChart_DoanhThu.setVisible(true);
        LocalDate dateStart = txtDateStart.getValue();
        LocalDate dateEnd = txtDateStart.getValue();
//        ============ UpDate 4 Ô TOP ==============
        List<HoaDon> dsHD = hoaDon_DAO.getHoaDonTheoNgayCuThe(dateStart, dateEnd);
        List<DonDatBan> dsDon = donDatBan_DAO.getAllDonDatBanTheoNgayCuThe(dateStart, dateEnd);
        List<String> dsKH = donDatBan_DAO.getKhachHangTheoNgayCuThe(dateStart, dateEnd);
        Double tongDoanhThu = hoaDon_DAO.getTongDoanhThuTheoNgayCuThe(dateStart, dateEnd);
        String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
        lblDoanhThu.setText(formattedDoanhThu);
        updateHDInfo(dsHD);
        updateDDBInfo(dsDon);
        updateKHInfo(dsKH);
//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
//        Map<LoaiGhe, Integer> countLoaiGhe = vedao.countLoaiGheTheoNgayCuThe(start, end);
//        Map<LoaiHanhKhach, Integer> countLoaiHK = vedao.countLoaiKhachHangTheoNgayCuThe(start, end);
//        Map<Double, String> doanhThuTau= vedao.doanhThuTauTheoNgayCuThe(start, end);
//        Map<String, Integer> countSoTuyenMoiTau = ltdao.countTuyenMoiTauTheoNgayCuThe(start, end);
//        Map<String, Integer> countSoTuyen = vedao.countSoTuyenTheoNgayCuThe(start, end);
//        Map<String, Integer> countSoGioDi = vedao.countThoiGianDiTheoNgayCuThe(start, end);

        updateChartDoanhThu(dsHD);
        updateChartDonDatBan(dsDon);
//        updateChartKhachHang(countLoaiHK);
//        updateChartGhe(countLoaiGhe);
//        updateChartDoanhThuTau(doanhThuTau);
//        updateChartSoChuyenTau(countSoTuyenMoiTau);
//        updateChartSoTuyen(countSoTuyen);
//        updateChartGioDi(countSoGioDi);
    }
    
    private void updateHDInfo(List<HoaDon> dsHD) {
        if (dsHD != null && !dsHD.isEmpty()) {
            lblHoaDon.setText(String.valueOf(dsHD.size()));
        } else {
        	lblHoaDon.setText("0");
        }
    }
    
    private void updateDDBInfo(List<DonDatBan> dsDon) {
        if (dsDon != null && !dsDon.isEmpty()) {
            lblDonDatBan.setText(String.valueOf(dsDon.size()));
        } else {
        	lblDonDatBan.setText("0");
        }
    }
    
    private void updateKHInfo(List<String> dsHK) {
        if (dsHK != null && !dsHK.isEmpty()) {
        	lblKhachHang.setText(String.valueOf(dsHK.size()));
        } else {
        	lblKhachHang.setText("0");
        }
    }
    
    private void updateChartDoanhThu(List<HoaDon> dsHD) {
        areaChart_DoanhThu.getData().clear();
        axis.setAutoRanging(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        if(radThangNam.isSelected()) {
            int nam= cmbNam.getValue();
            int thang = cmbThang.getValue();

            // Tạo danh sách đầy đủ các ngày trong tháng với doanh thu mặc định là 0
            Map<String, Double> doanhThuTheoNgay = new LinkedHashMap<>();
            YearMonth yearMonth = YearMonth.of(nam, thang);
            int daysInMonth = yearMonth.lengthOfMonth();
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = LocalDate.of(nam, thang, day);
                doanhThuTheoNgay.put(date.format(formatter), 0.0); // Set doanh thu là 0 cho mọi ngày trước tiên
            }

            // Cập nhật doanh thu cụ thể trong Map
            for (HoaDon hoaDon : dsHD) {
//                LocalDate ngayLap = hoaDon.getNgayLap().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            	
            	Instant instant = hoaDon.getNgayLap().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant();
            	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            	LocalDate ngayLap = zonedDateTime.toLocalDate();
            	
                String ngay = ngayLap.format(formatter);
                Double tongTien = hoaDon.getTongTien();
                doanhThuTheoNgay.put(ngay, doanhThuTheoNgay.getOrDefault(ngay, 0.0) + tongTien);
            }
            // Tạo Series cho biểu đồ
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu theo ngày");
            areaChart_DoanhThu.setTitle("DOANH THU THEO NGÀY TRONG THÁNG " + thang + " NĂM " + nam);
            
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
            double previousTotalDoanhThu = hoaDon_DAO.getTongDoanhThuTheoThang(thang - 1, nam);
            double revenueGrowth = 0; // Tăng trưởng doanh thu

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

            // Tính tăng trưởng doanh thu nếu có dữ liệu kỳ trước
            if (previousTotalDoanhThu > 0) {
                revenueGrowth = ((totalDoanhThu - previousTotalDoanhThu) / previousTotalDoanhThu) * 100;
            }

            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
            String daysWithMaxText = String.join(", ", daysWithMaxRevenue);
            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
            String daysWithMinText = String.join(", ", daysWithMinRevenue);
            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất khác 0
            String daysWithMinText2 = String.join(", ", daysWithMinKhac0);

            // Cập nhật thông tin cho label
            String infoText = String.format(
                    "Doanh thu cao nhất: %s (Ngày: %s)\nDoanh thu thấp nhất: %s (Ngày: %s)\nDoanh thu thấp nhất khác 0: %s (Ngày: %s)\nDoanh thu trung bình: %s\nSố ngày có doanh thu: %d/%d\nTăng trưởng doanh thu so với tháng trước: %.2f%%",
                    decimalFormat.format(maxDoanhThu), daysWithMaxText,
                    decimalFormat.format(minDoanhThu), daysWithMinText,
                    decimalFormat.format(minDoanhThu2), daysWithMinText2,
                    decimalFormat.format(averageDoanhThu),
                    daysWithRevenue,countDays,
                    revenueGrowth
            );
            lblThongTinDoanhThu.setText(infoText);
        }
        else if (radNgay.isSelected()){
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
//                LocalDate ngayLap = hoaDon.getNgayLap().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
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
    }
    
    //---
    
    private void updateChartDonDatBan(List<DonDatBan> dsDon) {
    	barChart_DonDatBan.getData().clear();
        NumberAxis yAxis = (NumberAxis) barChart_DonDatBan.getYAxis();
        axis.setAutoRanging(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        if(radThangNam.isSelected()){
            int nam = cmbNam.getValue();
            int thang = cmbThang.getValue();

            // Tạo danh sách đầy đủ các ngày trong tháng với số lượng vé mặc định là 0
            Map<String, Integer> donDatTheoNgay = new LinkedHashMap<>();
            YearMonth yearMonth = YearMonth.of(nam, thang);
            int daysInMonth = yearMonth.lengthOfMonth();
            for(int day = 1; day <= daysInMonth; day++){
                LocalDate date = LocalDate.of(nam, thang, day);
                donDatTheoNgay.put(date.format(formatter), 0); // Set số lượng vé là 0 cho mọi ngày trước tiên
            }

            // Cập nhật số lượng vé bán cụ thể trong Map
            for (DonDatBan don : dsDon){
//                LocalDate ngayGioLapDon = don.getNgayGioLapDon().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                Instant instant = don.getNgayGioLapDon().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant();
            	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            	LocalDate ngayGioLapDon = zonedDateTime.toLocalDate();
                
                String ngay = ngayGioLapDon.format(formatter);
                donDatTheoNgay.put(ngay, donDatTheoNgay.getOrDefault(ngay, 0) + 1); // Tăng số lượng vé bán
            }

            // Tạo Series cho biểu đồ
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng đơn đặt bàn theo ngày");
            barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO NGÀY TRONG THÁNG " + thang + " NĂM " + nam);

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
            int totalDoanhThu = 0;
            int countDays = donDatTheoNgay.size();
            List<String> daysWithMaxRevenue = new ArrayList<>();
            List<String> daysWithMinRevenue = new ArrayList<>();
            List<String> daysWithMinKhac0 = new ArrayList<>();

            int daysWithRevenue = 0;
            int previousTotalDoanhThu = donDatBan_DAO.getAllDonDatBanTheoThang(thang - 1, nam).size();
            double revenueGrowth = 0;

            // Duyệt qua từng ngày để tính các thông số
            for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
                String ngay = entry.getKey();
                double doanhThu = entry.getValue();

                // Cập nhật tổng doanh thu
                totalDoanhThu += (int) doanhThu;

                // Kiểm tra và cập nhật mức doanh thu cao nhất
                if (doanhThu > maxSoVe) {
                    maxSoVe = (int) doanhThu;
                    daysWithMaxRevenue.clear();
                    daysWithMaxRevenue.add(ngay);
                } else if (doanhThu == maxSoVe) {
                    daysWithMaxRevenue.add(ngay);
                }

                // Kiểm tra và cập nhật mức doanh thu thấp nhất
                if (doanhThu < minSoVe) {
                    minSoVe = (int) doanhThu;
                    daysWithMinRevenue.clear();
                    daysWithMinRevenue.add(ngay);
                } else if (doanhThu == minSoVe) {
                    daysWithMinRevenue.add(ngay);
                }

                // Kiểm tra và cập nhật mức doanh thu thấp nhất khác 0
                if (doanhThu < minSoVe2 && doanhThu != 0) {
                    minSoVe2 = (int) doanhThu;
                    daysWithMinKhac0.clear();
                    daysWithMinKhac0.add(ngay);
                } else if (doanhThu == minSoVe2) {
                    daysWithMinKhac0.add(ngay);
                }

                // Đếm số ngày có doanh thu khác 0
                if (doanhThu > 0) {
                    daysWithRevenue++;
                }
            }

            // Tính doanh thu trung bình
            double averageDoanhThu = (double) totalDoanhThu / countDays;

            // Tính tăng trưởng doanh thu nếu có dữ liệu kỳ trước
            if (previousTotalDoanhThu > 0) {
                revenueGrowth = ((double) (totalDoanhThu - previousTotalDoanhThu) / previousTotalDoanhThu) * 100;
            }

            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
            String daysWithMaxText = String.join(", ", daysWithMaxRevenue);
            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
            String daysWithMinText = String.join(", ", daysWithMinRevenue);
            // Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất khác 0
            String daysWithMinText2 = String.join(", ", daysWithMinKhac0);

            // Cập nhật thông tin cho label
            String infoText = String.format(
                    "Số đơn đặt bàn cao nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất: %d (Ngày: %s)\nSố vé thấp nhất khác 0: %d (Ngày: %s)\nSố đơn đặt bàn trung bình: %.1f\nSố ngày có bán được đơn đặt bàn: %d/%d\nTăng trưởng số vé so với tháng trước: %.2f%%",
                    maxSoVe, daysWithMaxText,
                    minSoVe, daysWithMinText,
                    minSoVe2, daysWithMinText2,
                    averageDoanhThu,
                    daysWithRevenue,countDays,
                    revenueGrowth
            );
            lblThongTinDonDatBan.setText(infoText);
            yAxis.setUpperBound(maxSoVe + 1);
        }
        else if (radNgay.isSelected()){
            LocalDate dateStart = txtDateStart.getValue();
            LocalDate dateEnd = txtDateEnd.getValue();

            // Tạo danh sách các ngày trong khoảng thời gian đã chọn
            Map<String, Integer> donDatTheoNgay = new LinkedHashMap<>();
            while (!dateStart.isAfter(dateEnd)) {
            	donDatTheoNgay.put(dateStart.format(formatter), 0); // Set số lượng vé là 0 cho mọi ngày trước tiên
                dateStart = dateStart.plusDays(1); // Chuyển đến ngày tiếp theo
            }

            // Cập nhật số lượng vé bán cụ thể trong Map
            for (DonDatBan don : dsDon) {
//                LocalDate ngayLap = don.getNgayGioLapDon().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                Instant instant = don.getNgayGioLapDon().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant();
            	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            	LocalDate ngayGioLapDon = zonedDateTime.toLocalDate();
                
                String ngay = ngayGioLapDon.format(formatter);
                if (donDatTheoNgay.containsKey(ngay)) {
                	donDatTheoNgay.put(ngay, donDatTheoNgay.get(ngay) + 1); // Tăng số lượng vé bán
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
                        Tooltip tooltip = new Tooltip("Số vé: " + entry.getValue());
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
                "Số vé cao nhất: %d (Ngày: %s)\nSố vé thấp nhất: %d (Ngày: %s)\nSố vé thấp nhất khác 0: %s (Ngày: %s)\nSố vé bán được trung bình: %.1f\nSố ngày bán được vé: %d/%d",
                maxSoVe, daysWithMaxText,
                minSoVe, daysWithMinText,
                minSoVe2, daysWithMinText2,
                averageDoanhThu,
                daysWithRevenue, countDays
        );
        lblThongTinDonDatBan.setText(infoText);
        yAxis.setUpperBound(maxSoVe + 1);
        }
    }
}
