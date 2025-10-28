package config.impl;

import config.DatabaseContext;
import dao.*;
import dao.impl.*;
import dao.KhachHang_DAO;
import dao.impl.KhachHang_DAOlmpl;

public class DatabaseContextImpl implements DatabaseContext{

	@Override
	public <R extends Entity_DAO<?>> R newEntity_DAO(Class<R> entityType) {
		if (entityType == TaiKhoan_DAO.class) {
            return (R) new TaiKhoan_DAOImpl();
        }
        if (entityType == NhanVien_DAO.class) {
            return (R) new NhanVien_DAOImpl();
        }
        if (entityType == KhachHang_DAO.class) {
            return (R) new KhachHang_DAOlmpl();
        }
        throw new IllegalArgumentException(
                "There is no Entity Dao for " +
                        entityType.getName()
        );
        
	}

}
