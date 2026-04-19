package service.impl;

import dao.KhuyenMai_DAO;
import dao.impl.KhuyenMai_DAOImpl;
import dto.KhuyenMai_DTO;
import entity.KhuyenMai;
import service.KhuyenMai_Service;
import util.MapperUtil;

public class KhuyenMai_ServiceImpl implements KhuyenMai_Service {

	private KhuyenMai_DAO khuyenMai_DAO;

	public KhuyenMai_ServiceImpl() {
		khuyenMai_DAO = new KhuyenMai_DAOImpl();
	}

	@Override
	public String getMaxMaKM() {
		return khuyenMai_DAO.getMaxMaKM();
	}

	@Override
	public KhuyenMai_DTO timTheoMa(String maKM) {
		if (maKM == null || maKM.trim().isEmpty()) {
			throw new IllegalArgumentException("maKM không được rỗng");
		}
		KhuyenMai khuyenMai = khuyenMai_DAO.timTheoMa(maKM);
		return MapperUtil.map(khuyenMai, KhuyenMai_DTO.class);
	}

	@Override
	public boolean sua(KhuyenMai_DTO km_DTO) {
		if (km_DTO == null) {
			throw new IllegalArgumentException("km_DTO không được rỗng");
		}
		if (km_DTO.getMaKM() == null || km_DTO.getMaKM().trim().isEmpty()) {
			throw new IllegalArgumentException("km_DTO.maKM không được rỗng");
		}
		return khuyenMai_DAO.sua(MapperUtil.map(km_DTO, KhuyenMai.class));
	}

	@Override
	public boolean xoa(String maKM) {
		if (maKM == null || maKM.trim().isEmpty()) {
			throw new IllegalArgumentException("maKM không được rỗng");
		}
		return khuyenMai_DAO.xoa(maKM);
	}

}
