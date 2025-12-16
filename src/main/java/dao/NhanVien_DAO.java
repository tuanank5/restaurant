package dao;

import entity.NhanVien;

public interface NhanVien_DAO extends Entity_DAO<NhanVien> {
	String getMaxMaNV();
}
