package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "LoaiBan.list",
        query = "select lb from LoaiBan lb"
    ),
    @NamedQuery(
        name = "LoaiBan.count",
        query = "select count(lb.maLoaiBan) from LoaiBan lb"
    )
})
public class LoaiBan {

    @Id
    @Column(name = "maLoaiBan", nullable = false, length = 20)
    private String maLoaiBan;

    @Column(name = "tenLoaiBan", nullable = false, length = 100)
    private String tenLoaiBan;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    public LoaiBan() {}

    public LoaiBan(String maLoaiBan, String tenLoaiBan, int soLuong) {
        this.maLoaiBan = maLoaiBan;
        this.tenLoaiBan = tenLoaiBan;
        this.soLuong = soLuong;
    }

    public String getMaLoaiBan() {
        return maLoaiBan;
    }

    public void setMaLoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    public String getTenLoaiBan() {
        return tenLoaiBan;
    }

    public void setTenLoaiBan(String tenLoaiBan) {
        this.tenLoaiBan = tenLoaiBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public int hashCode() {
        return maLoaiBan == null ? 0 : maLoaiBan.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LoaiBan)) return false;
        LoaiBan other = (LoaiBan) obj;
        return maLoaiBan != null && maLoaiBan.equals(other.maLoaiBan);
    }
}
