package dao.impl;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import config.RestaurantApplication;

public class KhuyenMai_DAOImpl extends Entity_DAOImpl<KhuyenMai> implements KhuyenMai_DAO {

	private final EntityManagerFactory emf =
			RestaurantApplication.getInstance()
					.getEntityManagerFactory();

	@Override
	public String getMaxMaKM() {
		EntityManager em = emf.createEntityManager();
		try {
			String result = em.createQuery(
					"SELECT MAX(k.maKM) FROM KhuyenMai k",
					String.class
			).getSingleResult();

			System.out.println("MAX ID DB = " + result);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			em.close();
		}
	}

	@Override
	public KhuyenMai timTheoMa(String maKM) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery(
							"SELECT k FROM KhuyenMai k WHERE k.maKM = :ma",
							KhuyenMai.class)
					.setParameter("ma", maKM)
					.getSingleResult();

		} catch (NoResultException ex) {
			return null;

		} catch (Exception e) {
			e.printStackTrace(); // 🔥 FIX
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

			// 🔥 FIX: đảm bảo entity tồn tại trước khi merge
			KhuyenMai existing = em.find(KhuyenMai.class, km.getMaKM());

			if (existing == null) {
				System.out.println("❌ Không tìm thấy KM để update: " + km.getMaKM());
				return false;
			}

			em.merge(km);

			em.getTransaction().commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace(); // 🔥 FIX
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			return false;

		} finally {
			em.close();
		}
	}

	@Override
	public boolean xoa(String maKM) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			KhuyenMai km = em.find(KhuyenMai.class, maKM);

			if (km == null) {
				System.out.println("❌ Không tìm thấy KM để xóa: " + maKM);
				return false;
			}

			em.remove(km);

			em.getTransaction().commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace(); // 🔥 FIX
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			return false;

		} finally {
			em.close();
		}
	}
}