package service;

import dto.Ban_DTO;

import java.util.List;

public interface Ban_Service {

	// CRUD giống flow KhuyenMai
	List<Ban_DTO> getAll();

	boolean add(Ban_DTO dto);

	boolean update(Ban_DTO dto);

	boolean delete(String maBan);

	String generateId();
}
