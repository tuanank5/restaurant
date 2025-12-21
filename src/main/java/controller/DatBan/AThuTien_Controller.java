package controller.DatBan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.HoaDon_DAO;
import entity.HoaDon;
import entity.KhachHang;
import dao.KhachHang_DAO;
import dao.impl.KhachHang_DAOlmpl;
import entity.KhuyenMai;
import entity.MonAn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
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
    private Button btnInChuyenKhoan;

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
    private HoaDon hoaDonHienTai = AThanhToan_Controller.aTT.hoaDonHienTai;
    private Map<MonAn, Integer> dsMonAn = AThanhToan_Controller.aTT.dsMonAn;
    private KhuyenMai kmDangChon = AThanhToan_Controller.aTT.kmDangChon;
    private double thueHD = AThanhToan_Controller.aTT.thueHD;    
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();
    private double tongThanhTienLamTron = Math.round(tongThanhTien / 1000.0) * 1000;
    
    private int tienKH = 0;
    private double tienTra;
	
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
    	
    	btnLamMoi.setTooltip(new Tooltip("Làm mới số tiền khách đưa"));
    	btnInChuyenKhoan.setTooltip(new Tooltip("Thanh toán bằng chuyển khoản"));
    	btnIn.setTooltip(new Tooltip("Thanh toán bằng tiền mặt"));
    	btnHuy.setTooltip(new Tooltip("Hủy bỏ thanh toán"));
	}
    
	private void hienThiTienTra() {
		tienKH = Integer.parseInt(txtTienKH.getText());
		tienTra = tienKH - tongThanhTienLamTron;
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
        	int tienNhan = Integer.parseInt(txtTienKH.getText());
            if (tienTra >= 0) {
            	LocalDate localDate = LocalDate.now();
                Date dateNow = Date.valueOf(localDate);
            	hoaDonHienTai.setNgayLap(dateNow);
            	
            	hoaDonHienTai.setKieuThanhToan("Tiền mặt");
            	hoaDonHienTai.setKhuyenMai(kmDangChon);
            	hoaDonHienTai.setNhanVien(MenuNV_Controller.taiKhoan.getNhanVien());
            	hoaDonHienTai.setTongTien(tongThanhTienLamTron);
            	hoaDonHienTai.setTienNhan((double) tienNhan);
            	hoaDonHienTai.setTienThua(tienTra);
            	hoaDonHienTai.setThue(thueHD);
            	boolean check = RestaurantApplication.getInstance()
   	                    .getDatabaseContext()
   	                    .newEntity_DAO(HoaDon_DAO.class)
   	                    .capNhat(hoaDonHienTai);
   	            //Kiểm tra kết quả thêm
   	            if (check) {
   	            	capNhatDiemTichLuySauThanhToan();
   	                xuatHD();
	   	            Stage stage = (Stage) btnHuy.getScene().getWindow();
	   	            stage.close();
	   	            MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
   	            } else {
   	                showAlert("Thông báo", "Thanh toán thất bại!", Alert.AlertType.WARNING);
   	            }
            	
            } else {
            	showAlert(Alert.AlertType.ERROR, "Số tiền khách đưa cho hóa đơn chưa đủ!");
            }
        } else if (source == btnInChuyenKhoan) {
        	LocalDate localDate = LocalDate.now();
            Date dateNow = Date.valueOf(localDate);
        	hoaDonHienTai.setNgayLap(dateNow);
        	hoaDonHienTai.setKieuThanhToan("Chuyển khoản");
        	hoaDonHienTai.setKhuyenMai(kmDangChon);
        	hoaDonHienTai.setNhanVien(MenuNV_Controller.taiKhoan.getNhanVien());
        	hoaDonHienTai.setTongTien(tongThanhTienLamTron);
        	hoaDonHienTai.setTienNhan(tongThanhTienLamTron);
        	hoaDonHienTai.setTienThua(0);
        	hoaDonHienTai.setThue(thueHD);
        	boolean check = RestaurantApplication.getInstance()
	                    .getDatabaseContext()
	                    .newEntity_DAO(HoaDon_DAO.class)
	                    .capNhat(hoaDonHienTai);
	        //Kiểm tra kết quả thêm
        	if (check) {
            	capNhatDiemTichLuySauThanhToan();
                xuatHDChuyenKhoan();
	            Stage stage = (Stage) btnHuy.getScene().getWindow();
	            stage.close();
	            MenuNV_Controller.instance.readyUI("DatBan/aBanHienTai");
            } else {
                showAlert("Thông báo", "Thanh toán thất bại!", Alert.AlertType.WARNING);
            }
	            
        }
    }
    
    private void xuatHDChuyenKhoan() {
    	try {
            // Mở hộp thoại lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu hóa đơn");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("HoaDon_" + hoaDonHienTai.getMaHoaDon() + ".pdf");

            Stage stage = (Stage) btnIn.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) return;

            // Khởi tạo tài liệu PDF
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Font Unicode (Arial hoặc font có tiếng Việt)
            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 18, Font.BOLD);
            Font fontHeader = new Font(bf, 12, Font.BOLD);
            Font fontNormal = new Font(bf, 12, Font.NORMAL);

            //TIÊU ĐỀ 
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Ngày lập: " + hoaDonHienTai.getNgayLap(), fontNormal));
            document.add(new Paragraph("Mã hóa đơn: " + hoaDonHienTai.getMaHoaDon(), fontNormal));
            document.add(Chunk.NEWLINE);

            //THÔNG TIN KHÁCH HÀNG
            document.add(new Paragraph("Khách hàng: " + hoaDonHienTai.getKhachHang().getTenKH(), fontNormal));
            document.add(new Paragraph("SĐT: " + hoaDonHienTai.getKhachHang().getSdt(), fontNormal));
            document.add(new Paragraph("Nhân viên: " + hoaDonHienTai.getNhanVien().getTenNV(), fontNormal));
            document.add(Chunk.NEWLINE);

            // BẢNG MÓN ĂN
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 5f, 2f, 3f, 3f});
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Tiêu đề bảng
            String[] headers = {"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Dữ liệu bảng
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            for (MonAn mon : dsMonAn.keySet()) {
                int sl = dsMonAn.get(mon);
                double thanhTien = mon.getDonGia() * sl;

                table.addCell(new PdfPCell(new Phrase(mon.getMaMon(), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(mon.getTenMon(), fontNormal)));

                PdfPCell slCell = new PdfPCell(new Phrase(String.valueOf(sl), fontNormal));
                slCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(slCell);

                PdfPCell giaCell = new PdfPCell(new Phrase(currencyVN.format(mon.getDonGia()), fontNormal));
                giaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(giaCell);

                PdfPCell ttCell = new PdfPCell(new Phrase(currencyVN.format(thanhTien), fontNormal));
                ttCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(ttCell);
            }

            document.add(table);

            // TỔNG KẾT 
            document.add(new Paragraph("Tổng thanh toán: " + lblTienCanThu.getText(), fontNormal));

            document.add(Chunk.NEWLINE);
            Paragraph thanks = new Paragraph("Cảm ơn quý khách đã sử dụng dịch vụ!", fontNormal);
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
            
            // THÔNG BÁO 
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất hóa đơn");
            alert.setHeaderText("Thành công!");
            alert.setContentText("Hóa đơn đã được lưu tại:\n" + file.getAbsolutePath());
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể xuất hóa đơn!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
    
    private void xuatHD() {
      	 try {
               // Mở hộp thoại lưu file
               FileChooser fileChooser = new FileChooser();
               fileChooser.setTitle("Chọn nơi lưu hóa đơn");
               fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
               fileChooser.setInitialFileName("HoaDon_" + hoaDonHienTai.getMaHoaDon() + ".pdf");

               Stage stage = (Stage) btnIn.getScene().getWindow();
               File file = fileChooser.showSaveDialog(stage);
               if (file == null) return;

               // Khởi tạo tài liệu PDF
               Document document = new Document(PageSize.A4, 40, 40, 40, 40);
               PdfWriter.getInstance(document, new FileOutputStream(file));
               document.open();

               // Font Unicode (Arial hoặc font có tiếng Việt)
               BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
               Font fontTitle = new Font(bf, 18, Font.BOLD);
               Font fontHeader = new Font(bf, 12, Font.BOLD);
               Font fontNormal = new Font(bf, 12, Font.NORMAL);

               //TIÊU ĐỀ 
               Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
               title.setAlignment(Element.ALIGN_CENTER);
               document.add(title);
               document.add(new Paragraph("Ngày lập: " + hoaDonHienTai.getNgayLap(), fontNormal));
               document.add(new Paragraph("Mã hóa đơn: " + hoaDonHienTai.getMaHoaDon(), fontNormal));
               document.add(Chunk.NEWLINE);

               //THÔNG TIN KHÁCH HÀNG
               document.add(new Paragraph("Khách hàng: " + hoaDonHienTai.getKhachHang().getTenKH(), fontNormal));
               document.add(new Paragraph("SĐT: " + hoaDonHienTai.getKhachHang().getSdt(), fontNormal));
               document.add(new Paragraph("Nhân viên: " + hoaDonHienTai.getNhanVien().getTenNV(), fontNormal));
               document.add(Chunk.NEWLINE);

               // BẢNG MÓN ĂN
               PdfPTable table = new PdfPTable(5);
               table.setWidthPercentage(100);
               table.setWidths(new float[]{2f, 5f, 2f, 3f, 3f});
               table.setSpacingBefore(10f);
               table.setSpacingAfter(10f);

               // Tiêu đề bảng
               String[] headers = {"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền"};
               for (String h : headers) {
                   PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                   table.addCell(cell);
               }

               // Dữ liệu bảng
               NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
               for (MonAn mon : dsMonAn.keySet()) {
                   int sl = dsMonAn.get(mon);
                   double thanhTien = mon.getDonGia() * sl;

                   table.addCell(new PdfPCell(new Phrase(mon.getMaMon(), fontNormal)));
                   table.addCell(new PdfPCell(new Phrase(mon.getTenMon(), fontNormal)));

                   PdfPCell slCell = new PdfPCell(new Phrase(String.valueOf(sl), fontNormal));
                   slCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   table.addCell(slCell);

                   PdfPCell giaCell = new PdfPCell(new Phrase(currencyVN.format(mon.getDonGia()), fontNormal));
                   giaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                   table.addCell(giaCell);

                   PdfPCell ttCell = new PdfPCell(new Phrase(currencyVN.format(thanhTien), fontNormal));
                   ttCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                   table.addCell(ttCell);
               }

               document.add(table);

               // TỔNG KẾT 
               document.add(new Paragraph("Tổng thanh toán: " + lblTienCanThu.getText(), fontNormal));
               document.add(new Paragraph("Tiền khách đưa: " + formatTienVN(Double.parseDouble(txtTienKH.getText())), fontNormal));
               document.add(new Paragraph("Tiền thừa: " + lblTienCanTra.getText(), fontNormal));

               document.add(Chunk.NEWLINE);
               Paragraph thanks = new Paragraph("Cảm ơn quý khách đã sử dụng dịch vụ!", fontNormal);
               thanks.setAlignment(Element.ALIGN_CENTER);
               document.add(thanks);

               document.close();
               
               // THÔNG BÁO 
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Xuất hóa đơn");
               alert.setHeaderText("Thành công!");
               alert.setContentText("Hóa đơn đã được lưu tại:\n" + file.getAbsolutePath());
               alert.showAndWait();
               
           } catch (Exception e) {
               e.printStackTrace();
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Lỗi");
               alert.setHeaderText("Không thể xuất hóa đơn!");
               alert.setContentText(e.getMessage());
               alert.showAndWait();
           }
   	}
    
    private void capNhatDiemTichLuySauThanhToan() {
        KhachHang kh = hoaDonHienTai.getKhachHang();

        int diemCu = kh.getDiemTichLuy();
        int diemSuDung = AThanhToan_Controller.aTT.diemSuDung;

        int diemMoi = diemCu - diemSuDung + 50;
        if (diemMoi < 0) diemMoi = 0;

        kh.setDiemTichLuy(diemMoi);

        // cập nhật DB
        khachHangDAO.capNhat(kh);
    }

    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
}
