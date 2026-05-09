package dto;

import java.io.Serializable;

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
public class Ban_DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maBan;

	private String viTri;

	private String trangThai;

	private String maLoaiBan;

}
