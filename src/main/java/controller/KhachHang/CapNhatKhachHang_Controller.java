package controller.KhachHang;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.HangKhachHang_DAO;
import dao.KhachHang_DAO;
import entity.HangKhachHang;
import entity.KhachHang;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class CapNhatKhachHang_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnHoanTac;

    @FXML
    private Button btnLuuLai;

    @FXML
    private Button btnQuayLai;
    
    @FXML
    private Button btnTaoLaiMK;

    @FXML
    private ComboBox<String> comBoxHangKH;

    @FXML
    private Label lblDanhSachKhachHang;

    @FXML
    private Label lblThongTinChiTiet;

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
    
    private KhachHang khachHang;
    
    private List<HangKhachHang> danhSachHangKhachHangDB;

    @FXML
    public void initialize() {
        Platform.runLater(() -> txtTenKH.requestFocus());
        loadData();
    }
    
    @FXML
    void controller(ActionEvent event) {
    	Object src = event.getSource();
        if (src == btnLuuLai) {
            luuLai();
        } 
        else if (src == btnHoanTac) {
            hoanTac();
        } 
        else if (src == btnTaoLaiMK) {
            xacNhanLuu("KhachHang/ThongTinChiTietKhachHang");
        } else if(src == btnQuayLai) {
        	MenuNV_Controller.instance.readyUI("KhachHang/ThongTinChiTietKhachHang");
        }
    }

    
    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Object source = event.getSource();
        // Khi người dùng click vào label "Danh Sách Khách Hàng"
        if (source == lblDanhSachKhachHang) {
            xacNhanLuu("KhachHang/KhachHang");
        } 
        // Khi người dùng click vào label "Thông Tin Chi Tiết"
        else if (source == lblThongTinChiTiet) {
            xacNhanLuu("KhachHang/ThongTinChiTietKhachHang");
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) {
        Object source = event.getSource();
        if (source == borderPane) {
            // Ctrl + S
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                luuLai();
            }
            // ESC
            else if (event.getCode() == KeyCode.ESCAPE) {
                troLai("KhachHang/ThongTinChiTietKhachHang");
            }
            // Ctrl + Z
            else if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                hoanTac();
            }
        }
    }
    
    //Sự kiện
    private void luuLai() {
        KhachHang khachHangNew = getKhachHangNew();
        if (khachHangNew != null) {
            Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc chắn muốn lưu thay đổi?");
            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
                return;
            }

            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
                boolean check = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(KhachHang_DAO.class)
                        .capNhat(khachHangNew);

                if (check) {
                    showAlert("Thông báo", "Cập nhật thông tin khách hàng thành công!", Alert.AlertType.INFORMATION);
                    this.khachHang = khachHangNew;
                    troLai("KhachHang/ThongTinChiTietKhachHang");
                } else {
                    showAlert("Thông báo", "Cập nhật thất bại!", Alert.AlertType.WARNING);
                }
            }
        } else {
            showAlert("Lỗi", "Xảy ra lỗi khi cập nhật thông tin khách hàng!", Alert.AlertType.ERROR);
        }
    }
    
    private void hoanTac(){
        Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc chắn muốn hoàn tác?");
        if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
            return;
        }
        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(KhachHang_DAO.class)
                .capNhat(khachHang);
        if (check) {
            showAlert("Thông báo", "Hoàn tác thành công!", Alert.AlertType.INFORMATION);
            hienThiThongTin(khachHang);
        } else {
            showAlert("Thông báo", "Hoàn tác thất bại!", Alert.AlertType.WARNING);
        }
    }
    
    private void xacNhanLuu(String ui) {
        KhachHang khachHangNew = getKhachHangNew();
        if (khachHangNew != null && !khachHangNew.equals(khachHang)) {
            Optional<ButtonType> buttonType = showAlertConfirm("Bạn có muốn lưu cập nhật?");
            
            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
                troLai(ui);
            } 
            else if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
                //Lưu dữ liệu khách hàng vào CSDL
                boolean check = RestaurantApplication.getInstance()
                        .getDatabaseContext()
                        .newEntity_DAO(KhachHang_DAO.class)
                        .capNhat(khachHangNew);
                if (check) {
                    showAlert("Thông báo", "Cập nhật thành công!", Alert.AlertType.INFORMATION);
                    this.khachHang = khachHangNew;
                    troLai(ui);
                } else {
                    showAlert("Thông báo", "Cập nhật thất bại!", Alert.AlertType.WARNING);
                }
            }
        } else {
            troLai(ui);
        }
    }
    
    private void troLai(String ui) {
        if (khachHang != null && ui.equalsIgnoreCase("KhachHang/ThongTinChiTietKhachHang")) {
            ThongTinKhachHang_Controller thongTinKhachHangController =
                    MenuNV_Controller.instance.readyUI(ui).getController();
            thongTinKhachHangController.setKhachHang(khachHang);
        } else {
        	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
        }
    }
    
    public void hienThiThongTin(KhachHang khachHang) {
        if (khachHang != null) {
            txtMaKH.setText(khachHang.getMaKH());
            txtTenKH.setText(khachHang.getTenKH());
            txtSDT.setText(khachHang.getSdt());
            txtEmail.setText(khachHang.getEmail());
            txtDiaChi.setText(khachHang.getDiaChi());
            txtDiemTichLuy.setText(String.valueOf(khachHang.getDiemTichLuy()));
            HangKhachHang hang = khachHang.getHangKhachHang();
        	if(hang != null) {
        		comBoxHangKH.setValue(hang.getTenHang());
        	}
        }
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        hienThiThongTin(khachHang);
    }
    
    private KhachHang getKhachHangNew() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String diemStr = txtDiemTichLuy.getText().trim();
        String hangKH = comBoxHangKH.getValue();

        //Kiểm tra các trường bắt buộc
        if (tenKH.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập tên khách hàng!", Alert.AlertType.WARNING);
            txtTenKH.requestFocus();
            return null;
        }

        if (sdt.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập số điện thoại!", Alert.AlertType.WARNING);
            txtSDT.requestFocus();
            return null;
        } else {
            String regexPhone = "(84|0)[35789][0-9]{8,9}\\b";
            if (!sdt.matches(regexPhone)) {
                showAlert("Cảnh báo", "Số điện thoại không hợp lệ!", Alert.AlertType.WARNING);
                txtSDT.requestFocus();
                return null;
            }
        }

        if (email.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập email!", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return null;
        } else {
            if (!email.matches("^[a-zA-Z][a-zA-Z0-9._]*@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
                showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
                txtEmail.requestFocus();
                return null;
            }
        }

        if (diaChi.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
            txtDiaChi.requestFocus();
            return null;
        }
        
        int diemTichLuy = 0;
        if (!diemStr.isEmpty()) {
            try {
                diemTichLuy = Integer.parseInt(diemStr);
                if (diemTichLuy < 0) {
                    showAlert("Cảnh báo", "Điểm tích lũy không được âm!", Alert.AlertType.WARNING);
                    txtDiemTichLuy.requestFocus();
                    return null;
                }
            } catch (NumberFormatException e) {
                showAlert("Cảnh báo", "Điểm tích lũy phải là số nguyên!", Alert.AlertType.WARNING);
                txtDiemTichLuy.requestFocus();
                return null;
            }
        }
        if (hangKH == null) {
            showAlert("Cảnh báo", "Vui lòng chọn hạng khách hàng!", Alert.AlertType.WARNING);
            comBoxHangKH.requestFocus();
            return null;
        }
        KhachHang kh = new KhachHang();
        kh.setMaKH(maKH);
        kh.setTenKH(tenKH);
        kh.setSdt(sdt);
        kh.setEmail(email);
        kh.setDiaChi(diaChi);
        kh.setDiemTichLuy(diemTichLuy);
        HangKhachHang hang = danhSachHangKhachHangDB.stream()
                .filter(h -> h.getTenHang().equals(hangKH))
                .findFirst()
                .orElse(null);
        kh.setHangKhachHang(hang);   
        return kh;
    }
    
    private void loadData() {
        Map<String,Object> filter = new HashMap<>();
        danhSachHangKhachHangDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(HangKhachHang_DAO.class)
                .getDanhSach(HangKhachHang.class, filter);
        comBoxHangKH.getItems().clear();
        //comBoxHangKH.getItems().add("Tất cả");
        for(HangKhachHang hang : danhSachHangKhachHangDB) {
            comBoxHangKH.getItems().add(hang.getTenHang());
        }
        comBoxHangKH.getSelectionModel().selectFirst();
    }
   
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);
        ButtonType buttonLuu = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonKhongLuu = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType buttonHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonLuu, buttonKhongLuu, buttonHuy);
        return alert.showAndWait();
    }
}
