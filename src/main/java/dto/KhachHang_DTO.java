package dto;

import java.io.Serializable;

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
public class KhachHang_DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maKH;

	private String tenKH;

	private String sdt;

	private String email;

	private String diaChi;

	@Builder.Default
	private int diemTichLuy = 0;

	private String maHangKhachHang;

}
