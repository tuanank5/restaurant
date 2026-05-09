package controller.ThongKe;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import network.Client;
import network.common.CommandType;
import network.common.Request;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThongKeNV_Controller {

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
	private JFXRadioButton radNgay;

	@FXML
	private JFXRadioButton radThangNam;

	@FXML
	private DatePicker txtDateEnd;

	@FXML
	private DatePicker txtDateStart;

	@FXML
	private CategoryAxis axis;

	private Client client;

	//	private NhanVien nhanVien = MenuNV_Controller.taiKhoan.getNhanVien();
	private final NhanVien nhanVien = NhanVien.builder()
			.maNV("NV001")
			.tenNV("Nguyen Van A")
			.chucVu("NhanVien")
			.email("nva@gmail.com")
			.namSinh(Date.valueOf("2000-01-01"))
			.diaChi("Go Vap")
			.gioiTinh(true)
			.ngayVaoLam(Date.valueOf("2024-01-01"))
			.trangThai(true)
			.build();

	public ThongKeNV_Controller() {

	}

	DecimalFormat decimalFormat = new DecimalFormat("#,##0 VNĐ");

	@FXML
	public void initialize() throws Exception {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
		btnGroup();
		setCmb();
		displayData();
		barChart_DoanhThuNam.setVisible(false);
		anchorPane_main.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.setOnKeyPressed(keyEvent -> {
					if (keyEvent.getCode() == KeyCode.F5)
						btnLoc.fire();
				});
			}
		});
		btnLoc.setOnAction(actionEvent -> {
			try {
				displayData();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (radNgay.isSelected()) {
				checkDate();
			}

		});
	}

	private void btnGroup() {
		radThangNam.setSelected(true);
		radThangNam.setOnAction(actionEvent -> radNgay.setSelected(!radThangNam.isSelected()));
		radNgay.setOnAction(actionEvent -> radThangNam.setSelected(!radNgay.isSelected()));
	}

	private void setCmb() {
		for (int i = 0; i <= 12; i++) {
			cmbThang.getItems().add(i);
		}
		int currentYear = LocalDate.now().getYear();
		for (int i = 2025; i <= currentYear; i++) {
			cmbNam.getItems().add(i);
		}

		cmbThang.setValue(LocalDate.now().getMonthValue());
		cmbNam.setValue(currentYear);

		txtDateStart.setValue(LocalDate.now());
		txtDateEnd.setValue(LocalDate.now());
	}

	private void displayData() throws Exception {
		if (radThangNam.isSelected()) {
			Integer nam = cmbNam.getValue();
			if (cmbThang.getValue() != 0) {
				showMonthlyData(nam);
			} else {
				showYearlyData(nam);
			}
		} else if (radNgay.isSelected()) {
			showCustomDateRangeData();
		}
	}

	private void checkDate() {
		if (txtDateStart.getValue().isAfter(LocalDate.now()) || txtDateEnd.getValue().isAfter(LocalDate.now())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setHeaderText(null);
			alert.setContentText("Chưa có dữ liệu!");
			alert.showAndWait();
			txtDateEnd.setValue(LocalDate.now());
			txtDateStart.setValue(LocalDate.now());
		}
		if (txtDateEnd.getValue() == null) {
			txtDateEnd.setValue(txtDateStart.getValue());
		}
		if (txtDateEnd.getValue().isBefore(txtDateStart.getValue())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setHeaderText(null);
			alert.setContentText("Ngày kết thúc không được trước ngày bắt đầu!");
			alert.showAndWait();
			txtDateEnd.setValue(txtDateStart.getValue());
		}
	}

	private void showMonthlyData(Integer nam) throws Exception {
		barChart_DoanhThuNam.setVisible(false);
		areaChart_DoanhThu.setVisible(true);

		Integer thang = cmbThang.getValue();
//        ============ UpDate 4 Ô TOP ==============
		List<HoaDon_DTO> dsHD = (List<HoaDon_DTO>) client.send(
				new Request(
						CommandType.HOADON_GET_ALL_HOADON_NV_THEO_THANG,
						Map.of(
								"thang", thang,
								"nam", nam,
								"maNV", nhanVien.getMaNV()
						)
				)
		).getData();

		List<DonDatBan_DTO> dsDon =
				(List<DonDatBan_DTO>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_ALL_DONDATBAN_NV_THEO_THANG,
								Map.of(
										"thang", thang,
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		List<String> dsKH =
				(List<String>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_KHACHHANG_THEO_THANG_NV_CUTHE,
								Map.of(
										"thang", thang,
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		Double tongDoanhThu =
				(Double) client.send(
						new Request(
								CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_THANG,
								Map.of(
										"thang", thang,
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
		lblDoanhThu.setText(formattedDoanhThu);
		updateHDInfo(dsHD);
		updateDDBInfo(dsDon);
		updateKHInfo(dsKH);
//        ============ END - UpDate 4 Ô TOP ==============
//        ============ UpDate Chart ==============
		updateChartDoanhThu(dsHD);
		updateChartDonDatBan(dsDon);
	}

	private void showYearlyData(Integer nam) throws Exception {
		barChart_DoanhThuNam.setVisible(true);
		areaChart_DoanhThu.setVisible(false);

		Map<String, Double> doanhThuTungThang =
				(Map<String, Double>) client.send(
						new Request(
								CommandType.HOADON_GET_DOANHTHU_NV_THEO_NAM,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();
		// UpDate 4 Ô TOP
		List<HoaDon_DTO> dsHD =
				(List<HoaDon_DTO>) client.send(
						new Request(
								CommandType.HOADON_GET_HOADON_NV_THEO_NAM,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		List<DonDatBan_DTO> dsDDB =
				(List<DonDatBan_DTO>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_ALL_DONDATBAN_THEO_NAM_NV_CUTHE,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		List<String> dsKH =
				(List<String>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_KHACHHANG_THEO_NAM_NV_CUTHE,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		Double tongDoanhThu =
				(Double) client.send(
						new Request(
								CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_NAM,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
		lblDoanhThu.setText(formattedDoanhThu);
		updateHDInfo(dsHD);
		updateDDBInfo(dsDDB);
		updateKHInfo(dsKH);
		// END - UpDate 4 Ô TOP

		// UpDate Chart
		Map<String, Integer> countDonDatBan =
				(Map<String, Integer>) client.send(
						new Request(
								CommandType.DONDATBAN_COUNT_DONDATBAN_THEO_NAM_NV_CUTHE,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		updateChartDoanhThuTheoNam(doanhThuTungThang);
		updateChartDonDatBanTheoNam(countDonDatBan);
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
								CommandType.HOADON_GET_HOADON_NV_THEO_NGAY_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		List<DonDatBan_DTO> dsDon =
				(List<DonDatBan_DTO>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_ALL_DONDATBAN_THEO_NGAY_NV_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		List<String> dsKH =
				(List<String>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_KHACHHANG_THEO_NGAY_NV_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		Double tongDoanhThu =
				(Double) client.send(
						new Request(
								CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_NGAY_CUTHE,
								Map.of(
										"dateStart", dateStart,
										"dateEnd", dateEnd,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();

		String formattedDoanhThu = decimalFormat.format(tongDoanhThu);
		lblDoanhThu.setText(formattedDoanhThu);
		updateHDInfo(dsHD);
		updateDDBInfo(dsDon);
		updateKHInfo(dsKH);
		// END - UpDate 4 Ô TOP

		// UpDate Chart
		updateChartDoanhThu(dsHD);
		updateChartDonDatBan(dsDon);
	}

	private void updateHDInfo(List<HoaDon_DTO> dsHD) {
		if (dsHD != null && !dsHD.isEmpty()) {
			lblHoaDon.setText(String.valueOf(dsHD.size()));
		} else {
			lblHoaDon.setText("0");
		}
	}

	private void updateDDBInfo(List<DonDatBan_DTO> dsDon) {
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

	private void updateChartDoanhThu(List<HoaDon_DTO> dsHD) throws Exception {
		areaChart_DoanhThu.getData().clear();
		axis.setAutoRanging(true);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
		if (radThangNam.isSelected()) {
			int nam = cmbNam.getValue();
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
			for (HoaDon_DTO hoaDon : dsHD) {
				Instant instant = hoaDon.getNgayLap().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
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
			double minDoanhThu = 0;
			double minDoanhThuKhac0 = 0;
			double totalDoanhThu = 0;
			int countDays = doanhThuTheoNgay.size();
			List<String> daysWithMaxRevenue = new ArrayList<>();
			List<String> daysWithMinRevenue = new ArrayList<>();
			List<String> daysWithMinKhac0 = new ArrayList<>();

			int daysWithRevenue = 0; // Biến đếm số ngày có doanh thu khác 0
			Double previousTotalDoanhThu =
					(Double) client.send(
							new Request(
									CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_THANG,
									Map.of(
											"thang", thang,
											"nam", nam,
											"maNV", nhanVien.getMaNV()
									)
							)
					).getData();
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
				if (doanhThu < minDoanhThuKhac0 && doanhThu != 0) {
					minDoanhThuKhac0 = doanhThu;
					daysWithMinKhac0.clear();
					daysWithMinKhac0.add(ngay);
				} else if (doanhThu == minDoanhThuKhac0) {
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
					decimalFormat.format(maxDoanhThu), daysWithMaxText, decimalFormat.format(minDoanhThu),
					daysWithMinText, decimalFormat.format(minDoanhThuKhac0), daysWithMinText2,
					decimalFormat.format(averageDoanhThu), daysWithRevenue, countDays, revenueGrowth);
			lblThongTinDoanhThu.setText(infoText);
		}

		else if (radNgay.isSelected()) {
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
//                LocalDate ngayLap = hoaDon.getNgayLap().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

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
					"DOANH THU THEO NGÀY TỪ " + txtDateStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM"))
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
					decimalFormat.format(maxDoanhThu), daysWithMaxText, decimalFormat.format(minDoanhThu),
					daysWithMinText, decimalFormat.format(minDoanhThu2), daysWithMinText2,
					decimalFormat.format(averageDoanhThu), daysWithRevenue, countDays);
			lblThongTinDoanhThu.setText(infoText);

		}
	}

	// ---

	private void updateChartDonDatBan(List<DonDatBan_DTO> dsDon) throws Exception {
		barChart_DonDatBan.getData().clear();
		NumberAxis yAxis = (NumberAxis) barChart_DonDatBan.getYAxis();
		axis.setAutoRanging(true);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

		if (radThangNam.isSelected()) {
			int nam = cmbNam.getValue();
			int thang = cmbThang.getValue();

			// Tạo danh sách đầy đủ các ngày trong tháng với số lượng đơn mặc định là 0
			Map<String, Integer> donDatTheoNgay = new LinkedHashMap<>();
			YearMonth yearMonth = YearMonth.of(nam, thang);
			int daysInMonth = yearMonth.lengthOfMonth();
			for (int day = 1; day <= daysInMonth; day++) {
				LocalDate date = LocalDate.of(nam, thang, day);
				donDatTheoNgay.put(date.format(formatter), 0); // Set số lượng đơn là 0 cho mọi ngày trước tiên
			}

			// Cập nhật số lượng đơn bán cụ thể trong Map
			for (DonDatBan_DTO don : dsDon) {
//                LocalDate ngayGioLapDon = don.getNgayGioLapDon().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				Instant instant = don.getNgayGioLapDon().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
				ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
				LocalDate ngayGioLapDon = zonedDateTime.toLocalDate();

				String ngay = ngayGioLapDon.format(formatter);
				donDatTheoNgay.put(ngay, donDatTheoNgay.getOrDefault(ngay, 0) + 1); // Tăng số lượng đơn đặt
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
			int maxSoDDB = 0;
			int minSoDDB = Integer.MAX_VALUE;
			int minSoDDB2 = Integer.MAX_VALUE;
			int totalDoanhThu = 0;
			int countDays = donDatTheoNgay.size();
			List<String> daysWithMaxRevenue = new ArrayList<>();
			List<String> daysWithMinRevenue = new ArrayList<>();
			List<String> daysWithMinKhac0 = new ArrayList<>();

			int daysWithRevenue = 0;
			int previousTotalDoanhThu =
					((List<DonDatBan_DTO>) client.send(
							new Request(
									CommandType.DONDATBAN_GET_ALL_DONDATBAN_NV_THEO_THANG,
									Map.of(
											"thang", thang,
											"nam", nam,
											"maNV", nhanVien.getMaNV()
									)
							)
					).getData()).size();
			double revenueGrowth = 0;

			// Duyệt qua từng ngày để tính các thông số
			for (Map.Entry<String, Integer> entry : donDatTheoNgay.entrySet()) {
				String ngay = entry.getKey();
				double doanhThu = entry.getValue();

				// Cập nhật tổng doanh thu
				totalDoanhThu += (int) doanhThu;

				// Kiểm tra và cập nhật mức doanh thu cao nhất
				if (doanhThu > maxSoDDB) {
					maxSoDDB = (int) doanhThu;
					daysWithMaxRevenue.clear();
					daysWithMaxRevenue.add(ngay);
				} else if (doanhThu == maxSoDDB) {
					daysWithMaxRevenue.add(ngay);
				}

				// Kiểm tra và cập nhật mức doanh thu thấp nhất
				if (doanhThu < minSoDDB) {
					minSoDDB = (int) doanhThu;
					daysWithMinRevenue.clear();
					daysWithMinRevenue.add(ngay);
				} else if (doanhThu == minSoDDB) {
					daysWithMinRevenue.add(ngay);
				}

				// Kiểm tra và cập nhật mức doanh thu thấp nhất khác 0
				if (doanhThu < minSoDDB2 && doanhThu != 0) {
					minSoDDB2 = (int) doanhThu;
					daysWithMinKhac0.clear();
					daysWithMinKhac0.add(ngay);
				} else if (doanhThu == minSoDDB2) {
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
					"Số đơn đặt bàn cao nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất khác 0: %d (Ngày: %s)\nSố đơn đặt bàn trung bình: %.1f\nSố ngày có đơn đặt bàn: %d/%d\nTăng trưởng số đơn đặt bàn so với tháng trước: %.2f%%",
					maxSoDDB, daysWithMaxText, minSoDDB, daysWithMinText, minSoDDB2, daysWithMinText2, averageDoanhThu,
					daysWithRevenue, countDays, revenueGrowth);
			lblThongTinDonDatBan.setText(infoText);
			yAxis.setUpperBound(maxSoDDB + 1);
		} else if (radNgay.isSelected()) {
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
//                LocalDate ngayLap = don.getNgayGioLapDon().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

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
			barChart_DonDatBan.setTitle("SỐ LƯỢNG ĐƠN ĐẶT BÀN THEO NGÀY TỪ "
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
			int totalSoDDB = dsDon.size();
			int countDays = donDatTheoNgay.size();
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

			// Tính doanh thu trung bình
			double averageDoanhThu = (double) totalSoDDB / countDays;

			// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
			String daysWithMaxText = String.join(", ", daysWithMaxRevenue);
			// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
			String daysWithMinText = String.join(", ", daysWithMinRevenue);
			// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất khác 0
			String daysWithMinText2 = String.join(", ", daysWithMinKhac0);

			// Cập nhật thông tin cho label
			String infoText = String.format(
					"Số đơn đặt bàn cao nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất: %d (Ngày: %s)\nSố đơn đặt bàn thấp nhất khác 0: %s (Ngày: %s)\nSố đơn đặt bàn bán được trung bình: %.1f\nSố ngày có đơn đặt bàn: %d/%d",
					maxSoDDB, daysWithMaxText, minSoDDB, daysWithMinText, minSoDDB2, daysWithMinText2, averageDoanhThu,
					daysWithRevenue, countDays);
			lblThongTinDonDatBan.setText(infoText);
			yAxis.setUpperBound(maxSoDDB + 1);
		}
	}

	private void updateChartDoanhThuTheoNam(Map<String, Double> dsMap) throws Exception {
		barChart_DoanhThuNam.getData().clear();
		axis.setAutoRanging(true);
		int nam = cmbNam.getValue();

		// Chuyển đổi Map thành List và sắp xếp theo thứ tự tháng (1 đến 12)
		List<Map.Entry<String, Double>> entryList = new ArrayList<>(dsMap.entrySet());

		entryList.sort((entry1, entry2) -> {
			// Loại bỏ tiền tố "Tháng " và chuyển đổi tháng thành số nguyên để so sánh
			int month1 = Integer.parseInt(entry1.getKey().replace("Tháng ", "").trim());
			int month2 = Integer.parseInt(entry2.getKey().replace("Tháng ", "").trim());
			return Integer.compare(month1, month2);
		});
		// Tạo Series cho biểu đồ
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Doanh thu theo tháng");
		barChart_DoanhThuNam.setTitle("DOANH THU CÁC THÁNG TRONG NĂM " + nam);
		for (Map.Entry<String, Double> entry : entryList) {
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
		barChart_DoanhThuNam.getData().add(series);

		// ========== Cập nhật thông tin Doanh Thu =============
		double maxDoanhThu = 0;
		double minDoanhThu = Double.MAX_VALUE;
		Double totalDoanhThu =
				(Double) client.send(
						new Request(
								CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_NAM,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();
		List<String> monthWithMaxRevenue = new ArrayList<>();
		List<String> monthWithMinRevenue = new ArrayList<>();

		Double previousTotalDoanhThu =
				(Double) client.send(
						new Request(
								CommandType.HOADON_GET_TONG_DOANHTHU_NV_THEO_NAM,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData();
		double revenueGrowth = 0; // Tăng trưởng doanh thu

		// Duyệt qua từng ngày để tính các thông số
		for (Map.Entry<String, Double> entry : dsMap.entrySet()) {
			String thang = entry.getKey();
			double doanhThu = entry.getValue();

			// Kiểm tra và cập nhật mức doanh thu cao nhất
			if (doanhThu > maxDoanhThu) {
				maxDoanhThu = doanhThu;
				monthWithMaxRevenue.clear(); // Xóa danh sách cũ nếu tìm thấy doanh thu cao hơn
				monthWithMaxRevenue.add(thang); // Thêm ngày hiện tại vào danh sách
			} else if (doanhThu == maxDoanhThu) {
				monthWithMaxRevenue.add(thang); // Thêm ngày vào danh sách nếu doanh thu bằng mức cao nhất
			}

			// Kiểm tra và cập nhật mức doanh thu thấp nhất
			if (doanhThu < minDoanhThu) {
				minDoanhThu = doanhThu;
				monthWithMinRevenue.clear();
				monthWithMinRevenue.add(thang);
			} else if (doanhThu == minDoanhThu) {
				monthWithMinRevenue.add(thang);
			}
		}

		// Tính doanh thu trung bình
		double averageDoanhThu = totalDoanhThu / dsMap.size();

		// Tính tăng trưởng doanh thu nếu có dữ liệu kỳ trước
		if (previousTotalDoanhThu > 0) {
			revenueGrowth = ((totalDoanhThu - previousTotalDoanhThu) / previousTotalDoanhThu) * 100;
		}

		// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
		String monthsWithMaxText = String.join(", ", monthWithMaxRevenue);
		// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
		String monthsWithMinText = String.join(", ", monthWithMinRevenue);

		// Cập nhật thông tin cho label
		String infoText = String.format(
				"Doanh thu cao nhất: %s (Tháng: %s)\nDoanh thu thấp nhất: %s (Tháng: %s)\nDoanh thu trung bình: %s\nTăng trưởng doanh thu so với năm trước: %.2f%%",
				decimalFormat.format(maxDoanhThu), monthsWithMaxText, decimalFormat.format(minDoanhThu),
				monthsWithMinText, decimalFormat.format(averageDoanhThu), revenueGrowth);
		lblThongTinDoanhThu.setText(infoText);
	}

	private void updateChartDonDatBanTheoNam(Map<String, Integer> dsMap) throws Exception {
		barChart_DonDatBan.getData().clear();
		NumberAxis yAxis = (NumberAxis) barChart_DonDatBan.getYAxis();
		axis.setAutoRanging(true);
		int nam = cmbNam.getValue();

		// Chuyển đổi Map thành List và sắp xếp theo thứ tự tháng (1 đến 12)
		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(dsMap.entrySet());

		entryList.sort((entry1, entry2) -> {
			// Loại bỏ tiền tố "Tháng " và chuyển đổi tháng thành số nguyên để so sánh
			int month1 = Integer.parseInt(entry1.getKey().replace("Tháng ", "").trim());
			int month2 = Integer.parseInt(entry2.getKey().replace("Tháng ", "").trim());
			return Integer.compare(month1, month2);
		});
		// Tạo Series cho biểu đồ
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Số đơn đặt bàn theo tháng");
		barChart_DonDatBan.setTitle("SỐ ĐƠN ĐẶT BÀN BÁN CÁC THÁNG TRONG NĂM " + nam);
		for (Map.Entry<String, Integer> entry : entryList) {
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
		int totalDDB = dsMap.values().stream().mapToInt(Integer::intValue).sum();
		List<String> monthsWithMaxRevenue = new ArrayList<>();
		List<String> monthsWithMinRevenue = new ArrayList<>();

		int previousTotalDoanhThu =
				((List<DonDatBan_DTO>) client.send(
						new Request(
								CommandType.DONDATBAN_GET_ALL_DONDATBAN_THEO_NAM_NV_CUTHE,
								Map.of(
										"nam", nam,
										"maNV", nhanVien.getMaNV()
								)
						)
				).getData()).size();
		double revenueGrowth = 0;

		// Duyệt qua từng tháng để tính các thông số
		for (Map.Entry<String, Integer> entry : dsMap.entrySet()) {
			String ngay = entry.getKey();
			double doanhThu = entry.getValue();

			// Kiểm tra và cập nhật mức doanh thu cao nhất
			if (doanhThu > maxSoDDB) {
				maxSoDDB = (int) doanhThu;
				monthsWithMaxRevenue.clear();
				monthsWithMaxRevenue.add(ngay);
			} else if (doanhThu == maxSoDDB) {
				monthsWithMaxRevenue.add(ngay);
			}

			// Kiểm tra và cập nhật mức doanh thu thấp nhất
			if (doanhThu < minSoDDB) {
				minSoDDB = (int) doanhThu;
				monthsWithMinRevenue.clear();
				monthsWithMinRevenue.add(ngay);
			} else if (doanhThu == minSoDDB) {
				monthsWithMinRevenue.add(ngay);
			}
		}

		// Tính doanh thu trung bình
		double averageDoanhThu = (double) totalDDB / dsMap.size();

		// Tính tăng trưởng doanh thu nếu có dữ liệu kỳ trước
		if (previousTotalDoanhThu > 0) {
			revenueGrowth = ((double) (totalDDB - previousTotalDoanhThu) / previousTotalDoanhThu) * 100;
		}

		// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu cao nhất
		String daysWithMaxText = String.join(", ", monthsWithMaxRevenue);
		// Chuẩn bị chuỗi văn bản cho các ngày có doanh thu thấp nhất
		String daysWithMinText = String.join(", ", monthsWithMinRevenue);

		// Cập nhật thông tin cho label
		String infoText = String.format(
				"Số đơn đặt bàn cao nhất: %d (Tháng: %s)\nSố đơn đặt bàn thấp nhất: %d (Tháng: %s)\nSố đơn đặt bàn trung bình: %.0f\nTăng trưởng đơn đặt bàn với năm trước: %.2f%%",
				maxSoDDB, daysWithMaxText, minSoDDB, daysWithMinText, averageDoanhThu, revenueGrowth);
		lblThongTinDonDatBan.setText(infoText);
		yAxis.setUpperBound(maxSoDDB + 1);
	}
}
