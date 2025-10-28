package controller.Menu;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import config.RestaurantApplication;
import controller.Dashboard_Controller;
import controller.KhachHang.KhachHang_Controller;
import controller.NhanVien.NhanVien_Controller;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuNVQL_Controller {
	
	public static MenuNVQL_Controller instance;

	private TaiKhoan taiKhoan;
	
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnDoiMatKhau;

    @FXML
    private Button btnKhuyenMai;

    @FXML
    private Button btnNV;

    @FXML
    private Button btnQLBan;

    @FXML
    private Button btnTaiKhoan;

    @FXML
    private Button btnThongKe;

    @FXML
    private TextField txtThongTin;

    @FXML
    void handleKhuyenMaiAction(ActionEvent event) {
        readyUI("KhuyenMai/KhuyenMai");
    }

    @FXML
    public void handleNhanVienAction(ActionEvent event) {
        readyUI("NhanVien/NhanVien");
    }

    @FXML
    private void handleThongKeAction(ActionEvent event) {
        readyUI("ThongKe");
    }

    @FXML
    void handleTaiKhoanAction(ActionEvent event) {
        readyUI("TaiKhoan/TaiKhoan");
    }
	
	public FXMLLoader readyUI(String ui) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(getClass().getResource("/view/fxml/" + ui + ".fxml"));
            root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fxmlLoader;
    }
	
	public void setThongTin(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        String hoTen = taiKhoan.getNhanVien().getTenNV() + " - " + taiKhoan.getNhanVien().getMaNV();
        txtThongTin.setText(hoTen);
        dashBoard();
    }
	
	@FXML
	private void dangXuat(ActionEvent actionEvent) {
        Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc muốn đăng xuất?");
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
            
            // Để Nhân chỉnh lại - load lên bị lỗi
            try {
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
                
                FXMLLoader fxmlLoader = new FXMLLoader(
            			getClass().getResource("/view/fxml/Login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Để Nhân chỉnh lại - load lên bị lỗi
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
	
	public void dashBoard() {
//		Dashboard_Controller dashBoardController = readyUI("Dashboard").getController();
//		dashBoardController.setThongTin(taiKhoan);
		
		NhanVien_Controller nhanVienController = readyUI("NhanVien/NhanVien").getController();
	}
}
