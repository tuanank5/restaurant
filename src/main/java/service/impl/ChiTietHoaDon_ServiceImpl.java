package service.impl;

import java.util.List;

import dao.ChiTietHoaDon_DAO;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dto.ChiTietHoaDon_DTO;
import entity.ChiTietHoaDon;
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
		return chiTietHoaDon_DAO.themChiTiet(MapperUtil.map(cthd_DTO, ChiTietHoaDon.class));
	}

	@Override
	public List<ChiTietHoaDon_DTO> getChiTietTheoMaHoaDon(String maHD) {
		if (maHD == null || maHD.trim().isEmpty()) {
			throw new IllegalArgumentException("maHD không được rỗng");
		}
		List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDon_DAO.getChiTietTheoMaHoaDon(maHD);
		return chiTietHoaDons.stream().map(chiTietHoaDon -> MapperUtil.map(chiTietHoaDon, ChiTietHoaDon_DTO.class))
				.toList();
	}

	@Override
	public void deleteByMaHoaDon(String maHD) {
		if (maHD == null || maHD.trim().isEmpty()) {
			throw new IllegalArgumentException("maHD không được rỗng");
		}
		chiTietHoaDon_DAO.deleteByMaHoaDon(maHD);
	}

}
