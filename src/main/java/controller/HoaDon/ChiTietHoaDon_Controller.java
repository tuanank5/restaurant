package controller.HoaDon;

import entity.KhachHang;
import entity.NhanVien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ChiTietHoaDon_Controller {
	 @FXML
	    private BorderPane borderPane;

	    @FXML
	    private Button btnTroLai;

	    @FXML
	    private TableView<String> tblDanhSachMon;

	    @FXML
	    private TextField txtMaHoaDon;
	    
	    private String kh;
	    private String nhanVien;
	    private String ban;
	    private String khuyenMai;

	    @FXML
	    void controller(ActionEvent event) {
	    	
	    }
	    
	    public void layThongTin(String kh,String nhanVien,String ban,String khuyenMai){
	    	this.kh = kh;
	    	this.nhanVien = nhanVien;
	    	this.ban = ban;
	    	this.khuyenMai = khuyenMai;
	    }
}
