package controller.DatBan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import entity.Ban;
import entity.KhachHang;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import dto.KhachHang_DTO;

public class ADatBanHienTai_Controller implements Initializable {

	// --- ComboBox lọc ---
	@FXML
	private ComboBox<String> cmbTrangThai;
	@FXML
	private ComboBox<String> cmbLoaiBan;

	// --- Button và GridPane ---
	@FXML
	private Button btnQuayLai;
	@FXML
	private GridPane gridPaneBan;

	// --- DAO ---
	private Ban_DAO banDAO = new Ban_DAOImpl();
	private Client client;

	// --- Danh sách và trạng thái ---
	private List<Ban> danhSachBan = new ArrayList<>();
	private Ban banDangChon = null;

	private KhachHang aBanHienTai_KH;

	@FXML
	private void controller(ActionEvent event) {

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	}

	@FXML
	void btnQuayLai(ActionEvent event) {
		MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
	}

	private void khoiTaoComboBoxes() {
		cmbTrangThai.getItems().clear();
		cmbTrangThai.getItems().addAll("Tất cả", "Trống", "Đang phục vụ", "Đã được đặt");
		cmbTrangThai.getSelectionModel().select("Tất cả");
		cmbLoaiBan.getItems().clear();
		cmbLoaiBan.getItems().add("Tất cả");
		List<Ban> danhSachBanTemp = banDAO.getDanhSach("Ban.list", Ban.class);
		for (Ban ban : danhSachBanTemp) {
			String tenLoai = ban.getLoaiBan().getTenLoaiBan();
			if (!cmbLoaiBan.getItems().contains(tenLoai)) {
				cmbLoaiBan.getItems().add(tenLoai);
			}
		}
		cmbLoaiBan.getSelectionModel().select("Tất cả");
	}

	private void loadDanhSachBan() {
		gridPaneBan.getChildren().clear();
		banDangChon = null;

		danhSachBan = banDAO.getDanhSach("Ban.list", Ban.class);
		int col = 0, row = 0;
		final int MAX_COLS = 6;

		String trangThaiLoc = cmbTrangThai.getValue();
		String loaiBanLoc = cmbLoaiBan.getValue();

		for (Ban ban : danhSachBan) {
			boolean matchStatus = "Tất cả".equals(trangThaiLoc) || trangThaiLoc.equals(ban.getTrangThai());
			boolean matchType = "Tất cả".equals(loaiBanLoc) || loaiBanLoc.equals(ban.getLoaiBan().getTenLoaiBan());

			if (matchStatus && matchType) {

				Button btnBan = new Button(ban.getMaBan() + "\n" + ban.getLoaiBan().getTenLoaiBan());
				btnBan.setPrefSize(185, 110);
				btnBan.setStyle(getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));

				btnBan.setOnMouseClicked(event -> {
					if (event.getClickCount() == 1) {
						if (!"Trống".equals(ban.getTrangThai())) {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setTitle("Không thể chọn bàn");
							alert.setHeaderText("Bàn này không được phép chọn!");
							alert.setContentText("Trạng thái hiện tại: " + ban.getTrangThai()
									+ "\nChỉ có thể chọn những bàn đang trống!");
							alert.showAndWait();

							btnBan.setStyle(
									getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
							return;
						}
						int soLuongChoNgoi = ban.getLoaiBan().getSoLuong();

						Dialog<ButtonType> dialog = new Dialog<>();
						dialog.setTitle("Xác nhận chọn bàn");
						dialog.setHeaderText("Bạn muốn chọn bàn này?");

						GridPane grid = new GridPane();
						grid.setHgap(10);
						grid.setVgap(10);

						Label lblMaBan = new Label("Mã bàn: " + ban.getMaBan());
						Label lblLoaiBan = new Label("Loại bàn: " + ban.getLoaiBan().getTenLoaiBan());

						TextField txtSoLuong = new TextField();
						txtSoLuong.setPromptText("Số lượng khách (tối đa " + soLuongChoNgoi + ")");

						TextField txtSDT = new TextField();
						txtSDT.setPromptText("Số điện thoại khách hàng");

						TextField txtKH = new TextField();
						txtKH.setPromptText("Họ tên khách hàng");
						txtKH.setEditable(false);
						aBanHienTai_KH = new KhachHang();

						// đang lỗi báo nhiều thông báo
						txtSDT.textProperty().addListener((obs, oldV, newV) -> {
							if (!newV.matches("\\d*")) {
								showAlert(Alert.AlertType.ERROR, "Số điện thoại chỉ được chứa số!");
								txtSDT.setText(oldV);
								return;
							}
							if (newV.length() > 10) {
								showAlert(Alert.AlertType.ERROR, "SĐT không được vượt quá 10 số!");
								txtSDT.setText(oldV);
								return;
							}
							if (newV.length() == 10) {
								KhachHang kh = getKhachHangBySDT(newV);
								if (kh == null) {
									showAlert(Alert.AlertType.WARNING, "Không tìm thấy khách hàng!");
									txtKH.setText("Không tìm thấy");
									aBanHienTai_KH = null;
								} else {
									txtKH.setText(kh.getTenKH());
									aBanHienTai_KH = kh;
								}
							} else {
								txtKH.setText("");
								aBanHienTai_KH = null;
							}
						});

						grid.add(lblMaBan, 0, 0);
						grid.add(lblLoaiBan, 0, 1);
						grid.add(new Label("Số điện thoại:"), 0, 2);
						grid.add(txtSDT, 1, 2);
						grid.add(new Label("Khách hàng:"), 0, 3);
						grid.add(txtKH, 1, 3);
						grid.add(new Label("Số lượng khách:"), 0, 4);
						grid.add(txtSoLuong, 1, 4);

						dialog.getDialogPane().setContent(grid);
						dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

						Optional<ButtonType> result = dialog.showAndWait();

						if (result.isPresent() && result.get() == ButtonType.OK) {
							try {
								int soLuongNhap = Integer.parseInt(txtSoLuong.getText());

								if (soLuongNhap <= 0 || soLuongNhap > soLuongChoNgoi) {
									Alert err = new Alert(Alert.AlertType.ERROR);
									err.setHeaderText("Số lượng không hợp lệ!");
									err.setContentText("Vui lòng nhập số lượng từ 1 đến " + soLuongChoNgoi);
									err.show();
									return;
								}

								// SĐT chỉ được nhập số

								if (txtSDT.getText().trim().isEmpty()) {
									Alert err = new Alert(Alert.AlertType.ERROR);
									err.setHeaderText("Số điện thoại không hợp lệ!");
									err.setContentText("Vui lòng nhập số điện thoại của khách hàng");
									err.show();
								}

								if (txtKH.getText().trim().isEmpty()) {
									Alert err = new Alert(Alert.AlertType.ERROR);
									err.setHeaderText("Số điện thoại không hợp lệ!");
									err.setContentText("Số điện thoại của khách hàng không tồn tại");
									err.show();
									return;
								}

								MenuNV_Controller.banDangChon = ban;
								MenuNV_Controller.soLuongKhach = soLuongNhap;
								MenuNV_Controller.khachHangDangChon = aBanHienTai_KH;
								MenuNV_Controller.instance.readyUI("DatBan/aDatMon");

							} catch (NumberFormatException ex) {
								Alert err = new Alert(Alert.AlertType.ERROR);
								err.setHeaderText("Dữ liệu không hợp lệ");
								err.setContentText("Vui lòng nhập số nguyên!");
								err.show();
							}

						} else {
							// Bấm Hủy
							btnBan.setStyle(
									getStyleByStatusAndType(ban.getTrangThai(), ban.getLoaiBan().getMaLoaiBan()));
						}
					}
				});

				GridPane.setMargin(btnBan, new Insets(5.0));
				gridPaneBan.add(btnBan, col, row);

				col++;
				if (col >= MAX_COLS) {
					col = 0;
					row++;
				}
			}
		}
	}

	private void showAlert(Alert.AlertType type, String msg) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	// ("Tất cả", "Trống", "Đã được đặt", "Đang phục vụ");
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

	@FXML
	void btnNhanBan(ActionEvent event) {
		if (banDangChon == null) {
			showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bàn trước!");
			return;
		}
		String trangThai = banDangChon.getTrangThai();
		if (!"Đã được đặt".equals(trangThai)) {
			showAlert(Alert.AlertType.WARNING, "Chỉ những bàn đã được đặt mới nhận phục vụ!");
			return;
		}
		// Cập nhật trạng thái bàn
		banDangChon.setTrangThai("Đang phục vụ");
		boolean capNhat = banDAO.sua(banDangChon);
		if (capNhat) {
			showAlert(Alert.AlertType.INFORMATION, "Bàn đã được nhận, trạng thái: Đang phục vụ");
			loadDanhSachBan(); // Load lại UI để cập nhật màu sắc, trạng thái
		} else {
			showAlert(Alert.AlertType.ERROR, "Cập nhật trạng thái thất bại!");
		}
	}

	private KhachHang getKhachHangBySDT(String sdt) {
		try {
			Response res = client.send(new Request(CommandType.KHACHHANG_GET_BY_SDT, sdt));
			if (res != null && res.isSuccess() && res.getData() != null) {
				KhachHang_DTO dto = (KhachHang_DTO) res.getData();
				return KhachHang.builder()
						.maKH(dto.getMaKH())
						.tenKH(dto.getTenKH())
						.sdt(dto.getSdt())
						.diemTichLuy(dto.getDiemTichLuy())
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}