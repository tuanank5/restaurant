package controller.KhachHang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Menu.MenuNV_Controller;
import dto.HangKhachHang_DTO;
import dto.KhachHang_DTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ThongTinKhachHang_Controller {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnHuy;

	@FXML
	private Button btnSuaTT;

	@FXML
	private Button btnTroLai;

	@FXML
	private Button btnHelp;

	@FXML
	private ComboBox<String> comBoxHangKH;

	@FXML
	private Label lblDanhSachKhachHang;

	@FXML
	private TextField txtDiaChi;

	@FXML
	private TextField txtDiemTichLuy;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtMaKH;

	@FXML
	private TextField txtSDT;

	@FXML
	private TextField txtTenKH;

	private KhachHang_DTO khachHang;

	private final Map<String, String> tenHangByMaHang = new HashMap<>();
	private Client client;

	public void initialize() {
		Platform.runLater(() -> btnTroLai.requestFocus());

		client = Client.tryCreate();

		loadData();
	}

	@FXML
	private void controller(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnSuaTT) {
			showThongTin();
		} else if (source == btnTroLai) {
			troLai();
		} else if (source == btnHuy) {
			MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
		} else if (source == btnHelp) {
			MenuNV_Controller.instance.readyUI("Help/HelpScroll");
		}
	}

	@FXML
	void mouseClicked(MouseEvent event) {
		Object source = event.getSource();
		if (source == lblDanhSachKhachHang) {
			troLai();
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			troLai();
		} else if (event.getCode() == KeyCode.X) {
			btnHuy.fire();
		}
	}

	private void showThongTin() {
		if (khachHang != null) {
			CapNhatKhachHang_Controller capNhatHanhKhachController = MenuNV_Controller.instance
					.readyUI("KhachHang/CapNhatKhachHang").getController();
			capNhatHanhKhachController.setKhachHang(khachHang);
		}
	}

	public void hienThiThongTin(KhachHang_DTO khachHang) {
		if (khachHang != null) {
			txtMaKH.setText(khachHang.getMaKH());
			txtTenKH.setText(khachHang.getTenKH());
			txtSDT.setText(khachHang.getSdt());
			txtEmail.setText(khachHang.getEmail());
			txtDiaChi.setText(khachHang.getDiaChi());
			txtDiemTichLuy.setText(String.valueOf(khachHang.getDiemTichLuy()));
			String tenHang = tenHangByMaHang.get(khachHang.getMaHangKhachHang());
			comBoxHangKH.setValue(tenHang == null ? "" : tenHang);
		}
	}

	public void setKhachHang(KhachHang_DTO khachHang) {
		this.khachHang = khachHang;
		hienThiThongTin(khachHang);
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request request = Request.builder().commandType(CommandType.HANGKHACHHANG_GET_ALL).build();
			Response response = client == null ? null : client.send(request);
			Object data = response == null ? null : response.getData();

			if (!(data instanceof List<?> rawList)) {
				return;
			}

			List<HangKhachHang_DTO> list = (List<HangKhachHang_DTO>) rawList;

			tenHangByMaHang.clear();
			comBoxHangKH.getItems().clear();

			if (list != null) {
				for (HangKhachHang_DTO hang : list) {
					if (hang != null && hang.getMaHang() != null) {
						tenHangByMaHang.put(hang.getMaHang(), hang.getTenHang());
						comBoxHangKH.getItems().add(hang.getTenHang());
					}
				}
			}

			comBoxHangKH.getSelectionModel().selectFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void troLai() {
		MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
	}
}
