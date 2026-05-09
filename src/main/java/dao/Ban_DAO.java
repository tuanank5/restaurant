package dao;

import entity.Ban;
import entity.LoaiBan;

import java.util.List;

public interface Ban_DAO extends Entity_DAO<Ban> {
    LoaiBan timLoaiBanTheoTen(String tenLoaiBan);
    Ban timTheoMa(String maBan);
    String getMaxMaBan();

    boolean sua(Ban ban);
    List<Ban> getAllBan();

}