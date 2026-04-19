package service;

import dto.TaiKhoan_DTO;

public interface TaiKhoan_Service {
	int demSoTaiKhoanTheoChucVu(String chucVu);

	boolean themTaiKhoan(TaiKhoan_DTO taiKhoan_DTO);

	String getMaxMaTK();
}
