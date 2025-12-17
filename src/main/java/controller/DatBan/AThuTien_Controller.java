package controller.DatBan;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    
    @FXML
    private Label lblTienCanTra;
    
    private double tongThanhTien = AThanhToan_Controller.aTT.tongSauVAT;
    private double tongThanhTienLamTron = Math.round(tongThanhTien / 1000.0) * 1000;
    
    private int tienKH = 0;
	
	@FXML
    public void initialize() {
    	lblTienCanThu.setText(formatTienVN(tongThanhTienLamTron));
    	txtTienKH.setText(tienKH + "");
    	txtTienKH.textProperty().addListener((observable, oldValue, newValue) -> {
            // Xóa tất cả ký tự không phải số
            String numericValue = newValue.replaceAll("[^\\d]", "");
            
            if (!newValue.equals(numericValue)) {
                showAlert(Alert.AlertType.ERROR, "Số tiền khách đưa phải nhập số!");
            }
            
            // Cập nhật giá trị vào txtTienKH với định dạng
            txtTienKH.setText(numericValue);
            
            // Đặt con trỏ vào cuối TextField
            txtTienKH.positionCaret(txtTienKH.getText().length());
            hienThiTienTra();
        });
    	
    	btn500.setOnAction(e -> handlePlus(500000));
    	btn200.setOnAction(e -> handlePlus(200000));
    	btn100.setOnAction(e -> handlePlus(100000));
    	btn50.setOnAction(e -> handlePlus(50000));
    	btn20.setOnAction(e -> handlePlus(20000));
    	btn10.setOnAction(e -> handlePlus(10000));
    	btn5.setOnAction(e -> handlePlus(5000));
    	btn2.setOnAction(e -> handlePlus(2000));
    	btn1.setOnAction(e -> handlePlus(1000));
    	
    	hienThiGoiY();
    	hienThiTienTra();
	}
    
	private void hienThiTienTra() {
		tienKH = Integer.parseInt(txtTienKH.getText());
		double tienTra = tienKH - tongThanhTienLamTron;
		lblTienCanTra.setText(formatTienVN(tienTra));
	}

	private void hienThiGoiY() {
	    btnGoiY1.setVisible(true);
	    btnGoiY2.setVisible(true);
	    btnGoiY3.setVisible(true);

	    double goiY1 = tongThanhTienLamTron;
	    double goiY2 = tongThanhTienLamTron + 1000;
	    double goiY3;

	    // Làm tròn lên mệnh giá lớn
	    if (tongThanhTienLamTron % 100000 != 0) {
	        goiY3 = Math.ceil(tongThanhTienLamTron / 100000) * 100000;
	    } else if (tongThanhTienLamTron % 10000 != 0) {
	        goiY3 = Math.ceil(tongThanhTienLamTron / 10000) * 10000;
	    } else if (tongThanhTienLamTron % 5000 != 0) {
	        goiY3 = Math.ceil(tongThanhTienLamTron / 5000) * 5000;
	    } else {
	        goiY3 = 0;
	    }

	    // Set text + action
	    btnGoiY1.setText(formatNum(goiY1 + ""));
	    btnGoiY1.setOnAction(e -> setTienKH(goiY1));

	    if (goiY2 != 0) {
	        btnGoiY2.setText(formatNum(goiY2 + ""));
	        btnGoiY2.setOnAction(e -> setTienKH(goiY2));
	    } else {
	        btnGoiY2.setVisible(false);
	    }

	    if (goiY3 != 0 && goiY3 != goiY2) {
	        btnGoiY3.setText(formatNum(goiY3 + ""));
	        btnGoiY3.setOnAction(e -> setTienKH(goiY3));
	    } else {
	        btnGoiY3.setVisible(false);
	    }
	}
	
	private String formatNum(String value) {
	    if (value.isEmpty()) return "";
	    
	    double number = Double.parseDouble(value);
	    return String.format("%,.0f", number).replace(',', '.');
	}
	
	private void setTienKH(double tien) {
	    tienKH = (int) tien;
	    txtTienKH.setText(tienKH + "");
	}

	private void handlePlus(int tien) {
		tienKH = Integer.parseInt(txtTienKH.getText());
    	tienKH += tien;
    	txtTienKH.setText(tienKH + "");
	}
	
	@FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnLamMoi) {
        	tienKH = 0;
        	txtTienKH.setText(tienKH + "");
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
    
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
