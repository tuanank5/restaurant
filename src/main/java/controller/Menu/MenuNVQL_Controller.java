package controller.Menu;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import config.RestaurantApplication;
import controller.Dashboard.DashboardNVQLScroll_Controller;
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

public class MenuNVQL_Controller {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtThongTin;
    
    public static MenuNVQL_Controller instance;
    public static TaiKhoan taiKhoan;

    @FXML
    void btnDangXuat(ActionEvent event) {

        Optional<ButtonType> buttonType = showAlertConfirm("Bạn có chắc muốn đăng xuất?");
        if (!buttonType.isPresent() 
            || buttonType.get().getButtonData() != ButtonBar.ButtonData.YES) {
            return;
        }

        try {
            Date dateNow = Date.valueOf(LocalDate.now());
            taiKhoan.setNgayDangXuat(dateNow);

            RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(TaiKhoan_DAO.class)
                    .capNhat(taiKhoan);
            
            Stage currentStage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            currentStage.close();

            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/view/fxml/Login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        instance = this;
    }
    
    public static MenuNVQL_Controller getInstance() {
        return instance;
    }
    
    @FXML
    void btnDashboard(ActionEvent event) {
        readyUI("Dashboard/DashboardNVQLScroll");
    }

    @FXML
    void btnDoiMatKhau(ActionEvent event) {
    	readyUI("DoiMatKhauQL");
    }

    @FXML
    void btnKhuyenMai(ActionEvent event) {
        readyUI("KhuyenMai/KhuyenMai");
    }

    @FXML
    void btnNhanVien(ActionEvent event) {
        readyUI("NhanVien/NhanVien");
    }

    @FXML
    void btnTaiKhoan(ActionEvent event) {
        readyUI("TaiKhoan/TaiKhoanTA");
    }

    @FXML
    void btnThongKe(ActionEvent event) {
        readyUI("ThongKe/ThongKe");
    }

    @FXML
    void btnBan(ActionEvent event) {
        readyUI("QuanLyBan/QuanLyBan");
    }

    public FXMLLoader readyUI(String ui) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(
                    getClass().getResource("/view/fxml/" + ui + ".fxml"));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    public void setThongTin(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        String hoTen = taiKhoan.getNhanVien().getTenNV()
                + " - " + taiKhoan.getNhanVien().getMaNV();
        txtThongTin.setText(hoTen);
        dashBoard();
    }

    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);

        ButtonType btnYes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        return alert.showAndWait();
    }
    
    public void setCenterUI(Node node) {
        borderPane.setCenter(node);
    }

    public void dashBoard() {
        DashboardNVQLScroll_Controller controller =
                readyUI("Dashboard/DashboardNVQLScroll").getController();
    }
}
