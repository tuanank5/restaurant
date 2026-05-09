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
public class KhuyenMai_DTO implements Serializable {

	private String maKM;

	private String tenKM;

	private String loaiKM;

	private Date ngayBatDau;

	private Date ngayKetThuc;

	private int phanTramGiamGia;

}
