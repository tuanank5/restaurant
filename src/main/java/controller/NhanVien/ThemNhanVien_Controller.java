package controller.NhanVien;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import dao.TaiKhoan_DAO;
import dao.impl.TaiKhoan_DAOImpl;
import entity.TaiKhoan;
import Service.EmailService;

import config.DatabaseContext;
import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import controller.Menu.MenuNV_Controller;
import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.AutoIDUitl;
import util.EmployeeCodeGeneratorUtil;

public class ThemNhanVien_Controller implements Initializable{
	@FXML
    private Button btnHuy;

    @FXML
    private Button btnLuu;

    @FXML
    private TextField txtMaNV;

    @FXML
    private Button btnQuayLai;

    @FXML
    private ComboBox<String> cmbChucVu;
    
    @FXML
    private Label lblDanhSachNhanVien;

    @FXML
    private ComboBox<String> cmbGioiTinh;

    @FXML
    private TableColumn<NhanVien, String> tblChucVu;

    @FXML
    private TableColumn<NhanVien, String> tblDiaChi;

    @FXML
    private TableColumn<NhanVien, String> tblEmail;

    @FXML
    private TableColumn<NhanVien, String> tblGioiTinh;

    @FXML
    private TableColumn<NhanVien, String> tblMaNV;

    @FXML
    private TableColumn<NhanVien, Date> tblNamSinh;
    
    @FXML
    private DatePicker txtNgayVaoLam;

    @FXML
    private TableColumn<NhanVien, String> tblTenNV;

    @FXML
    private TableView<NhanVien> tblThemNV;

    @FXML
    private TableColumn<NhanVien, String> tblTrangThai;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtNamSinh;

    @FXML
    private TextField txtTenNV;
    
    private NhanVien nhanVien;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadData();
		hienThiMaNhanVienMoi();
	}
    @FXML
    void controller(ActionEvent event) {
    	Object src = event.getSource();
    	if(src == btnQuayLai) {
//    		if(!lblDanhSachNhanVien.getText().equalsIgnoreCase("Danh Sách Khách Hàng")) {
//    			//xacNhanLuu("DatBan/DatBan");
//    			MenuNV_Controller.instance.readyUI("NhanVien/NhanVien");
//    		}else {
//    			MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
//    		}
    	}else if(src ==  btnLuu) {
    		luuLai();
    	} else if(src == btnHuy) {
    		resetAllField();
    	}
    }
    
    private NhanVien getNhanVienNew() {
        String tenNV = txtTenNV.getText().trim();
        String email = txtEmail.getText().trim();
        LocalDate namSinh = txtNamSinh.getValue();
        String diaChi = txtDiaChi.getText().trim();
        String gioiTinhStr = cmbGioiTinh.getValue();
        String chucVu = cmbChucVu.getValue();
        LocalDate ngayVaoLam = txtNgayVaoLam.getValue();

        DatabaseContext db = RestaurantApplication
        		.getInstance()
        		.getDatabaseContext();

        if (tenNV.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập tên nhân viên!", Alert.AlertType.WARNING);
            txtTenNV.requestFocus();
            return null;
        }

        if (email.isEmpty() ||
            !email.matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
            showAlert("Cảnh báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return null;
        }

        Map<String, Object> filter = new HashMap<>();
        filter.put("email", email);
        if (!db.newEntity_DAO(NhanVien_DAO.class)
                .getDanhSach(NhanVien.class, filter).isEmpty()) {
            showAlert("Cảnh báo", "Email đã tồn tại!", Alert.AlertType.WARNING);
            return null;
        }

        if (namSinh == null || Period.between(namSinh, LocalDate.now()).getYears() < 18) {
            showAlert("Cảnh báo", "Nhân viên phải đủ 18 tuổi!", Alert.AlertType.WARNING);
            return null;
        }

        if (diaChi.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
            return null;
        }

        if (gioiTinhStr == null || chucVu == null) {
            showAlert("Cảnh báo", "Vui lòng chọn giới tính và chức vụ!", Alert.AlertType.WARNING);
            return null;
        }
        
        if (ngayVaoLam == null) {
            showAlert("Cảnh báo", "Vui lòng chọn ngày vào làm!", Alert.AlertType.WARNING);
            return null;
        }

        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText());
        nv.setTenNV(tenNV);
        nv.setNamSinh(Date.valueOf(namSinh));
        nv.setGioiTinh(gioiTinhStr.equals("Nữ"));
        nv.setEmail(email);
        nv.setDiaChi(diaChi);
        nv.setChucVu(chucVu);
        nv.setTrangThai(true);
        nv.setNgayVaoLam(Date.valueOf(ngayVaoLam));
        return nv;
    }
    
    public void luuLai(){
        NhanVien nvNew = getNhanVienNew();
        try {
            if (nvNew != null) {
                DatabaseContext db = RestaurantApplication.getInstance().getDatabaseContext();

                boolean check = db
                        .newEntity_DAO(NhanVien_DAO.class)
                        .them(nvNew);

                if (check) {
                    TaiKhoan_DAO tkDAO = new TaiKhoan_DAOImpl();

                    String chucVu = nvNew.getChucVu();
                    int soThuTu = tkDAO.demSoTaiKhoanTheoChucVu(chucVu) + 1;

                    String username;
                    String password;

                    if (chucVu.equalsIgnoreCase("Quản lý")) {
                        username = String.format("QL%04d", soThuTu);
                        password = "ql_pass" + String.format("%02d", soThuTu);
                    } else {
                        username = String.format("NV%04d", soThuTu);
                        password = "nv_pass" + String.format("%02d", soThuTu);
                    }

                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTaiKhoan(AutoIDUitl.sinhMaTaiKhoan());
                    tk.setTenTaiKhoan(username);
                    tk.setMatKhau(password);
                    tk.setNhanVien(nvNew);
                    tkDAO.themTaiKhoan(tk);

                    String noiDungMail =
                            "Chào " + nvNew.getTenNV() + ",\n\n"
                            + "Tài khoản đăng nhập hệ thống của bạn:\n"
                            + "Tên đăng nhập: " + username + "\n"
                            + "Mật khẩu: " + password + "\n\n"
                            + "Vui lòng đổi mật khẩu sau khi đăng nhập.";

                    EmailService.sendEmail(nvNew.getEmail(),"Thông tin tài khoản nhân viên", noiDungMail);
                    showAlert("Thông báo",
                            "Thêm nhân viên thành công!\nTài khoản đã được gửi qua email.", Alert.AlertType.INFORMATION);

                    this.nhanVien = nvNew;
                    if (!lblDanhSachNhanVien.getText().equalsIgnoreCase("Danh Sách Nhân Viên")) {
                        MenuNVQL_Controller.instance.readyUI("NhanVien/NhanVien");
                    }
                    resetAllField();
                } else {
                    showAlert("Thông báo", "Thêm nhân viên thất bại!", Alert.AlertType.WARNING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi thêm nhân viên!", Alert.AlertType.ERROR);
        }
    }
    
    private void hienThiMaNhanVienMoi() {
        String maNV = util.AutoIDUitl.sinhMaNhanVien();
        txtMaNV.setText(maNV);
        txtMaNV.setEditable(false);
    }

    
    private void resetAllField() {
        txtTenNV.setText("");
        txtEmail.setText("");
        txtNamSinh.setValue(null);
        txtDiaChi.setText("");
        cmbGioiTinh.getSelectionModel().selectFirst();
        cmbChucVu.getSelectionModel().selectFirst();
    }
    
    private void loadData() {
        cmbGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        cmbGioiTinh.getSelectionModel().selectFirst();
        cmbChucVu.setItems(FXCollections.observableArrayList("Nhân viên", "Quản lý"));
        cmbChucVu.getSelectionModel().selectFirst();
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
