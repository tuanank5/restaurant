package controller.HoaDon;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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

import controller.Menu.MenuNV_Controller;
import entity.Ban;
import entity.KhachHang;
import entity.MonAn;
import entity.TaiKhoan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChiTietHoaDon_Controller {
	private static int soHoaDonCuoi = 0;
	
	@FXML
    private Button btnTroLai;

    @FXML
    private Button btnXuatHD;
	
    @FXML
    private Label lblTienTra;

    @FXML
    private Label lblTongThanhToan;

    @FXML
    private TableView<MonAn> tblDanhSachMon;

    @FXML
    private TableColumn<MonAn, String> tblDonGia;

    @FXML
    private TableColumn<MonAn, String> tblMaMonAn;

    @FXML
    private TableColumn<MonAn, String> tblSoLuong;

    @FXML
    private TableColumn<MonAn, String> tblTenMonAn;

    @FXML
    private TableColumn<MonAn, String> tblThanhTien;

    @FXML
    private TextField txtMaHoaDon;

    @FXML
    private TextField txtNV;

    @FXML
    private TextField txtNgay;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtTien;
    
    private String tongTienSauVAT = MenuNV_Controller.tongTienSauVAT;
    private Ban banDangChon = MenuNV_Controller.banDangChon;
    private KhachHang khachHangDangChon = MenuNV_Controller.khachHangDangChon;
    private Map<MonAn, Integer> dsMonAnDangChon = MenuNV_Controller.dsMonAnDangChon;
    private TaiKhoan taiKhoan = MenuNV_Controller.taiKhoan;
    
    private ObservableList<MonAn> danhSachMonAn = FXCollections.observableArrayList();

    @FXML
    void controller(ActionEvent event) {
    	Object source = event.getSource();
        if (source == btnTroLai) {
        	MenuNV_Controller.instance.readyUI("MonAn/DatMon");
        } else if (source == btnXuatHD) {
            themHD();
        } 
    }
    
    private void themHD() {
//    	try {
//            // Tạo thư mục lưu
//            File dir = new File("exports");
//            if (!dir.exists()) dir.mkdirs();
//
//            // Tên file PDF
//            String fileName = "exports/HoaDon_" + txtMaHoaDon.getText() + ".pdf";
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, new FileOutputStream(fileName));
//            document.open();
//
//            // Font tiếng Việt (nếu có font unicode, ví dụ Arial Unicode)
//            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font fontTitle = new Font(bf, 18, Font.BOLD);
//            Font fontNormal = new Font(bf, 12, Font.NORMAL);
//
//            // Tiêu đề
//            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph("Ngày lập: " + txtNgay.getText(), fontNormal));
//            document.add(new Paragraph("Mã hóa đơn: " + txtMaHoaDon.getText(), fontNormal));
//            document.add(new Paragraph(" "));
//
//            // Thông tin khách hàng
//            document.add(new Paragraph("Khách hàng: " + txtTenKH.getText(), fontNormal));
//            document.add(new Paragraph("SĐT: " + txtSDT.getText(), fontNormal));
//            document.add(new Paragraph("Nhân viên: " + txtNV.getText(), fontNormal));
//            document.add(new Paragraph(" "));
//
//            // Bảng món ăn
//            PdfPTable table = new PdfPTable(5);
//            table.setWidthPercentage(100);
//            table.setWidths(new float[]{2, 5, 2, 3, 3});
//            table.addCell("Mã món");
//            table.addCell("Tên món");
//            table.addCell("SL");
//            table.addCell("Đơn giá");
//            table.addCell("Thành tiền");
//
//            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//            for (MonAn mon : dsMonAnDangChon.keySet()) {
//                int sl = dsMonAnDangChon.get(mon);
//                double thanhTien = mon.getDonGia() * sl;
//
//                table.addCell(new PdfPCell(new Phrase(mon.getMaMon(), fontNormal)));
//                table.addCell(new PdfPCell(new Phrase(mon.getTenMon(), fontNormal)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(sl), fontNormal)));
//                table.addCell(new PdfPCell(new Phrase(currencyVN.format(mon.getDonGia()), fontNormal)));
//                table.addCell(new PdfPCell(new Phrase(currencyVN.format(thanhTien), fontNormal)));
//            }
//
//            document.add(table);
//            document.add(new Paragraph(" "));
//
//            // Tổng tiền
//            document.add(new Paragraph("Tổng thanh toán: " + lblTongThanhToan.getText(), fontNormal));
//            document.add(new Paragraph("Tiền khách đưa: " + txtTien.getText() + " VND", fontNormal));
//            document.add(new Paragraph("Tiền thừa/trả lại: " + lblTienTra.getText(), fontNormal));
//
//            document.add(new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!", fontNormal));
//            document.close();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Xuất hóa đơn");
//            alert.setHeaderText(null);
//            alert.setContentText("Hóa đơn đã được xuất tại: " + fileName);
//            alert.showAndWait();
//            System.out.println("Đã xuất hóa đơn: " + fileName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    	try {
            // Mở hộp thoại lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu hóa đơn");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );

            // Gợi ý tên file
            fileChooser.setInitialFileName("HoaDon_" + txtMaHoaDon.getText() + ".pdf");

            // Lấy Stage hiện tại
            Stage stage = (Stage) btnXuatHD.getScene().getWindow();

            // Hiển thị hộp thoại và chờ người dùng chọn vị trí lưu
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) return; // Nếu người dùng hủy thì dừng lại

            // Bắt đầu tạo PDF
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Font tiếng Việt (sử dụng Arial hoặc font khác có Unicode)
            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 18, Font.BOLD);
            Font fontNormal = new Font(bf, 12, Font.NORMAL);

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Ngày lập: " + txtNgay.getText(), fontNormal));
            document.add(new Paragraph("Mã hóa đơn: " + txtMaHoaDon.getText(), fontNormal));
            document.add(new Paragraph(" "));

            // Thông tin khách hàng
            document.add(new Paragraph("Khách hàng: " + txtTenKH.getText(), fontNormal));
            document.add(new Paragraph("SĐT: " + txtSDT.getText(), fontNormal));
            document.add(new Paragraph("Nhân viên: " + txtNV.getText(), fontNormal));
            document.add(new Paragraph(" "));

            // Bảng món ăn
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 5, 2, 3, 3});
            table.addCell("Mã món");
            table.addCell("Tên món");
            table.addCell("SL");
            table.addCell("Đơn giá");
            table.addCell("Thành tiền");

            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            for (MonAn mon : dsMonAnDangChon.keySet()) {
                int sl = dsMonAnDangChon.get(mon);
                double thanhTien = mon.getDonGia() * sl;

                table.addCell(new PdfPCell(new Phrase(mon.getMaMon(), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(mon.getTenMon(), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(sl), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(currencyVN.format(mon.getDonGia()), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(currencyVN.format(thanhTien), fontNormal)));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Tổng tiền và tiền thừa
            document.add(new Paragraph("Tổng thanh toán: " + lblTongThanhToan.getText(), fontNormal));
            document.add(new Paragraph("Tiền khách đưa: " + txtTien.getText() + " VND", fontNormal));
            document.add(new Paragraph("Tiền thừa/trả lại: " + lblTienTra.getText(), fontNormal));

            document.add(new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!", fontNormal));
            document.close();

            // Thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất hóa đơn");
            alert.setHeaderText("Thành công!");
            //alert.setContentText("Hóa đơn đã được lưu tại:\n" + file.getAbsolutePath());
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

	@FXML
    private void initialize() {
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
	    // Mã & Tên
	    tblMaMonAn.setCellValueFactory(new PropertyValueFactory<>("maMon"));
	    tblTenMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
	    // Số lượng
	    tblSoLuong.setCellValueFactory(cell -> {
	        Integer sl = dsMonAnDangChon.get(cell.getValue());
	        return new SimpleStringProperty(String.valueOf(sl != null ? sl : 0));
	    });
	    // Đơn giá
	    tblDonGia.setCellValueFactory(cell -> {
	        double donGia = cell.getValue().getDonGia();
	        return new SimpleStringProperty(formatTienVN(donGia));
	    });

	    // Thành tiền
	    tblThanhTien.setCellValueFactory(cell -> {
	        MonAn mon = cell.getValue();
	        int sl = dsMonAnDangChon.getOrDefault(mon, 0);
	        double thanhTien = mon.getDonGia() * sl;
	        return new SimpleStringProperty(formatTienVN(thanhTien));
	    });
	    // Load dữ liệu
	    tblDanhSachMon.setItems(FXCollections.observableArrayList(dsMonAnDangChon.keySet()));
	    // Thông tin khách, NV, ngày
	    txtMaHoaDon.setText(taoMaHoaDon());
	    txtTenKH.setText(khachHangDangChon.getTenKH());
	    txtSDT.setText(khachHangDangChon.getSdt());
	    txtNV.setText(taiKhoan.getNhanVien().getTenNV());
	    txtNgay.setText(LocalDate.now().toString());
	    // Tổng thanh toán
	    try {
	        double tong = Double.parseDouble(tongTienSauVAT);
	        lblTongThanhToan.setText(formatTienVN(tong));
	    } catch (NumberFormatException e) {
	        lblTongThanhToan.setText(tongTienSauVAT);
	    }
	    // Tính tiền trả
	    txtTien.textProperty().addListener((obs, oldVal, newVal) -> calculateTienTra());
    }

    

    private void calculateTienTra() {
    	NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    	try {
            double tienNhap = Double.parseDouble(txtTien.getText());
            double tong = Double.parseDouble(tongTienSauVAT);
            double tienTra = tienNhap - tong;
            lblTienTra.setText(tienTra < 0 
                ?formatTienVN(-tienTra) 
                :formatTienVN(tienTra));
        } catch (NumberFormatException e) {
            lblTienTra.setText("Vui lòng nhập số hợp lệ!");
        }
	}

	private void loadDataToTable() {
    	ObservableList<MonAn> data = FXCollections.observableArrayList(dsMonAnDangChon.keySet());
        tblDanhSachMon.setItems(data);
        
        // Format cột giá và thành tiền sang VNĐ
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tblDonGia.setCellFactory(column -> new TableCell<MonAn, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        double donGia = Double.parseDouble(item);
                        setText(currencyVN.format(donGia));
                    } catch (NumberFormatException e) {
                        setText(item);
                    }
                }
            }
        });

        tblThanhTien.setCellFactory(column -> new TableCell<MonAn, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        double thanhTien = Double.parseDouble(item);
                        setText(currencyVN.format(thanhTien));
                    } catch (NumberFormatException e) {
                        setText(item);
                    }
                }
            }
        });
	}
	private String formatTienVN(double tien) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
	    nf.setGroupingUsed(true);
	    //nf.setMaximumFractionDigits(0); // không hiển thị phần thập phân
	    return nf.format(tien) + " VND";
	}
	
	private String taoMaHoaDon() {
	    soHoaDonCuoi++; // tăng số hóa đơn
	    return String.format("HD%04d", soHoaDonCuoi);
	}
}
