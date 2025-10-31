package dao;

import entity.LoaiBan;

public interface LoaiBan_DAO extends Entity_DAO<LoaiBan> {
    LoaiBan timLoaiBanTheoTen(String tenLoaiBan);
}