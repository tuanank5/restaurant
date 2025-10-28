package config.impl;

import config.DatabaseContext;
import dao.*;
import dao.impl.*;
import dao.KhachHang_DAO;

public class DatabaseContextImpl implements DatabaseContext{

	@Override
	public <R extends Entity_DAO<?>> R newEntity_DAO(Class<R> entityType) {
		if (entityType == TaiKhoan_DAO.class) {
            return (R) new TaiKhoan_DAOImpl();
        }
        if (entityType == NhanVien_DAO.class) {
            return (R) new NhanVien_DAOImpl();
        }
        if(entityType == HangKhachHang_DAO.class) {
        	return (R) new HangKhachHang_DAOImpl();
        }
        if (entityType == KhachHang_DAO.class) {
            return (R) new KhachHang_DAOlmpl();
        }
        if (entityType == Ban_DAO.class) {
            return (R) new Ban_DAOImpl();
        }
        if (entityType == LoaiBan_DAO.class) {
            return (R) new LoaiBan_DAOImpl();
        }
        if (entityType == KhuyenMai_DAO.class) {
            return (R) new KhuyenMai_DAOImpl();
        }
        throw new IllegalArgumentException(
                "There is no Entity Dao for " +
                        entityType.getName()
        );
        
	}

}
