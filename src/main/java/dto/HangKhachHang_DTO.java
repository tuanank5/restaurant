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

	private static final long serialVersionUID = 1L;

	private String maHang;

	private String tenHang;

	private int diemHang;

	private double giamGia;

	private String moTa;

}
