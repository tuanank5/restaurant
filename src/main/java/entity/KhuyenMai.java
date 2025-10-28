package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "KhuyenMai")
@NamedQueries({
    @NamedQuery(name = "KhuyenMai.findAll", query = "SELECT km FROM KhuyenMai km"),
    @NamedQuery(name = "KhuyenMai.findByMaKM", query = "SELECT km FROM KhuyenMai km WHERE km.maKM = :maKM")
})
public class KhuyenMai {

    @Id
    @Column(name = "maKM", nullable = false, length = 20)
    private String maKM;

    @Column(name = "tenKM", nullable = false, length = 100)
    private String tenKM;

    @Column(name = "loaiKM", nullable = false, length = 50)
    private String loaiKM;

    @Column(name = "sanPhamKM", length = 100)
    private String sanPhamKM;

    @Column(name = "ngayBatDau", nullable = false)
    private Date ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private Date ngayKetThuc;

    @Column(name = "phanTramGiamGia", nullable = false)
    private int phanTramGiamGia;

    // ===== Constructors =====
    public KhuyenMai() {
        super();
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

    // ===== Getters & Setters =====
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

    // ===== toString() =====
    @Override
    public String toString() {
        return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", loaiKM=" + loaiKM + ", sanPhamKM=" + sanPhamKM
                + ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", phanTramGiamGia="
                + phanTramGiamGia + "]";
    }
}
