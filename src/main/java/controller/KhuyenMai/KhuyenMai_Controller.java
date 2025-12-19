package controller.KhuyenMai;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Service.EmailService;
import config.RestaurantApplication;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.impl.KhachHang_DAOlmpl;
import entity.KhachHang;
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
import util.AutoIDUitl;

public class KhuyenMai_Controller {

    @FXML private BorderPane borderPane;

    @FXML private Button btnThemKM;
    @FXML private Button btnSuaKM;
    @FXML private Button btnXoaKM;

    @FXML private TableColumn<KhuyenMai, String> colMaKM;
    @FXML private TableColumn<KhuyenMai, String> colTenKM;
    @FXML private TableColumn<KhuyenMai, String> colloaiKM;
    @FXML private TableColumn<KhuyenMai, Date> colNgayBatDau;
    @FXML private TableColumn<KhuyenMai, Date> colNgayKetThuc;
    @FXML private TableColumn<KhuyenMai, Integer> colPhanTramGiamGia;

    @FXML private TableView<KhuyenMai> tblKM;
    @FXML private TextField txtTimKiem;

    @FXML private TextField txtMaKM, txtTenKM;
    @FXML private ComboBox<String> comBoxLoaiKM;
    @FXML private ComboBox<Integer> comBoxPhanTram;
    @FXML private DatePicker dpNgayBatDau, dpNgayKetThuc;

    private ObservableList<KhuyenMai> danhSachKhuyenMai = FXCollections.observableArrayList();

    private final KhuyenMai_DAO khuyenMaiDAO = RestaurantApplication.getInstance()
            .getDatabaseContext()
            .newEntity_DAO(KhuyenMai_DAO.class);
    
    private final KhachHang_DAO khachHangDAO = RestaurantApplication.getInstance()
    		.getDatabaseContext()
    		.newEntity_DAO(KhachHang_DAO.class);
    
    //private final SmsService smsService = new SmsService();
    
    @FXML
    private void initialize() {
        setValueTable();
        setComboBoxValue();
        loadData();
        timKiem();
        hienThiMaKMMoi();
        btnSuaKM.setDisable(true);
        btnXoaKM.setDisable(true);
        tblKM.setOnMouseClicked(event -> {
            KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
            if (km != null) {
                fillForm(km); // Điền dữ liệu lên form
                btnSuaKM.setDisable(false);
                btnXoaKM.setDisable(false);
            }
        });

    }

    private void setComboBoxValue() {
        comBoxLoaiKM.setItems(FXCollections.observableArrayList(
        		"Ưu đãi cho khách hàng Kim Cương",
                "Khuyến mãi trên tổng hóa đơn"
                ));
        comBoxPhanTram.setItems(FXCollections.observableArrayList(5,10,15,20,25,30,35,40));
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
    	        fillForm(km); // Gọi phương thức điền form
    	    } else {
    	        // Nếu không còn dòng nào được chọn
    	        btnSuaKM.setDisable(true);
    	        btnXoaKM.setDisable(true);
    	        clearForm();
    	    }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) huyChonDong();
    }

    private void them() {
        if (!validateKhuyenMai()) return;
        try {
            KhuyenMai km = new KhuyenMai(
                    txtMaKM.getText(),
                    txtTenKM.getText().trim(),
                    comBoxLoaiKM.getValue(),
                    Date.valueOf(dpNgayBatDau.getValue()),
                    Date.valueOf(dpNgayKetThuc.getValue()),
                    comBoxPhanTram.getValue()
            );

            if (khuyenMaiDAO.them(km)) {
                loadData();
                showAlert("Thành công", "Thêm khuyến mãi thành công!", Alert.AlertType.INFORMATION);
                guiEmailThongBaoKM(km);
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể thêm khuyến mãi!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    
    private void guiEmailThongBaoKM(KhuyenMai km) {
        List<KhachHang> dsKH = khachHangDAO.getDanhSach(KhachHang.class, new HashMap<>());
        String subject = "🎉 Khuyến mãi mới: " + km.getTenKM();

        String contentTemplate = "🎁 THÔNG BÁO KHUYẾN MÃI MỚI\n\n"
                + "Tên khuyến mãi: " + km.getTenKM() + "\n"
                + "Loại KM: " + km.getLoaiKM() + "\n"
                + "Thời gian: " + km.getNgayBatDau() + " → " + km.getNgayKetThuc() + "\n"
                + "Mức giảm giá: " + km.getPhanTramGiamGia() + "%\n\n"
                + "👉 Hãy đến nhà hàng để nhận ưu đãi nhé!\n";
        // Tạo thread pool với 15 luồng (có thể thay đổi số luồng)
        ExecutorService executor = Executors.newFixedThreadPool(15);
        
        for (KhachHang kh : dsKH) {
            if (kh.getEmail() == null || kh.getEmail().trim().isEmpty()) {
                System.out.println("❌ Bỏ qua KH không có email: " + kh.getTenKH());
                continue;
            }
            String emailContent = "Xin chào " + kh.getTenKH() + ",\n\n" + contentTemplate;
            // Submit task gửi email vào thread pool
            executor.submit(() -> {
                try {
                    EmailService.sendEmail(kh.getEmail(), subject, emailContent);
                    System.out.println("Đã gửi email đến: " + kh.getEmail());
                } catch (Exception e) {
                    System.err.println("Gửi email thất bại: " + kh.getEmail());
                    e.printStackTrace();
                }
            });
        }
        // Đóng executor sau khi submit xong
        executor.shutdown();
        System.out.println("Tất cả email đã được submit để gửi.");
    }
 
    private void sua() {
        KhuyenMai km = tblKM.getSelectionModel().getSelectedItem();
        if (km == null) return;
        if (dangDienRa(km)) {
            showAlert("Lỗi", "Khuyến mãi đang diễn ra, không thể cập nhật!", Alert.AlertType.ERROR);
            return;
        }
        if (!validateKhuyenMai()) return;
        try {
            km.setTenKM(txtTenKM.getText().trim());
            km.setLoaiKM(comBoxLoaiKM.getValue());
            km.setNgayBatDau(Date.valueOf(dpNgayBatDau.getValue()));
            km.setNgayKetThuc(Date.valueOf(dpNgayKetThuc.getValue()));
            km.setPhanTramGiamGia(comBoxPhanTram.getValue());

            if (khuyenMaiDAO.sua(km)) {
                loadData();
                showAlert("Thành công", "Cập nhật thành công!", Alert.AlertType.INFORMATION);
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Dữ liệu không hợp lệ!", Alert.AlertType.ERROR);
            e.printStackTrace();
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
        comBoxLoaiKM.setValue(km.getLoaiKM());
        comBoxPhanTram.setValue(km.getPhanTramGiamGia());
        dpNgayBatDau.setValue(km.getNgayBatDau().toLocalDate());
        dpNgayKetThuc.setValue(km.getNgayKetThuc().toLocalDate());
    }

    private void huyChonDong() {
        tblKM.getSelectionModel().clearSelection();
        btnSuaKM.setDisable(true);
        btnXoaKM.setDisable(true);
        clearForm();
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
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colPhanTramGiamGia.setCellValueFactory(new PropertyValueFactory<>("phanTramGiamGia"));
    }

    private void timKiem() {
        FilteredList<KhuyenMai> filtered = new FilteredList<>(danhSachKhuyenMai, p -> true);
        txtTimKiem.textProperty().addListener((obs, oldValue, newValue) -> {
            String keyword = newValue.toLowerCase();
            filtered.setPredicate(km -> km.getMaKM().toLowerCase().contains(keyword)
                 || km.getTenKM().toLowerCase().contains(keyword)
                 || km.getLoaiKM().toLowerCase().contains(keyword)
            );
        });
        tblKM.setItems(filtered);
    }
    
    private void clearForm() {
        txtMaKM.clear();
        txtTenKM.clear();
        comBoxLoaiKM.getSelectionModel().clearSelection();
        comBoxPhanTram.getSelectionModel().clearSelection();
        dpNgayBatDau.setValue(null);
        dpNgayKetThuc.setValue(null);
        hienThiMaKMMoi();
    }
    
    private boolean validateKhuyenMai() {
        if (txtTenKM.getText().isBlank()
                || comBoxLoaiKM.getValue() == null
                || dpNgayBatDau.getValue() == null
                || dpNgayKetThuc.getValue() == null
                || comBoxPhanTram.getValue() == null) {

            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.ERROR);
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        Date start = Date.valueOf(dpNgayBatDau.getValue());
        Date end   = Date.valueOf(dpNgayKetThuc.getValue());
        //Ngày bắt đầu < hôm nay
        if (start.before(today)) {
            showAlert("Lỗi", "Ngày bắt đầu không được nhỏ hơn ngày hiện tại!", Alert.AlertType.ERROR);
            return false;
        }

        //Ngày kết thúc <= ngày bắt đầu
        if (!end.after(start)) {
            showAlert("Lỗi", "Ngày kết thúc phải lớn hơn ngày bắt đầu!", Alert.AlertType.ERROR);
            return false;
        }

        //Trùng khuyến mãi
        if (khuyenMaiDaTonTai()) {
            showAlert("Lỗi", "Khuyến mãi đã tồn tại hoặc trùng thời gian!", Alert.AlertType.ERROR);
            return false;
        }
        //Giới hạn %
        int giam = comBoxPhanTram.getValue();
        if (comBoxLoaiKM.getValue().contains("tổng") && giam > 30) {
            showAlert("Lỗi", "Khuyến mãi tổng hóa đơn tối đa 30%!", Alert.AlertType.ERROR);
            return false;
        }

        if (comBoxLoaiKM.getValue().contains("Kim Cương") && giam > 40) {
            showAlert("Lỗi", "Ưu đãi Kim Cương tối đa 40%!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
    
    private void hienThiMaKMMoi() {
        String maMoi = AutoIDUitl.sinhMaKhuyenMai();
        txtMaKM.setText(maMoi);
        txtMaKM.setEditable(false);
    }
    
    private boolean dangDienRa(KhuyenMai km) {
        Date today = new Date(System.currentTimeMillis());
        return !today.before(km.getNgayBatDau()) && !today.after(km.getNgayKetThuc());
    }
    
    private boolean khuyenMaiDaTonTai() {
        List<KhuyenMai> ds = khuyenMaiDAO.getDanhSach(KhuyenMai.class, new HashMap<>());
        Date start = Date.valueOf(dpNgayBatDau.getValue());
        Date end   = Date.valueOf(dpNgayKetThuc.getValue());
        for (KhuyenMai km : ds) {
            boolean trungTenLoai =
                    km.getTenKM().equalsIgnoreCase(txtTenKM.getText().trim())
                    && km.getLoaiKM().equalsIgnoreCase(comBoxLoaiKM.getValue());
            boolean giaoThoiGian =
                    !(end.before(km.getNgayBatDau()) || start.after(km.getNgayKetThuc()));

            if (trungTenLoai && giaoThoiGian) {
                return true;
            }
        }
        return false;
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
