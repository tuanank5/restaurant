package dao;

import java.util.List;

import entity.KhachHang;

public interface KhachHang_DAO extends Entity_DAO<KhachHang> {
	KhachHang timTheoSDT(String sdt);
	List<KhachHang> getAll();
}
