package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "HoaDon.list",
        query = "SELECT hd FROM HoaDon hd"
    ),
    @NamedQuery(
        name = "HoaDon.count",
        query = "SELECT COUNT(hd.maHoaDon) FROM HoaDon hd"
    ),
    @NamedQuery(
        name = "HoaDon.trangThai",
        query = "SELECT hd FROM HoaDon hd WHERE hd.trangThai = :status"
    )
})
public class HoaDon {
    @Id
    @Column(name = "maHoaDon", nullable = false, length = 20)
    private String maHoaDon;

    @Column(name = "ngayLap", nullable = false)
    private Date ngayLap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKM", nullable = true)
    private KhuyenMai khuyenMai;

    @Column(name = "trangThai", nullable = false, length = 50)
    private String trangThai;

    @Column(name = "kieuThanhToan", nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean kieuThanhToan; // true = tiền mặt, false = chuyển khoản

    // -------------------------
    // Constructors
    // -------------------------
    public HoaDon() {}

    public HoaDon(String maHoaDon, Date ngayLap, KhachHang khachHang, NhanVien nhanVien,
                  KhuyenMai khuyenMai, String trangThai, boolean kieuThanhToan) {
        super();
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.khuyenMai = khuyenMai;
        this.trangThai = trangThai;
        this.kieuThanhToan = kieuThanhToan;
    }

    // -------------------------
    // Getters & Setters
    // -------------------------
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

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isKieuThanhToan() {
        return kieuThanhToan;
    }

    public void setKieuThanhToan(boolean kieuThanhToan) {
        this.kieuThanhToan = kieuThanhToan;
    }

    // -------------------------
    // hashCode & equals
    // -------------------------
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

    // -------------------------
    // toString()
    // -------------------------
    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon 
            + ", ngayLap=" + ngayLap 
            + ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null")
            + ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNV() : "null")
            + ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKM() : "null")
            + ", trangThai=" + trangThai
            + ", kieuThanhToan=" + kieuThanhToan + "]";
    }
}
