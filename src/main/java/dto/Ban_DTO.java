package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Ban_DTO implements Serializable {

	private String maBan;

	private String viTri;

	private String trangThai;
	private String maLoaiBan;
	private String tenLoaiBan;
}
