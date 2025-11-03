package entity;

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
        name = "KhachHang.findAll",
        query = "SELECT KH FROM KhachHang KH"
    ),
    @NamedQuery(
        name = "KhachHang.findByMaKH",
        query = "SELECT KH FROM KhachHang KH WHERE KH.maKH = :maKH"
    )
})
public class KhachHang {

	@Id
	@Column(name = "maKH", nullable = false, length = 20)
	private String maKH;

	@Column(name = "tenKH", nullable = false, length = 100)
	private String tenKH;

	@Column(name = "sdt", nullable = false, length = 20)
	private String sdt;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "diaChi", length = 200)
	private String diaChi;
	
	@Column(name = "diemTichLuy")
	private int diemTichLuy;
	
	@ManyToOne
    @JoinColumn(name = "maHang", referencedColumnName = "maHang", nullable = false)
    private HangKhachHang hangKhachHang;
	
	public KhachHang() {
		
	}

	public KhachHang(String maKH, String tenKH, String sdt, String email, String diaChi,
			int diemTichLuy, HangKhachHang hangKhachHang) {
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
		this.email = email;
		this.diaChi = diaChi;
		this.diemTichLuy = diemTichLuy;
		this.hangKhachHang = hangKhachHang;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}

	public HangKhachHang getHangKhachHang() {
		return hangKhachHang;
	}

	public void setHangKhachHang(HangKhachHang hangKhachHang) {
		this.hangKhachHang = hangKhachHang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaChi == null) ? 0 : diaChi.hashCode());
		result = prime * result + diemTichLuy;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((hangKhachHang == null) ? 0 : hangKhachHang.hashCode());
		result = prime * result + ((maKH == null) ? 0 : maKH.hashCode());
		result = prime * result + ((sdt == null) ? 0 : sdt.hashCode());
		result = prime * result + ((tenKH == null) ? 0 : tenKH.hashCode());
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
		KhachHang other = (KhachHang) obj;
		if (diaChi == null) {
			if (other.diaChi != null)
				return false;
		} else if (!diaChi.equals(other.diaChi))
			return false;
		if (diemTichLuy != other.diemTichLuy)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (hangKhachHang == null) {
			if (other.hangKhachHang != null)
				return false;
		} else if (!hangKhachHang.equals(other.hangKhachHang))
			return false;
		if (maKH == null) {
			if (other.maKH != null)
				return false;
		} else if (!maKH.equals(other.maKH))
			return false;
		if (sdt == null) {
			if (other.sdt != null)
				return false;
		} else if (!sdt.equals(other.sdt))
			return false;
		if (tenKH == null) {
			if (other.tenKH != null)
				return false;
		} else if (!tenKH.equals(other.tenKH))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt + ", email=" + email + ", diaChi="
				+ diaChi + ", diemTichLuy=" + diemTichLuy + ", hangKhachHang=" + hangKhachHang + "]";
	}

	
}
