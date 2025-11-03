package entity;

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
        name = "Ban.list",
        query = "SELECT B FROM Ban B"
    ),
    @NamedQuery(
        name = "Ban.count",
        query = "SELECT COUNT(maBan) FROM Ban"
    )
})
public class Ban {

    @Id
    @Column(name = "maBan", nullable = false, length = 20)
    private String maBan;

    @Column(name = "viTri", nullable = false, length = 100)
    private String viTri;

    @Column(name = "trangThai", nullable = false, length = 50)
    private String trangThai;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;   // 🆕 Thêm số lượng riêng cho mỗi bàn

    @ManyToOne
    @JoinColumn(name = "maLoaiBan", referencedColumnName = "maLoaiBan", nullable = false)
    private LoaiBan loaiBan;

    // --- Constructors ---
    public Ban() {
    }

    public Ban(String maBan, String viTri, String trangThai, int soLuong, LoaiBan loaiBan) {
        this.maBan = maBan;
        this.viTri = viTri;
        this.trangThai = trangThai;
        this.soLuong = soLuong;
        this.loaiBan = loaiBan;
    }

    // --- Getters & Setters ---
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }

    // --- hashCode, equals, toString ---
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maBan == null) ? 0 : maBan.hashCode());
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
        Ban other = (Ban) obj;
        if (maBan == null) {
            if (other.maBan != null)
                return false;
        } else if (!maBan.equals(other.maBan))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Ban [maBan=" + maBan + ", viTri=" + viTri + ", trangThai=" + trangThai +
               ", soLuong=" + soLuong + ", loaiBan=" + loaiBan + "]";
    }
}
