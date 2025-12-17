package controller.Menu;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import config.RestaurantApplication;
import controller.Dashboard.DashboardNV_Controller;
import dao.Ban_DAO;
import dao.TaiKhoan_DAO;
import entity.Ban;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import entity.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	
	public static HoaDon aBanHienTai_HD;
	public static int getSoLuongKhach() {
		return soLuongKhach;
	}
	public static void setSoLuongKhach(int soLuongKhach) {
		MenuNV_Controller.soLuongKhach = soLuongKhach;
	}
	public static int soLuongKhach = 0;
	
	public static MenuNV_Controller instance;
	public static Map<entity.MonAn, Integer> dsMonAnDangChon;
	public static String tongTienSauVAT;
	public static Ban banDangChon;
	public static DonDatBan DonDatBan;
	public static KhachHang khachHangDangChon;
	public static DonDatBan donDatBanDangDoi;
	public static ObservableList<Ban> danhSachBan = FXCollections.observableArrayList();
	
	// Lưu món ăn tạm theo từng bàn
	public static Map<String, Map<MonAn, Integer>> dsMonTheoBan = new HashMap<>();

	public static TaiKhoan taiKhoan;

	@FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtThongTin;
    
    @FXML
    private void initialize() {
        instance = this; // Gán instance khi FXML được load
       
    }
    public static MenuNV_Controller getInstance() {
        return instance;
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
	public void refreshBanUI() {
		if (banDangChon != null) {
	        for (Ban b : danhSachBan) {
	            if (b.getMaBan().equals(banDangChon.getMaBan())) {
	                b.setTrangThai("Trống");
	                break;
	            }
	        }
	    }
	    // Nếu DatBan đang hiển thị, reload UI để cập nhật trạng thái bàn
	    readyUI("DatBan/DatBan-test");
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
    	readyUI("DatBan/aBanHienTai");
    }

    @FXML
    void btnDatBanTruoc(ActionEvent event) {
    	readyUI("DatBan/DonDatBan");
    }

    @FXML
    void btnDoiMatKhau(ActionEvent event) {
    	readyUI("DoiMatKhauTA");
    }

    @FXML
    void btnHoaDon(ActionEvent event) {
    	readyUI("HoaDon/HoaDonTA");
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
