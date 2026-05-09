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
import dto.ChiTietHoaDon_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import dto.NhanVien_DTO;
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
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class ChiTietHoaDon_Controller {
	@FXML
	private Button btnTroLai;

	@FXML
	private Label lblTongThanhToan;

	@FXML
	private Label lblTienTra;

	@FXML
	private TableView<ChiTietHoaDon_DTO> tblDanhSachMon;

	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> tblMaMonAn;

	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> tblTenMonAn;

	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> tblSoLuong;

	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> tblDonGia;

	@FXML
	private TableColumn<ChiTietHoaDon_DTO, String> tblThanhTien;

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
	private HoaDon_DTO hoaDonDto;
	private final ObservableList<ChiTietHoaDon_DTO> danhSachCTHD = FXCollections.observableArrayList();
	private Client client;

	@FXML
	private void initialize() {
		try {
			client = new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cauHinhBang();
		tblDanhSachMon.setItems(danhSachCTHD);
	}

	@FXML
	void controller(ActionEvent event) {
		Object src = event.getSource();
		if (src == btnTroLai) {
			MenuNV_Controller.instance.readyUI("HoaDon/HoaDonTA");
		} else if (src == btnXuatHD) {
			xuatHoaDonPDF();
			MenuNV_Controller.instance.readyUI("HoaDon/HoaDonTA");
		}
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
		this.hoaDonDto = null;
		hienThiThongTinHoaDon();
		loadChiTietHoaDon();
	}

	public void setHoaDonDto(HoaDon_DTO dto) {
		this.hoaDonDto = dto;
		this.hoaDon = null;
		hienThiThongTinHoaDon();
		loadChiTietHoaDon();
	}

	private String maHoaDonHienTai() {
		if (hoaDonDto != null) {
			return hoaDonDto.getMaHD();
		}
		if (hoaDon != null) {
			return hoaDon.getMaHD();
		}
		return "";
	}

	private void hienThiThongTinHoaDon() {
		if (hoaDonDto != null) {
			txtMaHoaDon.setText(hoaDonDto.getMaHD() != null ? hoaDonDto.getMaHD() : "");
			KhachHang_DTO kh = hoaDonDto.getKhachHang();
			txtTenKH.setText(kh != null && kh.getTenKH() != null ? kh.getTenKH() : "");
			txtSDT.setText(kh != null && kh.getSdt() != null ? kh.getSdt() : "");
			NhanVien_DTO nv = hoaDonDto.getNhanVien();
			txtNV.setText(nv != null && nv.getTenNV() != null ? nv.getTenNV() : "");
			txtNgay.setText(hoaDonDto.getNgayLap() != null ? hoaDonDto.getNgayLap().toString() : "");
			lblTongThanhToan.setText(formatTienVN(hoaDonDto.getTongTien()));
			txtKieuThanhToan.setText(hoaDonDto.getKieuThanhToan() != null ? hoaDonDto.getKieuThanhToan() : "");
			lblTienTra.setText(formatTienVN(hoaDonDto.getTienThua()));
			return;
		}
		if (hoaDon != null) {
			txtMaHoaDon.setText(hoaDon.getMaHD());
			txtTenKH.setText(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKH() : "");
			txtSDT.setText(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getSdt() : "");
			txtNV.setText(hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNV() : "");
			txtNgay.setText(hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().toString() : "");
			lblTongThanhToan.setText(formatTienVN(hoaDon.getTongTien()));
			txtKieuThanhToan.setText(hoaDon.getKieuThanhToan());
			lblTienTra.setText(formatTienVN(hoaDon.getTienThua()));
		}
	}

	private void loadChiTietHoaDon() {
		String ma = maHoaDonHienTai();
		if (ma == null || ma.isBlank()) {
			return;
		}
		try {
			Response res = client.send(new Request(CommandType.CTHD_GET_BY_MAHD, ma));
			if (res != null && res.isSuccess()) {
				@SuppressWarnings("unchecked")
				var list = (java.util.List<ChiTietHoaDon_DTO>) res.getData();
				danhSachCTHD.setAll(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cauHinhBang() {
		tblMaMonAn.setCellValueFactory(ct -> new SimpleStringProperty(ct.getValue().getMaMonAn()));

		tblTenMonAn.setCellValueFactory(ct -> new SimpleStringProperty(ct.getValue().getTenMonAn()));

		tblSoLuong.setCellValueFactory(ct -> new SimpleStringProperty(String.valueOf(ct.getValue().getSoLuong())));

		tblDonGia.setCellValueFactory(ct -> new SimpleStringProperty(formatTienVN(ct.getValue().getDonGia())));

		tblThanhTien.setCellValueFactory(ct -> new SimpleStringProperty(formatTienVN(ct.getValue().getThanhTien())));
	}

	private void xuatHoaDonPDF() {
		try {
			String ma = maHoaDonHienTai();
			if (ma.isBlank()) {
				return;
			}
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Lưu hóa đơn");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
			fileChooser.setInitialFileName("HoaDon_" + ma + ".pdf");

			Stage stage = (Stage) btnXuatHD.getScene().getWindow();
			File file = fileChooser.showSaveDialog(stage);
			if (file == null)
				return;

			Document document = new Document(PageSize.A4, 40, 40, 40, 40);
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font titleFont = new Font(bf, 18, Font.BOLD);
			Font headerFont = new Font(bf, 12, Font.BOLD);
			Font normalFont = new Font(bf, 12);

			Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			String tenKH = "";
			String sdt = "";
			String tenNV = "";
			String ngayLapStr = "";
			String kieuTT = "";
			double tong = 0;
			double tienThua = 0;

			if (hoaDonDto != null) {
				KhachHang_DTO kh = hoaDonDto.getKhachHang();
				tenKH = kh != null && kh.getTenKH() != null ? kh.getTenKH() : "";
				sdt = kh != null && kh.getSdt() != null ? kh.getSdt() : "";
				NhanVien_DTO nv = hoaDonDto.getNhanVien();
				tenNV = nv != null && nv.getTenNV() != null ? nv.getTenNV() : "";
				ngayLapStr = hoaDonDto.getNgayLap() != null ? hoaDonDto.getNgayLap().toString() : "";
				kieuTT = hoaDonDto.getKieuThanhToan() != null ? hoaDonDto.getKieuThanhToan() : "";
				tong = hoaDonDto.getTongTien();
				tienThua = hoaDonDto.getTienThua();
			} else if (hoaDon != null) {
				tenKH = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKH() : "";
				sdt = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getSdt() : "";
				tenNV = hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNV() : "";
				ngayLapStr = hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().toString() : "";
				kieuTT = hoaDon.getKieuThanhToan() != null ? hoaDon.getKieuThanhToan() : "";
				tong = hoaDon.getTongTien();
				tienThua = hoaDon.getTienThua();
			}

			document.add(new Paragraph("Mã hóa đơn: " + ma, normalFont));
			document.add(new Paragraph("Ngày lập: " + ngayLapStr, normalFont));
			document.add(new Paragraph("Nhân viên: " + tenNV, normalFont));
			document.add(new Paragraph("Khách hàng: " + tenKH, normalFont));
			document.add(new Paragraph("SĐT: " + sdt, normalFont));
			document.add(new Paragraph("Thanh toán: " + kieuTT, normalFont));

			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 2f, 4f, 2f, 3f, 3f });

			String[] headers = { "Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền" };
			for (String h : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				table.addCell(cell);
			}

			for (ChiTietHoaDon_DTO ct : danhSachCTHD) {
				table.addCell(new Phrase(ct.getMaMonAn(), normalFont));
				table.addCell(new Phrase(ct.getTenMonAn(), normalFont));

				PdfPCell slCell = new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), normalFont));
				slCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(slCell);

				table.addCell(new Phrase(formatTienVN(ct.getDonGia()), normalFont));

				table.addCell(new Phrase(formatTienVN(ct.getThanhTien()), normalFont));
			}

			document.add(table);

			document.add(new Paragraph(" "));
			document.add(new Paragraph("Tổng thanh toán: " + formatTienVN(tong), headerFont));
			document.add(new Paragraph("Tiền thừa: " + formatTienVN(tienThua), headerFont));

			Paragraph thanks = new Paragraph("Cảm ơn quý khách đã sử dụng dịch vụ!", normalFont);
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
