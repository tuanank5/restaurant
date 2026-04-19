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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NamedQueries({ @NamedQuery(name = "ChiTietHoaDon.list", query = "SELECT CTHD FROM ChiTietHoaDon CTHD") })
public class ChiTietHoaDon {
	@Id
	@ManyToOne
	@JoinColumn(name = "maHD", referencedColumnName = "maHD", nullable = false)
	private HoaDon hoaDon;

	@Id
	@ManyToOne
	@JoinColumn(name = "maMon", referencedColumnName = "maMon", nullable = false)
	private MonAn monAn;

	@EqualsAndHashCode.Include
	public String getMaHD() {
		return hoaDon != null ? hoaDon.getMaHD() : null;
	}

	@EqualsAndHashCode.Include
	public String getMaMon() {
		return monAn != null ? monAn.getMaMon() : null;
	}

	@Column(name = "soLuong", nullable = false)
	private int soLuong;

	// thanhTien cần xóa
	@Column(name = "thanhTien", nullable = false)
	private double thanhTien;

}
