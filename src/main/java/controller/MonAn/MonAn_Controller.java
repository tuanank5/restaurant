package controller.MonAn;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dto.MonAn_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;

public class MonAn_Controller implements Initializable {

	@FXML
	private Button btnSua, btnThem, btnThemAnh, btnXoa;

	@FXML
	private ComboBox<String> cmbLoaiMon;

	@FXML
	private ImageView img;

	@FXML
	private TableView<MonAn_DTO> tblMon;

	@FXML
	private TableColumn<MonAn_DTO, String> colMa, colTen, colLoaiMon;

	@FXML
	private TableColumn<MonAn_DTO, String> colDonGia;

	@FXML
	private TextField txtMaMon, txtTenMon, txtDonGia, txtTimKiem;

	private String duongDanAnh;

	private ObservableList<MonAn_DTO> danhSach = FXCollections.observableArrayList();
	private FilteredList<MonAn_DTO> filteredList;

	private Client client;

	@FXML
	private void handleThemAnh() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Chọn ảnh món ăn");

		// Chỉ cho chọn file ảnh
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			duongDanAnh = file.getAbsolutePath(); // Lưu đường dẫn ảnh

			Image image = new Image(file.toURI().toString());
			img.setImage(image);

			// Giữ nguyên kích thước đã đặt trong FXML — KHÔNG set lại fitWidth / fitHeight
			img.setPreserveRatio(true);
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtMaMon.setEditable(false);

		setTable();
		setComboBox();
		loadData();
		timKiem();
		generateId();

		btnSua.setDisable(true);
		btnXoa.setDisable(true);

		tblMon.setOnMouseClicked(e -> {
			MonAn_DTO mon = tblMon.getSelectionModel().getSelectedItem();
			if (mon != null) {
				fillForm(mon);
				btnSua.setDisable(false);
				btnXoa.setDisable(false);
			}
		});

		btnSua.setOnAction(e -> sua());
		btnXoa.setOnAction(e -> xoa());

		btnSua.setTooltip(new Tooltip("Thông báo cho nút Sửa món!"));
		btnThem.setTooltip(new Tooltip("Thông báo cho nút Thêm món!"));
		btnThemAnh.setTooltip(new Tooltip("Thông báo cho nút Thêm ảnh món!"));
		btnXoa.setTooltip(new Tooltip("Thông báo cho nút Xoá món!"));
		txtTenMon.setTooltip(new Tooltip("Nhập tên món!"));
		txtDonGia.setTooltip(new Tooltip("Nhập đơn giá món!"));
		cmbLoaiMon.setTooltip(new Tooltip("Chọn loại món!"));
		txtMaMon.setTooltip(new Tooltip("Mã của món!"));
	}

	// ================= TABLE =================
	private void setTable() {
		colMa.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getMaMon()));

		colTen.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getTenMon()));

		colDonGia.setCellValueFactory(c ->
				new SimpleStringProperty(String.valueOf(c.getValue().getDonGia())));

		colLoaiMon.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getLoaiMon()));
	}

	// ================= COMBOBOX =================
	private void setComboBox() {
		cmbLoaiMon.setItems(FXCollections.observableArrayList(
				"Món chính",
				"Tráng miệng",
				"Nước uống"
		));
	}

	// ================= LOAD DATA =================
	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request req = new Request(CommandType.MONAN_GET_ALL, null);
			Response res = client.send(req);

			if (res != null && res.isSuccess()) {
				List<MonAn_DTO> list = (List<MonAn_DTO>) res.getData();
				danhSach.setAll(list);
				filteredList = new FilteredList<>(danhSach, p -> true);
				tblMon.setItems(filteredList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= GENERATE ID =================
	private void generateId() {
		try {
			Request req = new Request(CommandType.MONAN_GENERATE_ID, null);
			Response res = client.send(req);
			if (res != null && res.isSuccess() && res.getData() != null) {
				txtMaMon.setText(res.getData().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleXoaMon() {
		xoa();
	}

	@FXML
	private void handleSuaMon() {
		sua();
	}

	@FXML
	private void handleThemMon() {
		them();
	}

	// ================= ADD =================
	private void them() {
		if (!validate()) return;

		try {
			MonAn_DTO mon = getFormData();
			Request req = new Request(CommandType.MONAN_ADD, mon);
			Response res = client.send(req);

			if (res != null) {
				if (res.isSuccess()) {
					loadData();
					clearForm();
					AlertUtil.showAlert("OK", "Thêm thành công", Alert.AlertType.INFORMATION);
				} else {
					AlertUtil.showAlert("Lỗi", res.getMessage(), Alert.AlertType.ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= UPDATE =================
	private void sua() {
		if (!validate()) return;

		MonAn_DTO selected = tblMon.getSelectionModel().getSelectedItem();
		if (selected == null) return;

		try {
			MonAn_DTO mon = getFormData();
			Request req = new Request(CommandType.MONAN_UPDATE, mon);
			Response res = client.send(req);

			if (res != null) {
				if (res.isSuccess()) {
					loadData();
					clearForm();
					AlertUtil.showAlert("OK", "Cập nhật thành công", Alert.AlertType.INFORMATION);
				} else {
					AlertUtil.showAlert("Lỗi", res.getMessage(), Alert.AlertType.ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= DELETE =================
	private void xoa() {
		MonAn_DTO mon = tblMon.getSelectionModel().getSelectedItem();
		if (mon == null) return;

		if (AlertUtil.showAlertConfirm("Bạn có chắc muốn xóa món: " + mon.getTenMon() + " ?")
				.filter(bt -> bt.getButtonData() == javafx.scene.control.ButtonBar.ButtonData.YES)
				.isEmpty()) {
			return;
		}

		try {
			Request req = new Request(CommandType.MONAN_DELETE, mon.getMaMon());
			Response res = client.send(req);

			if (res != null && res.isSuccess()) {
				loadData();
				clearForm();
				AlertUtil.showAlert("OK", "Xóa thành công", Alert.AlertType.INFORMATION);
			} else if (res != null) {
				AlertUtil.showAlert("Lỗi", res.getMessage(), Alert.AlertType.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= GET FORM =================
	private MonAn_DTO getFormData() {
		double donGia = Double.parseDouble(txtDonGia.getText().trim());

		return new MonAn_DTO(
				txtMaMon.getText(),
				txtTenMon.getText().trim(),
				donGia,
				duongDanAnh,
				cmbLoaiMon.getValue()
		);
	}

	// ================= FILL FORM =================
	private void fillForm(MonAn_DTO mon) {
		txtMaMon.setText(mon.getMaMon());
		txtTenMon.setText(mon.getTenMon());
		txtDonGia.setText(String.valueOf(mon.getDonGia()));

		cmbLoaiMon.setValue(mon.getLoaiMon());

		if (mon.getDuongDanAnh() != null && !mon.getDuongDanAnh().isBlank()) {
			File file = new File(mon.getDuongDanAnh());
			if (file.exists()) {
				img.setImage(new Image(file.toURI().toString()));
				duongDanAnh = mon.getDuongDanAnh();
			} else {
				img.setImage(null);
				duongDanAnh = null;
			}
		} else {
			img.setImage(null);
			duongDanAnh = null;
		}
	}

	// ================= CLEAR =================
	private void clearForm() {
		txtTenMon.clear();
		txtDonGia.clear();
		img.setImage(null);
		duongDanAnh = null;

		cmbLoaiMon.getSelectionModel().clearSelection();
		tblMon.getSelectionModel().clearSelection();

		btnSua.setDisable(true);
		btnXoa.setDisable(true);

		generateId();
	}

	// ================= SEARCH =================
	private void timKiem() {
		txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
			if (filteredList == null) return;

			String f = newV == null ? "" : newV.toLowerCase().trim();

			filteredList.setPredicate(m ->
					m.getMaMon().toLowerCase().contains(f)
							|| m.getTenMon().toLowerCase().contains(f)
							|| (m.getLoaiMon() != null && m.getLoaiMon().toLowerCase().contains(f))
			);
		});
	}

	// ================= VALIDATE =================
	private boolean validate() {

		if (txtTenMon.getText().isBlank()) {
			AlertUtil.showAlert("Lỗi", "Tên món ăn không được rỗng", Alert.AlertType.WARNING);
			return false;
		}

		if (txtDonGia.getText().isBlank()) {
			AlertUtil.showAlert("Lỗi", "Đơn giá không được rỗng", Alert.AlertType.WARNING);
			return false;
		}

		double donGia;
		try {
			donGia = Double.parseDouble(txtDonGia.getText().trim());
		} catch (Exception e) {
			AlertUtil.showAlert("Lỗi", "Đơn giá phải là số hợp lệ", Alert.AlertType.WARNING);
			return false;
		}

		if (donGia <= 0) {
			AlertUtil.showAlert("Lỗi", "Đơn giá phải > 0", Alert.AlertType.WARNING);
			return false;
		}

		if (cmbLoaiMon.getValue() == null || cmbLoaiMon.getValue().isBlank()) {
			AlertUtil.showAlert("Lỗi", "Vui lòng chọn loại món", Alert.AlertType.WARNING);
			return false;
		}

		return true;
	}
}
