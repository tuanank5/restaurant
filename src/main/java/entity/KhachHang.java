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
import jakarta.persistence.Table;

@Entity
@Table(name = "KhachHang")
@NamedQueries({
    @NamedQuery(name = "KhachHang.findAll", query = "SELECT kh FROM KhachHang kh"),
    @NamedQuery(name = "KhachHang.findByMaKH", query = "SELECT kh FROM KhachHang kh WHERE kh.maKH = :maKH")
})
public class KhachHang {

    @Id
    @Column(name = "maKH", nullable = false, length = 20)
    private String maKH;

    @Column(name = "tenKH", nullable = false, length = 100)
    private String tenKH;

    @Column(name = "sdt", nullable = false, length = 15)
    private String sdt;

    @Column(name = "diaChi", length = 200)
    private String diaChi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHang", nullable = false)
    private HangKhachHang hangKhachHang;

    @Column(name = "diemTichLuy")
    private int diemTichLuy;

    @Column(name = "ngayDangKy")
    private Date ngayDangKy;

    // ===== Constructors =====
    public KhachHang() {
        super();
    }

    public KhachHang(String maKH, String tenKH, String sdt, String diaChi, HangKhachHang hangKhachHang,
            int diemTichLuy, Date ngayDangKy) {
        super();
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.hangKhachHang = hangKhachHang;
        this.diemTichLuy = diemTichLuy;
        this.ngayDangKy = ngayDangKy;
    }

    // ===== Getters & Setters =====
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public HangKhachHang getHangKhachHang() {
        return hangKhachHang;
    }

    public void setHangKhachHang(HangKhachHang hangKhachHang) {
        this.hangKhachHang = hangKhachHang;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    // ===== toString() =====
    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt + ", diaChi=" + diaChi
                + ", hangKhachHang=" + (hangKhachHang != null ? hangKhachHang.getTenHang() : "null")
                + ", diemTichLuy=" + diemTichLuy + ", ngayDangKy=" + ngayDangKy + "]";
    }
}
