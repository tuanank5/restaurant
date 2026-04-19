package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = "maLoaiBan")
@Entity
@NamedQueries({ @NamedQuery(name = "LoaiBan.list", query = "SELECT LB FROM LoaiBan LB"),
		@NamedQuery(name = "LoaiBan.count", query = "SELECT COUNT(LB.maLoaiBan) FROM LoaiBan LB") })
public class LoaiBan {

	@Id
	@Column(name = "maLoaiBan", nullable = false, length = 20)
	private String maLoaiBan;

	@Column(name = "tenLoaiBan", nullable = false, length = 50)
	private String tenLoaiBan;

	@Column(name = "soLuong", nullable = false)
	private int soLuong;

}
