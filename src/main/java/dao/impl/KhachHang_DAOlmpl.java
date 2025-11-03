package dao.impl;

import dao.KhachHang_DAO;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class KhachHang_DAOlmpl extends Entity_DAOImpl<KhachHang> implements KhachHang_DAO {

    @Override
    public KhachHang timTheoSDT(String sdt) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        KhachHang kh = null;

        try {
            emf = Persistence.createEntityManagerFactory("CRKPU"); // tên persistence-unit
            em = emf.createEntityManager();

            List<KhachHang> ds = em.createQuery(
                    "SELECT k FROM KhachHang k WHERE k.sdt = :sdt", KhachHang.class)
                    .setParameter("sdt", sdt)
                    .getResultList();

            if (!ds.isEmpty()) {
                kh = ds.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }

        return kh;
    }
}
