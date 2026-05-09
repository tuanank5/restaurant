package service.impl;

import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dto.Ban_DTO;
import dto.LoaiBan_DTO;
import entity.Ban;
import entity.LoaiBan;
import service.Ban_Service;
import util.MapperUtil;

import java.util.List;

public class Ban_ServiceImpl implements Ban_Service {

	private Ban_DAO ban_DAO;

	public Ban_ServiceImpl() {
		ban_DAO = new Ban_DAOImpl();
	}

	@Override
	public LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan) {
		if (tenLoaiBan == null || tenLoaiBan.trim().isEmpty()) {
			throw new IllegalArgumentException("tenLoaiBan không được rỗng");
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
			throw new IllegalArgumentException("ban_DTO không được rỗng");
		}
		if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
			throw new IllegalArgumentException("ban_DTO.maBan không được rỗng");
		}
		Ban banCu = ban_DAO.timTheoMa(ban_DTO.getMaBan());
		if (banCu == null) {
			throw new IllegalArgumentException("Không tìm thấy bàn");
		}

		banCu.setTrangThai(ban_DTO.getTrangThai());
		banCu.setViTri(ban_DTO.getViTri());
		return ban_DAO.sua(banCu);
	}

	@Override
	public List<Ban_DTO> getAllBan() {
		List<Ban> ds = ban_DAO.getAllBan();
		return ds.stream().map(ban -> {
			Ban_DTO dto = MapperUtil.map(ban, Ban_DTO.class);
			if (ban.getLoaiBan() != null) {
				dto.setMaLoaiBan(ban.getLoaiBan().getMaLoaiBan());
				dto.setTenLoaiBan(ban.getLoaiBan().getTenLoaiBan());
			}
			return dto;
		}).toList();
	}

}
