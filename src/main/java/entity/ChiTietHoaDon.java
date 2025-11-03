package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {
	@Id
    @Column(name = "maHD", nullable = false, length = 20)
    private String maHD;

    @Id
    @Column(name = "maMon", nullable = false, length = 20)
    private String maMon;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;
    //thanhTien cần xóa
    @Column(name = "thanhTien", nullable = false)
    private double thanhTien;
    
    @ManyToOne
    @JoinColumn(name = "maHD", referencedColumnName = "maHD", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "maMon", referencedColumnName = "maMon", nullable = false)
    private MonAn monAn;

	public ChiTietHoaDon() {
		
	}

	public ChiTietHoaDon(String maHD, String maMon, int soLuong, double thanhTien, HoaDon hoaDon, MonAn monAn) {
		this.maHD = maHD;
		this.maMon = maMon;
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
		this.hoaDon = hoaDon;
		this.monAn = monAn;
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public String getMaMon() {
		return maMon;
	}

	public void setMaMon(String maMon) {
		this.maMon = maMon;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public MonAn getMonAn() {
		return monAn;
	}

	public void setMonAn(MonAn monAn) {
		this.monAn = monAn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maHD == null) ? 0 : maHD.hashCode());
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
		ChiTietHoaDon other = (ChiTietHoaDon) obj;
		if (maHD == null) {
			if (other.maHD != null)
				return false;
		} else if (!maHD.equals(other.maHD))
			return false;
		if (maMon == null) {
			if (other.maMon != null)
				return false;
		} else if (!maMon.equals(other.maMon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [maHD=" + maHD + ", maMon=" + maMon + ", soLuong=" + soLuong + ", thanhTien=" + thanhTien
				+ ", hoaDon=" + hoaDon + ", monAn=" + monAn + "]";
	}
    
    
}
