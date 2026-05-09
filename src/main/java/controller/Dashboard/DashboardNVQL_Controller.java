package controller.Dashboard;

import dao.impl.DonDatBan_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.LoaiBan_DTO;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import network.Client;
import network.common.CommandType;
import network.common.Request;

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
	private PieChart pieChart_LoaiBan;

	private DatePicker txtDateEnd = new DatePicker();

	private DatePicker txtDateStart = new DatePicker();

	@FXML
	private CategoryAxis axis;

	private Client client;

	public DashboardNVQL_Controller() {

	}

	DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");

	@FXML
	public void initialize() throws Exception {
		try {
			client = Client.tryCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		txtDateEnd.setValue(LocalDate.now());
		txtDateStart.setValue(LocalDate.now().minusDays(6));
		displayData();
		barChart_DoanhThuNam.setVisible(false);
		anchorPane_main.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.setOnKeyPressed(keyEvent -> {
					if (keyEvent.getCode() == KeyCode.F5) {
						try {
							displayData();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		});
	}

	private void displayData() throws Exception {
		showCustomDateRangeData();
	}

	private void showCustomDateRangeData() throws Exception {
		barChart_DoanhThuNam.setVisible(false);
		areaChart_DoanhThu.setVisible(true);
		LocalDate dateStart = txtDateStart.getValue();
		LocalDate dateEnd = txtDateEnd.getValue();

		// UpDate 4 Ô TOP
		List<HoaDon_DTO> dsHD =
				(List<HoaDon_DTO>) client.send(
						new Request(
								CommandType.HOADON_GET_HOADON_THEO_NGAY_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd
								)
						)
				).getData();

		List<DonDatBan_DTO> dsDon =
				(List<DonDatBan_DTO>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_ALL_DONDATBAN_THEO_NGAY_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd
								)
						)
				).getData();
		// END - UpDate 4 Ô TOP

		// UpDate Chart
		Map<LoaiBan_DTO, Integer> countLoaiGhe =
				(Map<LoaiBan_DTO, Integer>) client.send(
						new Request(
								CommandType.DONDATBAN_COUNT_LOAIBAN_THEO_NGAY_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd
								)
						)
				).getData();

		updateChartDoanhThu(dsHD);
		updateChartDonDatBan(dsDon);
		updateChartBan(countLoaiGhe);
	}

	private void updateChartDoanhThu(List<HoaDon_DTO> dsHD) {
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
		// Tạo Series cho biểu đồ
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Doanh thu theo ngày");
		areaChart_DoanhThu.setTitle(
				"DOANH THU THEO TUẦN TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM"))
						+ " ĐẾN " + txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

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

	// ---

	private void updateChartDonDatBan(List<DonDatBan_DTO> dsDon) {
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
		for (DonDatBan_DTO don : dsDon) {

			Instant instant = don.getNgayGioLapDon().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
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
		barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO TUẦN TỪ "
				+ txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM")) + " ĐẾN "
				+ txtDateEnd.getValue().format(DateTimeFormatter.ofPattern("dd/MM")));

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
		}

		yAxis.setUpperBound(maxSoDDB + 1);
	}

	private void updateChartBan(Map<LoaiBan_DTO, Integer> dsBan) {
		pieChart_LoaiBan.getData().clear();

		// Tính tổng giá trị của tất cả các phần trong biểu đồ
		double total = dsBan.values().stream().mapToInt(Integer::intValue).sum();

		// Lặp qua các mục trong danh sách và thêm vào biểu đồ
		for (Map.Entry<LoaiBan_DTO, Integer> entry : dsBan.entrySet()) {
			// Tạo phần của Pie chart
			PieChart.Data slice = new PieChart.Data(entry.getKey().getTenLoaiBan(), entry.getValue());

			// Tính phần trăm của phần này
			double percentage = (entry.getValue() / total) * 100;

			// Cập nhật tên của phần để hiển thị phần trăm
			slice.setName(String.format("%s (%.1f%%)", entry.getKey().getTenLoaiBan(), percentage));

			// Thêm phần vào biểu đồ
			pieChart_LoaiBan.getData().add(slice);

			// Thêm Tooltip cho phần pie chart
			Tooltip tooltip = new Tooltip(String.format("%s: %.1f%% \nSố lượng: %d", entry.getKey().getTenLoaiBan(),
					percentage, entry.getValue()));

			// Đặt thời gian trễ khi hiển thị Tooltip (0 ms để không có delay)
			tooltip.setShowDelay(Duration.millis(100));
			tooltip.setHideDelay(Duration.millis(200)); // Vẫn giữ thời gian ẩn Tooltip

			// Đảm bảo Tooltip được áp dụng đúng vào mỗi phần của Pie chart
			Tooltip.install(slice.getNode(), tooltip);
		}
	}
}
