package dto;

import java.sql.Date;

import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
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
public class HoaDon {

	private String maHD;

	private Date ngayLap;

	private double tongTien;

	private double thue;

	private String trangThai;

	private String kieuThanhToan;

	private double tienNhan;

	private double tienThua;

	private KhachHang khachHang;

	private KhuyenMai khuyenMai;

	private NhanVien nhanVien;

	private DonDatBan donDatBan;

	private Coc coc;

}
