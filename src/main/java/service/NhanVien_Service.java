package service;

import dto.NhanVien_DTO;

import java.util.List;

public interface NhanVien_Service {
	String getMaxMaNV();

	List<NhanVien_DTO> getAll();
}
