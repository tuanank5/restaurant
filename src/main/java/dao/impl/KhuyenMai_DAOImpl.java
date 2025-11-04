package dao.impl;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class KhuyenMai_DAOImpl extends Entity_DAOImpl<KhuyenMai> implements KhuyenMai_DAO {

    private final EntityManagerFactory emf = restaurantApplication.getEntityManagerFactory();

    @Override
    public String getMaxMaKM() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT MAX(k.maKM) FROM KhuyenMai k", String.class)
                     .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public KhuyenMai timTheoMa(String maKM) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT k FROM KhuyenMai k WHERE k.maKM = :ma",
                    KhuyenMai.class).setParameter("ma", maKM).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean sua(KhuyenMai km) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(km);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean xoa(String maKM) {
        EntityManager em = emf.createEntityManager();
        try {
            KhuyenMai km = em.find(KhuyenMai.class, maKM);
            if(km == null) return false;
            em.getTransaction().begin();
            em.remove(km);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
}