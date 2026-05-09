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
public class DonLapDoiHuyBan_DTO implements Serializable {

	private String maDonDatBan;

	private String lyDo;

	private Date ngayGioLapDon;

	private double tienHoanTra;

}
