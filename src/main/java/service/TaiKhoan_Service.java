package service;

import java.util.List;

import dto.TaiKhoan_DTO;

public interface TaiKhoan_Service {
	List<TaiKhoan_DTO> getAll();

	int demSoTaiKhoanTheoChucVu(String chucVu);

	boolean themTaiKhoan(TaiKhoan_DTO taiKhoan_DTO);

	String getMaxMaTK();
}
