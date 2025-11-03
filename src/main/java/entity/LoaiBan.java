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
        query = "SELECT LB FROM LoaiBan LB"
    ),
    @NamedQuery(
        name = "LoaiBan.count",
        query = "SELECT COUNT(LB.maLoaiBan) FROM LoaiBan LB"
    )
})
public class LoaiBan {

    @Id
    @Column(name = "maLoaiBan", nullable = false, length = 20)
    private String maLoaiBan;

    @Column(name = "tenLoaiBan", nullable = false, length = 50)
    private String tenLoaiBan;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    public LoaiBan() {
    	
    }

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maLoaiBan == null) ? 0 : maLoaiBan.hashCode());
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
		LoaiBan other = (LoaiBan) obj;
		if (maLoaiBan == null) {
			if (other.maLoaiBan != null)
				return false;
		} else if (!maLoaiBan.equals(other.maLoaiBan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoaiBan [maLoaiBan=" + maLoaiBan + ", tenLoaiBan=" + tenLoaiBan + ", soLuong=" + soLuong + "]";
	}

    
}
