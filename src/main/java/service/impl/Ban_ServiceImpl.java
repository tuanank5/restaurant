package service.impl;

import config.RestaurantApplication;
import dao.Ban_DAO;
import dto.Ban_DTO;
import entity.Ban;
import entity.LoaiBan;
import service.Ban_Service;

import java.util.HashMap;
import java.util.List;

public class Ban_ServiceImpl implements Ban_Service {

	private final Ban_DAO dao =
			RestaurantApplication.getInstance()
					.getDatabaseContext()
					.newEntity_DAO(Ban_DAO.class);

	@Override
	public List<Ban_DTO> getAll() {
		List<Ban> list = dao.getDanhSach(Ban.class, new HashMap<>());
		return list.stream().map(ban ->
				Ban_DTO.builder()
						.maBan(ban.getMaBan())
						.viTri(ban.getViTri())
						.trangThai(ban.getTrangThai())
						.maLoaiBan(ban.getLoaiBan() != null ? ban.getLoaiBan().getMaLoaiBan() : null)
						.build()
		).toList();
	}

	@Override
	public boolean add(Ban_DTO dto) {

		Ban ban = toEntityWithLoai(dto);

		if (ban == null) return false;

		// AUTO ID (server)
		ban.setMaBan(generateId());

		if (!validateAdd(ban))
			return false;

		return dao.them(ban);
	}

	@Override
	public boolean update(Ban_DTO dto) {

		Ban ban = toEntityWithLoai(dto);

		if (!validateUpdate(ban))
			return false;

		return dao.sua(ban);
	}

	@Override
	public boolean delete(String maBan) {

		if (maBan == null || maBan.isBlank())
			return false;

		Ban ban = dao.timTheoMa(maBan);

		if (ban == null)
			return false;

		return dao.xoa(ban);
	}

	@Override
	public String generateId() {

		String max = dao.getMaxMaBan();

		if (max == null)
			return "B01";

		try {

			int so = Integer.parseInt(max.replaceAll("\\D+", ""));

			return String.format("B%02d", so + 1);

		} catch (Exception e) {

			return "B01";
		}
	}

	private Ban toEntityWithLoai(Ban_DTO dto) {

		if (dto == null)
			return null;

		LoaiBan loaiBan = null;

		if (dto.getMaLoaiBan() != null && !dto.getMaLoaiBan().isBlank()) {

			loaiBan = dao.timLoaiBanTheoMa(dto.getMaLoaiBan());
		}

		return Ban.builder()
				.maBan(dto.getMaBan())
				.viTri(dto.getViTri())
				.trangThai(dto.getTrangThai())
				.loaiBan(loaiBan)
				.build();
	}

	private boolean validateAdd(Ban ban) {

		if (ban == null)
			return false;

		if (ban.getViTri() == null || ban.getViTri().isBlank())
			return false;

		if (ban.getLoaiBan() == null)
			return false;

		if (ban.getTrangThai() == null || ban.getTrangThai().isBlank())
			return false;

		return true;
	}

	private boolean validateUpdate(Ban ban) {

		if (!validateAdd(ban))
			return false;

		if (ban.getMaBan() == null || ban.getMaBan().isBlank())
			return false;

		return dao.timTheoMa(ban.getMaBan()) != null;
	}
}