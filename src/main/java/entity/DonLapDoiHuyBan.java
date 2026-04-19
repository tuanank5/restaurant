package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DonLapDoiHuyBan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DonLapDoiHuyBan {
	@Id
	@ManyToOne
	@JoinColumn(name = "maDatBan", referencedColumnName = "maDatBan", nullable = false)
	private DonDatBan donDatBan;

	@Column(name = "lyDo", nullable = false, length = 200)
	private String lyDo;

	@Column(name = "ngayGioLapDon", nullable = false)
	private Date ngayGioLapDon;

	@Column(name = "tienHoanTra", nullable = false)
	private double tienHoanTra;
}
