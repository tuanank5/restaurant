package dao.impl;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dao.DonDatBan_DAO;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class DonDatBan_DAOImpl extends Entity_DAOImpl<DonDatBan> implements DonDatBan_DAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public List<DonDatBan> timTheoKhachHang(KhachHang kh) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM DonDatBan d WHERE d.khachHang.maKH = :maKH", DonDatBan.class)
                    .setParameter("maKH", kh.getMaKH())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<DonDatBan> timTheoBan(Ban ban) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM DonDatBan d WHERE d.ban.maBan = :maBan ORDER BY d.ngayGioLapDon DESC", DonDatBan.class)
                    .setParameter("maBan", ban.getMaBan())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean them(DonDatBan donDatBan) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(donDatBan);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

	@Override
	public List<DonDatBan> getAllDonDatBan() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<DonDatBan> dsDon = null;

        try {
            // Truy vấn để lấy tất cả hóa đơn
        	dsDon = entityManager.createQuery("SELECT DDB FROM DonDatBan DDB", DonDatBan.class).getResultList();

        } finally {
            entityManager.close(); // Đóng EntityManager
        }

        return dsDon;
	}

	@Override
	public List<DonDatBan> getAllDonDatBanTheoThang(int thang, int nam) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<DonDatBan> dsDon = null;

        try {
            String jpql = "SELECT DDB FROM DonDatBan DDB WHERE FUNCTION('MONTH', DDB.ngayGioLapDon) = :thang AND FUNCTION('YEAR', DDB.ngayGioLapDon) = :nam";
            TypedQuery<DonDatBan> query = entityManager.createQuery(jpql, DonDatBan.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);

            dsDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return dsDon;
	}
	
	@Override
    public List<String> getKhachHangTheoThang(int thang, int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<String> dsKH = null;

        try {
            String jpql = "SELECT DISTINCT DDB.khachHang.maKH " +
                    "FROM DonDatBan DDB WHERE FUNCTION('MONTH', DDB.ngayGioLapDon) = :thang AND FUNCTION('YEAR', DDB.ngayGioLapDon) = :nam";

            TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);

            dsKH = query.getResultList();

        } finally {
            entityManager.close();
        }

        return dsKH;
    }
	
	@Override
    public List<DonDatBan> getAllDonDatBanTheoNam(int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<DonDatBan> dsDon = null;

        try {
            String jpql = "SELECT DDB FROM DonDatBan DDB WHERE FUNCTION('YEAR', DDB.ngayGioLapDon) = :nam";
            TypedQuery<DonDatBan> query = entityManager.createQuery(jpql, DonDatBan.class);
            query.setParameter("nam", nam);

            dsDon = query.getResultList();
        } finally {
            entityManager.close();
        }
        return dsDon;
    }
	
	@Override
    public List<String> getKhachHangTheoNam(int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<String> dsKH = null;

        try {
        	String jpql = "SELECT DISTINCT DDB.khachHang.maKH " +
                    "FROM DonDatBan DDB WHERE FUNCTION('YEAR', DDB.ngayGioLapDon) = :nam";

            TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
            query.setParameter("nam", nam);

            dsKH = query.getResultList();

        } finally {
            entityManager.close();
        }

        return dsKH;
    }
	
	@Override
    public List<DonDatBan> getAllDonDatBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<DonDatBan> dsDon = null;

        try {
            String jpql = "SELECT DDB FROM DonDatBan DDB WHERE DDB.ngayGioLapDon BETWEEN :dateStart AND :dateEnd";
            TypedQuery<DonDatBan> query = entityManager.createQuery(jpql, DonDatBan.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));

            dsDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return dsDon;
    }
	
	@Override
    public List<String> getKhachHangTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<String> dsKH = null;

        try {
        	String jpql = "SELECT DISTINCT DDB.khachHang.maKH " +
                    "FROM DonDatBan DDB WHERE DDB.ngayGioLapDon BETWEEN :dateStart AND :dateEnd";

            TypedQuery<String> query = entityManager.createQuery(jpql, String.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));

            dsKH = query.getResultList();
        } finally {
            entityManager.close();
        }
        return dsKH;
    }
}
