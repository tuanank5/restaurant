package dao.impl;

import dao.NhanVien_DAO;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class NhanVien_DAOImpl extends Entity_DAOImpl<NhanVien> implements NhanVien_DAO {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	@Override
	public String getMaxMaNV() {
		EntityManager em = getEntityManager();
		try {
			return em.createQuery("SELECT MAX(n.maNV) FROM NhanVien n", String.class).getSingleResult();
		} finally {
			em.close();
		}
	}

}
