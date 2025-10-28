package controller.KhuyenMai;

import java.sql.Date;

import entity.KhuyenMai;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class KhuyenMai_Controller {
	  @FXML
	    void btnCapNhatKM(ActionEvent event) {

	    }

	    @FXML
	    void btnThemKM(ActionEvent event) {

	    }
	    @FXML
	    private TableColumn<KhuyenMai,String> tblLoaiKM;

	    @FXML
	    private TableColumn<KhuyenMai, String> tblMaKM;

	    @FXML
	    private TableColumn<KhuyenMai, Date> tblNgayBatDauKM;

	    @FXML
	    private TableColumn<KhuyenMai, Date> tblNgayKetThucKM;

	    @FXML
	    private TableColumn<KhuyenMai, String> tblPhanTramGiamGia;

	    @FXML
	    private TableColumn<KhuyenMai, String> tblSanPhamKM;

	    @FXML
	    private TableColumn<KhuyenMai, String> tblTenKM;

	    @FXML
	    private TextField txtTimKiem;
	    
	    @FXML
	    void controller(ActionEvent event) {

	    }

}