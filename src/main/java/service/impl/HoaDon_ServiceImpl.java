package service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import dto.Ban_DTO;
import dto.ChiTietHoaDon_DTO;
import dto.DonDatBan_DTO;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import service.HoaDon_Service;

public class HoaDon_ServiceImpl implements HoaDon_Service {

	@Override
	public long tongSoHoaDon() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean themHoaDon(HoaDon_DTO hoaDon_DTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDonTheoThang(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getHoaDonTheoNam(int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuTheoThang(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuTheoNam(int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getAllHoaDonNVTheoThang(int thang, int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getHoaDonNVTheoNam(int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuNVTheoThang(int thang, int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuNVTheoNam(int nam, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HoaDon_DTO> getHoaDonNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTongDoanhThuNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaxMaHoaDon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HoaDon_DTO getHoaDonTheoMaDatBan(String maDatBan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KhachHang_DTO getKhachHangTheoMaDatBan(String maDatBan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HoaDon_DTO timHoaDonDangPhucVuTheoBan(Ban_DTO ban_DTO, LocalDate ngay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HoaDon_DTO timHoaDonTheoDonDatBan(DonDatBan_DTO donDatBan_DTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChiTietHoaDon_DTO> findByMaHoaDon(String maHoaDon) {
		// TODO Auto-generated method stub
		return null;
	}

}
