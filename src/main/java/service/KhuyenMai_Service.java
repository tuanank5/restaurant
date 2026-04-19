package service;

import dto.KhuyenMai_DTO;

public interface KhuyenMai_Service {
	String getMaxMaKM();

	KhuyenMai_DTO timTheoMa(String maKM);

	boolean sua(KhuyenMai_DTO km_DTO);

	boolean xoa(String maKM);
}
