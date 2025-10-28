package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "Ban.list",
        query = "select b from Ban b"
    ),
    @NamedQuery(
        name = "Ban.count",
        query = "select count(b.maBan) from Ban b"
    ),
    @NamedQuery(
        name = "Ban.state",
        query = "select b from Ban b where b.trangThai = :status"
    )
})
public class QuanLyBan {

    @Id
    @Column(name = "maBan", nullable = false, length = 20)
    private String maBan;

    @Column(name = "viTri", nullable = false, length = 100)
    private String viTri;

    @Column(name = "trangThai", nullable = false, length = 50)
    private String trangThai; // "Trống", "Đang dùng", "Đặt trước"

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiBan", nullable = false, length = 50)
    private LoaiBan loaiBan;

    public QuanLyBan() {
    }

    public QuanLyBan(String maBan, String viTri, String trangThai, LoaiBan loaiBan) {
        this.maBan = maBan;
        this.viTri = viTri;
        this.trangThai = trangThai;
        this.loaiBan = loaiBan;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }

    @Override
    public int hashCode() {
        return maBan == null ? 0 : maBan.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuanLyBan)) return false;
        QuanLyBan other = (QuanLyBan) obj;
        return maBan != null && maBan.equals(other.maBan);
    }
}
