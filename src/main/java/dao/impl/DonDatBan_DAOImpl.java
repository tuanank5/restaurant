package dao.impl;

import java.util.List;

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

    @Override
    public List<DonDatBan> timTheoBan(Ban ban) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM DonDatBan d WHERE d.ban.maBan = :maBan ORDER BY d.ngayGioLapDon DESC", DonDatBan.class)
                    .setParameter("maBan", ban.getMaBan())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean them(DonDatBan donDatBan) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(donDatBan);
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
}
