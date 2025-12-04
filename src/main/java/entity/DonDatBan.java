package entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DonDatBan")
public class DonDatBan {

    @Id
    @Column(name = "maDatBan", nullable = false, length = 20)
    private String maDatBan;

    @Column(name = "ngayGio", nullable = false)
    private LocalDateTime ngayGioLapDon;   // đúng với SQL

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maBan", referencedColumnName = "maBan", nullable = false)
    private Ban ban;

    @Column(name = "gioBatDau")
    private LocalTime gioBatDau;

    // ❌ ĐÃ BỎ: gioKetThuc vì SQL đã xóa

    @Column(name = "trangThai")
    private String trangThai; // mới thêm trong SQL

    public DonDatBan() {
    }

    public DonDatBan(String maDatBan, LocalDateTime ngayGioLapDon, int soLuong, 
                     KhachHang khachHang, Ban ban, String trangThai) {
        this.maDatBan = maDatBan;
        this.ngayGioLapDon = ngayGioLapDon;
        this.soLuong = soLuong;
        this.khachHang = khachHang;
        this.ban = ban;
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

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
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
        return "DonDatBan [maDatBan=" + maDatBan + ", ngayGio=" + ngayGioLapDon + ", soLuong=" + soLuong
                + ", khachHang=" + khachHang + ", ban=" + ban + ", gioBatDau="
                + gioBatDau + ", trangThai=" + trangThai + "]";
    }

}
