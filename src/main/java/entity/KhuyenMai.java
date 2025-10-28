package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedQueries;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "KhuyenMai.list",
        query = "select km from KhuyenMai km"
    ),
    @NamedQuery(
        name = "KhuyenMai.count",
        query = "select count(maKM) from KhuyenMai"
    )
})

public class KhuyenMai {
    @Id
    @Column(name = "maKM", nullable = false, length = 20)
    private String maKM;

    @Column(name = "tenKM", nullable = false, length = 100)
    private String tenKM;

    @Column(name = "loaiKM", nullable = false, length = 50)
    private String loaiKM;

    @Column(name = "sanPhamKM", nullable = false, length = 50)
    private String sanPhamKM;

    @Column(name = "ngayBatDau", nullable = false)
    private Date ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private Date ngayKetThuc;

    @Column(name = "phanTramGiamGia", nullable = false)
    private int phanTramGiamGia;

	public KhuyenMai() {
		
	}

	public KhuyenMai(String maKM, String tenKM, String loaiKM, String sanPhamKM, Date ngayBatDau, Date ngayKetThuc,
			int phanTramGiamGia) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.loaiKM = loaiKM;
		this.sanPhamKM = sanPhamKM;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.phanTramGiamGia = phanTramGiamGia;
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public String getLoaiKM() {
		return loaiKM;
	}

	public void setLoaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public String getSanPhamKM() {
		return sanPhamKM;
	}

	public void setSanPhamKM(String sanPhamKM) {
		this.sanPhamKM = sanPhamKM;
	}

	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public int getPhanTramGiamGia() {
		return phanTramGiamGia;
	}

	public void setPhanTramGiamGia(int phanTramGiamGia) {
		this.phanTramGiamGia = phanTramGiamGia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loaiKM == null) ? 0 : loaiKM.hashCode());
		result = prime * result + ((maKM == null) ? 0 : maKM.hashCode());
		result = prime * result + ((ngayBatDau == null) ? 0 : ngayBatDau.hashCode());
		result = prime * result + ((ngayKetThuc == null) ? 0 : ngayKetThuc.hashCode());
		result = prime * result + phanTramGiamGia;
		result = prime * result + ((sanPhamKM == null) ? 0 : sanPhamKM.hashCode());
		result = prime * result + ((tenKM == null) ? 0 : tenKM.hashCode());
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
		KhuyenMai other = (KhuyenMai) obj;
		if (loaiKM == null) {
			if (other.loaiKM != null)
				return false;
		} else if (!loaiKM.equals(other.loaiKM))
			return false;
		if (maKM == null) {
			if (other.maKM != null)
				return false;
		} else if (!maKM.equals(other.maKM))
			return false;
		if (ngayBatDau == null) {
			if (other.ngayBatDau != null)
				return false;
		} else if (!ngayBatDau.equals(other.ngayBatDau))
			return false;
		if (ngayKetThuc == null) {
			if (other.ngayKetThuc != null)
				return false;
		} else if (!ngayKetThuc.equals(other.ngayKetThuc))
			return false;
		if (phanTramGiamGia != other.phanTramGiamGia)
			return false;
		if (sanPhamKM == null) {
			if (other.sanPhamKM != null)
				return false;
		} else if (!sanPhamKM.equals(other.sanPhamKM))
			return false;
		if (tenKM == null) {
			if (other.tenKM != null)
				return false;
		} else if (!tenKM.equals(other.tenKM))
			return false;
		return true;
	}
	
}


