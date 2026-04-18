package dto;

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
public class NhanVien_DTO {

	private String maNV;

	private String tenNV;

	private String chucVu;

	private String email;

	private Date namSinh;

	private String diaChi;

	private boolean gioiTinh;

	private Date ngayVaoLam;

	private boolean trangThai;

}
