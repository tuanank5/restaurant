package dto;

import lombok.*;

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

}
