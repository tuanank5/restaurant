package dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
