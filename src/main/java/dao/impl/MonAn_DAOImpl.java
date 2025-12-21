package dao.impl;

import dao.MonAn_DAO;
import entity.MonAn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MonAn_DAOImpl extends Entity_DAOImpl<MonAn> implements MonAn_DAO {

    private final EntityManagerFactory emf = restaurantApplication.getEntityManagerFactory();

    // ✅ LẤY DANH SÁCH MON AN – KHÔNG DÙNG getAll()
    public List<MonAn> getDanhSachMonAn() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<MonAn> query = em.createQuery(
                    "SELECT m FROM MonAn m", MonAn.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // trả về list rỗng an toàn
        } finally {
            em.close();
        }
    }

    @Override
    public MonAn timTheoMa(String maMon) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(MonAn.class, maMon);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean them(MonAn mon) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Merge khuyến mãi để đảm bảo managed entity
//            KhuyenMai kmManaged = em.merge(mon.getKhuyenMai());
//          mon.setKhuyenMai(kmManaged);
            em.persist(mon);
            em.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean capNhat(MonAn mon) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mon); // merge để cập nhật dữ liệu
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean xoa(String maMon) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MonAn mon = em.find(MonAn.class, maMon);
            if (mon != null) {
                em.remove(mon);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}