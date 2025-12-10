package dao;

import entity.LoaiBan;

import java.util.List;

public interface LoaiBan_DAO extends Entity_DAO<LoaiBan> {
    
    LoaiBan timLoaiBanTheoTen(String tenLoaiBan);

    List<LoaiBan> getAll();
}
