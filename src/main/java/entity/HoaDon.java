package entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "maHD")
@Entity
@NamedQueries({ @NamedQuery(name = "HoaDon.list", query = "SELECT HD FROM HoaDon HD"),
		@NamedQuery(name = "HoaDon.count", query = "SELECT COUNT(HD.maHD) FROM HoaDon HD"),
		@NamedQuery(name = "HoaDon.trangThai", query = "SELECT HD FROM HoaDon HD WHERE HD.trangThai = :status") })
public class HoaDon {

	@Id
	@Column(name = "maHD", nullable = false, length = 20)
	private String maHD;

	@Column(name = "ngayLap", nullable = false)
	private Date ngayLap;

	@Column(name = "tongTien", nullable = false)
	private double tongTien;

	@Column(name = "thue", nullable = false)
	private double thue;

	@Column(name = "trangThai", nullable = false, length = 50)
	private String trangThai;

	@Column(name = "kieuThanhToan", nullable = false, length = 50)
	private String kieuThanhToan;

	@Column(name = "tienNhan", nullable = false)
	private double tienNhan;

	@Column(name = "tienThua", nullable = false)
	private double tienThua;

	@ManyToOne
	@JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
	private KhachHang khachHang;

	@ManyToOne
	@JoinColumn(name = "maKM", referencedColumnName = "maKM", nullable = true)
	private KhuyenMai khuyenMai;

	@ManyToOne
	@JoinColumn(name = "maNV", referencedColumnName = "maNV", nullable = false)
	private NhanVien nhanVien;

	@ManyToOne
	@JoinColumn(name = "maDatBan", referencedColumnName = "maDatBan", nullable = false)
	private DonDatBan donDatBan;

	@ManyToOne
	@JoinColumn(name = "maCoc", referencedColumnName = "maCoc", nullable = true)
	private Coc coc;

}
