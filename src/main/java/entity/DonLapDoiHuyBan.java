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
        name = "DonLapDoiHuyBan.list",
        query = "select b from DonLapDoiHuyBan b"
    ),
    @NamedQuery(
        name = "DonLapDoiHuyBan.count",
        query = "select count(maDonLapDoiHuyBan) from DonLapDoiHuyBan"
    )
})
public class DonLapDoiHuyBan {
	@Id
	@Column(name = "maDonLapDoiHuyBan", nullable = false, length = 20)
    private String maDonLapDoiHuyBan;

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

	public DonLapDoiHuyBan(String maDonLapDoiHuyBan, String lyDo, Date ngayGioLapDon, double tienHoanTra,
			DonDatBan donDatBan) {
		this.maDonLapDoiHuyBan = maDonLapDoiHuyBan;
		this.lyDo = lyDo;
		this.ngayGioLapDon = ngayGioLapDon;
		this.tienHoanTra = tienHoanTra;
		this.donDatBan = donDatBan;
	}

	public String getMaDonLapDoiHuyBan() {
		return maDonLapDoiHuyBan;
	}

	public void setMaDonLapDoiHuyBan(String maDonLapDoiHuyBan) {
		this.maDonLapDoiHuyBan = maDonLapDoiHuyBan;
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
