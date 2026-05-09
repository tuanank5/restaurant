package service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dao.HoaDon_DAO;
import dao.impl.HoaDon_DAOImpl;
import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;
import service.HoaDon_Service;
import util.MapperUtil;

public class HoaDon_ServiceImpl implements HoaDon_Service {

	private HoaDon_DAO hoaDon_DAO;

	public HoaDon_ServiceImpl() {
		hoaDon_DAO = new HoaDon_DAOImpl();
	}

	@Override
	public long tongSoHoaDon() {
		return hoaDon_DAO.tongSoHoaDon();
	}

	@Override
	public boolean themHoaDon(HoaDon_DTO hoaDon_DTO) {
		if (hoaDon_DTO == null) {
			throw new IllegalArgumentException("hoaDon_DTO không được rỗng");
		}
		if (hoaDon_DTO.getMaHD() == null || hoaDon_DTO.getMaHD().trim().isEmpty()) {
			throw new IllegalArgumentException("hoaDon_DTO.maHD() không được rỗng");
		}
		return hoaDon_DAO.themHoaDon(MapperUtil.map(hoaDon_DTO, HoaDon.class));
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDons() {
		List<HoaDon> hoaDons = hoaDon_DAO.getAllHoaDons();
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDonTheoThang(int thang, int nam) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		List<HoaDon> hoaDons = hoaDon_DAO.getAllHoaDonTheoThang(thang, nam);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public List<HoaDon_DTO> getHoaDonTheoNam(int nam) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		List<HoaDon> hoaDons = hoaDon_DAO.getHoaDonTheoNam(nam);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public Double getTongDoanhThuTheoThang(int thang, int nam) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		return hoaDon_DAO.getTongDoanhThuTheoThang(thang, nam);
	}

	@Override
	public Double getTongDoanhThuTheoNam(int nam) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		return hoaDon_DAO.getTongDoanhThuTheoNam(nam);
	}

	@Override
	public List<HoaDon_DTO> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		List<HoaDon> hoaDons = hoaDon_DAO.getHoaDonTheoNgayCuThe(dateStart, dateEnd);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		if (dateStart == null) {
			throw new IllegalArgumentException("dateStart không được rỗng");
		}
		if (dateEnd == null) {
			throw new IllegalArgumentException("dateEnd không được rỗng");
		}
		if (dateStart.isAfter(dateEnd)) {
			throw new IllegalArgumentException("dateStart không được lớn hơn dateEnd");
		}
		return hoaDon_DAO.getTongDoanhThuTheoNgayCuThe(dateStart, dateEnd);
	}

	@Override
	public Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV) {
		if (ngayLap == null) {
			throw new IllegalArgumentException("ngayLap không được rỗng");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return hoaDon_DAO.getTongDoanhThuTheoNgayVaNhanVien(ngayLap, maNV);
	}

	@Override
	public Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV) {
		if (ngayLap == null) {
			throw new IllegalArgumentException("ngayLap không được rỗng");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return hoaDon_DAO.countHoaDonTheoNgayVaNhanVien(ngayLap, maNV);
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDonNVTheoThang(int thang, int nam, String maNV) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		List<HoaDon> hoaDons = hoaDon_DAO.getAllHoaDonNVTheoThang(thang, nam, maNV);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public List<HoaDon_DTO> getHoaDonNVTheoNam(int nam, String maNV) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		List<HoaDon> hoaDons = hoaDon_DAO.getHoaDonNVTheoNam(nam, maNV);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public Double getTongDoanhThuNVTheoThang(int thang, int nam, String maNV) {
		if (thang < 1 || thang > 12) {
			throw new IllegalArgumentException("thang không hợp lệ (1-12)");
		}
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return hoaDon_DAO.getTongDoanhThuNVTheoThang(thang, nam, maNV);
	}

	@Override
	public Double getTongDoanhThuNVTheoNam(int nam, String maNV) {
		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}
		return hoaDon_DAO.getTongDoanhThuNVTheoNam(nam, maNV);
	}

	@Override
	public List<HoaDon_DTO> getHoaDonNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
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
		List<HoaDon> hoaDons = hoaDon_DAO.getHoaDonNVTheoNgayCuThe(dateStart, dateEnd, maNV);
		return hoaDons.stream().map(hoaDon -> MapperUtil.map(hoaDon, HoaDon_DTO.class)).toList();
	}

	@Override
	public Double getTongDoanhThuNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
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
		return hoaDon_DAO.getTongDoanhThuNVTheoNgayCuThe(dateStart, dateEnd, maNV);
	}

	@Override
	public String getMaxMaHoaDon() {
		return hoaDon_DAO.getMaxMaHoaDon();
	}

	@Override
	public HoaDon_DTO getHoaDonTheoMaDatBan(String maDatBan) {
		if (maDatBan == null || maDatBan.trim().isEmpty()) {
			throw new IllegalArgumentException("maDatBan không được rỗng");
		}
		HoaDon hoaDon = hoaDon_DAO.getHoaDonTheoMaDatBan(maDatBan);
		return MapperUtil.map(hoaDon, HoaDon_DTO.class);
	}

	@Override
	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan) {
		if (maDatBan == null || maDatBan.trim().isEmpty()) {
			throw new IllegalArgumentException("maDatBan không được rỗng");
		}
		KhachHang khachHang = hoaDon_DAO.getKhachHangTheoMaDatBan(maDatBan);
		return MapperUtil.map(khachHang, KhachHang_DTO.class);
	}

	@Override
	public HoaDon_DTO timHoaDonDangPhucVuTheoBan(Ban_DTO ban_DTO, LocalDate ngay) {
		if (ban_DTO == null) {
			throw new IllegalArgumentException("ban_DTO không được rỗng");
		}
		if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
			throw new IllegalArgumentException("ban_DTO.maBan() không được rỗng");
		}
		if (ngay == null) {
			throw new IllegalArgumentException("ngay không được rỗng");
		}
		HoaDon hoaDon = hoaDon_DAO.timHoaDonDangPhucVuTheoBan(MapperUtil.map(ban_DTO, Ban.class), ngay);
		return MapperUtil.map(hoaDon, HoaDon_DTO.class);
	}

	@Override
	public HoaDon_DTO timHoaDonTheoDonDatBan(DonDatBan_DTO donDatBan_DTO) {
		if (donDatBan_DTO == null) {
			throw new IllegalArgumentException("donDatBan_DTO không được rỗng");
		}
		if (donDatBan_DTO.getMaDatBan() == null || donDatBan_DTO.getMaDatBan().trim().isEmpty()) {
			throw new IllegalArgumentException("donDatBan_DTO.maDonDatBan() không được rỗng");
		}
		HoaDon hoaDon = hoaDon_DAO.timHoaDonTheoDonDatBan(MapperUtil.map(donDatBan_DTO, DonDatBan.class));
		return MapperUtil.map(hoaDon, HoaDon_DTO.class);
	}

	@Override
	public List<ChiTietHoaDon_DTO> findByMaHoaDon(String maHoaDon) {
		if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
			throw new IllegalArgumentException("maHoaDon không được rỗng");
		}
		List<ChiTietHoaDon> chiTietHoaDons = hoaDon_DAO.findByMaHoaDon(maHoaDon);
		return chiTietHoaDons.stream().map(chiTiet -> MapperUtil.map(chiTiet, ChiTietHoaDon_DTO.class)).toList();
	}

	@Override
	public Map<String, Double> getDoanhThuNVTheoNam(int nam, String maNV) {

		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}

		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("maNV không được rỗng");
		}

		return hoaDon_DAO.getDoanhThuNVTheoNam(nam, maNV);
	}

	@Override
	public Map<String, Double> getDoanhThuTheoNam(int nam) {

		if (nam < 1900 || nam > 2100) {
			throw new IllegalArgumentException("nam không hợp lệ");
		}

		return hoaDon_DAO.getDoanhThuTheoNam(nam);
	}
}
