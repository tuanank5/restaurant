package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import dao.DonDatBan_DAO;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class DonDatBan_DAOImpl extends Entity_DAOImpl<DonDatBan> implements DonDatBan_DAO {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    @Override
    public List<DonDatBan> timTheoKhachHang(KhachHang kh) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                "SELECT d FROM DonDatBan d WHERE d.khachHang.maKH = :maKH", DonDatBan.class)
                .setParameter("maKH", kh.getMaKH())
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean them(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
//    public DonDatBan timTheoBan(Ban ban) {
//    	EntityManager em = getEntityManager();
//        List<DonDatBan> ds = em.createQuery("SELECT d FROM DonDatBan d WHERE d.ban = :ban ORDER BY d.ngayGioLapDon DESC", DonDatBan.class)
//                               .setParameter("ban", ban)
//                               .setMaxResults(1)
//                               .getResultList();
//        return ds.isEmpty() ? null : ds.get(0);
//    }
}
