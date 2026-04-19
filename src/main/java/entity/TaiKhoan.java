package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import util.PasswordUtil;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "maTaiKhoan")
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
	@Id
	@Column(name = "maTaiKhoan", nullable = false, length = 20)
	private String maTaiKhoan;

	@Column(name = "tenTaiKhoan", nullable = false, length = 20)
	private String tenTaiKhoan;

	@Column(name = "matKhau", nullable = false, length = 100)
	private String matKhau;

	@Column(name = "ngayDangNhap")
	private Date ngayDangNhap;

	@Column(name = "ngayDangXuat")
	private Date ngayDangXuat;

	@Column(name = "ngaySuaDoi")
	private Date ngaySuaDoi;

	@ManyToOne
	@JoinColumn(name = "maNV", referencedColumnName = "maNV", nullable = false)
	private NhanVien nhanVien;

	public boolean kiemTraDangNhap(String tenTaiKhoan, String matKhau) {
		return tenTaiKhoan.equalsIgnoreCase(this.tenTaiKhoan) && matKhau.equals(this.matKhau);
	}

	public boolean kiemTraDoiMatKhau(String matKhau) {
		return PasswordUtil.checkPassword(matKhau, this.matKhau);
	}

}
