package service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;

public interface HoaDon_Service {
	long tongSoHoaDon();

	boolean themHoaDon(HoaDon_DTO hoaDon_DTO);

	List<HoaDon_DTO> getAllHoaDons();

	List<HoaDon_DTO> getAllHoaDonTheoThang(int thang, int nam);

	List<HoaDon_DTO> getHoaDonTheoNam(int nam);

	Double getTongDoanhThuTheoThang(int thang, int nam);

	Double getTongDoanhThuTheoNam(int nam);

	List<HoaDon_DTO> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

	Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

	//
	Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV);

	Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV);
	//

	List<HoaDon_DTO> getAllHoaDonNVTheoThang(int thang, int nam, String maNV);

	List<HoaDon_DTO> getHoaDonNVTheoNam(int nam, String maNV);

	Double getTongDoanhThuNVTheoThang(int thang, int nam, String maNV);

	Double getTongDoanhThuNVTheoNam(int nam, String maNV);

	List<HoaDon_DTO> getHoaDonNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);

	Double getTongDoanhThuNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);

	String getMaxMaHoaDon();

	HoaDon_DTO getHoaDonTheoMaDatBan(String maDatBan);

	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan);

	HoaDon_DTO timHoaDonDangPhucVuTheoBan(Ban_DTO ban_DTO, LocalDate ngay);

	HoaDon_DTO timHoaDonTheoDonDatBan(DonDatBan_DTO donDatBan_DTO);

	List<ChiTietHoaDon_DTO> findByMaHoaDon(String maHoaDon);

    Map<String, Double> getDoanhThuTheoNam(int nam);

	Map<String, Double> getDoanhThuNVTheoNam(int nam, String maNV);

	boolean capNhat(HoaDon_DTO dto);

	List<HoaDon_DTO> getDanhSach(String namedQuery);

}
