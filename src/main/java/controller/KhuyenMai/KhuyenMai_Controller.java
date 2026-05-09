package controller.KhuyenMai;

import dto.KhuyenMai_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class KhuyenMai_Controller implements Initializable {

	@FXML private BorderPane borderPane;

	@FXML private Button btnThemKM, btnSuaKM, btnXoaKM;

	// TABLE
	@FXML private TableView<KhuyenMai_DTO> tblKM;
	@FXML private TableColumn<KhuyenMai_DTO, String> colMaKM;
	@FXML private TableColumn<KhuyenMai_DTO, String> colTenKM;
	@FXML private TableColumn<KhuyenMai_DTO, String> colloaiKM;
	@FXML private TableColumn<KhuyenMai_DTO, String> colNgayBatDau;
	@FXML private TableColumn<KhuyenMai_DTO, String> colNgayKetThuc;
	@FXML private TableColumn<KhuyenMai_DTO, String> colPhanTramGiamGia;

	// FORM
	@FXML private TextField txtTimKiem, txtMaKM, txtTenKM;
	@FXML private ComboBox<String> comBoxLoaiKM;
	@FXML private ComboBox<Integer> comBoxPhanTram;
	@FXML private DatePicker dpNgayBatDau, dpNgayKetThuc;

	private ObservableList<KhuyenMai_DTO> danhSach = FXCollections.observableArrayList();
	private FilteredList<KhuyenMai_DTO> filteredList;

	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtMaKM.setEditable(false);
		setTable();
		setComboBox();
		loadData();
		timKiem();

		btnSuaKM.setDisable(true);
		btnXoaKM.setDisable(true);

		tblKM.setOnMouseClicked(e -> {

			KhuyenMai_DTO km = tblKM.getSelectionModel().getSelectedItem();

			if (km != null) {

				fillForm(km);

				btnSuaKM.setDisable(false);
				btnXoaKM.setDisable(false);
			}
		});
	}

	// ================= TABLE =================
	private void setTable() {

		colMaKM.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getMaKM()));

		colTenKM.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getTenKM()));

		colloaiKM.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getLoaiKM()));

		colNgayBatDau.setCellValueFactory(c ->
				new SimpleStringProperty(
						c.getValue().getNgayBatDau() != null
								? c.getValue().getNgayBatDau().toString()
								: ""
				)
		);

		colNgayKetThuc.setCellValueFactory(c ->
				new SimpleStringProperty(
						c.getValue().getNgayKetThuc() != null
								? c.getValue().getNgayKetThuc().toString()
								: ""
				)
		);

		colPhanTramGiamGia.setCellValueFactory(c ->
				new SimpleStringProperty(
						String.valueOf(c.getValue().getPhanTramGiamGia())
				)
		);
	}

	// ================= COMBOBOX =================
	private void setComboBox() {

		comBoxLoaiKM.setItems(FXCollections.observableArrayList(
				"Ưu đãi VIP",
				"Khuyến mãi hóa đơn"
		));

		comBoxPhanTram.setItems(FXCollections.observableArrayList(
				5, 10, 15, 20, 25, 30, 35, 40
		));
	}

	// ================= LOAD DATA =================
	@SuppressWarnings("unchecked")
	private void loadData() {

		try {

			Request req = new Request(
					CommandType.KHUYENMAI_GET_ALL,
					null
			);

			Response res = client.send(req);

			if (res != null && res.isSuccess()) {

				List<KhuyenMai_DTO> list =
						(List<KhuyenMai_DTO>) res.getData();

				danhSach.setAll(list);

				filteredList =
						new FilteredList<>(danhSach, p -> true);

				tblKM.setItems(filteredList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= BUTTON =================
	@FXML
	private void controller(ActionEvent event) {

		Object source = event.getSource();

		if (source == btnThemKM) {
			them();

		} else if (source == btnSuaKM) {
			sua();

		} else if (source == btnXoaKM) {
			xoa();
		}
	}

	// ================= ADD =================
	private void them() {

		if (!validate()) return;

		try {

			KhuyenMai_DTO km = getFormData();

			Request req = new Request(
					CommandType.KHUYENMAI_ADD,
					km
			);

			Response res = client.send(req);

			if (res != null) {

				if (res.isSuccess()) {

					loadData();

					clearForm();

					AlertUtil.showAlert(
							"OK",
							"Thêm thành công",
							Alert.AlertType.INFORMATION
					);

				} else {

					AlertUtil.showAlert(
							"Lỗi",
							res.getMessage(),
							Alert.AlertType.ERROR
					);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= UPDATE =================
	private void sua() {

		if (!validate()) return;

		try {

			KhuyenMai_DTO km = getFormData();

			Request req = new Request(
					CommandType.KHUYENMAI_UPDATE,
					km
			);

			Response res = client.send(req);

			if (res != null) {

				if (res.isSuccess()) {

					loadData();

					clearForm();

					AlertUtil.showAlert(
							"OK",
							"Cập nhật thành công",
							Alert.AlertType.INFORMATION
					);

				} else {

					AlertUtil.showAlert(
							"Lỗi",
							res.getMessage(),
							Alert.AlertType.ERROR
					);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= DELETE =================
	private void xoa() {

		KhuyenMai_DTO km =
				tblKM.getSelectionModel().getSelectedItem();

		if (km == null) return;

		try {

			Request req = new Request(
					CommandType.KHUYENMAI_DELETE,
					km.getMaKM()
			);

			Response res = client.send(req);

			if (res != null && res.isSuccess()) {

				loadData();

				clearForm();

				AlertUtil.showAlert(
						"OK",
						"Xóa thành công",
						Alert.AlertType.INFORMATION
				);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= GET FORM =================
	private KhuyenMai_DTO getFormData() {

		return new KhuyenMai_DTO(
				txtMaKM.getText(),

				txtTenKM.getText(),

				comBoxLoaiKM.getValue(),

				Date.valueOf(dpNgayBatDau.getValue()),

				Date.valueOf(dpNgayKetThuc.getValue()),

				comBoxPhanTram.getValue()
		);
	}

	// ================= FILL FORM =================
	private void fillForm(KhuyenMai_DTO km) {

		txtMaKM.setText(km.getMaKM());

		txtTenKM.setText(km.getTenKM());

		comBoxLoaiKM.setValue(km.getLoaiKM());

		comBoxPhanTram.setValue(km.getPhanTramGiamGia());

		dpNgayBatDau.setValue(
				km.getNgayBatDau().toLocalDate()
		);

		dpNgayKetThuc.setValue(
				km.getNgayKetThuc().toLocalDate()
		);
	}

	// ================= CLEAR =================
	private void clearForm() {

		txtMaKM.clear();

		txtTenKM.clear();

		comBoxLoaiKM
				.getSelectionModel()
				.clearSelection();

		comBoxPhanTram
				.getSelectionModel()
				.clearSelection();

		dpNgayBatDau.setValue(null);

		dpNgayKetThuc.setValue(null);

		tblKM.getSelectionModel().clearSelection();

		btnSuaKM.setDisable(true);

		btnXoaKM.setDisable(true);
	}

	// ================= SEARCH =================
	private void timKiem() {

		txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {

			String f = newV == null ? "" : newV.toLowerCase().trim();

			filteredList.setPredicate(km ->

					km.getMaKM().toLowerCase().contains(f)

							|| km.getTenKM().toLowerCase().contains(f)

							|| km.getLoaiKM().toLowerCase().contains(f)
			);
		});
	}

	// ================= VALIDATE =================
	private boolean validate() {

		if (txtTenKM.getText().isBlank()) {

			AlertUtil.showAlert(
					"Lỗi",
					"Tên khuyến mãi không được rỗng",
					Alert.AlertType.WARNING
			);

			return false;
		}

		if (dpNgayBatDau.getValue() == null
				|| dpNgayKetThuc.getValue() == null) {

			AlertUtil.showAlert(
					"Lỗi",
					"Vui lòng chọn ngày",
					Alert.AlertType.WARNING
			);

			return false;
		}

		if (!dpNgayKetThuc.getValue()
				.isAfter(dpNgayBatDau.getValue())) {

			AlertUtil.showAlert(
					"Lỗi",
					"Ngày kết thúc phải sau ngày bắt đầu",
					Alert.AlertType.WARNING
			);

			return false;
		}

		return true;
	}

}