package controller.DatBan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ADoiBan_Controller implements Initializable {

	@FXML
	private Button btnBan;

	@FXML
	private Button btnTroLai;

	@FXML
	private Button btnXacNhan;

	@FXML
	private ComboBox<String> cmbLoaiBan;

	@FXML
	private ComboBox<String> cmbTrangThai;

	@FXML
	private GridPane gridPaneBan;

	@FXML
	void btnXacNhan(ActionEvent event) {

		if (banMoiDuocChon == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn mới!");
			return;
		}

		int soChoToiDa = 20;

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Xác nhận đổi bàn");
		dialog.setHeaderText("Nhập số lượng khách");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10));

		Label lblBanMoi = new Label(
				"Bàn mới: " + banMoiDuocChon.getMaBan());

		TextField txtSoLuong = new TextField();
		txtSoLuong.setPromptText("Tối đa " + soChoToiDa + " khách");

		grid.add(lblBanMoi, 0, 0, 2, 1);
		grid.add(new Label("Số lượng khách:"), 0, 1);
		grid.add(txtSoLuong, 1, 1);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE),
				ButtonType.CANCEL);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.isEmpty() || result.get().getButtonData() != ButtonBar.ButtonData.OK_DONE) {
			return;
		}

		// Validate số lượng
		int soLuongMoi;
		try {
			soLuongMoi = Integer.parseInt(txtSoLuong.getText());
			if (soLuongMoi <= 0 || soLuongMoi > soChoToiDa) {
				showAlert(Alert.AlertType.ERROR, "Số lượng phải từ 1 đến " + soChoToiDa);
				return;
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, "Số lượng không hợp lệ!");
			return;
		}

		// GỌI HÀM ĐỔI BÀN
		thucHienDoiBan(soLuongMoi);
	}

	private void thucHienDoiBan(int soLuongMoi) {
		// 1. Kiểm tra đơn đặt bàn
		DonDatBan_DTO don = util.MapperUtil.map(
				MenuNV_Controller.donDatBanDangDoi,
				DonDatBan_DTO.class
		);
		if (don == null) {
			showAlert(Alert.AlertType.ERROR, "Không tìm thấy đơn đặt bàn cần đổi!");
			return;
		}

		// 2. Kiểm tra bàn cũ
		Ban_DTO banCu = don.getBan();
		if (banCu == null) {
			showAlert(Alert.AlertType.ERROR, "Đơn đặt bàn chưa có bàn cũ!");
			return;
		}

		// 3. Kiểm tra bàn mới
		if (banMoiDuocChon == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn mới trước khi xác nhận!");
			return;
		}

		// 4. Kiểm tra số lượng chỗ
		if (soLuongMoi <= 0) {
			showAlert(Alert.AlertType.WARNING, "Số lượng chỗ không hợp lệ!");
			return;
		}

		try {
			// 5. Cập nhật trạng thái bàn cũ
			banCu.setTrangThai("Trống");
			capNhatBan(banCu);

			// 6. Cập nhật trạng thái bàn mới
			banMoiDuocChon.setTrangThai("Đã được đặt");
			capNhatBan(banMoiDuocChon);

			// 7. Cập nhật đơn đặt bàn
			don.setBan(banMoiDuocChon);
			don.setSoLuong(soLuongMoi); // CÀI LẠI SỐ LƯỢNG CHỖ

			capNhatDonDatBan(don);

			showAlert(Alert.AlertType.INFORMATION, "Đổi bàn thành công!");

			// 8. Quay về màn hình hiện tại
			MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");

		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Đổi bàn thất bại! Vui lòng thử lại.");
		}
	}

	@FXML
	private Button btnQuayLai;

	@FXML
	void btnQuayLai(ActionEvent event) {
		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	public static DonDatBan_DTO donDatBanDuocChon;
	private List<Ban_DTO> danhSachBan = new ArrayList<>();
	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = Client.tryCreate();
		khoiTaoComboBoxes();
		loadDanhSachBan();
		cmbTrangThai.setOnAction(e -> loadDanhSachBan());
		cmbLoaiBan.setOnAction(e -> loadDanhSachBan());

		Tooltip toolTipQL = new Tooltip("Quay lại danh sách đặt bàn");
		btnQuayLai.setTooltip(toolTipQL);

		Tooltip toolTipLB = new Tooltip("Lọc danh sách theo loại bàn");
		cmbLoaiBan.setTooltip(toolTipLB);

		Tooltip toolTipTT = new Tooltip("Lọc danh sách theo trạng thái");
		cmbTrangThai.setTooltip(toolTipTT);

		btnXacNhan.setTooltip(new Tooltip("Xác nhận đổi bàn"));
	}

	private void khoiTaoComboBoxes() {
		cmbTrangThai.getItems().clear();
		cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đang phục vụ", "Đã được đặt");
		cmbTrangThai.getSelectionModel().select("Tất cả");
		cmbLoaiBan.getItems().clear();
		cmbLoaiBan.getItems().add("Tất cả");
		List<Ban_DTO> danhSachBanTemp = getAllBan();
		for (Ban_DTO ban : danhSachBanTemp) {
			String tenLoai = ban.getMaLoaiBan();
			if (!cmbLoaiBan.getItems().contains(tenLoai)) {
				cmbLoaiBan.getItems().add(tenLoai);
			}
		}
		cmbLoaiBan.getSelectionModel().select("Tất cả");
	}

	private Ban_DTO banMoiDuocChon = null; // đổi bàn
	private Button btnBanDangChonUI = null;

	private void loadDanhSachBan() {
		gridPaneBan.getChildren().clear();
		danhSachBan = getAllBan();

		int col = 0, row = 0;
		final int MAX_COLS = 6;

		String trangThaiLoc = cmbTrangThai.getValue();
		String loaiBanLoc = cmbLoaiBan.getValue();

		for (Ban_DTO ban : danhSachBan) {
			boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
			boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getMaLoaiBan());

			if (matchStatus && matchType) {

				Button btnBan = new Button(ban.getMaBan() + "\n" + ban.getMaLoaiBan());
				btnBan.setPrefSize(185, 110);
				btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getMaLoaiBan()));

				btnBan.setOnMouseClicked(event -> {
					if (!"Trống".equals(ban.getTrangThai())) {
						showAlert(Alert.AlertType.WARNING, "Chỉ được chọn bàn trống để đổi!");
						return;
					}

					// Hoàn nguyên style bàn cũ
					if (btnBanDangChonUI != null && banMoiDuocChon != null) {
						btnBanDangChonUI.setStyle(getStyleByStatusAndType(banMoiDuocChon.getTrangThai(),
								banMoiDuocChon.getMaLoaiBan()));
					}

					// Lưu bàn mới
					banMoiDuocChon = ban;
					btnBanDangChonUI = btnBan;

					// Highlight bàn đang chọn
					btnBan.setStyle(
							"-fx-background-color: yellow;" + "-fx-text-fill: black;" + "-fx-font-weight: bold;");
				});

				GridPane.setMargin(btnBan, new Insets(5));
				gridPaneBan.add(btnBan, col, row);

				col++;
				if (col >= MAX_COLS) {
					col = 0;
					row++;
				}
			}
		}
	}

	private String getStyleByStatusAndType(String trangThai, String maLoaiBan) {

		String backgroundColor = "white";
		if (trangThai != null && !trangThai.isEmpty()) {
			switch (trangThai) {
			case "Đã được đặt":
				backgroundColor = "red";
				break;
			case "Trống":
				backgroundColor = "green";
				break;
			case "Đang phục vụ":
				backgroundColor = "#f39c12";
			}
		}
		return String.format(
				"-fx-background-color: %s;" + "-fx-background-radius: 15;" + "-fx-padding: 10;" + "-fx-font-size: 18px;"
						+ "-fx-font-weight: bold;" + "-fx-text-fill: white;" + "-fx-font-family: 'Times New Roman';"
						+ "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 6, 0.6, 2, 2);",
				backgroundColor);
	}

	private void showAlert(Alert.AlertType type, String msg) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	@SuppressWarnings("unchecked")
	private List<Ban_DTO> getAllBan() {
		try {
			Request req = Request.builder().commandType(CommandType.BAN_GET_ALL).build();
			Response response = client == null ? null : client.send(req);
			Object data = response == null ? null : response.getData();
			if (!(data instanceof List<?> rawList)) {
				return List.of();
			}
			return (List<Ban_DTO>) rawList;
		} catch (Exception e) {
			return List.of();
		}
	}

	private boolean capNhatBan(Ban_DTO ban) {
		try {
			Request req = Request.builder().commandType(CommandType.BAN_UPDATE).data(ban).build();
			Response response = client == null ? null : client.send(req);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean capNhatDonDatBan(DonDatBan_DTO donDatBan) {
		try {
			Request req = Request.builder().commandType(CommandType.DONDATBAN_UPDATE).data(donDatBan).build();
			Response response = client == null ? null : client.send(req);
			return response != null && response.isSuccess();
		} catch (Exception e) {
			return false;
		}
	}
}
