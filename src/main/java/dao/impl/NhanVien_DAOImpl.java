package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.DatabaseContext;
import dao.NhanVien_DAO;
import entity.NhanVien;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import dao.impl.NhanVien_DAOImpl;
import dao.impl.TaiKhoan_DAOImpl;
import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class NhanVien_DAOImpl extends Entity_DAOImpl<NhanVien> implements NhanVien_DAO {
	
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
   
    @Override
    public String getMaxMaNV() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT MAX(n.maNV) FROM NhanVien n",
                    String.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

}
