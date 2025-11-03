package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DonLapDoiHuyBan")
public class DonDatBan {
	@Id
	@Column(name = "maDatBan", nullable = false, length = 20)
    private String maDatBan;

    @Column(name = "ngayGio", nullable = false)
    private Date ngayGioLapDon;

    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
    private KhachHang khachHang;
    
    @ManyToOne
    @JoinColumn(name = "maBan", referencedColumnName = "maBan", nullable = false)
    private Ban ban;

	public DonDatBan() {
		
	}

	public DonDatBan(String maDatBan, Date ngayGioLapDon, KhachHang khachHang, Ban ban) {
		this.maDatBan = maDatBan;
		this.ngayGioLapDon = ngayGioLapDon;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maDatBan == null) ? 0 : maDatBan.hashCode());
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
		if (maDatBan == null) {
			if (other.maDatBan != null)
				return false;
		} else if (!maDatBan.equals(other.maDatBan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DonDatBan [maDatBan=" + maDatBan + ", ngayGioLapDon=" + ngayGioLapDon + ", khachHang=" + khachHang
				+ ", ban=" + ban + "]";
	}
	
	
}
