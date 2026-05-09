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
public class ChiTietHoaDon_DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maHoaDon;

	private String maMonAn;

	private int soLuong;

	private double thanhTien;

	private String tenMonAn;

	private double donGia;

}
