package service;

import java.util.List;

import dto.MonAn_DTO;

public interface MonAn_Service {
	// CRUD giống flow KhuyenMai
	List<MonAn_DTO> getAll();

	boolean add(MonAn_DTO mon_DTO);

	boolean update(MonAn_DTO mon_DTO);

	boolean delete(String maMon);

	String generateId();

	// giữ tương thích các tên method cũ
	default List<MonAn_DTO> getDanhSachMonAn() {
		return getAll();
	}

	default boolean them(MonAn_DTO mon_DTO) {
		return add(mon_DTO);
	}

	default boolean capNhat(MonAn_DTO mon_DTO) {
		return update(mon_DTO);
	}

	default boolean xoa(String maMon) {
		return delete(maMon);
	}
}
