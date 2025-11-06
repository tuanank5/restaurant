package dao.impl;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DonDatBan_DAO;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.LoaiBan;
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
    public DonDatBan layDonDatTheoBan(String maBan) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<DonDatBan> query = em.createQuery(
                "SELECT d FROM DonDatBan d WHERE d.ban.maBan = :maBan ORDER BY d.ngayGioLapDon DESC", 
                DonDatBan.class
            );
            query.setParameter("maBan", maBan);
            return query.setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
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
	
	@Override
    public Map<String, Integer> countDonDatBanTheoNam(int year) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<String, Integer> demDonDatBan = new HashMap<>();
        try {
            String jpql = "SELECT MONTH(DDB.ngayGioLapDon), count(DDB.maDatBan) " +
                    "FROM DonDatBan DDB " +
                    "WHERE YEAR(DDB.ngayGioLapDon) = :year " +
                    "GROUP BY MONTH(DDB.ngayGioLapDon) " +
                    "ORDER BY MONTH(DDB.ngayGioLapDon)";
            ;
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("year", year);

            for (Object[] result : query.getResultList()) {
                int thang = (Integer) result[0];
                Long dem = (Long) result[1];
                demDonDatBan.put("Tháng " + thang, dem.intValue());
            }
        } finally {
            entityManager.close();
        }
        return demDonDatBan;
    }
	
	@Override
    public Map<LoaiBan, Integer> countLoaiBanTheoThang(int month, int year) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<LoaiBan, Integer> demLoaiBan = new HashMap<>();
        try {
            String jpql = "SELECT B.loaiBan, count(B.loaiBan) " +
                    "FROM DonDatBan DDB " +
                    "JOIN DDB.ban B " +
                    "WHERE MONTH(DDB.ngayGioLapDon) = :month " +
                    "AND YEAR(DDB.ngayGioLapDon) = :year " +
                    "GROUP BY B.loaiBan";
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("month", month);
            query.setParameter("year", year);

            for (Object[] result : query.getResultList()) {
            	LoaiBan loai = (LoaiBan) result[0];
                Long dem = (Long) result[1];
                demLoaiBan.put(loai, dem.intValue());
            }
        } finally {
            entityManager.close();
        }
        return demLoaiBan;
    }
	
	@Override
    public Map<LoaiBan, Integer> countLoaiBanTheoNam(int year) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<LoaiBan, Integer> demLoaiBan = new HashMap<>();
        try {
            String jpql = "SELECT B.loaiBan, count(B.loaiBan) " +
                    "FROM DonDatBan DDB " +
                    "JOIN DDB.ban B " +
                    "WHERE YEAR(DDB.ngayGioLapDon) = :year " +
                    "GROUP BY B.loaiBan";
            
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("year", year);

            for (Object[] result : query.getResultList()) {
            	LoaiBan loai = (LoaiBan) result[0];
                Long dem = (Long) result[1];
                demLoaiBan.put(loai, dem.intValue());
            }
        } finally {
            entityManager.close();
        }
        return demLoaiBan;
    }
	
	@Override
    public Map<LoaiBan, Integer> countLoaiBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<LoaiBan, Integer> demLoaiBan = new HashMap<>();
        try {
            String jpql = "SELECT B.loaiBan, count(B.loaiBan) " +
                    "FROM DonDatBan DDB " +
                    "JOIN DDB.ban B " +
                    "WHERE DDB.ngayGioLapDon BETWEEN :dateStart AND :dateEnd " +
                    "GROUP BY B.loaiBan";
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

            query.setParameter("dateStart", java.sql.Timestamp.valueOf(dateStart.atStartOfDay()));
            query.setParameter("dateEnd", java.sql.Timestamp.valueOf(dateEnd.atStartOfDay()));

            for (Object[] result : query.getResultList()) {
            	LoaiBan loai = (LoaiBan) result[0];
                Long dem = (Long) result[1];
                demLoaiBan.put(loai, dem.intValue());
            }
        } finally {
            entityManager.close();
        }
        return demLoaiBan;
    }
}
