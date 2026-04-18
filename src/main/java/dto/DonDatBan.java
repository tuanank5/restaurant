package dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class DonDatBan {

	private String maDatBan;

	private LocalDateTime ngayGioLapDon;

	private int soLuong;

	private Ban ban;

	private LocalTime gioBatDau;

	private String trangThai;

}
