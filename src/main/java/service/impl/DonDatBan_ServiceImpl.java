package service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.DonDatBan_DAO;
import dao.impl.DonDatBan_DAOImpl;
import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.KhachHang_DTO;
import dto.LoaiBan_DTO;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.LoaiBan;
import service.DonDatBan_Service;
import util.MapperUtil;

public class DonDatBan_ServiceImpl implements DonDatBan_Service {

	private DonDatBan_DAO donDatBan_DAO;

	public DonDatBan_ServiceImpl() {
		donDatBan_DAO = new DonDatBan_DAOImpl();
	}

	@Override
	public DonDatBan_DTO layDonDatTheoBan(String maBan) {
		if (maBan == null || maBan.trim().isEmpty()) {
			throw new IllegalArgumentException("maBan không được rỗng");
		}
		DonDatBan donDatBan = donDatBan_DAO.layDonDatTheoBan(maBan);
		return MapperUtil.map(donDatBan, DonDatBan_DTO.class);
	}

	@Override
	public boolean them(DonDatBan_DTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("dto null");
		}
		return donDatBan_DAO.them(
				MapperUtil.map(dto, DonDatBan.class)
		);
	}

	@Override
	public List<DonDatBan_DTO> timTheoKhachHang(KhachHang_DTO kh_DTO) {
		if (kh_DTO == null) {
			throw new IllegalArgumentException("kh_DTO không được rỗng");
		}
		if (kh_DTO.getMaKH() == null || kh_DTO.getMaKH().trim().isEmpty()) {
			throw new IllegalArgumentException("kh_DTO.maKH không được rỗng");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.timTheoKhachHang(MapperUtil.map(kh_DTO, KhachHang.class));
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<DonDatBan_DTO> timTheoBan(Ban_DTO ban_DTO) {
		if (ban_DTO == null) {
			throw new IllegalArgumentException("ban_DTO không được rỗng");
		}
		if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
			throw new IllegalArgumentException("ban_DTO.maBan không được rỗng");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.timTheoBan(MapperUtil.map(ban_DTO, Ban.class));
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBan() {
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBan();
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoThang(int thang, int nam) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanTheoThang(thang, nam);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<String> getKhachHangTheoThang(int thang, int nam) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		return donDatBan_DAO.getKhachHangTheoThang(thang, nam);
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNam(int nam) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanTheoNam(nam);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<String> getKhachHangTheoNam(int nam) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		return donDatBan_DAO.getKhachHangTheoNam(nam);
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanTheoNgayCuThe(dateStart, dateEnd);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<String> getKhachHangTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		return donDatBan_DAO.getKhachHangTheoNgayCuThe(dateStart, dateEnd);
	}

	@Override
	public Map<String, Integer> countDonDatBanTheoNam(int year) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year không hợp lệ");
		}
		return donDatBan_DAO.countDonDatBanTheoNam(year);
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNamNVCuThe(int nam, String maNV) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanTheoNamNVCuThe(nam, maNV);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public Map<String, Integer> countDonDatBanTheoNamNVCuThe(int year, String maNV) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year không hợp lệ");
		}
		return donDatBan_DAO.countDonDatBanTheoNamNVCuThe(year, maNV);
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoThang(int month, int year) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("month không hợp lệ (1-12)");
		}
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year không hợp lệ");
		}
		Map<LoaiBan, Integer> counts = donDatBan_DAO.countLoaiBanTheoThang(month, year);
		return counts.entrySet().stream().collect(
				Collectors.toMap(entry -> MapperUtil.map(entry.getKey(), LoaiBan_DTO.class), Map.Entry::getValue));
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNam(int year) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year không hợp lệ");
		}
		Map<LoaiBan, Integer> counts = donDatBan_DAO.countLoaiBanTheoNam(year);
		return counts.entrySet().stream().collect(
				Collectors.toMap(entry -> MapperUtil.map(entry.getKey(), LoaiBan_DTO.class), Map.Entry::getValue));
	}

	@Override
	public Map<LoaiBan_DTO, Integer> countLoaiBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		Map<LoaiBan, Integer> counts = donDatBan_DAO.countLoaiBanTheoNgayCuThe(dateStart, dateEnd);
		return counts.entrySet().stream().collect(
				Collectors.toMap(entry -> MapperUtil.map(entry.getKey(), LoaiBan_DTO.class), Map.Entry::getValue));
	}

	@Override
	public String getMaxMaDatBan() {
		return donDatBan_DAO.getMaxMaDatBan();
	}

	@Override
	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan) {
		if (maDatBan == null || maDatBan.trim().isEmpty()) {
			throw new IllegalArgumentException("maDatBan không được rỗng");
		}
		KhachHang khachHang = donDatBan_DAO.getKhachHangTheoMaDatBan(maDatBan);
		return MapperUtil.map(khachHang, KhachHang_DTO.class);
	}

	@Override
	public List<DonDatBan_DTO> getAllDDBTruoc() {
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDDBTruoc();
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<String> getKhachHangTheoThangNVCuThe(int thang, int nam, String maNV) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return donDatBan_DAO.getKhachHangTheoThangNVCuThe(thang, nam, maNV);
	}

	@Override
	public List<String> getKhachHangTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return donDatBan_DAO.getKhachHangTheoNgayNVCuThe(dateStart, dateEnd, maNV);
	}

	@Override
	public List<String> getKhachHangTheoNamNVCuThe(int nam, String maNV) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return donDatBan_DAO.getKhachHangTheoNamNVCuThe(nam, maNV);
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanNVTheoThang(int thang, int nam, String maNV) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanNVTheoThang(thang, nam, maNV);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}

	@Override
	public List<DonDatBan_DTO> getAllDonDatBanTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		List<DonDatBan> donDatBans = donDatBan_DAO.getAllDonDatBanTheoNgayNVCuThe(dateStart, dateEnd, maNV);
		return donDatBans.stream().map(donDatBan -> MapperUtil.map(donDatBan, DonDatBan_DTO.class)).toList();
	}
}
