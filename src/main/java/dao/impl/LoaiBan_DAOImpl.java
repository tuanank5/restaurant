package dao.impl;

import dao.LoaiBan_DAO;
import entity.LoaiBan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class LoaiBan_DAOImpl extends Entity_DAOImpl<LoaiBan> implements LoaiBan_DAO {

    @Override
    public LoaiBan timLoaiBanTheoTen(String tenLoaiBan) {
        if (tenLoaiBan == null || tenLoaiBan.trim().isEmpty()) return null;

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM LoaiBan l WHERE UPPER(TRIM(l.tenLoaiBan)) = :ten", LoaiBan.class)
                    .setParameter("ten", tenLoaiBan.trim().toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}