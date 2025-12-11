package entity;

import java.util.List;

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
        name = "KhachHang.findAll",
        query = "SELECT KH FROM KhachHang KH"
    ),
    @NamedQuery(
        name = "KhachHang.findByMaKH",
        query = "SELECT KH FROM KhachHang KH WHERE KH.maKH = :maKH"
    )
})
public class KhachHang {

    @Id
    @Column(name = "maKH", nullable = false, length = 20)
    private String maKH;

    @Column(name = "tenKH", nullable = false, length = 100)
    private String tenKH;

    @Column(name = "sdt", nullable = false, length = 20)
    private String sdt;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "diaChi", length = 200)
    private String diaChi;

    @Column(name = "diemTichLuy")
    private int diemTichLuy;

    @ManyToOne
    @JoinColumn(name = "maHang", referencedColumnName = "maHang", nullable = true)
    private HangKhachHang hangKhachHang;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String sdt, String email, String diaChi,
                     int diemTichLuy, HangKhachHang hangKhachHang) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.diemTichLuy = diemTichLuy;
        this.hangKhachHang = hangKhachHang;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public HangKhachHang getHangKhachHang() {
        return hangKhachHang;
    }

    public void setHangKhachHang(HangKhachHang hangKhachHang) {
        this.hangKhachHang = hangKhachHang;
    }

    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt +
               ", email=" + email + ", diaChi=" + diaChi + ", diemTichLuy=" +
               diemTichLuy + ", hangKhachHang=" + hangKhachHang + "]";
    }
}
