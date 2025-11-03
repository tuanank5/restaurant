package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Coc")
public class Coc {
	@Id
    @Column(name = "maCoc", nullable = false, length = 20)
    private String maCoc;
	
	@Column(name = "loaiCoc", nullable = false)
    private boolean loaiCoc;

    @Column(name = "phanTramCoc", nullable = false)
    private int phanTramCoc;
    
    @Column(name = "soTienCoc", nullable = false)
    private double soTienCoc;

	public Coc() {
		
	}

	public Coc(String maCoc, boolean loaiCoc, int phanTramCoc, double soTienCoc) {
		this.maCoc = maCoc;
		this.loaiCoc = loaiCoc;
		this.phanTramCoc = phanTramCoc;
		this.soTienCoc = soTienCoc;
	}

	public String getMaCoc() {
		return maCoc;
	}

	public void setMaCoc(String maCoc) {
		this.maCoc = maCoc;
	}

	public boolean isLoaiCoc() {
		return loaiCoc;
	}

	public void setLoaiCoc(boolean loaiCoc) {
		this.loaiCoc = loaiCoc;
	}

	public int getPhanTramCoc() {
		return phanTramCoc;
	}

	public void setPhanTramCoc(int phanTramCoc) {
		this.phanTramCoc = phanTramCoc;
	}

	public double getSoTienCoc() {
		return soTienCoc;
	}

	public void setSoTienCoc(double soTienCoc) {
		this.soTienCoc = soTienCoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maCoc == null) ? 0 : maCoc.hashCode());
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
		Coc other = (Coc) obj;
		if (maCoc == null) {
			if (other.maCoc != null)
				return false;
		} else if (!maCoc.equals(other.maCoc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coc [maCoc=" + maCoc + ", loaiCoc=" + loaiCoc + ", phanTramCoc=" + phanTramCoc + ", soTienCoc="
				+ soTienCoc + "]";
	}
    
    
}
