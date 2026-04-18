package dto;

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
public class HangKhachHang {

	private String maHang;

	private String tenHang;

	private int diemHang;

	private double giamGia;

	private String moTa;

}
