package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "HangKhachHang")
@NamedQueries({
    @NamedQuery(name = "HangKhachHang.findAll", query = "SELECT h FROM HangKhachHang h"),
    @NamedQuery(name = "HangKhachHang.findByMaHang", query = "SELECT h FROM HangKhachHang h WHERE h.maHang = :maHang")
})
public class HangKhachHang {

    @Id
    @Column(name = "maHang", nullable = false, length = 20)
    private String maHang;

    @Column(name = "tenHang", nullable = false, length = 50)
    private String tenHang;

    @Column(name = "diemHang", nullable = false)
    private int diemHang;

    @Column(name = "giamGia", nullable = false)
    private int giamGia;

    @Column(name = "moTa", length = 200)
    private String moTa;

    // ===== Constructors =====
    public HangKhachHang() {
        super();
    }

    public HangKhachHang(String maHang, String tenHang, int diemHang, int giamGia, String moTa) {
        super();
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.diemHang = diemHang;
        this.giamGia = giamGia;
        this.moTa = moTa;
    }

    // ===== Getters & Setters =====
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

    public int getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(int giamGia) {
        this.giamGia = giamGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // ===== toString() =====
//    @Override
//    public String toString() {
//        return "HangKhachHang [maHang=" + maHang + ", tenHang=" + tenHang + ", diemHang=" + diemHang
//                + ", giamGia=" + giamGia + ", moTa=" + moTa + "]";
//    }
    @Override
    public String toString() {
        return tenHang;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diemHang;
		result = prime * result + giamGia;
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
		if (giamGia != other.giamGia)
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
    
}
