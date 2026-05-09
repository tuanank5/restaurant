package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MonAn_DTO implements Serializable {

	private String maMon;

	private String tenMon;

	private double donGia;

	private String duongDanAnh;

	private String loaiMon;

}