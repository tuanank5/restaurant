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
    @ManyToOne
    @JoinColumn(name = "maHD", referencedColumnName = "maHD", nullable = false)
    private HoaDon hoaDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "maMon", referencedColumnName = "maMon", nullable = false)
    private MonAn monAn;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;
    
    //thanhTien cần xóa
    @Column(name = "thanhTien", nullable = false)
    private double thanhTien;
    
	public ChiTietHoaDon() {
		
	}

	public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong, double thanhTien) {
		this.hoaDon = hoaDon;
		this.monAn = monAn;
		this.soLuong = soLuong;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hoaDon == null) ? 0 : hoaDon.hashCode());
		result = prime * result + ((monAn == null) ? 0 : monAn.hashCode());
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
		if (hoaDon == null) {
			if (other.hoaDon != null)
				return false;
		} else if (!hoaDon.equals(other.hoaDon))
			return false;
		if (monAn == null) {
			if (other.monAn != null)
				return false;
		} else if (!monAn.equals(other.monAn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + hoaDon + ", monAn=" + monAn + ", soLuong=" + soLuong + ", thanhTien="
				+ thanhTien + "]";
	}

	
}
