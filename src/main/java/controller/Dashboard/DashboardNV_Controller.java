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

import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.NhanVien_DTO;
import network.common.Response;
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
import network.Client;
import network.common.CommandType;
import network.common.Request;

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

	private Client client;

	private NhanVien_DTO nhanVien;

	public DashboardNV_Controller() {
	}

	DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");

	@FXML
	public void initialize() {
		try {
			client = Client.tryCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtDateEnd.setValue(LocalDate.now());
		txtDateStart.setValue(LocalDate.now().minusDays(6));
		barChart_DoanhThuNam.setVisible(false);
		anchorPane_main.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.setOnKeyPressed(keyEvent -> {
					if (keyEvent.getCode() == KeyCode.F5) {
						displayData();
					}
				});
			}
		});
	}

	/**
	 * Call after FXML load once the logged-in staff is known.
	 */
	public void setNhanVien(NhanVien_DTO nhanVien) {
		if (nhanVien == null || nhanVien.getMaNV() == null || nhanVien.getMaNV().isBlank()) {
			System.err.println("DashboardNV: NhanVien chưa được set hoặc thiếu maNV — không gọi API.");
			return;
		}
		this.nhanVien = nhanVien;
		displayData();
	}

	private void displayData() {
		if (nhanVien == null || nhanVien.getMaNV() == null || nhanVien.getMaNV().isBlank()) {
			return;
		}
		try {
			showCustomDateRangeData();
		} catch (Exception e) {
			System.err.println("DashboardNV: lỗi tải dữ liệu — " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void showCustomDateRangeData() throws Exception {
		barChart_DoanhThuNam.setVisible(false);
		areaChart_DoanhThu.setVisible(true);
		LocalDate dateStart = txtDateStart.getValue();
		LocalDate dateEnd = txtDateEnd.getValue();

		List<HoaDon_DTO> dsHD = sendHoaDonList(
				new Request(CommandType.HOADON_GET_HOADON_NV_THEO_NGAY_CUTHE,
						Map.of("dateStart", dateStart, "dateEnd", dateEnd, "maNV", nhanVien.getMaNV())));

		List<DonDatBan_DTO> dsDon = sendDonDatBanList(
				new Request(CommandType.DONDATBAN_GET_ALL_DONDATBAN_THEO_NGAY_NV_CUTHE,
						Map.of("dateStart", dateStart, "dateEnd", dateEnd, "maNV", nhanVien.getMaNV())));

		updateChartDoanhThu(dsHD);
		updateChartDonDatBan(dsDon);
	}

	@SuppressWarnings("unchecked")
	private List<HoaDon_DTO> sendHoaDonList(Request req) {
		try {
			if (client == null) {
				return List.of();
			}
			Response r = client.send(req);
			if (r == null || !r.isSuccess()) {
				if (r != null && r.getMessage() != null) {
					System.err.println("DashboardNV: " + r.getMessage());
				}
				return List.of();
			}
			Object d = r.getData();
			return d instanceof List<?> ? (List<HoaDon_DTO>) d : List.of();
		} catch (Exception e) {
			System.err.println("DashboardNV: request HoaDon thất bại — " + e.getMessage());
			return List.of();
		}
	}

	@SuppressWarnings("unchecked")
	private List<DonDatBan_DTO> sendDonDatBanList(Request req) {
		try {
			if (client == null) {
				return List.of();
			}
			Response r = client.send(req);
			if (r == null || !r.isSuccess()) {
				if (r != null && r.getMessage() != null) {
					System.err.println("DashboardNV: " + r.getMessage());
				}
				return List.of();
			}
			Object d = r.getData();
			return d instanceof List<?> ? (List<DonDatBan_DTO>) d : List.of();
		} catch (Exception e) {
			System.err.println("DashboardNV: request DonDatBan thất bại — " + e.getMessage());
			return List.of();
		}
	}

	private void updateChartDoanhThu(List<HoaDon_DTO> dsHD) {
		areaChart_DoanhThu.getData().clear();
		axis.setAutoRanging(true);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
		LocalDate dateStart = txtDateStart.getValue();
		LocalDate dateEnd = txtDateEnd.getValue();
		Map<String, Double> doanhThuTheoNgay = new LinkedHashMap<>();
		while (!dateStart.isAfter(dateEnd)) {
			doanhThuTheoNgay.put(dateStart.format(formatter), 0.0);
			dateStart = dateStart.plusDays(1);
		}

		for (HoaDon_DTO hoaDon : dsHD) {
			Instant instant = hoaDon.getNgayLap().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
			ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
			LocalDate ngayLap = zonedDateTime.toLocalDate();

			String ngay = ngayLap.format(formatter);
			Double tongTien = hoaDon.getTongTien();
			if (doanhThuTheoNgay.containsKey(ngay)) {
				doanhThuTheoNgay.put(ngay, doanhThuTheoNgay.get(ngay) + tongTien);
			}
		}
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Doanh thu theo ngày");
		areaChart_DoanhThu.setTitle("DOANH THU TUẦN TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM"))
				+ " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

		for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
			series.getData().add(data);
			data.nodeProperty().addListener((observable, oldNode, newNode) -> {
				if (newNode != null) {
					Tooltip tooltip = new Tooltip("Doanh Thu: " + decimalFormat.format(entry.getValue()));
					tooltip.setShowDelay(Duration.millis(100));
					tooltip.setHideDelay(Duration.millis(200));
					Tooltip.install(newNode, tooltip);
				}
			});
		}
		areaChart_DoanhThu.getData().add(series);
	}

	private void updateChartDonDatBan(List<DonDatBan_DTO> dsDon) {
		barChart_DonDatBan.getData().clear();
		NumberAxis yAxis = (NumberAxis) barChart_DonDatBan.getYAxis();
		axis.setAutoRanging(true);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
		LocalDate dateStart = txtDateStart.getValue();
		LocalDate dateEnd = txtDateEnd.getValue();

		Map<String, Integer> donDatTheoNgay = new LinkedHashMap<>();
		while (!dateStart.isAfter(dateEnd)) {
			donDatTheoNgay.put(dateStart.format(formatter), 0);
			dateStart = dateStart.plusDays(1);
		}

		for (DonDatBan_DTO don : dsDon) {

			Instant instant = don.getNgayGioLapDon().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
			ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
			LocalDate ngayGioLapDon = zonedDateTime.toLocalDate();

			String ngay = ngayGioLapDon.format(formatter);
			if (donDatTheoNgay.containsKey(ngay)) {
				donDatTheoNgay.put(ngay, donDatTheoNgay.get(ngay) + 1);
			}
		}

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Số lượng đơn đặt bàn theo ngày");
		barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO TUẦN TỪ "
				+ txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN "
				+ txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

		for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
			series.getData().add(data);
			data.nodeProperty().addListener((observable, oldNode, newNode) -> {
				if (newNode != null) {
					Tooltip tooltip = new Tooltip("Số đơn đặt bàn: " + entry.getValue());
					tooltip.setShowDelay(Duration.millis(100));
					tooltip.setHideDelay(Duration.millis(200));
					Tooltip.install(newNode, tooltip);
				}
			});
		}

		barChart_DonDatBan.getData().add(series);

		int maxSoDDB = 0;
		int minSoDDB = Integer.MAX_VALUE;
		int minSoDDB2 = Integer.MAX_VALUE;
		List<String> daysWithMaxRevenue = new ArrayList<>();
		List<String> daysWithMinRevenue = new ArrayList<>();
		List<String> daysWithMinKhac0 = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
			String ngay = entry.getKey();
			double soDDB = entry.getValue();

			if (soDDB > maxSoDDB) {
				maxSoDDB = (int) soDDB;
				daysWithMaxRevenue.clear();
				daysWithMaxRevenue.add(ngay);
			} else if (soDDB == maxSoDDB) {
				daysWithMaxRevenue.add(ngay);
			}

			if (soDDB < minSoDDB) {
				minSoDDB = (int) soDDB;
				daysWithMinRevenue.clear();
				daysWithMinRevenue.add(ngay);
			} else if (soDDB == minSoDDB) {
				daysWithMinRevenue.add(ngay);
			}

			if (soDDB < minSoDDB2 && soDDB != 0) {
				minSoDDB2 = (int) soDDB;
				daysWithMinKhac0.clear();
				daysWithMinKhac0.add(ngay);
			} else if (soDDB == minSoDDB2) {
				daysWithMinKhac0.add(ngay);
			}
		}

		yAxis.setUpperBound(maxSoDDB + 1);
	}
}
