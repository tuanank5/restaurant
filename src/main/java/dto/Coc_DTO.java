package dto;

import lombok.*;

import java.io.Serializable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Coc_DTO implements Serializable {

	private String maCoc;

	private boolean loaiCoc;

	private int phanTramCoc;

	private double soTienCoc;

}
