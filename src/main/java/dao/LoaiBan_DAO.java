package dao;

import java.util.List;

import entity.LoaiBan;

public interface LoaiBan_DAO extends Entity_DAO<LoaiBan> {

	LoaiBan timLoaiBanTheoTen(String tenLoaiBan);

	List<LoaiBan> getAll();
}
