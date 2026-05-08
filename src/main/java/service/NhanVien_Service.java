package service;

import dto.NhanVien_DTO;
import entity.NhanVien;

public interface NhanVien_Service {
	String getMaxMaNV();
	NhanVien_DTO findById(String maNV);
}
