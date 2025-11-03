package config.impl;

import config.DatabaseContext;
import dao.*;
import dao.impl.*;

public class DatabaseContextImpl implements DatabaseContext{
	@Override
	public <R extends Entity_DAO<?>> R newEntity_DAO(Class<R> entityType) {
		if (entityType == Ban_DAO.class) {
            return (R) new Ban_DAOImpl();
        }
		if (entityType == ChiTietHoaDon_DAO.class) {
            return (R) new ChiTietHoaDon_DAOImpl();
        }
		if (entityType == Coc_DAO.class) {
            return (R) new Coc_DAOImpl();
        }
		if (entityType == DonDatBan_DAO.class) {
            return (R) new DonDatBan_DAOImpl();
        }

		if (entityType == DonLapDoiHuyBan_DAO.class) {
            return (R) new DonLapDoiHuyBan_DAOImpl();
        }
		if(entityType == HangKhachHang_DAO.class) {
        	return (R) new HangKhachHang_DAOImpl();
        }
		if (entityType == HoaDon_DAO.class) {
            return (R) new HoaDon_DAOImpl();
        }
		if (entityType == KhachHang_DAO.class) {
            return (R) new KhachHang_DAOlmpl();
        }
		if (entityType == KhuyenMai_DAO.class) {
            return (R) new KhuyenMai_DAOImpl();
        }
		if (entityType == LoaiBan_DAO.class) {
            return (R) new LoaiBan_DAOImpl();
        }
		if (entityType == NhanVien_DAO.class) {
            return (R) new NhanVien_DAOImpl();
        }
		if (entityType == TaiKhoan_DAO.class) {
            return (R) new TaiKhoan_DAOImpl();
        }
        throw new IllegalArgumentException(
                "There is no Entity Dao for " +
                        entityType.getName()
        );
        
	}
	
	
}
