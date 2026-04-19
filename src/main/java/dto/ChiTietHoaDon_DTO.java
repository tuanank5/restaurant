package dto;

import entity.HoaDon;
import entity.MonAn;
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
public class ChiTietHoaDon_DTO {

	private HoaDon hoaDon;

	private MonAn monAn;

	private int soLuong;

	// thanhTien cần xóa
	private double thanhTien;

}
