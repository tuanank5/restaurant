package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of = "maBan")
@Entity
@NamedQueries({ @NamedQuery(name = "Ban.list", query = "SELECT B FROM Ban B"),
		@NamedQuery(name = "Ban.count", query = "SELECT COUNT(maBan) FROM Ban") })
public class Ban {

	@Id
	@Column(name = "maBan", nullable = false, length = 20)
	private String maBan;

	@Column(name = "viTri", nullable = false, length = 100)
	private String viTri;

	@Column(name = "trangThai", nullable = false, length = 50)
	private String trangThai;

	@ManyToOne
	@JoinColumn(name = "maLoaiBan", referencedColumnName = "maLoaiBan", nullable = false)
	private LoaiBan loaiBan;

}
