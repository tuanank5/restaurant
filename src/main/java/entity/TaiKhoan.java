package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import util.PasswordUtil;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
	@Id
    @Column(name = "maTaiKhoan", nullable = false, length = 20)
    private String maTaiKhoan;
	
    @Column(name = "tenTaiKhoan", nullable = false, length = 20)
    private String tenTaiKhoan;

    @Column(name = "matKhau", nullable = false, length = 20)
    private String matKhau;

    @Column(name = "ngayDangNhap", nullable = false)
    private Date ngayDangNhap;

    @Column(name = "ngayDangXuat", nullable = false)
    private Date ngayDangXuat;

    @Column(name = "ngaySuaDoi", nullable = false)
    private Date ngaySuaDoi;

    @ManyToOne
    @JoinColumn(name = "maNV", referencedColumnName = "maNV", nullable = false)
    private NhanVien nhanVien;

	public TaiKhoan() {
		
	}

	public TaiKhoan(String maTaiKhoan, String tenTaiKhoan, String matKhau, Date ngayDangNhap, Date ngayDangXuat,
			Date ngaySuaDoi, NhanVien nhanVien) {
		this.maTaiKhoan = maTaiKhoan;
		this.tenTaiKhoan = tenTaiKhoan;
		this.matKhau = matKhau;
		this.ngayDangNhap = ngayDangNhap;
		this.ngayDangXuat = ngayDangXuat;
		this.ngaySuaDoi = ngaySuaDoi;
		this.nhanVien = nhanVien;
	}
	
	public boolean kiemTraDangNhap(String tenTaiKhoan, String matKhau) {
		return tenTaiKhoan.equalsIgnoreCase(this.tenTaiKhoan) &&
		           matKhau.equals(this.matKhau);
//        return tenTaiKhoan.equalsIgnoreCase(this.tenTaiKhoan) &&
//                PasswordUtil.checkPassword(matKhau, this.matKhau);
    }
	
    public boolean kiemTraDoiMatKhau(String matKhau) {
        return PasswordUtil.checkPassword(matKhau, this.matKhau);
    }

	public String getMaTaiKhoan() {
		return maTaiKhoan;
	}

	public void setMaTaiKhoan(String maTaiKhoan) {
		this.maTaiKhoan = maTaiKhoan;
	}

	public String getTenTaiKhoan() {
		return tenTaiKhoan;
	}

	public void setTenTaiKhoan(String tenTaiKhoan) {
		this.tenTaiKhoan = tenTaiKhoan;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public Date getNgayDangNhap() {
		return ngayDangNhap;
	}

	public void setNgayDangNhap(Date ngayDangNhap) {
		this.ngayDangNhap = ngayDangNhap;
	}

	public Date getNgayDangXuat() {
		return ngayDangXuat;
	}

	public void setNgayDangXuat(Date ngayDangXuat) {
		this.ngayDangXuat = ngayDangXuat;
	}

	public Date getNgaySuaDoi() {
		return ngaySuaDoi;
	}

	public void setNgaySuaDoi(Date ngaySuaDoi) {
		this.ngaySuaDoi = ngaySuaDoi;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maTaiKhoan == null) ? 0 : maTaiKhoan.hashCode());
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
		TaiKhoan other = (TaiKhoan) obj;
		if (maTaiKhoan == null) {
			if (other.maTaiKhoan != null)
				return false;
		} else if (!maTaiKhoan.equals(other.maTaiKhoan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaiKhoan [maTaiKhoan=" + maTaiKhoan + ", tenTaiKhoan=" + tenTaiKhoan + ", matKhau=" + matKhau
				+ ", ngayDangNhap=" + ngayDangNhap + ", ngayDangXuat=" + ngayDangXuat + ", ngaySuaDoi=" + ngaySuaDoi
				+ ", nhanVien=" + nhanVien + "]";
	}
    
    
}
