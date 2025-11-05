package controller.KhuyenMai;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import config.RestaurantApplication;
import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class KhuyenMai_Controller {

    @FXML private BorderPane borderPane;

    @FXML private Button btnThemKM;
    @FXML private Button btnSuaKM;
    @FXML private Button btnXoaKM;

    @FXML private TableColumn<KhuyenMai, String> colMaKM;
    @FXML private TableColumn<KhuyenMai, String> colTenKM;
    @FXML private TableColumn<KhuyenMai, String> colloaiKM;
    @FXML private TableColumn<KhuyenMai, String> colSanPhamKM;
    @FXML private TableColumn<KhuyenMai, Date> colNgayBatDau;
    @FXML private TableColumn<KhuyenMai, Date> colNgayKetThuc;
    @FXML private TableColumn<KhuyenMai, Integer> colPhanTramGiamGia;

    @FXML private TableView<KhuyenMai> tblKM;
    @FXML private TextField txtTimKiem;

    @FXML private TextField txtMaKM, txtTenKM, txtSanPhamKM;
    @FXML private ComboBox<String> comBoxLoaiKM;
    @FXML private ComboBox<Integer> comBoxPhanTram;
    @FXML private DatePicker dpNgayBatDau, dpNgayKetThuc;

    private ObservableList<KhuyenMai> danhSachKhuyenMai = FXCollections.observableArrayList();

    private final KhuyenMai_DAO khuyenMaiDAO = RestaurantApplication.getInstance()
            .getDatabaseContext()
            .newEntity_DAO(KhuyenMai_DAO.class);

    @FXML
    private void initialize() {
        setValueTable();
        setComboBoxValue();
        loadData();
        timKiem();
        btnSuaKM.setDisable(true);
        btnXoaKM.setDisable(true);
    }

    private void setComboBoxValue() {
        comBoxLoaiKM.setItems(FXCollections.observableArrayList(
        		"Ưu đãi cho khách hàng VIP",
                "Khuyến mãi món ăn",
                "Khuyến mãi trên tổng hóa đơn"
                ));
        comBoxPhanTram.setItems(FXCollections.observableArrayList(5,10,15,20,25,30,40,50));
    }

    @FXML
    private void controller(ActionEvent event) {
        if (event.getSource() == btnThemKM) them();
        if (event.getSource() == btnSuaKM) sua();
        if (event.getSource() == btnXoaKM) xoa();
    }

    @FXML
    void mouseClicked(MouseEvent event) {
        KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
        if (km != null) {
            btnSuaKM.setDisable(false);
            btnXoaKM.setDisable(false);
            fillForm(km);
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) huyChonDong();
    }

    private void them() {
        try {
        	// ✅ Tự tạo Mã KM
            String max = khuyenMaiDAO.getMaxMaKM();
            String maMoi = (max == null) ? "KM001" :
                    "KM" + String.format("%03d", Integer.parseInt(max.substring(2)) + 1);

            KhuyenMai km = new KhuyenMai(
                    maMoi,
                    txtTenKM.getText().trim(),
                    comBoxLoaiKM.getValue(),
                    txtSanPhamKM.getText().trim(),
                    Date.valueOf(dpNgayBatDau.getValue()),
                    Date.valueOf(dpNgayKetThuc.getValue()),
                    comBoxPhanTram.getValue()
            );

            if (khuyenMaiDAO.them(km)) {
                loadData();
                showAlert("Thành công", "Thêm khuyến mãi thành công!", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ và đúng thông tin!", Alert.AlertType.ERROR);
        }
    }

    private void sua() {
        KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
        if (km == null) return;

        try {
            km.setTenKM(txtTenKM.getText().trim());
            km.setLoaiKM(comBoxLoaiKM.getValue());
            km.setSanPhamKM(txtSanPhamKM.getText().trim());
            km.setNgayBatDau(Date.valueOf(dpNgayBatDau.getValue()));
            km.setNgayKetThuc(Date.valueOf(dpNgayKetThuc.getValue()));
            km.setPhanTramGiamGia(comBoxPhanTram.getValue());

            if (khuyenMaiDAO.sua(km)) {
                loadData();
                showAlert("Thành công", "Cập nhật thành công!", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            showAlert("Lỗi", "Dữ liệu không hợp lệ!", Alert.AlertType.ERROR);
        }
    }

    private void xoa() {
        KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
        if (km == null) return;

        Optional<ButtonType> confirm = showAlertConfirm("Bạn có chắc muốn xóa?");
        if (confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {
            if (khuyenMaiDAO.xoa(km.getMaKM())) {
                loadData();
                showAlert("Thành công", "Xóa thành công!", Alert.AlertType.INFORMATION);
            }
        }
    }

    private void fillForm(KhuyenMai km) {
        txtMaKM.setText(km.getMaKM());
        txtTenKM.setText(km.getTenKM());
        txtSanPhamKM.setText(km.getSanPhamKM());
        comBoxLoaiKM.setValue(km.getLoaiKM());
        comBoxPhanTram.setValue(km.getPhanTramGiamGia());
        dpNgayBatDau.setValue(km.getNgayBatDau().toLocalDate());
        dpNgayKetThuc.setValue(km.getNgayKetThuc().toLocalDate());
    }

    private void huyChonDong() {
        tblKM.getSelectionModel().clearSelection();
        btnSuaKM.setDisable(true);
        btnXoaKM.setDisable(true);
    }

    private void loadData() {
        List<KhuyenMai> list = khuyenMaiDAO.getDanhSach(KhuyenMai.class, new HashMap<>());
        danhSachKhuyenMai.setAll(list);
        tblKM.setItems(danhSachKhuyenMai);
    }

    private void setValueTable() {
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colloaiKM.setCellValueFactory(new PropertyValueFactory<>("loaiKM"));
        colSanPhamKM.setCellValueFactory(new PropertyValueFactory<>("sanPhamKM"));
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colPhanTramGiamGia.setCellValueFactory(new PropertyValueFactory<>("phanTramGiamGia"));
    }

    private void timKiem() {
        FilteredList<KhuyenMai> filtered = new FilteredList<>(danhSachKhuyenMai, p -> true);

        txtTimKiem.textProperty().addListener((obs, oldValue, newValue) ->
                filtered.setPredicate(km -> km.getMaKM().toLowerCase().contains(newValue.toLowerCase())
                        || km.getTenKM().toLowerCase().contains(newValue.toLowerCase())
                        || km.getSanPhamKM().toLowerCase().contains(newValue.toLowerCase()))
        );

        tblKM.setItems(filtered);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }

    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(content);
        alert.getButtonTypes().setAll(
                new ButtonType("Có", ButtonBar.ButtonData.YES),
                new ButtonType("Không", ButtonBar.ButtonData.NO)
        );
        return alert.showAndWait();
    }
}
       