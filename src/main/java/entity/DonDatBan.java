package entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "DonDatBan")
@NamedQueries({
    @NamedQuery(
        name = "DonDatBan.list",
        query = "SELECT DDB FROM DonDatBan DDB"
    ),
    @NamedQuery(
        name = "DonDatBan.count",
        query = "SELECT COUNT(DDB.maDatBan) FROM DonDatBan DDB"
    )
})
public class DonDatBan {

    @Id
    @Column(name = "maDatBan", nullable = false, length = 20)
    private String maDatBan;

    @Column(name = "ngayGio", nullable = false)
    private LocalDateTime ngayGioLapDon;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "maBan", referencedColumnName = "maBan", nullable = false)
    private Ban ban;

    @Column(name = "gioBatDau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "trangThai")
    private String trangThai;

    public DonDatBan() {
    }

    public DonDatBan(String maDatBan, LocalDateTime ngayGioLapDon, int soLuong,
                     Ban ban, LocalTime gioBatDau, String trangThai) {
        this.maDatBan = maDatBan;
        this.ngayGioLapDon = ngayGioLapDon;
        this.soLuong = soLuong;
        this.ban = ban;
        this.gioBatDau = gioBatDau;
        this.trangThai = trangThai;
    }

    public String getMaDatBan() {
        return maDatBan;
    }

    public void setMaDatBan(String maDatBan) {
        this.maDatBan = maDatBan;
    }

    public LocalDateTime getNgayGioLapDon() {
        return ngayGioLapDon;
    }

    public void setNgayGioLapDon(LocalDateTime ngayGio) {
        this.ngayGioLapDon = ngayGio;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

	@Override
	public String toString() {
		return "DonDatBan [maDatBan=" + maDatBan + ", ngayGioLapDon=" + ngayGioLapDon + ", soLuong=" + soLuong
				+ ", ban=" + ban + ", gioBatDau=" + gioBatDau + ", trangThai=" + trangThai + "]";
	}

}
