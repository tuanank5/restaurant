package service;

import java.util.List;

import dto.LoaiBan_DTO;

public interface LoaiBan_Service {
	LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan);

	List<LoaiBan_DTO> getAll();
}
