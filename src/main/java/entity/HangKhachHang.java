package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@NamedQueries({ @NamedQuery(name = "HangKhachHang.findAll", query = "SELECT H FROM HangKhachHang H"),
		@NamedQuery(name = "HangKhachHang.findByMaHang", query = "SELECT H FROM HangKhachHang H WHERE H.maHang = :maHang") })
public class HangKhachHang {
	@Id
	@Column(name = "maHang", nullable = false, length = 20)
	private String maHang;

	@Column(name = "tenHang", nullable = false, length = 100)
	private String tenHang;

	@Column(name = "diemHang", nullable = false)
	private int diemHang;

	@Column(name = "giamGia", nullable = false)
	private double giamGia;

	@Column(name = "moTa", length = 200)
	private String moTa;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diemHang;
		long temp;
		temp = Double.doubleToLongBits(giamGia);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((maHang == null) ? 0 : maHang.hashCode());
		result = prime * result + ((moTa == null) ? 0 : moTa.hashCode());
		result = prime * result + ((tenHang == null) ? 0 : tenHang.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HangKhachHang other = (HangKhachHang) obj;
		if (diemHang != other.diemHang)
			return false;
		if (Double.doubleToLongBits(giamGia) != Double.doubleToLongBits(other.giamGia))
			return false;
		if (maHang == null) {
			if (other.maHang != null)
				return false;
		} else if (!maHang.equals(other.maHang))
			return false;
		if (moTa == null) {
			if (other.moTa != null)
				return false;
		} else if (!moTa.equals(other.moTa))
			return false;
		if (tenHang == null) {
			if (other.tenHang != null)
				return false;
		} else if (!tenHang.equals(other.tenHang))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.tenHang;
	}

}
