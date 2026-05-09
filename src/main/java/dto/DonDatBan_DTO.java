package dto;

import java.io.Serializable;
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
public class DonDatBan_DTO implements Serializable {

	private String maDatBan;

	private LocalDateTime ngayGioLapDon;

	private int soLuong;

	private Ban_DTO ban;

	private LocalTime gioBatDau;

	private String trangThai;

}
