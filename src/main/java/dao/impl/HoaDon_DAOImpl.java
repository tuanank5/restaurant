package dao.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.HoaDon_DAO;
import entity.HoaDon;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class HoaDon_DAOImpl extends Entity_DAOImpl<HoaDon> implements HoaDon_DAO {
	
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
	
	private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
	
	@Override
	public boolean themHoaDon(HoaDon hoaDon) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
			try {
	            tx.begin();
	            em.persist(hoaDon);
	            tx.commit();
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (tx.isActive()) tx.rollback();
	            return false;
	        } finally {
	            em.close();
	        }
	    }
	 
    @Override
    public long tongSoHoaDon() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        long count = 0;
        try {
            String jpql = "SELECT COUNT(HD) FROM HoaDon HD";
            count = entityManager.createQuery(jpql, Long.class).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return count;
    }

    @Override
    public List<HoaDon> getAllHoaDons() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHoaDon = null;
        try {
            // Truy vấn để lấy tất cả hóa đơn
            danhSachHoaDon = entityManager.createQuery("SELECT HD FROM HoaDon HD", HoaDon.class).getResultList();
        } finally {
            entityManager.close(); // Đóng EntityManager
        }
        return danhSachHoaDon;
    }

    @Override
    public List<HoaDon> getAllHoaDonTheoThang(int thang, int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHoaDon = null;

        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);

            danhSachHoaDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHoaDon;
    }

    @Override
    public List<HoaDon> getHoaDonTheoNam(int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHD = null;
        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
            query.setParameter("nam", nam);

            danhSachHD = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHD;
    }

    @Override
    public Double getTongDoanhThuTheoThang(int thang, int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);

            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }

        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
    }

    @Override
    public Double getTongDoanhThuTheoNam(int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
            query.setParameter("nam", nam);

            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }
        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
    }

    @Override
    public List<HoaDon> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHoaDon = null;
        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE HD.ngayLap BETWEEN :dateStart AND :dateEnd";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));

            danhSachHoaDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHoaDon;
    }

    @Override
    public Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE HD.ngayLap BETWEEN :dateStart AND :dateEnd";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));
            
            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }

        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
    }

    public Map<String, Double> getDoanhThuTheoNam(int nam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<String, Double> doanhThuTheoThang = new HashMap<>();

        try {
            // Truy vấn tính tổng doanh thu theo tháng trong năm
            String jpql = "SELECT MONTH(HD.ngayLap), SUM(HD.tongTien) " +
                    "FROM HoaDon HD " +
                    "WHERE YEAR(HD.ngayLap) = :nam " +
                    "GROUP BY MONTH(HD.ngayLap)" +
                    "ORDER BY MONTH(HD.ngayLap)";
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("nam", nam);

            // Lấy kết quả và chuyển thành Map (Tháng -> Doanh thu)
            for (Object[] result : query.getResultList()) {
                int thang = (Integer) result[0];
                double doanhThu = (Double) result[1];
                doanhThuTheoThang.put("Tháng " + thang, doanhThu);
            }

        } finally {
            entityManager.close();
        }

        return doanhThuTheoThang;
    }

    @Override
    public Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('DATE', HD.ngayLap) = :ngayLap AND HD.nhanVien.maNV = :maNV";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
            query.setParameter("ngayLap", ngayLap);
            query.setParameter("maNV", maNV);

            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }

        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
    }

    @Override
    public Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Long soLuongHoaDon = 0L;

        try {
            String jpql = "SELECT COUNT(HD) FROM HoaDon HD WHERE FUNCTION('DATE', HD.ngayLap) = :ngayLap AND HD.nhanVien.maNV = :maNV";
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
            query.setParameter("ngayLap", ngayLap);
            query.setParameter("maNV", maNV);

            soLuongHoaDon = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            soLuongHoaDon = 0L;
        } finally {
            entityManager.close();
        }

        return (soLuongHoaDon != null) ? soLuongHoaDon : 0L;
    }

	@Override
	public List<HoaDon> getAllHoaDonNVTheoThang(int thang, int nam, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHoaDon = null;

        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam AND HD.nhanVien.maNV = :maNV";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);
            query.setParameter("maNV", maNV);

            danhSachHoaDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHoaDon;
	}

	@Override
	public List<HoaDon> getHoaDonNVTheoNam(int nam, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHD = null;
        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam AND HD.nhanVien.maNV = :maNV";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
            query.setParameter("nam", nam);
            query.setParameter("maNV", maNV);

            danhSachHD = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHD;
	}

	@Override
	public Double getTongDoanhThuNVTheoThang(int thang, int nam, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam AND HD.nhanVien.maNV = :maNV";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
            query.setParameter("thang", thang);
            query.setParameter("nam", nam);
            query.setParameter("maNV", maNV);

            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }

        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
	}

	@Override
	public Double getTongDoanhThuNVTheoNam(int nam, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam AND HD.nhanVien.maNV = :maNV";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
            query.setParameter("nam", nam);
            query.setParameter("maNV", maNV);

            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }
        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
	}

	@Override
	public List<HoaDon> getHoaDonNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<HoaDon> danhSachHoaDon = null;
        try {
            String jpql = "SELECT HD FROM HoaDon HD WHERE HD.ngayLap BETWEEN :dateStart AND :dateEnd AND HD.nhanVien.maNV = :maNV";
            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));
            query.setParameter("maNV", maNV);

            danhSachHoaDon = query.getResultList();

        } finally {
            entityManager.close();
        }

        return danhSachHoaDon;
	}

	@Override
	public Double getTongDoanhThuNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
        Double tongDoanhThu = 0.0;

        try {
            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE HD.ngayLap BETWEEN :dateStart AND :dateEnd AND HD.nhanVien.maNV = :maNV";
            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);

            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));
            query.setParameter("maNV", maNV);
            
            tongDoanhThu = query.getSingleResult();
        } catch (NoResultException e) {
            // Nếu không có hóa đơn, trả về 0
            tongDoanhThu = 0.0;
        } finally {
            entityManager.close();
        }

        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
	}
	
	public Map<String, Double> getDoanhThuNVTheoNam(int nam, String maNV) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map<String, Double> doanhThuTheoThang = new HashMap<>();

        try {
            // Truy vấn tính tổng doanh thu theo tháng trong năm
            String jpql = "SELECT MONTH(HD.ngayLap), SUM(HD.tongTien) " +
                    "FROM HoaDon HD " +
                    "WHERE YEAR(HD.ngayLap) = :nam AND HD.nhanVien.maNV = :maNV " +
                    "GROUP BY MONTH(HD.ngayLap)" +
                    "ORDER BY MONTH(HD.ngayLap)";
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("nam", nam);
            query.setParameter("maNV", maNV);

            // Lấy kết quả và chuyển thành Map (Tháng -> Doanh thu)
            for (Object[] result : query.getResultList()) {
                int thang = (Integer) result[0];
                double doanhThu = (Double) result[1];
                doanhThuTheoThang.put("Tháng " + thang, doanhThu);
            }

        } finally {
            entityManager.close();
        }

        return doanhThuTheoThang;
    }
	
	@Override
	public String getMaxMaHoaDon() {
		EntityManager em = entityManagerFactory.createEntityManager();
	    try {
	        String jpql = "SELECT hd.maHD FROM HoaDon hd "
	                     + "ORDER BY CAST(SUBSTRING(hd.maHD, 3) AS int) DESC";

	        return em.createQuery(jpql, String.class)
	                 .setMaxResults(1)
	                 .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        em.close();
	    }
	}
	
	@Override
	public HoaDon getHoaDonTheoMaBan(String maBan) {
	    EntityManager em = emf.createEntityManager();
	    try {
	    	return em.createQuery(
	    	        "SELECT h FROM HoaDon h WHERE h.donDatBan.ban.maBan = :mb",
	    	        HoaDon.class)
	    	        .setParameter("mb", maBan)
	    	        .getResultStream()
	    	        .findFirst()
	    	        .orElse(null);

	    } finally {
	        em.close();
	    }
	}
	
	@Override
	public KhachHang getKhachHangTheoMaDatBan(String maDatBan) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.createQuery(
	            "SELECT hd.khachHang FROM HoaDon hd WHERE hd.donDatBan.maDatBan = :ma",
	            KhachHang.class
	        )
	        .setParameter("ma", maDatBan)
	        .getSingleResult();
	    } catch (Exception e) {
	        return null;
	    } finally {
	        em.close();
	    }
	}


}
