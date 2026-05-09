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
public class DonLapDoiHuyBan_DTO implements Serializable {

	private String maDonDatBan;

	private String lyDo;

	private Date ngayGioLapDon;

	private double tienHoanTra;

}
