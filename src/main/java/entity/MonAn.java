package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MonAn")
public class MonAn {
	@Id
    @Column(name = "maMon", nullable = false, length = 20)
    private String maMon;
	
    @Column(name = "tenMon", nullable = false, length = 100)
    private String tenTaiKhoan;

    @Column(name = "donGia", nullable = false)
    private double donGia;

    @ManyToOne
    @JoinColumn(name = "maKM", referencedColumnName = "maKM", nullable = false)
    private KhuyenMai khuyenMai;

	public MonAn() {

	}

	public MonAn(String maMon, String tenTaiKhoan, double donGia, KhuyenMai khuyenMai) {
		this.maMon = maMon;
		this.tenTaiKhoan = tenTaiKhoan;
		this.donGia = donGia;
		this.khuyenMai = khuyenMai;
	}

	public String getMaMon() {
		return maMon;
	}

	public void setMaMon(String maMon) {
		this.maMon = maMon;
	}

	public String getTenTaiKhoan() {
		return tenTaiKhoan;
	}

	public void setTenTaiKhoan(String tenTaiKhoan) {
		this.tenTaiKhoan = tenTaiKhoan;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maMon == null) ? 0 : maMon.hashCode());
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
		MonAn other = (MonAn) obj;
		if (maMon == null) {
			if (other.maMon != null)
				return false;
		} else if (!maMon.equals(other.maMon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MonAn [maMon=" + maMon + ", tenTaiKhoan=" + tenTaiKhoan + ", donGia=" + donGia + ", khuyenMai="
				+ khuyenMai + "]";
	}
}
