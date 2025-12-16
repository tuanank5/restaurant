package dao.impl;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TaiKhoan_DAOImpl extends Entity_DAOImpl<TaiKhoan> implements TaiKhoan_DAO {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public int demSoTaiKhoanTheoChucVu(String chucVu) {
        EntityManager em = getEntityManager();
        try {
            String prefix = chucVu.equalsIgnoreCase("Quản lý") ? "QL%" : "NV%";

            Long count = em.createQuery(
                    "SELECT COUNT(t) FROM TaiKhoan t WHERE t.tenDangNhap LIKE :prefix",
                    Long.class
            )
            .setParameter("prefix", prefix)
            .getSingleResult();

            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return 0;
    }
    
    @Override
    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        return super.them(taiKhoan); // dùng lại Entity_DAOImpl
    }
    
    @Override
    public String getMaxMaTK() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT MAX(t.maTaiKhoan) FROM TaiKhoan t",
                    String.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

}
