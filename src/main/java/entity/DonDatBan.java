package entity;

import java.sql.Date;

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
        name = "DonDatBan.list",
        query = "select b from DonDatBan b"
    ),
    @NamedQuery(
        name = "DonDatBan.count",
        query = "select count(maDatBan) from DonDatBan"
    )
})
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
	
	
}
