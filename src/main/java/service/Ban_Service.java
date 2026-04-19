package service;

import dto.Ban_DTO;
import dto.LoaiBan_DTO;

public interface Ban_Service {
	LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan);

	String getMaxMaBan();

	boolean sua(Ban_DTO ban_DTO);
}
