package dao.impl;

import dao.ChiTietHoaDon_DAO;
import entity.ChiTietHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ChiTietHoaDon_DAOImpl extends Entity_DAOImpl<ChiTietHoaDon> implements ChiTietHoaDon_DAO {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

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
}
