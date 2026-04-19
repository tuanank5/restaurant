package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(of = "maCoc")
@Entity
@Table(name = "Coc")
public class Coc {
	@Id
	@Column(name = "maCoc", nullable = false, length = 20)
	private String maCoc;

	@Column(name = "loaiCoc", nullable = false)
	private boolean loaiCoc;

	@Column(name = "phanTramCoc", nullable = false)
	private int phanTramCoc;

	@Column(name = "soTienCoc", nullable = false)
	private double soTienCoc;

}
