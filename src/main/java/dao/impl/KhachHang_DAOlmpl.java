package dao.impl;

import dao.KhachHang_DAO;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class KhachHang_DAOlmpl extends Entity_DAOImpl<KhachHang> implements KhachHang_DAO {
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public KhachHang timTheoSDT(String sdt) {
        EntityManager em = getEntityManager();
        try {
            List<KhachHang> ds = em.createQuery(
                    "SELECT k FROM KhachHang k WHERE k.sdt = :sdt", KhachHang.class)
                    .setParameter("sdt", sdt)
                    .getResultList();

            if (!ds.isEmpty()) {
                return ds.get(0);
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public KhachHang timTheoMa(String maKH) {
        EntityManager em = getEntityManager();
        try {
            List<KhachHang> ds = em.createQuery(
                    "SELECT k FROM KhachHang k WHERE k.maKH = :maKH", KhachHang.class)
                    .setParameter("maKH", maKH)
                    .getResultList();

            if (!ds.isEmpty()) {
                return ds.get(0);
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
	@Override
	public List<KhachHang> getAll() {
		EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT k FROM KhachHang k", KhachHang.class)
                    .getResultList();
        } finally {
            em.close();
        }
	}
	
	@Override
	public KhachHang getKhachHangTheoSDT(String sdt) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.createQuery(
	            "SELECT k FROM KhachHang k WHERE k.sdt = :s",
	            KhachHang.class
	        )
	        .setParameter("s", sdt)
	        .getSingleResult();
	    } catch (Exception e) {
	        return null;
	    } finally {
	        em.close();
	    }
	}

}
