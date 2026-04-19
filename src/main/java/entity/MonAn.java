package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(of = "maMon")
@Entity
@Table(name = "MonAn")
public class MonAn {

	@Id
	@Column(name = "maMon", nullable = false, length = 20)
	private String maMon;

	@Column(name = "tenMon", nullable = false, length = 100)
	private String tenMon;

	@Column(name = "donGia", nullable = false)
	private double donGia;

	@Column(name = "duongDanAnh", length = 255)
	private String duongDanAnh;

	@Column(name = "loaiMon", length = 100)
	private String loaiMon;

}