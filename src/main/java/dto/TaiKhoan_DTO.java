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
public class TaiKhoan_DTO implements Serializable {

	private String maTaiKhoan;

	private String tenTaiKhoan;

	private String matKhau;

	private Date ngayDangNhap;

	private Date ngayDangXuat;

	private Date ngaySuaDoi;
	
	private String maNhanVien;
	
	private String tenNhanVien;

	private String chucVu;

}
