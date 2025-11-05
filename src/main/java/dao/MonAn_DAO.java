package dao;

import java.util.List;

import entity.MonAn;

public interface MonAn_DAO extends Entity_DAO<MonAn> {
	    MonAn timTheoMa(String maMon);
	    boolean them(MonAn mon);
	    boolean sua(MonAn mon);
	    boolean xoa(String maMon);
}