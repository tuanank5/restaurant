package service.impl;

import java.util.List;

import dao.ChiTietHoaDon_DAO;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dto.ChiTietHoaDon_DTO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.MonAn;
import service.ChiTietHoaDon_Service;
import util.MapperUtil;

public class ChiTietHoaDon_ServiceImpl implements ChiTietHoaDon_Service {

	private ChiTietHoaDon_DAO chiTietHoaDon_DAO;

	public ChiTietHoaDon_ServiceImpl() {
		chiTietHoaDon_DAO = new ChiTietHoaDon_DAOImpl();
	}

	@Override
	public boolean themChiTiet(ChiTietHoaDon_DTO cthd_DTO) {
		if (cthd_DTO == null) {
			throw new IllegalArgumentException("cthd_DTO không được rỗng");
		}
		if (cthd_DTO.getMaHoaDon() == null || cthd_DTO.getMaHoaDon().trim().isEmpty()) {
			throw new IllegalArgumentException("cthd_DTO.maHoaDon không được rỗng");
		}
		if (cthd_DTO.getMaMonAn() == null || cthd_DTO.getMaMonAn().trim().isEmpty()) {
			throw new IllegalArgumentException("cthd_DTO.maMonAn không được rỗng");
		}

		ChiTietHoaDon entity = new ChiTietHoaDon();
		entity.setSoLuong(cthd_DTO.getSoLuong());
		entity.setThanhTien(cthd_DTO.getThanhTien());

		// set HoaDon
		HoaDon hd = new HoaDon();
		hd.setMaHD(cthd_DTO.getMaHoaDon());
		entity.setHoaDon(hd);

		// set MonAn
		MonAn mon = new MonAn();
		mon.setMaMon(cthd_DTO.getMaMonAn());
		entity.setMonAn(mon);
		return chiTietHoaDon_DAO.themChiTiet(entity);
	}

	@Override
	public List<ChiTietHoaDon_DTO> getChiTietTheoMaHoaDon(String maHD) {
		List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDon_DAO.getChiTietTheoMaHoaDon(maHD);
		return chiTietHoaDons.stream().map(ct -> {
			ChiTietHoaDon_DTO dto = new ChiTietHoaDon_DTO();
			dto.setSoLuong(ct.getSoLuong());
			dto.setThanhTien(ct.getThanhTien());
			if (ct.getHoaDon() != null) {
				dto.setMaHoaDon(ct.getHoaDon().getMaHD());
			}
			if (ct.getMonAn() != null) {
				dto.setMaMonAn(ct.getMonAn().getMaMon());
				dto.setTenMon(ct.getMonAn().getTenMon());
				dto.setDonGia(ct.getMonAn().getDonGia());
			}
			return dto;
		}).toList();
	}

	@Override
	public void deleteByMaHoaDon(String maHD) {
		if (maHD == null || maHD.trim().isEmpty()) {
			throw new IllegalArgumentException("maHD không được rỗng");
		}
		chiTietHoaDon_DAO.deleteByMaHoaDon(maHD);
	}

}
