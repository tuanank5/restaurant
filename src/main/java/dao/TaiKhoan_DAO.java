package dao;

import entity.TaiKhoan;

public interface TaiKhoan_DAO extends Entity_DAO<TaiKhoan> {
	int demSoTaiKhoanTheoChucVu(String chucVu);
	boolean themTaiKhoan(TaiKhoan taiKhoan);
	String getMaxMaTK();
}
