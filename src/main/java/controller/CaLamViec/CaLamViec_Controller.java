package controller.CaLamViec;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;

import dao.HoaDon_DAO;
import dao.TaiKhoan_DAO;
import dao.impl.HoaDon_DAOImpl;
import entity.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CaLamViec_Controller {
	
    @FXML
    private JFXButton Btn_1000;

    @FXML
    private JFXButton Btn_1500;

    @FXML
    private Label label_TienPhaiNop;

    @FXML
    private Label label_TongDoanhThu;

    @FXML
    private Label label_TongHoaDon;

    @FXML
    private Label label_TongSoVe;

    @FXML
    private TextField textField_TienNhan;

    @FXML
    private Label text_TenNhanVien;

    @FXML
    private Label text_maNV;

    @FXML
    private ImageView userImage;

    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private TaiKhoan taiKhoan;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    // ================== INITIALIZE ==================
    @FXML
    public void initialize() {
        chonTienMacDinh();
        setupEnterKeyHandler();
        taiKhoan = MenuNV_Controller.taiKhoan;

        if (taiKhoan == null) {
            System.out.println("❌ CaLamViec: taiKhoan = null");
            return;
        }

        System.out.println("✅ CaLamViec nhận NV: " + taiKhoan.getNhanVien().getMaNV());
        capNhatThongTinNhanVien();
        capNhatDoanhThu();
        tinhTienPhaiNop();
    }

    // ================== CẬP NHẬT NHÂN VIÊN ==================
    private void capNhatThongTinNhanVien() {
        if (taiKhoan == null) return;

        text_TenNhanVien.setText(taiKhoan.getNhanVien().getTenNV());
        text_maNV.setText(taiKhoan.getNhanVien().getMaNV());
    }

    // ================== CẬP NHẬT DOANH THU ==================
    private void capNhatDoanhThu() {
        try {
            LocalDate today = LocalDate.now();
            Date homNay = Date.valueOf(today);
            String maNV = taiKhoan.getNhanVien().getMaNV();

            Double doanhThu = hoaDonDAO.getTongDoanhThuTheoNgayVaNhanVien(homNay, maNV);
            Long soHoaDon = hoaDonDAO.countHoaDonTheoNgayVaNhanVien(homNay, maNV);

            label_TongDoanhThu.setText(decimalFormat.format(doanhThu != null ? doanhThu : 0));
            label_TongHoaDon.setText(String.valueOf(soHoaDon != null ? soHoaDon : 0));
        } catch (Exception e) {
            System.err.println("⚠️ Không lấy được doanh thu ca làm");
            label_TongDoanhThu.setText("0");
            label_TongHoaDon.setText("0");
        } finally {
            tinhTienPhaiNop();
        }
    }

    // ================== TÍNH TIỀN PHẢI NỘP ==================
    private void tinhTienPhaiNop() {
        if (taiKhoan == null) return;
        try {
            // Tiền đầu ca
            String text = textField_TienNhan.getText().replaceAll("[^0-9]", "");
            long tienDauCa = text.isEmpty() ? 0 : Long.parseLong(text);

            // Tổng doanh thu trong ngày
            String doanhThuText = label_TongDoanhThu.getText().replaceAll("[^0-9]", "");
            long tongDoanhThu = doanhThuText.isEmpty() ? 0 : Long.parseLong(doanhThuText);

            // Tiền phải nộp
            long tienPhaiNop = tienDauCa + tongDoanhThu;
            label_TienPhaiNop.setText(decimalFormat.format(tienPhaiNop));
        } catch (Exception e) {
            label_TienPhaiNop.setText("0");
            System.err.println("⚠️ Lỗi tính tiền phải nộp");
        }
    }

    // ================== NÚT TIỀN NHANH ==================
    private void chonTienMacDinh() {
        Btn_1000.setOnAction(e -> {
            textField_TienNhan.setText(decimalFormat.format(1000000));
            tinhTienPhaiNop();
        });

        Btn_1500.setOnAction(e -> {
            textField_TienNhan.setText(decimalFormat.format(1500000));
            tinhTienPhaiNop();
        });
    }

    // ================== ENTER KEY ==================
    private void setupEnterKeyHandler() {
        textField_TienNhan.setOnAction(e -> onEnterPressed());
    }

    @FXML
    void btnXacNhan(ActionEvent event) {
        btnDangXuat(event);
    }

    @FXML
    private void btnDangXuat(ActionEvent event) {
        Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc muốn kết ca?");
        if (buttonType.isPresent() && buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
            return;
        }
        if (buttonType.isPresent() && buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
            // Cập nhật lại ngày giờ đăng nhập
            LocalDate localDate = LocalDate.now();
            Date dateNow = Date.valueOf(localDate);
            this.taiKhoan.setNgayDangXuat(dateNow);
            RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(TaiKhoan_DAO.class)
                    .capNhat(taiKhoan);

            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/Login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onEnterPressed() {
        String text = textField_TienNhan.getText().replace(",", "");
        try {
            long value = Long.parseLong(text);
            textField_TienNhan.setText(decimalFormat.format(value));
            tinhTienPhaiNop();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
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