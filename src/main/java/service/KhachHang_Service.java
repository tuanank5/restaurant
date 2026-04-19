package service;

import java.util.List;

import dto.KhachHang_DTO;

public interface KhachHang_Service {
	KhachHang_DTO timTheoSDT(String sdt);

	KhachHang_DTO timTheoMa(String maKH);

	List<KhachHang_DTO> getAll();

	KhachHang_DTO getKhachHangTheoSDT(String sdt);
}
