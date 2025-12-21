package controller.DatBan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

import controller.Menu.MenuNV_Controller;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.KhuyenMai_DAOImpl;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.MonAn;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AThanhToan_Controller {
	
	@FXML
    private Button btnDiemTichLuy;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnTamTinh;

    @FXML
    private Button btnThuTien;

    @FXML
    private ComboBox<KhuyenMai> cmbKM;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colDonGia;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSTT;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colSoLuong;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colTenMon;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colThanhTien;

    @FXML
    private Label lblThanhTien;

    @FXML
    private Label lblThue;

    @FXML
    private Label lblTong;

    @FXML
    private TableView<ChiTietHoaDon> tblDS;

    @FXML
    private TextField txtBan;

    @FXML
    private TextField txtKH;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSL;
    
    @FXML
    private Label lblTichLuy;

    @FXML
    private Label lblTienGiam;
    
    @FXML
    private Label lblSauGiam;
    
    // --- DAO ---
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAOImpl();
    private ChiTietHoaDon_DAO cthdDAO = new ChiTietHoaDon_DAOImpl();
    private KhachHang_DAO khachHangDAO = new KhachHang_DAOlmpl();    
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAOImpl();

    // --- Danh sách và trạng thái ---
    private List<HoaDon> dsHoaDon = new ArrayList<>();
    private ObservableList<ChiTietHoaDon> dsCTHD = FXCollections.observableArrayList();
    private List<ChiTietHoaDon> dsCTHD_DB;
    
    private double thanhTien = 0;
	private double Vat_Rate = 0.1;
	private double tongTruocVAT;
	private int diemHienTai = 0;
	static int diemSuDung = 0; 
	double tongSauVAT;
	HoaDon hoaDonHienTai;
	Map<MonAn, Integer> dsMonAn;
	KhuyenMai kmDangChon;
	double thueHD;
	
	public static AThanhToan_Controller aTT;

    @FXML
    public void initialize() {
    	aTT = this;
    	String maHD = MenuNV_Controller.instance.aBanHienTai_HD.getMaHoaDon();
    	dsCTHD_DB = cthdDAO.getChiTietTheoMaHoaDon(maHD);
    	
    	dsMonAn = new HashMap<>();
    	for (ChiTietHoaDon cthd : dsCTHD_DB) {
    		thanhTien += cthd.getMonAn().getDonGia() * cthd.getSoLuong();
    		dsMonAn.put(cthd.getMonAn(), cthd.getSoLuong());
    	}
    
    	setValueTbl();
        loadData(dsCTHD_DB);
        
        dsHoaDon = hoaDonDAO.getDanhSach("HoaDon.list", HoaDon.class);
        for (HoaDon hd : dsHoaDon) {
        	if (hd.getMaHoaDon().equals(maHD)) {
        		hoaDonHienTai = hd;
        	}
        }
        
        txtSDT.setText(hoaDonHienTai.getKhachHang().getSdt());
        txtKH.setText(hoaDonHienTai.getKhachHang().getTenKH());
        txtSL.setText(hoaDonHienTai.getDonDatBan().getSoLuong() + "");
        txtBan.setText(hoaDonHienTai.getDonDatBan().getBan().getMaBan());
        
        loadCmbKM();
        capNhatTongTien();
        
        lblTichLuy.setText(formatTienVN(Double.parseDouble("0")));
        lblThanhTien.setText(formatTienVN(thanhTien));
        
        cmbKM.setTooltip(new Tooltip("Khuyến mãi áp dụng cho hóa đơn"));
        btnDiemTichLuy.setTooltip(new Tooltip("Sử dụng điểm tích lũy của khách hàng"));
        
        btnTamTinh.setTooltip(new Tooltip("In hóa đơn tạm tính"));
        btnThuTien.setTooltip(new Tooltip("Thanh toán hóa đơn"));
    }
    
    @FXML
    private void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();
        if (source == btnTamTinh) {
        	xuatHD();
        } else if (source == btnThuTien) {
            readyUI("DatBan/aThuTien");
        } else if (source == btnEdit) {
            
        } else if (source == btnDiemTichLuy) {
        	hienDialogDiemTichLuy();
        }
    }
    
    private void loadCmbKM() {
    	List<KhuyenMai> dsKM = kmDAO.getDanhSach("KhuyenMai.list", KhuyenMai.class);
    	dsKM = dsKM.stream()
                .sorted(Comparator.comparingDouble(KhuyenMai::getPhanTramGiamGia).reversed())
                .collect(Collectors.toList());
    	for(KhuyenMai km : dsKM) {
            cmbKM.getItems().add(km);
        }
    	
    	cmbKM.setValue(cmbKM.getItems().get(0));
    	
    	cmbKM.setOnAction(e -> capNhatTongTien());
	}
    
    private void capNhatTongTien() {
        double tienGiam = 0;
        KhuyenMai km = cmbKM.getValue();
        kmDangChon = km;
        
        if (km != null) {
            switch (km.getLoaiKM()) {
                case "Khuyến mãi trên tổng hóa đơn":
                    tienGiam = thanhTien * km.getPhanTramGiamGia() / 100.0;
                    break;

                case "Ưu đãi cho khách hàng Kim Cương":
                    KhachHang kh = khachHangDAO.timTheoMa(hoaDonHienTai.getKhachHang().getMaKH());
                    if (kh != null && kh.getHangKhachHang() != null &&
                            kh.getHangKhachHang().getTenHang().equalsIgnoreCase("Hạng Kim Cương")) {
                        tienGiam = thanhTien * km.getPhanTramGiamGia() / 100.0;
                    }
                    break;
            }
        }
        tongTruocVAT = thanhTien - tienGiam - diemSuDung;
        if (tongTruocVAT < 0) tongTruocVAT = 0;
        
        double thue = tongTruocVAT * Vat_Rate;
        thueHD = thue;
        tongSauVAT = tongTruocVAT + thue;

        lblTienGiam.setText(formatTienVN(tienGiam));
        lblTichLuy.setText(formatTienVN(diemSuDung));
        lblSauGiam.setText(formatTienVN(tongTruocVAT));
        lblThue.setText(formatTienVN(thue));
        lblTong.setText(formatTienVN(tongSauVAT));
    }

	private void setValueTbl() {
    	colSTT.setCellValueFactory(col -> {
            int index = tblDS.getItems().indexOf(col.getValue());
            return new ReadOnlyObjectWrapper<>(index >= 0 ? index + 1 : 0);
        });
    	
	    colTenMon.setCellValueFactory(cell -> {
	        String tenMon = cell.getValue().getMonAn().getTenMon();
	        return new SimpleStringProperty(tenMon);
	    });
	    
	    colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
	    
	    colDonGia.setCellValueFactory(cell -> {
	        double donGia = cell.getValue().getMonAn().getDonGia();
	        return new SimpleStringProperty(formatTienVN(donGia));
	    });
	    
	    colThanhTien.setCellValueFactory(cell -> {
	        MonAn mon = cell.getValue().getMonAn();
	        int sl = cell.getValue().getSoLuong();
	        double thanhTien = mon.getDonGia() * sl;
	        return new SimpleStringProperty(formatTienVN(thanhTien));
	    });
	}
	
	private void xuatHD() {
   	 try {
            // Mở hộp thoại lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu hóa đơn");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("HoaDon_" + hoaDonHienTai.getMaHoaDon() + ".pdf");

            Stage stage = (Stage) btnTamTinh.getScene().getWindow();
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
            document.add(new Paragraph("Tổng thanh toán: " + lblTong.getText(), fontNormal));
//            document.add(new Paragraph("Tiền khách đưa: " + .getText() + " VND", fontNormal));
//            document.add(new Paragraph("Tiền thừa: " + .getText(), fontNormal));

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
    
    private void loadData(List<ChiTietHoaDon> chitietHDs) {
    	dsCTHD.clear();
    	dsCTHD.addAll(chitietHDs);
        tblDS.setItems(dsCTHD);
    }
    
    private String formatTienVN(double tien) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
	    nf.setGroupingUsed(true);
	    return nf.format(tien) + " VND";
	}
    
    public FXMLLoader readyUI(String ui) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(getClass().getResource("/view/fxml/" + ui + ".fxml"));
            root = fxmlLoader.load();
            
            Stage newStage = new Stage();
            newStage.setTitle("Thu tiền");
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setScene(new Scene(root));
            
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fxmlLoader;
    }
    
    private void hienDialogDiemTichLuy() {
        KhachHang kh = hoaDonHienTai.getKhachHang();
        diemHienTai = kh.getDiemTichLuy(); // lấy điểm từ entity KhachHang

        Stage dialog = new Stage();
        dialog.setTitle("Sử dụng điểm tích lũy");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(300);
        dialog.setMinWidth(300);
        dialog.setHeight(220);

        Label lblDiemCo = new Label("Điểm hiện có: " + diemHienTai);
        Label lblQuyDoi = new Label("Quy đổi: 1.000 điểm = 1.000 VND");

        TextField txtDiem = new TextField();
        txtDiem.setPromptText("Nhập số điểm muốn sử dụng");

        Label lblLoi = new Label();
        lblLoi.setStyle("-fx-text-fill: red;");

        Button btnApDung = new Button("Áp dụng");
        Button btnHuy = new Button("Hủy");

        btnApDung.setOnAction(e -> {
            try {
                int diemNhap = Integer.parseInt(txtDiem.getText());
                if (diemNhap <= 0) {
                    lblLoi.setText("Số điểm phải lớn hơn 0");
                    return;
                }

                if (diemNhap > diemHienTai) {
                    lblLoi.setText("Không đủ điểm tích lũy");
                    return;
                }
                diemSuDung = diemNhap;
                lblTichLuy.setText(formatTienVN(diemSuDung));

                capNhatTongTien();
                dialog.close();
            } catch (NumberFormatException ex) {
                lblLoi.setText("Vui lòng nhập số hợp lệ");
            }
        });

        btnHuy.setOnAction(e -> dialog.close());
        VBox box = new VBox(10,
                lblDiemCo,
                lblQuyDoi,
                txtDiem,
                lblLoi,
                new HBox(20, btnApDung, btnHuy)
        );
        box.setStyle("-fx-padding: 20;");
        dialog.setScene(new Scene(box));
        dialog.showAndWait();
    }

}
