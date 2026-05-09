package dao;

import java.util.List;

import entity.MonAn;

public interface MonAn_DAO extends Entity_DAO<MonAn> {
	List<MonAn> getDanhSachMonAn();

	String getMaxMaMon();

	MonAn timTheoMa(String maMon);

	boolean them(MonAn mon);

	boolean capNhat(MonAn mon);

	boolean xoa(String maMon);
}