package service;

import dto.Ban_DTO;
import dto.LoaiBan_DTO;

import java.util.List;

public interface Ban_Service {
    LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan);

    String getMaxMaBan();

    boolean sua(Ban_DTO ban_DTO);

    boolean them(Ban_DTO ban_DTO);

    boolean xoaTheoMa(String maBan);

    String sinhMaBanTiepTheo();

    List<Ban_DTO> getAllBan();
}
