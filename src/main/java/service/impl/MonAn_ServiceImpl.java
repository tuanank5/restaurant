package service.impl;

import config.RestaurantApplication;
import dao.MonAn_DAO;
import java.util.List;

import dto.MonAn_DTO;
import entity.MonAn;
import service.MonAn_Service;
import util.MonAnMapper;

public class MonAn_ServiceImpl implements MonAn_Service {

	private final MonAn_DAO dao =
			RestaurantApplication.getInstance()
					.getDatabaseContext()
					.newEntity_DAO(MonAn_DAO.class);

	@Override
	public List<MonAn_DTO> getAll() {
		List<MonAn> list = dao.getDanhSach(MonAn.class, null);
		return list.stream().map(MonAnMapper::toDTO).toList();
	}

	@Override
	public boolean add(MonAn_DTO dto) {
		MonAn mon = MonAnMapper.toEntity(dto);

		// AUTO ID (server)
		mon.setMaMon(generateMaMon());

		if (!validateAdd(mon)) return false;

		return dao.them(mon);
	}

	@Override
	public boolean update(MonAn_DTO dto) {
		MonAn mon = MonAnMapper.toEntity(dto);

		if (!validateUpdate(mon)) return false;

		return dao.capNhat(mon);
	}

	@Override
	public boolean delete(String maMon) {
		return dao.xoa(maMon);
	}

	@Override
	public String generateId() {
		return generateMaMon();
	}

	// ================= AUTO ID =================
	private String generateMaMon() {
		String maxId = dao.getMaxMaMon();

		if (maxId == null || maxId.isEmpty()) {
			return "M001";
		}

		try {
			int num = Integer.parseInt(maxId.replace("M", ""));
			num++;
			return String.format("M%03d", num);
		} catch (Exception e) {
			return "M001";
		}
	}

	// ================= VALIDATE ADD =================
	private boolean validateAdd(MonAn mon) {
		if (mon == null) return false;
		if (mon.getTenMon() == null || mon.getTenMon().isBlank()) return false;
		if (mon.getDonGia() <= 0) return false;
		return mon.getLoaiMon() != null && !mon.getLoaiMon().isBlank();
	}

	// ================= VALIDATE UPDATE =================
	private boolean validateUpdate(MonAn mon) {
		if (!validateAdd(mon)) return false;
		if (mon.getMaMon() == null || mon.getMaMon().isBlank()) return false;
		return dao.timTheoMa(mon.getMaMon()) != null;
	}
}
