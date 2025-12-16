package controller.DatBan;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import controller.Menu.MenuNV_Controller;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AThuTien_Controller {

	@FXML
    private Button btn1;

    @FXML
    private Button btn10;

    @FXML
    private Button btn100;

    @FXML
    private Button btn2;

    @FXML
    private Button btn20;

    @FXML
    private Button btn200;

    @FXML
    private Button btn5;

    @FXML
    private Button btn50;

    @FXML
    private Button btn500;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnIn;

    @FXML
    private Label lblTienCanThu;

    @FXML
    private Button btnLamMoi;

    @FXML
    private TextField txtTienKH;
    
    @FXML
    private Button btnGoiY1;

    @FXML
    private Button btnGoiY2;

    @FXML
    private Button btnGoiY3;
    
    private double tongThanhTien = AThanhToan_Controller.aTT.tongSauVAT;
    
    private double tienKH = 0;
	
	@FXML
    public void initialize() {
    	lblTienCanThu.setText(formatTienVN(tongThanhTien));
    	txtTienKH.setText(formatTienVN(tienKH));
    	
    	btn500.setOnAction(e -> hienTien(500000));
    	btn200.setOnAction(e -> hienTien(200000));
    	btn100.setOnAction(e -> hienTien(100000));
    	btn50.setOnAction(e -> hienTien(50000));
    	btn20.setOnAction(e -> hienTien(20000));
    	btn10.setOnAction(e -> hienTien(10000));
    	btn5.setOnAction(e -> hienTien(5000));
    	btn2.setOnAction(e -> hienTien(2000));
    	btn1.setOnAction(e -> hienTien(1000));
    	
    	hienThiGoiY();
	}
    
	private void hienThiGoiY() {

	    btnGoiY1.setVisible(true);
	    btnGoiY2.setVisible(true);
	    btnGoiY3.setVisible(true);

	    double tong = tongThanhTien;

	    double goiY1 = tong;
	    Double goiY2 = null;
	    Double goiY3 = null;

	    // Gợi ý +1k
	    goiY2 = tong + 1000;

	    // Làm tròn lên mệnh giá lớn
	    if (tong % 100000 != 0) {
	        goiY3 = lamTronLen(tong, 100000);
	    } else if (tong % 10000 != 0) {
	        goiY3 = lamTronLen(tong, 10000);
	    } else if (tong % 5000 != 0) {
	        goiY3 = lamTronLen(tong, 5000);
	    }

	    // Tránh trùng
	    if (goiY2 != null && goiY2.equals(goiY3)) {
	        goiY3 = null;
	    }

	    // ===== FIX LỖI LAMBDA =====
	    final double fGoiY1 = goiY1;
	    final Double fGoiY2 = goiY2;
	    final Double fGoiY3 = goiY3;

	    // Set text + action
	    btnGoiY1.setText(formatNum(fGoiY1 + ""));
	    btnGoiY1.setOnAction(e -> setTienKH(fGoiY1));

	    if (fGoiY2 != null) {
	        btnGoiY2.setText(formatNum(fGoiY2 + ""));
	        btnGoiY2.setOnAction(e -> setTienKH(fGoiY2));
	    } else {
	        btnGoiY2.setVisible(false);
	    }

	    if (fGoiY3 != null) {
	        btnGoiY3.setText(formatNum(fGoiY3 + ""));
	        btnGoiY3.setOnAction(e -> setTienKH(fGoiY3));
	    } else {
	        btnGoiY3.setVisible(false);
	    }
	}
	
	private void setTienKH(double tien) {
	    tienKH = tien;
	    txtTienKH.setText(formatTienVN(tienKH));
	}
	
	private double lamTronLen(double soTien, int menhGia) {
	    return Math.ceil(soTien / menhGia) * menhGia;
	}

	private void hienTien(int tien) {
    	tienKH += tien;
    	txtTienKH.setText(formatTienVN(tienKH));
	}
    
	private String formatNum(String value) {
	    if (value.isEmpty()) return "";
	    
	    double number = Double.parseDouble(value);
	    return String.format("%,.0f", number).replace(',', '.'); // Định dạng và thay thế dấu phẩy bằng dấu chấm
	}
	
	@FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnLamMoi) {
        	txtTienKH.setText(formatTienVN(0));
        	tienKH = 0;
        } else if (source == btnHuy) {
        	Stage stage = (Stage) btnHuy.getScene().getWindow();
            stage.close();
        } else if (source == btnIn) {
            
        }
    }
    
    private String formatTienVN(double tien) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
	    nf.setGroupingUsed(true);
	    return nf.format(tien) + " VND";
	}
}
