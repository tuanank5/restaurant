package dto;

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
public class Coc_DTO {

	private String maCoc;

	private boolean loaiCoc;

	private int phanTramCoc;

	private double soTienCoc;

}
