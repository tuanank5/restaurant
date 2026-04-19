package service.impl;

import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dto.Ban_DTO;
import dto.LoaiBan_DTO;
import entity.Ban;
import entity.LoaiBan;
import service.Ban_Service;
import util.MapperUtil;

public class Ban_ServiceImpl implements Ban_Service {

	private Ban_DAO ban_DAO;

	public Ban_ServiceImpl() {
		ban_DAO = new Ban_DAOImpl();
	}

	@Override
	public LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan) {
		if (tenLoaiBan == null || tenLoaiBan.trim().isEmpty()) {
			throw new IllegalArgumentException("timLoaiBanTheoTen: tenLoaiBan không được rỗng");
		}
		LoaiBan loaiBan = ban_DAO.timLoaiBanTheoTen(tenLoaiBan);
		return MapperUtil.map(loaiBan, LoaiBan_DTO.class);
	}

	@Override
	public String getMaxMaBan() {
		return ban_DAO.getMaxMaBan();
	}

	@Override
	public boolean sua(Ban_DTO ban_DTO) {
		if (ban_DTO == null) {
			throw new IllegalArgumentException("sua: ban_DTO không được rỗng");
		}
		if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
			throw new IllegalArgumentException("sua: ban_DTO.maBan không được rỗng");
		}
		return ban_DAO.sua(MapperUtil.map(ban_DTO, Ban.class));
	}

}
