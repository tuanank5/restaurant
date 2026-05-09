package service;

import java.util.List;
import dto.KhuyenMai_DTO;

import java.util.List;

public interface KhuyenMai_Service {
	String getMaxMaKM();

	KhuyenMai_DTO timTheoMa(String maKM);

	boolean sua(KhuyenMai_DTO km_DTO);

	boolean xoa(String maKM);

    List<KhuyenMai_DTO> getAll();
    boolean add(KhuyenMai_DTO dto);
    boolean update(KhuyenMai_DTO dto);
    boolean delete(String maKM);
	List<KhuyenMai_DTO> getDanhSach(String namedQuery);
}
