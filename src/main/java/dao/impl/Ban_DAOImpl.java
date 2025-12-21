package dao.impl;

import dao.Ban_DAO;
import entity.Ban;
import entity.LoaiBan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class Ban_DAOImpl extends Entity_DAOImpl<Ban> implements Ban_DAO {

    private final EntityManagerFactory emf = restaurantApplication.getEntityManagerFactory();

    @Override
    public LoaiBan timLoaiBanTheoTen(String tenLoaiBan) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM LoaiBan l WHERE l.tenLoaiBan = :ten", LoaiBan.class)
                    .setParameter("ten", tenLoaiBan)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public String getMaxMaBan() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT MAX(b.maBan) FROM Ban b", String.class
            ).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    
    @Override
    public boolean sua(Ban ban) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ban); // Cập nhật bàn
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
}