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
public class NhanVien_DTO implements Serializable {

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
