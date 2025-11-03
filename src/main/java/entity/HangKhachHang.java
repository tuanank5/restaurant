package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "HangKhachHang.findAll",
        query = "SELECT H FROM HangKhachHang H"
    ),
    @NamedQuery(
        name = "HangKhachHang.findByMaHang",
        query = "SELECT H FROM HangKhachHang H WHERE H.maHang = :maHang"
    )
})
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

    public HangKhachHang() {
    	
    }

    public HangKhachHang(String maHang, String tenHang, int diemHang, double giamGia, String moTa) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.diemHang = diemHang;
        this.giamGia = giamGia;
        this.moTa = moTa;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public int getDiemHang() {
        return diemHang;
    }

    public void setDiemHang(int diemHang) {
        this.diemHang = diemHang;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

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
