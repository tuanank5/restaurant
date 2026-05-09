package service.impl;

import dao.KhuyenMai_DAO;
import dto.KhuyenMai_DTO;
import entity.KhuyenMai;
import service.KhuyenMai_Service;
import config.RestaurantApplication;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class KhuyenMai_ServiceImpl implements KhuyenMai_Service {

	private final KhuyenMai_DAO dao =
			RestaurantApplication.getInstance()
					.getDatabaseContext()
					.newEntity_DAO(KhuyenMai_DAO.class);

	@Override
	public String getMaxMaKM() {
		return dao.getMaxMaKM();
	}

	@Override
	public KhuyenMai_DTO timTheoMa(String maKM) {
		KhuyenMai km = dao.timTheoMa(maKM);
		if (km == null) {
			return null;
		}
		return toDto(km);
	}

	@Override
	public boolean sua(KhuyenMai_DTO km_DTO) {
		return update(km_DTO);
	}

	@Override
	public boolean xoa(String maKM) {
		return delete(maKM);
	}

	@Override
	public List<KhuyenMai_DTO> getDanhSach(String namedQuery) {
		List<KhuyenMai> list = dao.getDanhSach(namedQuery, KhuyenMai.class);
		if (list == null) {
			return List.of();
		}
		return list.stream().map(this::toDto).toList();
	}

	@Override
	public List<KhuyenMai_DTO> getAll() {
		List<KhuyenMai> list = dao.getDanhSach(KhuyenMai.class, new HashMap<>());
		return list.stream().map(this::toDto).toList();
	}

	private KhuyenMai_DTO toDto(KhuyenMai km) {
		if (km == null) {
			return null;
		}
		return KhuyenMai_DTO.builder()
				.maKM(km.getMaKM())
				.tenKM(km.getTenKM())
				.loaiKM(km.getLoaiKM())
				.ngayBatDau(km.getNgayBatDau())
				.ngayKetThuc(km.getNgayKetThuc())
				.phanTramGiamGia(km.getPhanTramGiamGia())
				.build();
	}

	// ================= ADD =================
	@Override
	public boolean add(KhuyenMai_DTO dto) {

		KhuyenMai km = toEntity(dto);

		// 🔥 AUTO ID
		km.setMaKM(generateMaKM());

		System.out.println("👉 AUTO ID: " + km.getMaKM());

		if (!validateAdd(km)) return false;

		return dao.them(km);
	}

	// ================= UPDATE =================
	@Override
	public boolean update(KhuyenMai_DTO dto) {

		KhuyenMai km = toEntity(dto);

		if (!validateUpdate(km)) return false;

		return dao.sua(km);
	}

	@Override
	public boolean delete(String maKM) {
		return dao.xoa(maKM);
	}

	// ================= AUTO ID =================
	private String generateMaKM() {
		String maxId = dao.getMaxMaKM();

		if (maxId == null || maxId.isEmpty()) {
			return "KM001";
		}

		int num = Integer.parseInt(maxId.replace("KM", ""));
		num++;

		return String.format("KM%03d", num);
	}

	// ================= VALIDATE ADD =================
	private boolean validateAdd(KhuyenMai km) {

		LocalDate today = LocalDate.now();

		if (km.getNgayBatDau().toLocalDate().isBefore(today)) {
			System.out.println("❌ Ngày bắt đầu sai");
			return false;
		}

		if (!km.getNgayKetThuc().after(km.getNgayBatDau())) {
			System.out.println("❌ Ngày kết thúc sai");
			return false;
		}

		return !isDuplicateAdd(km);
	}

	// ================= VALIDATE UPDATE =================
	private boolean validateUpdate(KhuyenMai km) {

		if (!km.getNgayKetThuc().after(km.getNgayBatDau())) {
			System.out.println("❌ Sai ngày update");
			return false;
		}

		return !isDuplicateUpdate(km);
	}

	// ================= DUPLICATE ADD =================
	private boolean isDuplicateAdd(KhuyenMai newKM) {

		List<KhuyenMai> ds = dao.getDanhSach(KhuyenMai.class, new HashMap<>());

		for (KhuyenMai km : ds) {

			boolean trung =
					km.getTenKM().equalsIgnoreCase(newKM.getTenKM()) &&
							km.getLoaiKM().equalsIgnoreCase(newKM.getLoaiKM());

			boolean overlap =
					!(newKM.getNgayKetThuc().before(km.getNgayBatDau()) ||
							newKM.getNgayBatDau().after(km.getNgayKetThuc()));

			if (trung && overlap) return true;
		}

		return false;
	}

	// ================= DUPLICATE UPDATE =================
	private boolean isDuplicateUpdate(KhuyenMai newKM) {

		List<KhuyenMai> ds = dao.getDanhSach(KhuyenMai.class, new HashMap<>());

		for (KhuyenMai km : ds) {

			if (km.getMaKM().equals(newKM.getMaKM())) continue;

			boolean trung =
					km.getTenKM().equalsIgnoreCase(newKM.getTenKM()) &&
							km.getLoaiKM().equalsIgnoreCase(newKM.getLoaiKM());

			boolean overlap =
					!(newKM.getNgayKetThuc().before(km.getNgayBatDau()) ||
							newKM.getNgayBatDau().after(km.getNgayKetThuc()));

			if (trung && overlap) return true;
		}

		return false;
	}

	private KhuyenMai toEntity(KhuyenMai_DTO dto) {
		if (dto == null) return null;

		return KhuyenMai.builder()
				.maKM(dto.getMaKM())
				.tenKM(dto.getTenKM())
				.loaiKM(dto.getLoaiKM())
				.ngayBatDau(dto.getNgayBatDau())
				.ngayKetThuc(dto.getNgayKetThuc())
				.phanTramGiamGia(dto.getPhanTramGiamGia())
				.build();
	}
}