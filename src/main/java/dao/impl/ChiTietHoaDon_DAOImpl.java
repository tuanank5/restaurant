package dao.impl;

import dao.ChiTietHoaDon_DAO;
import entity.ChiTietHoaDon;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Map;

public class ChiTietHoaDon_DAOImpl 
        extends Entity_DAOImpl<ChiTietHoaDon> 
        implements ChiTietHoaDon_DAO {

    private EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("default");
    @Override
    public void deleteByMaHoaDon(String maHD) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createQuery("DELETE FROM ChiTietHoaDon WHERE hoaDon.maHoaDon = :ma")
              .setParameter("ma", maHD)
              .executeUpdate();

            tx.commit();

        } finally {
            em.close();
        }
    }

    @Override
    public List<ChiTietHoaDon> getChiTietTheoMaHoaDon(String maHD) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.maHD = :maHD",
                    ChiTietHoaDon.class)
                    .setParameter("maHD", maHD)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean themChiTiet(ChiTietHoaDon cthd) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cthd);
            tx.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;

        } finally {
            em.close();
        }
    }


    @Override
    public boolean them(ChiTietHoaDon entity) {
        return themChiTiet(entity);
    }

    @Override
    public boolean themNhieu(List<ChiTietHoaDon> listEntity) {
        return false;
    }

    @Override
    public boolean capNhat(ChiTietHoaDon entity) {
        return false;
    }

    @Override
    public boolean xoa(ChiTietHoaDon entity) {
        return false;
    }

    @Override
    public List<ChiTietHoaDon> getDanhSach(String namedQuery, 
                                         Class<ChiTietHoaDon> entityType) {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> getDanhSach(String namedQuery, 
                                         Class<ChiTietHoaDon> entityType,
                                         int limit, int skip) {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> getDanhSach(Class<ChiTietHoaDon> entityType,
                                         Map<String, Object> parameters) {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> getDanhSach(Class<ChiTietHoaDon> entityType,
                                         Map<String, Object> parameters,
                                         int limit, int skip) {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> getDanhSachByDate(int day, int month,
                                               int year,
                                               Class<ChiTietHoaDon> entityType,
                                               String nameColumn) {
        return null;
    }

    @Override
    public Long count(String namedQuery,
                      Class<ChiTietHoaDon> entityType) {
        return null;
    }

}
