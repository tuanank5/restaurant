package service;

import java.util.List;
import dto.KhuyenMai_DTO;

public interface KhuyenMai_Service {
	List<KhuyenMai_DTO> getAll();
	boolean add(KhuyenMai_DTO dto);
	boolean update(KhuyenMai_DTO dto);
	boolean delete(String maKM);
}