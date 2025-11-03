package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "HoaDon.list",
        query = "SELECT HD FROM HoaDon HD"
    ),
    @NamedQuery(
        name = "HoaDon.count",
        query = "SELECT COUNT(HD.maHoaDon) FROM HoaDon HD"
    ),
    @NamedQuery(
        name = "HoaDon.trangThai",
        query = "SELECT HD FROM HoaDon HD WHERE HD.trangThai = :status"
    )
})
public class HoaDon {
    @Id
    @Column(name = "maHoaDon", nullable = false, length = 20)
    private String maHoaDon;

    @Column(name = "ngayLap", nullable = false)
    private Date ngayLap;

    @Column(name = "tongTien", nullable = false)
    private double tongTien;
    
    @Column(name = "thue", nullable = false)
    private double thue;
    
    @Column(name = "trangThai", nullable = false, length = 50)
    private String trangThai;

//    @Column(name = "kieuThanhToan", nullable = false, columnDefinition = "BIT DEFAULT 1")
//    private boolean kieuThanhToan; // true = tiền mặt, false = chuyển khoản
    
    @Column(name = "kieuThanhToan", nullable = false, length = 50)
    private String kieuThanhToan;
    
    @Column(name = "tienNhan", nullable = false)
    private double tienNhan;
    
    @Column(name = "tienThua", nullable = false)
    private double tienThua;

    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maKM", referencedColumnName = "maKM", nullable = false)
    private KhuyenMai khuyenMai;
    
    @ManyToOne
    @JoinColumn(name = "maNV", referencedColumnName = "maNV", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maBan", referencedColumnName = "maBan", nullable = false)
    private Ban ban;
    
    @ManyToOne
    @JoinColumn(name = "maCoc", referencedColumnName = "maCoc", nullable = false)
    private Coc coc;
    
    public HoaDon() {
    	
    }

	public HoaDon(String maHoaDon, Date ngayLap, double tongTien, double thue, String trangThai, String kieuThanhToan,
			double tienNhan, double tienThua, KhachHang khachHang, KhuyenMai khuyenMai, NhanVien nhanVien, Ban ban,
			Coc coc) {
		this.maHoaDon = maHoaDon;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
		this.thue = thue;
		this.trangThai = trangThai;
		this.kieuThanhToan = kieuThanhToan;
		this.tienNhan = tienNhan;
		this.tienThua = tienThua;
		this.khachHang = khachHang;
		this.khuyenMai = khuyenMai;
		this.nhanVien = nhanVien;
		this.ban = ban;
		this.coc = coc;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public Date getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(Date ngayLap) {
		this.ngayLap = ngayLap;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}

	public double getThue() {
		return thue;
	}

	public void setThue(double thue) {
		this.thue = thue;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getKieuThanhToan() {
		return kieuThanhToan;
	}

	public void setKieuThanhToan(String kieuThanhToan) {
		this.kieuThanhToan = kieuThanhToan;
	}

	public double getTienNhan() {
		return tienNhan;
	}

	public void setTienNhan(double tienNhan) {
		this.tienNhan = tienNhan;
	}

	public double getTienThua() {
		return tienThua;
	}

	public void setTienThua(double tienThua) {
		this.tienThua = tienThua;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public Ban getBan() {
		return ban;
	}

	public void setBan(Ban ban) {
		this.ban = ban;
	}

	public Coc getCoc() {
		return coc;
	}

	public void setCoc(Coc coc) {
		this.coc = coc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maHoaDon == null) ? 0 : maHoaDon.hashCode());
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
		HoaDon other = (HoaDon) obj;
		if (maHoaDon == null) {
			if (other.maHoaDon != null)
				return false;
		} else if (!maHoaDon.equals(other.maHoaDon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", tongTien=" + tongTien + ", thue=" + thue
				+ ", trangThai=" + trangThai + ", kieuThanhToan=" + kieuThanhToan + ", tienNhan=" + tienNhan
				+ ", tienThua=" + tienThua + ", khachHang=" + khachHang + ", khuyenMai=" + khuyenMai + ", nhanVien="
				+ nhanVien + ", ban=" + ban + ", coc=" + coc + "]";
	}

    
}
