package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.KhachHang_DTO;
import dto.LoaiBan_DTO;

public interface DonDatBan_Service {
	DonDatBan_DTO layDonDatTheoBan(String maBan);

	List<DonDatBan_DTO> timTheoKhachHang(KhachHang_DTO kh_DTO);

	List<DonDatBan_DTO> timTheoBan(Ban_DTO ban_DTO);

	List<DonDatBan_DTO> getAllDonDatBan();

	List<DonDatBan_DTO> getAllDonDatBanTheoThang(int thang, int nam);

	List<String> getKhachHangTheoThang(int thang, int nam);

	List<DonDatBan_DTO> getAllDonDatBanTheoNam(int nam);

	List<String> getKhachHangTheoNam(int nam);

	List<DonDatBan_DTO> getAllDonDatBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

	List<String> getKhachHangTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

	public Map<String, Integer> countDonDatBanTheoNam(int year);

	Map<String, Integer> countDonDatBanTheoNamNVCuThe(int year, String maNV);

	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoThang(int month, int year);

	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNam(int year);

	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

	String getMaxMaDatBan();

	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan);

	public List<DonDatBan_DTO> getAllDDBTruoc();

	List<String> getKhachHangTheoThangNVCuThe(int thang, int nam, String maNV);

	List<String> getKhachHangTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);

	List<String> getKhachHangTheoNamNVCuThe(int nam, String maNV);

	List<DonDatBan_DTO> getAllDonDatBanNVTheoThang(int thang, int nam, String maNV);

	List<DonDatBan_DTO> getAllDonDatBanTheoNamNVCuThe(int nam, String maNV);

	List<DonDatBan_DTO> getAllDonDatBanTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);
}
