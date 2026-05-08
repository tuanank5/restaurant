package dto;

import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HoaDon_DTO implements Serializable {

	private String maHD;

	private Date ngayLap;

	private double tongTien;

	private double thue;

	private String trangThai;

	private String kieuThanhToan;

	private double tienNhan;

	private double tienThua;

	private String maKhachHang;

	private String maKhuyenMai;

	private String maNhanVien;

	private String maDonDatBan;

	private String maCoc;

	private NhanVien_DTO nhanVien;
	private KhachHang_DTO khachHang;
	private DonDatBan_DTO donDatBan;

}
