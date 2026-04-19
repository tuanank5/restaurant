package service;

import java.util.List;

import dto.ChiTietHoaDon_DTO;

public interface ChiTietHoaDon_Service {
	boolean themChiTiet(ChiTietHoaDon_DTO cthd_DTO);

	List<ChiTietHoaDon_DTO> getChiTietTheoMaHoaDon(String maHD);

	void deleteByMaHoaDon(String maHD);
}
