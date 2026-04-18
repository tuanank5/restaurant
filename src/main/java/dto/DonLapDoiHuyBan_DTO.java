package dto;

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
public class DonLapDoiHuyBan_DTO {

	private DonDatBan_DTO donDatBan;

	private String lyDo;

	private Date ngayGioLapDon;

	private double tienHoanTra;

}
