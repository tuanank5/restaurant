package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
//có sai
@Entity
@NamedQueries({
    @NamedQuery(
        name = "DonLapDoiHuyBan.list",
        query = "SELECT D FROM DonLapDoiHuyBan D"
    ),
    @NamedQuery(
        name = "DonLapDoiHuyBan.count",
        query = "SELECT COUNT(maDatBans) FROM DonLapDoiHuyBan"
    )
})
public class DonLapDoiHuyBan {
	@Id
	@Column(name = "maDatBans", nullable = false, length = 20)
    private String maDatBans;

    @Column(name = "lyDo", nullable = false, length = 200)
    private String lyDo;
    
    @Column(name = "ngayGioLapDon", nullable = false)
    private Date ngayGioLapDon;
    
    @Column(name = "tienHoanTra", nullable = false)
    private double tienHoanTra;

    @ManyToOne
    @JoinColumn(name = "maDatBan", referencedColumnName = "maDatBan", nullable = false)
    private DonDatBan donDatBan;

	public DonLapDoiHuyBan() {
		
	}

	public DonLapDoiHuyBan(String maDatBan, String lyDo, Date ngayGioLapDon, double tienHoanTra,
			DonDatBan donDatBan) {
		this.maDatBans = maDatBan;
		this.lyDo = lyDo;
		this.ngayGioLapDon = ngayGioLapDon;
		this.tienHoanTra = tienHoanTra;
		this.donDatBan = donDatBan;
	}

	public String getMaDatBan() {
		return maDatBans;
	}

	public void setMaDatBan(String maDatBan) {
		this.maDatBans = maDatBan;
	}

	public String getLyDo() {
		return lyDo;
	}

	public void setLyDo(String lyDo) {
		this.lyDo = lyDo;
	}

	public Date getNgayGioLapDon() {
		return ngayGioLapDon;
	}

	public void setNgayGioLapDon(Date ngayGioLapDon) {
		this.ngayGioLapDon = ngayGioLapDon;
	}

	public double getTienHoanTra() {
		return tienHoanTra;
	}

	public void setTienHoanTra(double tienHoanTra) {
		this.tienHoanTra = tienHoanTra;
	}

	public DonDatBan getDonDatBan() {
		return donDatBan;
	}

	public void setDonDatBan(DonDatBan donDatBan) {
		this.donDatBan = donDatBan;
	}
    
	
}
