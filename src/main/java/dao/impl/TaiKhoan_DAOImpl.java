package dao.impl;

import dao.TaiKhoan_DAO;
import dto.TaiKhoan_DTO;
import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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

			Long count = em.createQuery("SELECT COUNT(t) FROM TaiKhoan t WHERE t.tenDangNhap LIKE :prefix", Long.class)
					.setParameter("prefix", prefix).getSingleResult();

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
			return em.createQuery("SELECT MAX(t.maTaiKhoan) FROM TaiKhoan t", String.class).getSingleResult();
		} finally {
			em.close();
		}
	}

	public TaiKhoan timTheoMa(String maTaiKhoan) {
		if (maTaiKhoan == null || maTaiKhoan.isBlank()) {
			return null;
		}
		EntityManager em = getEntityManager();
		try {
			return em.find(TaiKhoan.class, maTaiKhoan.trim());
		} finally {
			em.close();
		}
	}

	public boolean capNhatBangDto(TaiKhoan_DTO dto) {
		if (dto == null || dto.getMaTaiKhoan() == null || dto.getMaTaiKhoan().isBlank()) {
			return false;
		}
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			TaiKhoan t = em.find(TaiKhoan.class, dto.getMaTaiKhoan().trim());
			if (t == null) {
				tx.rollback();
				return false;
			}
			if (dto.getMatKhau() != null) {
				t.setMatKhau(dto.getMatKhau());
			}
			if (dto.getNgaySuaDoi() != null) {
				t.setNgaySuaDoi(dto.getNgaySuaDoi());
			}
			if (dto.getNgayDangNhap() != null) {
				t.setNgayDangNhap(dto.getNgayDangNhap());
			}
			if (dto.getNgayDangXuat() != null) {
				t.setNgayDangXuat(dto.getNgayDangXuat());
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			return false;
		} finally {
			em.close();
		}
	}

}
