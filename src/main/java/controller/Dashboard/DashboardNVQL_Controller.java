package controller.Dashboard;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import common.LoaiGhe;
import common.LoaiHanhKhach;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import entity.HoaDon;
import entity.Ve;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

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
    private Label lblHanhKhach;

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
    
    private final HoaDon_DAOImpl hoaDon_DAO;
    private final DonDatBan_DAOImpl donDatBan_DAO;
    
    public DashboardNVQL_Controller() {
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
                showYearlyData(nam);
            }
        }
        else if(radNgay.isSelected()){
            showCustomDateRangeData();
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
        
        List<Ve> dsVe = vedao.getAllVeTheoThang(selectedThang, selectedNam);
        List<String> dsHK = vedao.getHanhKhachTheoThang(selectedThang, selectedNam);
        Double tongDoanhThu = hddao.getTongDoanhThuTheoThang(selectedThang, selectedNam);
        String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
        numDoanhThu.setText(formattedDoanhThu);
        updateHoaDonInfo(dsHD);
        updateVeTauInfo(dsVe);
        updateHanhKhachInfo(dsHK);
//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
        Map<Double, String> doanhThuTauTheoThang = vedao.doanhThuTauTheoThang(selectedThang, selectedNam);
        Map<LoaiHanhKhach, Integer> countLoaiHK = vedao.countLoaiKhachHangTheoThang(selectedThang, selectedNam);
        Map<LoaiGhe, Integer> countLoaiGhe = vedao.countLoaiGheTheoThang(selectedThang, selectedNam);
        Map<String, Integer> countSoTuyenMoiTau = ltdao.countTuyenMoiTauTheoThang(selectedThang, selectedNam);
        Map<String, Integer> countSoTuyen = vedao.countSoTuyenTheoThang(selectedThang, selectedNam);
        Map<String, Integer> countSoGioDi = vedao.countThoiGianDiTheoThang(selectedThang, selectedNam);

        updateChartDoanhThu(dsHD);
        updateChartSoVe(dsVe);
        updateChartKhachHang(countLoaiHK);
        updateChartGhe(countLoaiGhe);
        updateChartDoanhThuTau(doanhThuTauTheoThang);
        updateChartSoChuyenTau(countSoTuyenMoiTau);
        updateChartSoTuyen(countSoTuyen);
        updateChartGioDi(countSoGioDi);
    }
}
