package controller.Menu;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import config.RestaurantApplication;
import controller.Dashboard.DashboardNV_Controller;
import dao.TaiKhoan_DAO;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuNV_Controller {
	
	public static MenuNV_Controller instance;
	
	private TaiKhoan taiKhoan;
	
	@FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtThongTin;
    
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
	private void btnDangXuat(ActionEvent event) {
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
            
            // Để chỉnh lại - load lên bị lỗi
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            // Để chỉnh lại - load lên bị lỗi
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
		DashboardNV_Controller dashboardNVController = readyUI("Dashboard/DashboardNV").getController();
	}
	
	@FXML
    void btnDashboard(ActionEvent event) {
		
    }
	
	@FXML
    void btnCaLam(ActionEvent event) {
		readyUI("KetCa/KetCa");
    }

    @FXML
    void btnDatBan(ActionEvent event) {
    	readyUI("DatBan/DatBan-test");
    }

    @FXML
    void btnDoiHuyBan(ActionEvent event) {

    }

    @FXML
    void btnDoiMatKhau(ActionEvent event) {

    }

    @FXML
    void btnHoaDon(ActionEvent event) {
    	readyUI("HoaDon/HoaDon");
    }

    @FXML
    void btnKhachHang(ActionEvent event) {
    	readyUI("KhachHang/KhachHang");
    }

    @FXML
    void btnMonAn(ActionEvent event) {
    	readyUI("MonAn/MonAn");
    }
}
