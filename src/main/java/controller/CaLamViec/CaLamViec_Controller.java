
//hihi
package controller.CaLamViec;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;
import controller.Menu.MenuNV_Controller;

import dao.HoaDon_DAO;
import dao.impl.HoaDon_DAOImpl;
import entity.TaiKhoan;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CaLamViec_Controller {

  @FXML
  private JFXButton Btn_1000;

  @FXML
  private JFXButton Btn_1500;

  @FXML
  private Button btnXacNhan;

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

      System.out.println("✅ CaLamViec nhận NV: "
              + taiKhoan.getNhanVien().getMaNV());

      capNhatThongTinNhanVien();
      capNhatDoanhThu();
      tinhTienPhaiNop();
  
      btnXacNhan.setOnAction(event -> {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Thông báo");
          alert.setHeaderText("Bạn có chắc chắn muốn kết ca?");

          ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
          ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
          alert.getButtonTypes().setAll(yes, no);

          alert.showAndWait().ifPresent(button -> {
              if (button.getButtonData() == ButtonBar.ButtonData.YES) {
                  Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
                  thongBao.setTitle("Thông báo");
                  thongBao.setHeaderText("Kết ca thành công!");
                  thongBao.showAndWait();
                  // dangXuat(); // nếu có màn đăng nhập thì mở lại sau
              }
          });
      });
  }

  // ================== SET TÀI KHOẢN ==================
//  public void setTaiKhoan(TaiKhoan taiKhoan) {
//      this.taiKhoan = taiKhoan;
//      //System.out.println("SET TAI KHOAN: " + taiKhoan.getNhanVien().getMaNV());
//      capNhatThongTinNhanVien();
//      capNhatDoanhThu();
//      tinhTienPhaiNop();
//  }

  // ================== CẬP NHẬT NHÂN VIÊN ==================
  private void capNhatThongTinNhanVien() {
      if (taiKhoan == null) return;

      text_TenNhanVien.setText(taiKhoan.getNhanVien().getTenNV());
      text_maNV.setText(taiKhoan.getNhanVien().getMaNV());
  }

  // ================== CẬP NHẬT DOANH THU ==================hihi
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
	//================== TÍNH TIỀN PHẢI NỘP ==================
	private void tinhTienPhaiNop() {
	   if (taiKhoan == null) return;
	
	   try {
	       // Tiền nhận đầu ca (user nhập)
	       String text = textField_TienNhan.getText().replace(",", "").trim();
	       long tienNhanDauCa = text.isEmpty() ? 0 : Long.parseLong(text);
	
	       // Tổng doanh thu (lấy từ label, không gọi DAO)
	       String doanhThuText = label_TongDoanhThu.getText().replace(",", "").trim();
	       double tongDoanhThu = doanhThuText.isEmpty() ? 0 : Double.parseDouble(doanhThuText);
	
	       // ✅ Tiền phải nộp lại = tổng doanh thu - tiền nhận đầu ca
	       double tienPhaiNop = tongDoanhThu - tienNhanDauCa;
	
	       // nếu bạn không muốn âm (trường hợp nhập sai), thì chặn:
	       // if (tienPhaiNop < 0) tienPhaiNop = 0;
	
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
	 }
