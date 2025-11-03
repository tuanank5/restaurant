package entity;

import java.sql.Date;
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
    private Date ngayGioLapDon;

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

    @Column(name = "gioKetThuc")
    private LocalTime gioKetThuc;

    public DonDatBan() {
    }

    public DonDatBan(String maDatBan, Date ngayGioLapDon, int soLuong, KhachHang khachHang, Ban ban) {
        this.maDatBan = maDatBan;
        this.ngayGioLapDon = ngayGioLapDon;
        this.soLuong = soLuong;
        this.khachHang = khachHang;
        this.ban = ban;
    }

    public String getMaDatBan() {
        return maDatBan;
    }

    public void setMaDatBan(String maDatBan) {
        this.maDatBan = maDatBan;
    }

    public Date getNgayGioLapDon() {
        return ngayGioLapDon;
    }

    public void setNgayGioLapDon(Date ngayGioLapDon) {
        this.ngayGioLapDon = ngayGioLapDon;
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

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

	@Override
	public String toString() {
		return "DonDatBan [maDatBan=" + maDatBan + ", ngayGioLapDon=" + ngayGioLapDon + ", soLuong=" + soLuong
				+ ", khachHang=" + khachHang + ", ban=" + ban + ", gioBatDau=" + gioBatDau + ", gioKetThuc="
				+ gioKetThuc + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ban == null) ? 0 : ban.hashCode());
		result = prime * result + ((gioBatDau == null) ? 0 : gioBatDau.hashCode());
		result = prime * result + ((gioKetThuc == null) ? 0 : gioKetThuc.hashCode());
		result = prime * result + ((khachHang == null) ? 0 : khachHang.hashCode());
		result = prime * result + ((maDatBan == null) ? 0 : maDatBan.hashCode());
		result = prime * result + ((ngayGioLapDon == null) ? 0 : ngayGioLapDon.hashCode());
		result = prime * result + soLuong;
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
		DonDatBan other = (DonDatBan) obj;
		if (ban == null) {
			if (other.ban != null)
				return false;
		} else if (!ban.equals(other.ban))
			return false;
		if (gioBatDau == null) {
			if (other.gioBatDau != null)
				return false;
		} else if (!gioBatDau.equals(other.gioBatDau))
			return false;
		if (gioKetThuc == null) {
			if (other.gioKetThuc != null)
				return false;
		} else if (!gioKetThuc.equals(other.gioKetThuc))
			return false;
		if (khachHang == null) {
			if (other.khachHang != null)
				return false;
		} else if (!khachHang.equals(other.khachHang))
			return false;
		if (maDatBan == null) {
			if (other.maDatBan != null)
				return false;
		} else if (!maDatBan.equals(other.maDatBan))
			return false;
		if (ngayGioLapDon == null) {
			if (other.ngayGioLapDon != null)
				return false;
		} else if (!ngayGioLapDon.equals(other.ngayGioLapDon))
			return false;
		if (soLuong != other.soLuong)
			return false;
		return true;
	}

    
}
