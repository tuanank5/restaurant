package service.impl;

import java.util.List;

import dao.LoaiBan_DAO;
import dao.impl.LoaiBan_DAOImpl;
import dto.LoaiBan_DTO;
import entity.LoaiBan;
import service.LoaiBan_Service;
import util.MapperUtil;

public class LoaiBan_ServiceImpl implements LoaiBan_Service {

	private LoaiBan_DAO loaiBan_DAO;

	public LoaiBan_ServiceImpl() {
		loaiBan_DAO = new LoaiBan_DAOImpl();
	}

	@Override
	public LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan) {
		if (tenLoaiBan == null || tenLoaiBan.trim().isEmpty()) {
			throw new IllegalArgumentException("tenLoaiBan không được rỗng");
		}
		LoaiBan loaiBan = loaiBan_DAO.timLoaiBanTheoTen(tenLoaiBan);
		return MapperUtil.map(loaiBan, LoaiBan_DTO.class);
	}

	@Override
	public List<LoaiBan_DTO> getAll() {
		List<LoaiBan> loaiBans = loaiBan_DAO.getAll();
		return loaiBans.stream().map(lb -> MapperUtil.map(lb, LoaiBan_DTO.class)).toList();
	}

}
