package service;

import java.util.List;

import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import service.impl.KhachHang_ServiceImpl;

public interface KhachHang_Service {
	KhachHang_DTO timTheoSDT(String sdt);

	KhachHang_DTO timTheoMa(String maKH);

	List<KhachHang_DTO> getAll();

	KhachHang_DTO getKhachHangTheoSDT(String sdt);
	boolean capNhat(KhachHang_DTO dto);
}
