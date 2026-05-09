package dto;

import java.io.Serializable;
import java.sql.Date;

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
public class KhuyenMai_DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maKM;

	private String tenKM;

	private String loaiKM;

	private Date ngayBatDau;

	private Date ngayKetThuc;

	private int phanTramGiamGia;

}
