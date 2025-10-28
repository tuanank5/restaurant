package dao.impl;

import dao.QuanLyBan_DAO;
import entity.QuanLyBan;

public class Ban_DAOImpl extends Entity_DAOImpl<QuanLyBan> implements QuanLyBan_DAO {

	 private final Class<QuanLyBan> entityClass = QuanLyBan.class;

	    public Ban_DAOImpl() {
	        // Không gọi super
	    }

	    public Class<QuanLyBan> getEntityClass() {
	        return entityClass;
	    }

}
