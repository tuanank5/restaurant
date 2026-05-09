package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChiTietHoaDon_DTO implements Serializable {

	private String maHoaDon;

	private String maMonAn;

	private int soLuong;

	// thanhTien cần xóa
	private double thanhTien;

}
