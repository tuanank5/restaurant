package entity;

import dto.KhachHang_DTO;
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
@EqualsAndHashCode(of = "maKH")
@Entity
@NamedQueries({ @NamedQuery(name = "KhachHang.findAll", query = "SELECT KH FROM KhachHang KH"),
		@NamedQuery(name = "KhachHang.findByMaKH", query = "SELECT KH FROM KhachHang KH WHERE KH.maKH = :maKH") })
public class KhachHang{

	@Id
	@Column(name = "maKH", nullable = false, length = 20)
	private String maKH;

	@Column(name = "tenKH", nullable = false, length = 100)
	private String tenKH;

	@Column(name = "sdt", nullable = false, length = 20, unique = true)
	private String sdt;

	@Column(name = "email", length = 100, unique = true)
	private String email;

	@Column(name = "diaChi", length = 200)
	private String diaChi;

	@Column(name = "diemTichLuy")
	@Builder.Default
	private int diemTichLuy = 0;

	@ManyToOne
	@JoinColumn(name = "maHang", referencedColumnName = "maHang", nullable = true)
	private HangKhachHang hangKhachHang;

}
