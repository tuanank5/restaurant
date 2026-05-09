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
public class LoaiBan_DTO implements Serializable {

	private String maLoaiBan;

	private String tenLoaiBan;

	private int soLuong;

}
