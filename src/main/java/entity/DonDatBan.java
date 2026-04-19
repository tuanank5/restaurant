package entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@EqualsAndHashCode(of = "maDatBan")
@Entity
@Table(name = "DonDatBan")
@NamedQueries({ @NamedQuery(name = "DonDatBan.list", query = "SELECT DDB FROM DonDatBan DDB"),
		@NamedQuery(name = "DonDatBan.count", query = "SELECT COUNT(DDB.maDatBan) FROM DonDatBan DDB") })
public class DonDatBan {

	@Id
	@Column(name = "maDatBan", nullable = false, length = 20)
	private String maDatBan;

	@Column(name = "ngayGio", nullable = false)
	private LocalDateTime ngayGioLapDon;

	@Column(name = "soLuong", nullable = false)
	private int soLuong;

	@ManyToOne
	@JoinColumn(name = "maBan", referencedColumnName = "maBan", nullable = false)
	private Ban ban;

	@Column(name = "gioBatDau", nullable = false)
	private LocalTime gioBatDau;

	@Column(name = "trangThai")
	private String trangThai;

}
