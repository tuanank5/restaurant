package controller.Menu;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.EventObject;
import java.util.Optional;

import config.RestaurantApplication;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
		NhanVien_Controller nhanVienController = readyUI("Dashboard/DashboardNV").getController();
	}
	@FXML
    void btnCaLam(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/KetCa/KetCa.fxml"));
			Pane ketCaPane = loader.load();
			// Hiển thị giao diện Khách Hàng vào vùng center của BorderPane
			borderPane.setCenter(ketCaPane);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.print("Không thể tải giao diện Đặt bàn"+ e.getMessage());
		}
    }

    @FXML
    void btnDatBan(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/DatBan/DatBan.fxml"));
			Pane datBanPane = loader.load();
			// Hiển thị giao diện Khách Hàng vào vùng center của BorderPane
			borderPane.setCenter(datBanPane);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.print("Không thể tải giao diện Đặt bàn"+ e.getMessage());
		}
    }

    @FXML
    void btnDoiHuyBan(ActionEvent event) {

    }

    @FXML
    void btnDoiMatKhau(ActionEvent event) {

    }

    @FXML
    void btnHoaDon(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/HoaDon/HoaDon.fxml"));
			Pane hoaDonPane = loader.load();
			// Hiển thị giao diện Khách Hàng vào vùng center của BorderPane
			borderPane.setCenter(hoaDonPane);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.print("Không thể tải giao diện Hóa đơn"+ e.getMessage());
		}
    }

    @FXML
    void btnKhachHang(ActionEvent event) {
    	try {
	        // Đường dẫn FXML chính xác theo cấu trúc thư mục của bạn
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/KhachHang/KhachHang.fxml"));
	        Pane khachHangPane = loader.load();
	        // Hiển thị giao diện Khách Hàng vào vùng center của BorderPane
	        borderPane.setCenter(khachHangPane);
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Không thể tải giao diện KhachHang" + e.getMessage());
	    }
    }

    @FXML
    void btnMonAn(ActionEvent event) {
    	readyUI("MonAn/MonAn");
    }
}
