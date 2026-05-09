package controller.Ban;

import java.util.List;

import dto.LoaiBan_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import dto.Ban_DTO;
import util.AlertUtil;

public class QuanLyBan_Controller {

	@FXML
	private BorderPane borderPane;

	@FXML
	private TextField txtTimKiem;

	@FXML
	private Button btnSua;

	@FXML
	private Button btnThem;

	@FXML
	private Button btnXoa;

	@FXML
	private TableColumn<Ban_DTO, String> colMaBan;

	@FXML
	private TableColumn<Ban_DTO, String> colMaLoaiBan;

	@FXML
	private TableColumn<Ban_DTO, String> colViTri;

	@FXML
	private TableView<Ban_DTO> tblBan;

	@FXML
	private TextField txtMaBan;

	@FXML
	private TextField txtViTri;

	@FXML
	private ComboBox<LoaiBan_DTO> comBoxLoaiBan;

	private ObservableList<Ban_DTO> danhSachBan = FXCollections.observableArrayList();
	private FilteredList<Ban_DTO> filteredList;

	private Client client;

	@FXML
	public void initialize() {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtMaBan.setEditable(false);

		setTable();

		try {
			Response rLb = client.send(new Request(CommandType.LOAIBAN_GET_ALL, null));
			if (rLb != null && rLb.isSuccess() && rLb.getData() != null) {
				@SuppressWarnings("unchecked")
				List<LoaiBan_DTO> listLoaiBan = (List<LoaiBan_DTO>) rLb.getData();
				comBoxLoaiBan.getItems().setAll(listLoaiBan);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		comBoxLoaiBan.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(LoaiBan_DTO item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? "" : item.getTenLoaiBan());
			}
		});
		comBoxLoaiBan.setButtonCell(new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(LoaiBan_DTO item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? "" : item.getTenLoaiBan());
			}
		});
		// Mặc định disable nút Sửa/Xóa
		btnSua.setDisable(true);
		btnXoa.setDisable(true);
		// Sửa
		tblBan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// Có dòng được chọn → enable nút Sửa/Xóa
				btnSua.setDisable(false);
				btnXoa.setDisable(false);
				// Điền dữ liệu vào các TextField và ComboBox
				fillForm(newSelection);

			} else {
				// Không có dòng nào được chọn → disable nút Sửa/Xóa
				btnSua.setDisable(true);
				btnXoa.setDisable(true);

				// Reset form
				clearForm();
			}
		});

		loadData();
		timKiem();
		generateId();

		btnSua.setTooltip(new Tooltip("Sửa thông tin bàn đã chọn"));
		btnThem.setTooltip(new Tooltip("Thêm bàn mới vào danh sách"));
		btnXoa.setTooltip(new Tooltip("Xóa bàn đã chọn"));
		txtMaBan.setTooltip(new Tooltip("Nhập mã bàn"));
		txtTimKiem.setTooltip(new Tooltip("Nhập mã bàn hoặc vị trí để tìm kiếm"));
		txtViTri.setTooltip(new Tooltip("Nhập vị trí của bàn trong nhà hàng"));
		comBoxLoaiBan.setTooltip(new Tooltip("Chọn loại bàn (nhỏ, thường, lớn)"));

	}

	// ================= TABLE =================
	private void setTable() {
		colMaBan.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getMaBan()));

		colViTri.setCellValueFactory(c ->
				new SimpleStringProperty(c.getValue().getViTri()));

		colMaLoaiBan.setCellValueFactory(c -> {
			String maLoai = c.getValue().getMaLoaiBan();
			String tenLoai = "";
			if (maLoai != null) {
				for (LoaiBan_DTO lb : comBoxLoaiBan.getItems()) {
					if (maLoai.equals(lb.getMaLoaiBan())) {
						tenLoai = lb.getTenLoaiBan();
						break;
					}
				}
			}
			return new SimpleStringProperty(tenLoai);
		});
	}

	// ================= LOAD DATA =================
	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			Request req = new Request(CommandType.BAN_GET_ALL, null);
			Response res = client.send(req);

			if (res != null && res.isSuccess()) {
				List<Ban_DTO> list = (List<Ban_DTO>) res.getData();
				danhSachBan.setAll(list);
				filteredList = new FilteredList<>(danhSachBan, p -> true);
				tblBan.setItems(filteredList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= GENERATE ID =================
	private void generateId() {
		try {
			Request req = new Request(CommandType.BAN_GENERATE_ID, null);
			Response res = client.send(req);
			if (res != null && res.isSuccess() && res.getData() != null) {
				txtMaBan.setText(res.getData().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= SEARCH =================
	private void timKiem() {
		txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
			if (filteredList == null) return;

			String kw = newV == null ? "" : newV.toLowerCase().trim();

			filteredList.setPredicate(b -> {
				String tenLoai = "";
				if (b.getMaLoaiBan() != null) {
					for (LoaiBan_DTO lb : comBoxLoaiBan.getItems()) {
						if (b.getMaLoaiBan().equals(lb.getMaLoaiBan())) {
							tenLoai = lb.getTenLoaiBan();
							break;
						}
					}
				}

				return b.getMaBan().toLowerCase().contains(kw)
						|| b.getViTri().toLowerCase().contains(kw)
						|| tenLoai.toLowerCase().contains(kw);
			});
		});
	}

	@FXML
	void controller(ActionEvent event) {
		if (event.getSource() == btnThem) {
			them();
		} else if (event.getSource() == btnSua) {
			sua();
		} else if (event.getSource() == btnXoa) {
			xoa();
		}
	}

	// ================= ADD =================
	private void them() {
		if (!validate()) return;

		try {
			Ban_DTO dto = getFormDataAdd();
			Request req = new Request(CommandType.BAN_ADD, dto);
			Response res = client.send(req);

			if (res != null) {
				if (res.isSuccess()) {
					loadData();
					clearForm();
					AlertUtil.showAlert("Thành công", "Thêm bàn thành công!", Alert.AlertType.INFORMATION);
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
		Ban_DTO selected = tblBan.getSelectionModel().getSelectedItem();
		if (selected == null) return;

		if (!validate()) return;

		try {
			Ban_DTO dto = getFormDataUpdate(selected);
			Request req = new Request(CommandType.BAN_UPDATE, dto);
			Response res = client.send(req);

			if (res != null) {
				if (res.isSuccess()) {
					loadData();
					clearForm();
					AlertUtil.showAlert("Thành công", "Sửa bàn thành công!", Alert.AlertType.INFORMATION);
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
		Ban_DTO banChon = tblBan.getSelectionModel().getSelectedItem();
		if (banChon == null) {
			AlertUtil.showAlert("Thông báo", "Vui lòng chọn bàn cần xóa!", Alert.AlertType.WARNING);
			return;
		}

		if (AlertUtil.showAlertConfirm("Xác nhận xóa bàn " + banChon.getMaBan() + "?")
				.filter(bt -> bt.getButtonData() == javafx.scene.control.ButtonBar.ButtonData.YES)
				.isEmpty()) {
			return;
		}

		try {
			Request req = new Request(CommandType.BAN_DELETE, banChon.getMaBan());
			Response res = client.send(req);
			if (res != null && res.isSuccess()) {
				loadData();
				clearForm();
				AlertUtil.showAlert("Thành công", "Xóa bàn thành công!", Alert.AlertType.INFORMATION);
			} else if (res != null) {
				AlertUtil.showAlert("Lỗi", res.getMessage(), Alert.AlertType.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================= FORM =================
	private Ban_DTO getFormDataAdd() {
		return Ban_DTO.builder()
				.maBan(txtMaBan.getText())
				.viTri(txtViTri.getText().trim())
				.trangThai("Trống")
				.maLoaiBan(comBoxLoaiBan.getValue().getMaLoaiBan())
				.build();
	}

	private Ban_DTO getFormDataUpdate(Ban_DTO selected) {
		return Ban_DTO.builder()
				.maBan(selected.getMaBan()) // không cho sửa ID
				.viTri(txtViTri.getText().trim())
				.trangThai(selected.getTrangThai() != null ? selected.getTrangThai() : "Trống")
				.maLoaiBan(comBoxLoaiBan.getValue().getMaLoaiBan())
				.build();
	}

	private void fillForm(Ban_DTO b) {
		txtMaBan.setText(b.getMaBan());
		txtViTri.setText(b.getViTri());

		if (b.getMaLoaiBan() != null) {
			for (LoaiBan_DTO lb : comBoxLoaiBan.getItems()) {
				if (b.getMaLoaiBan().equals(lb.getMaLoaiBan())) {
					comBoxLoaiBan.setValue(lb);
					break;
				}
			}
		}
	}

	private void clearForm() {
		txtViTri.clear();
		comBoxLoaiBan.getSelectionModel().clearSelection();
		tblBan.getSelectionModel().clearSelection();

		btnSua.setDisable(true);
		btnXoa.setDisable(true);

		generateId();
	}

	private boolean validate() {
		if (txtViTri.getText().isBlank()) {
			AlertUtil.showAlert("Lỗi", "Vị trí không được rỗng", Alert.AlertType.WARNING);
			return false;
		}
		if (comBoxLoaiBan.getValue() == null) {
			AlertUtil.showAlert("Lỗi", "Vui lòng chọn loại bàn", Alert.AlertType.WARNING);
			return false;
		}
		return true;
	}

}
