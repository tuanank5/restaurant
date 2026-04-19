package service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.KhachHang_DTO;
import dto.LoaiBan_DTO;
import service.DonDatBan_Service;

public class DonDatBan_ServiceImpl implements DonDatBan_Service {

	@Override
	public DonDatBan_DTO layDonDatTheoBan(String maBan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> timTheoKhachHang(KhachHang_DTO kh_DTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> timTheoBan(Ban_DTO ban_DTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoThang(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoThang(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNam(int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoNam(int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> countDonDatBanTheoNam(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> countDonDatBanTheoNamNVCuThe(int year, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoThang(int month, int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNam(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaxMaDatBan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDDBTruoc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoThangNVCuThe(int thang, int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKhachHangTheoNamNVCuThe(int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanNVTheoThang(int thang, int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNamNVCuThe(int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

}
