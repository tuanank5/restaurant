package service;

import java.util.List;

import dto.MonAn_DTO;

public interface MonAn_Service {
	List<MonAn_DTO> getDanhSachMonAn();

	MonAn_DTO timTheoMa(String maMon);

	boolean them(MonAn_DTO mon_DTO);

	boolean capNhat(MonAn_DTO mon_DTO);

	boolean xoa(String maMon);
}
