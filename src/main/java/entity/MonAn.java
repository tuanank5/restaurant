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
    private String tenMon;

    @Column(name = "donGia", nullable = false)
    private double donGia;

    @ManyToOne
    @JoinColumn(name = "maKM", referencedColumnName = "maKM", nullable = true)
    private KhuyenMai khuyenMai;

    @Column(name = "duongDanAnh", length = 255) // Thêm cột lưu đường dẫn ảnh
    private String duongDanAnh;

    @Column(name = "loaiMon", length = 100)
    private String loaiMon;
 
    public MonAn() {
        // constructor rỗng cho JPA
    }

    public MonAn(String maMon, String tenMon, double donGia,
            KhuyenMai khuyenMai, String duongDanAnh, String loaiMon) {
   this.maMon = maMon;
   this.tenMon = tenMon;
   this.donGia = donGia;
   this.khuyenMai = khuyenMai;
   this.duongDanAnh = duongDanAnh;
   this.loaiMon = loaiMon;
}

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
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

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }
    
    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }


    @Override
    public int hashCode() {
        return maMon == null ? 0 : maMon.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MonAn)) return false;
        MonAn other = (MonAn) obj;
        return maMon != null && maMon.equals(other.maMon);
    }

    @Override
    public String toString() {
        return "MonAn [maMon=" + maMon +
                ", tenMon=" + tenMon +
                ", donGia=" + donGia +
                ", khuyenMai=" + khuyenMai +
                ", duongDanAnh=" + duongDanAnh +
                ", loaiMon=" + loaiMon + "]";
    }

}