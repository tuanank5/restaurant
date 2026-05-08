package service.impl;

import java.util.List;
import java.util.stream.Collectors;

import dao.MonAn_DAO;
import dao.impl.MonAn_DAOImpl;
import dto.MonAn_DTO;
import entity.MonAn;
import service.MonAn_Service;
import util.MapperUtil;

public class MonAn_ServiceImpl implements MonAn_Service {

	private MonAn_DAO monAn_DAO;

	public MonAn_ServiceImpl() {
		monAn_DAO = new MonAn_DAOImpl();
	}

	@Override
	public List<MonAn_DTO> getDanhSachMonAn() {
		List<MonAn> monAns = monAn_DAO.getDanhSachMonAn();
		return monAns.stream()
				.map(ma -> MonAn_DTO.builder()
						.maMon(ma.getMaMon())
						.tenMon(ma.getTenMon())
						.donGia(ma.getDonGia())
						.duongDanAnh(ma.getDuongDanAnh())
						.loaiMon(ma.getLoaiMon())
						.build())
				.toList();
	}

	@Override
	public MonAn_DTO timTheoMa(String maMon) {
		if (maMon == null || maMon.trim().isEmpty()) {
			throw new IllegalArgumentException("maMon không được rỗng");
		}
		MonAn monAn = monAn_DAO.timTheoMa(maMon);
		return MapperUtil.map(monAn, MonAn_DTO.class);
	}

	@Override
	public boolean them(MonAn_DTO mon_DTO) {
		if (mon_DTO == null) {
			throw new IllegalArgumentException("mon_DTO không được rỗng");
		}
		if (mon_DTO.getMaMon() == null || mon_DTO.getMaMon().trim().isEmpty()) {
			throw new IllegalArgumentException("mon_DTO.maMon không được rỗng");
		}
		return monAn_DAO.them(MapperUtil.map(mon_DTO, MonAn.class));
	}

	@Override
	public boolean capNhat(MonAn_DTO mon_DTO) {
		if (mon_DTO == null) {
			throw new IllegalArgumentException("mon_DTO không được rỗng");
		}
		if (mon_DTO.getMaMon() == null || mon_DTO.getMaMon().trim().isEmpty()) {
			throw new IllegalArgumentException("mon_DTO.maMon không được rỗng");
		}
		return monAn_DAO.capNhat(MapperUtil.map(mon_DTO, MonAn.class));
	}

	@Override
	public boolean xoa(String maMon) {
		if (maMon == null || maMon.trim().isEmpty()) {
			throw new IllegalArgumentException("maMon không được rỗng");
		}
		return monAn_DAO.xoa(maMon);
	}

}
