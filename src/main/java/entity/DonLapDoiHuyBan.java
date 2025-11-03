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
public class DonLapDoiHuyBan {
	@Id
    @ManyToOne
    @JoinColumn(name = "maDatBan", referencedColumnName = "maDatBan", nullable = false)
    private DonDatBan donDatBan;

    @Column(name = "lyDo", nullable = false, length = 200)
    private String lyDo;
    
    @Column(name = "ngayGioLapDon", nullable = false)
    private Date ngayGioLapDon;
    
    @Column(name = "tienHoanTra", nullable = false)
    private double tienHoanTra;

	public DonLapDoiHuyBan() {
		
	}

	public DonLapDoiHuyBan(DonDatBan donDatBan, String lyDo, Date ngayGioLapDon, double tienHoanTra) {
		this.donDatBan = donDatBan;
		this.lyDo = lyDo;
		this.ngayGioLapDon = ngayGioLapDon;
		this.tienHoanTra = tienHoanTra;
	}

	public DonDatBan getDonDatBan() {
		return donDatBan;
	}

	public void setDonDatBan(DonDatBan donDatBan) {
		this.donDatBan = donDatBan;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((donDatBan == null) ? 0 : donDatBan.hashCode());
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
		DonLapDoiHuyBan other = (DonLapDoiHuyBan) obj;
		if (donDatBan == null) {
			if (other.donDatBan != null)
				return false;
		} else if (!donDatBan.equals(other.donDatBan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DonLapDoiHuyBan [donDatBan=" + donDatBan + ", lyDo=" + lyDo + ", ngayGioLapDon=" + ngayGioLapDon
				+ ", tienHoanTra=" + tienHoanTra + "]";
	}

	
}
