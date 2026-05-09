package entity;

import java.sql.Date;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "maKM")
@Entity
@NamedQueries({ @NamedQuery(name = "KhuyenMai.list", query = "SELECT KM FROM KhuyenMai KM"),
		@NamedQuery(name = "KhuyenMai.count", query = "SELECT COUNT(maKM) FROM KhuyenMai") })
public class KhuyenMai {
	@Id
	@Column(name = "maKM", nullable = false, length = 20)
	private String maKM;

	@Column(name = "tenKM", nullable = false, length = 100)
	private String tenKM;

	@Column(name = "loaiKM", nullable = false, length = 50)
	private String loaiKM;

	@Column(name = "ngayBatDau", nullable = false)
	private Date ngayBatDau;

	@Column(name = "ngayKetThuc", nullable = false)
	private Date ngayKetThuc;

	@Column(name = "phanTramGiamGia", nullable = false)
	private int phanTramGiamGia;

	@Override
	public String toString() {
		String loai = loaiKM != null ? loaiKM : "";
		return loai + " — " + phanTramGiamGia + "%";
	}

}
