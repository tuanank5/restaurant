package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HangKhachHang_DTO implements Serializable {

	private String maHang;

	private String tenHang;

	private int diemHang;

	private double giamGia;

	private String moTa;

}
