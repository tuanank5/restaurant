package controller.HoaDon;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
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
import dao.impl.HoaDon_DAOImpl;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChiTietHoaDon_Controller {
    @FXML
    private Button btnTroLai;

    @FXML
    private Label lblTongThanhToan;
    
    @FXML
    private Label lblTienTra;

    @FXML
    private TableView<ChiTietHoaDon> tblDanhSachMon;
    
    @FXML
    private TableColumn<ChiTietHoaDon, String> tblMaMonAn;
    
    @FXML
    private TableColumn<ChiTietHoaDon, String> tblTenMonAn;
    
    @FXML
    private TableColumn<ChiTietHoaDon, String> tblSoLuong;
    
    @FXML
    private TableColumn<ChiTietHoaDon, String> tblDonGia;
    
    @FXML
    private TableColumn<ChiTietHoaDon, String> tblThanhTien;

    @FXML
    private TextField txtMaHoaDon;
    
    @FXML
    private TextField txtTenKH;
    
    @FXML
    private Button btnXuatHD;
    
    @FXML
    private TextField txtSDT;
    
    @FXML
    private TextField txtNV;
    
    @FXML
    private TextField txtNgay;
    
    @FXML
    private TextField txtKieuThanhToan;

    private HoaDon hoaDon;
    private final ObservableList<ChiTietHoaDon> danhSachCTHD = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cauHinhBang();
        tblDanhSachMon.setItems(danhSachCTHD);
    }
    
    @FXML
    void controller(ActionEvent event) {
    	Object src = event.getSource();
    	if(src == btnTroLai) {
    		MenuNV_Controller.instance.readyUI("HoaDon/HoaDonTA");
    	}else if(src ==  btnXuatHD) {
    		xuatHoaDonPDF();
    		MenuNV_Controller.instance.readyUI("HoaDon/HoaDonTA");
    	}
    }
    
    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
        hienThiThongTinHoaDon();
        loadChiTietHoaDon();
    }

    private void hienThiThongTinHoaDon() {
        txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        txtTenKH.setText(hoaDon.getKhachHang().getTenKH());
        txtSDT.setText(hoaDon.getKhachHang().getSdt());
        txtNV.setText(hoaDon.getNhanVien().getTenNV());
        txtNgay.setText(hoaDon.getNgayLap().toString());

        lblTongThanhToan.setText(formatTienVN(hoaDon.getTongTien()));
        txtKieuThanhToan.setText(hoaDon.getKieuThanhToan());
        lblTienTra.setText(formatTienVN(hoaDon.getTienThua()));
    }

    private void loadChiTietHoaDon() {
        HoaDon_DAOImpl dao = new HoaDon_DAOImpl();
        var list = dao.findByMaHoaDon(hoaDon.getMaHoaDon());
        System.out.println("CTHD size = " + list.size());
        list.forEach(ct ->
            System.out.println(ct.getMonAn())
        );
        danhSachCTHD.setAll(list);
    }

    private void cauHinhBang() {
        tblMaMonAn.setCellValueFactory(ct ->
            new SimpleStringProperty(ct.getValue().getMonAn().getMaMon())
        );

        tblTenMonAn.setCellValueFactory(ct ->
            new SimpleStringProperty(ct.getValue().getMonAn().getTenMon())
        );

        tblSoLuong.setCellValueFactory(ct ->
            new SimpleStringProperty(String.valueOf(ct.getValue().getSoLuong()))
        );

        tblDonGia.setCellValueFactory(ct ->
            new SimpleStringProperty(formatTienVN(ct.getValue().getMonAn().getDonGia()))
        );

        tblThanhTien.setCellValueFactory(ct -> {
            double tt = ct.getValue().getSoLuong() * ct.getValue().getMonAn().getDonGia();
            return new SimpleStringProperty(formatTienVN(tt));
        });
    }
    
    private void xuatHoaDonPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu hóa đơn");
            fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("HoaDon_" + hoaDon.getMaHoaDon() + ".pdf");

            Stage stage = (Stage) btnXuatHD.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) return;

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // ===== FONT TIẾNG VIỆT =====
            BaseFont bf = BaseFont.createFont(
                "c:/windows/fonts/arial.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
            );
            Font titleFont = new Font(bf, 18, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font normalFont = new Font(bf, 12);

            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHoaDon(), normalFont));
            document.add(new Paragraph("Ngày lập: " + hoaDon.getNgayLap(), normalFont));
            document.add(new Paragraph("Nhân viên: " + hoaDon.getNhanVien().getTenNV(), normalFont));
            document.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getTenKH(), normalFont));
            document.add(new Paragraph("SĐT: " + hoaDon.getKhachHang().getSdt(), normalFont));
            document.add(new Paragraph("Thanh toán: " + hoaDon.getKieuThanhToan(), normalFont));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 4f, 2f, 3f, 3f});

            String[] headers = {"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (ChiTietHoaDon ct : danhSachCTHD) {
                table.addCell(new Phrase(ct.getMonAn().getMaMon(), normalFont));
                table.addCell(new Phrase(ct.getMonAn().getTenMon(), normalFont));

                PdfPCell slCell = new PdfPCell(
                    new Phrase(String.valueOf(ct.getSoLuong()), normalFont)
                );
                slCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(slCell);

                table.addCell(new Phrase(
                    formatTienVN(ct.getMonAn().getDonGia()), normalFont
                ));

                double thanhTien = ct.getSoLuong() * ct.getMonAn().getDonGia();
                table.addCell(new Phrase(
                    formatTienVN(thanhTien), normalFont
                ));
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                "Tổng thanh toán: " + formatTienVN(hoaDon.getTongTien()),
                headerFont
            ));
            document.add(new Paragraph(
                "Tiền thừa: " + formatTienVN(hoaDon.getTienThua()),
                headerFont
            ));

            Paragraph thanks = new Paragraph(
                "Cảm ơn quý khách đã sử dụng dịch vụ!",
                normalFont
            );
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất hóa đơn");
            alert.setHeaderText("Thành công");
            alert.setContentText("Hóa đơn đã được lưu:\n" + file.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể xuất hóa đơn");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    
    private String formatTienVN(double tien) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        nf.setGroupingUsed(true);
        nf.setMaximumFractionDigits(0);
        return nf.format(tien) + " VND";
    }
}
