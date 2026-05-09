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
    private static final long serialVersionUID = 1L;

    private String maHoaDon;
	private String maMonAn;
	private String tenMon;
	private double donGia;
	private int soLuong;

	private double thanhTien;

	private String tenMonAn;

	private double donGia;

}
