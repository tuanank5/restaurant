package dto;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

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

}
