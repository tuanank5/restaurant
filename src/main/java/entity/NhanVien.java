package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "NhanVien.list",
        query = "select nv from NhanVien nv"
    ),
    @NamedQuery(
        name = "NhanVien.count",
        query = "select count(maNV) from NhanVien"
    ),
    @NamedQuery(
    	name = "NhanVien.state",
    	query = "select nv from NhanVien nv where trangThai = :status"
    )
})
public class NhanVien {
	@Id
	@Column(name = "maNV", nullable = false, length = 20)
    private String maNV;

    @Column(name = "tenNV", nullable = false, length = 100)
    private String tenNV;

    @Column(name = "chucVu", nullable = false, length = 50)
    private String chucVu;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "namSinh", nullable = false)
    private Date namSinh;

    @Column(name = "diaChi", nullable = false, length = 200)
    private String diaChi;

    @Column(name = "gioiTinh", nullable = false)
    private boolean gioiTinh; 

    @Column(name = "ngayVaoLam", nullable = false)
    private Date ngayVaoLam;

    @Column(name = "trangThai", nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean trangThai;

	public NhanVien() {
		
	}
	
	public NhanVien(String maNV, String tenNV, String chucVu, String email, Date namSinh, String diaChi,
			boolean gioiTinh, Date ngayVaoLam, boolean trangThai) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.chucVu = chucVu;
		this.email = email;
		this.namSinh = namSinh;
		this.diaChi = diaChi;
		this.gioiTinh = gioiTinh;
		this.ngayVaoLam = ngayVaoLam;
		this.trangThai = trangThai;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getNamSinh() {
		return namSinh;
	}

	public void setNamSinh(Date namSinh) {
		this.namSinh = namSinh;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public Date getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(Date ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chucVu == null) ? 0 : chucVu.hashCode());
		result = prime * result + ((diaChi == null) ? 0 : diaChi.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (gioiTinh ? 1231 : 1237);
		result = prime * result + ((maNV == null) ? 0 : maNV.hashCode());
		result = prime * result + ((namSinh == null) ? 0 : namSinh.hashCode());
		result = prime * result + ((ngayVaoLam == null) ? 0 : ngayVaoLam.hashCode());
		result = prime * result + ((tenNV == null) ? 0 : tenNV.hashCode());
		result = prime * result + (trangThai ? 1231 : 1237);
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
		NhanVien other = (NhanVien) obj;
		if (chucVu == null) {
			if (other.chucVu != null)
				return false;
		} else if (!chucVu.equals(other.chucVu))
			return false;
		if (diaChi == null) {
			if (other.diaChi != null)
				return false;
		} else if (!diaChi.equals(other.diaChi))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gioiTinh != other.gioiTinh)
			return false;
		if (maNV == null) {
			if (other.maNV != null)
				return false;
		} else if (!maNV.equals(other.maNV))
			return false;
		if (namSinh == null) {
			if (other.namSinh != null)
				return false;
		} else if (!namSinh.equals(other.namSinh))
			return false;
		if (ngayVaoLam == null) {
			if (other.ngayVaoLam != null)
				return false;
		} else if (!ngayVaoLam.equals(other.ngayVaoLam))
			return false;
		if (tenNV == null) {
			if (other.tenNV != null)
				return false;
		} else if (!tenNV.equals(other.tenNV))
			return false;
		if (trangThai != other.trangThai)
			return false;
		return true;
	} 
	
}
