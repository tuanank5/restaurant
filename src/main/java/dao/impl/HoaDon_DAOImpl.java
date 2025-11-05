package dao.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.HoaDon_DAO;
import entity.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class HoaDon_DAOImpl extends Entity_DAOImpl<HoaDon> implements HoaDon_DAO {

//    @Override
//    public long tongSoHoaDon() {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        long count = 0;
//        try {
//            String jpql = "SELECT COUNT(HD) FROM HoaDon HD";
//            count = entityManager.createQuery(jpql, Long.class).getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            entityManager.close();
//        }
//
//        return count;
//    }
//
//    @Override
//    public List<HoaDon> getAllHoaDons() {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        List<HoaDon> danhSachHoaDon = null;
//
//        try {
//            // Truy vấn để lấy tất cả hóa đơn
//            danhSachHoaDon = entityManager.createQuery("SELECT HD FROM HoaDon HD", HoaDon.class).getResultList();
//
//        } finally {
//            entityManager.close(); // Đóng EntityManager
//        }
//
//        return danhSachHoaDon;
//    }
//
//    @Override
//    public List<HoaDon> getAllHoaDonTheoThang(int thang, int nam) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        List<HoaDon> danhSachHoaDon = null;
//
//        try {
//            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam";
//            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
//            query.setParameter("thang", thang);
//            query.setParameter("nam", nam);
//
//            danhSachHoaDon = query.getResultList();
//
//        } finally {
//            entityManager.close();
//        }
//
//        return danhSachHoaDon;
//    }
//
//    @Override
//    public List<HoaDon> getHoaDonTheoNam(int nam) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        List<HoaDon> danhSachHD = null;
//        try {
//            String jpql = "SELECT HD FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam";
//            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
//            query.setParameter("nam", nam);
//
//            danhSachHD = query.getResultList();
//
//        } finally {
//            entityManager.close();
//        }
//
//        return danhSachHD;
//    }
//
//    @Override
//    public Double getTongDoanhThuTheoThang(int thang, int nam) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Double tongDoanhThu = 0.0;
//
//        try {
//            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('MONTH', HD.ngayLap) = :thang AND FUNCTION('YEAR', HD.ngayLap) = :nam";
//            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
//            query.setParameter("thang", thang);
//            query.setParameter("nam", nam);
//
//            tongDoanhThu = query.getSingleResult();
//        } catch (NoResultException e) {
//            // Nếu không có hóa đơn, trả về 0
//            tongDoanhThu = 0.0;
//        } finally {
//            entityManager.close();
//        }
//
//        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
//    }
//
//    @Override
//    public Double getTongDoanhThuTheoNam(int nam) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Double tongDoanhThu = 0.0;
//
//        try {
//            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('YEAR', HD.ngayLap) = :nam";
//            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
//            query.setParameter("nam", nam);
//
//            tongDoanhThu = query.getSingleResult();
//        } catch (NoResultException e) {
//            // Nếu không có hóa đơn, trả về 0
//            tongDoanhThu = 0.0;
//        } finally {
//            entityManager.close();
//        }
//        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
//    }
//
//    @Override
//    public List<HoaDon> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        List<HoaDon> danhSachHoaDon = null;
//        try {
//            String jpql = "SELECT HD FROM HoaDon HD WHERE HD.ngayLap BETWEEN :startDate AND :endDate";
//            TypedQuery<HoaDon> query = entityManager.createQuery(jpql, HoaDon.class);
//
//            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
//            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));
//
//            danhSachHoaDon = query.getResultList();
//
//        } finally {
//            entityManager.close();
//        }
//
//        return danhSachHoaDon;
//    }
//
//    @Override
//    public Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Double tongDoanhThu = 0.0;
//
//        try {
//            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE HD.ngayLap BETWEEN :startDate AND :endDate";
//            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
//
//            query.setParameter("dateStart", java.sql.Date.valueOf(dateStart));
//            query.setParameter("dateEnd", java.sql.Date.valueOf(dateEnd));
//            
//            tongDoanhThu = query.getSingleResult();
//        } catch (NoResultException e) {
//            // Nếu không có hóa đơn, trả về 0
//            tongDoanhThu = 0.0;
//        } finally {
//            entityManager.close();
//        }
//
//        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
//    }
//
//    public Map<String, Double> getDoanhThuTheoNam(int nam) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Map<String, Double> doanhThuTheoThang = new HashMap<>();
//
//        try {
//            // Truy vấn tính tổng doanh thu theo tháng trong năm
//            String jpql = "SELECT MONTH(HD.ngayLap), SUM(HD.tongTien) " +
//                    "FROM HoaDon HD " +
//                    "WHERE YEAR(HD.ngayLap) = :nam " +
//                    "GROUP BY MONTH(HD.ngayLap)" +
//                    "ORDER BY MONTH(HD.ngayLap)";
//            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
//            query.setParameter("nam", nam);
//
//            // Lấy kết quả và chuyển thành Map (Tháng -> Doanh thu)
//            for (Object[] result : query.getResultList()) {
//                int thang = (Integer) result[0];
//                double doanhThu = (Double) result[1];
//                doanhThuTheoThang.put("Tháng " + thang, doanhThu);
//            }
//
//        } finally {
//            entityManager.close();
//        }
//
//        return doanhThuTheoThang;
//    }
//
//    @Override
//    public Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Double tongDoanhThu = 0.0;
//
//        try {
//            String jpql = "SELECT SUM(HD.tongTien) FROM HoaDon HD WHERE FUNCTION('DATE', HD.ngayLap) = :ngayLap AND HD.nhanVien.maNV = :maNV";
//            TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
//            query.setParameter("ngayLap", ngayLap);
//            query.setParameter("maNV", maNV);
//
//            tongDoanhThu = query.getSingleResult();
//        } catch (NoResultException e) {
//            // Nếu không có hóa đơn, trả về 0
//            tongDoanhThu = 0.0;
//        } finally {
//            entityManager.close();
//        }
//
//        return (tongDoanhThu != null) ? tongDoanhThu : 0.0;
//    }
//
//    @Override
//    public Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Long soLuongHoaDon = 0L;
//
//        try {
//            String jpql = "SELECT COUNT(HD) FROM HoaDon HD WHERE FUNCTION('DATE', HD.ngayLap) = :ngayLap AND HD.nhanVien.maNV = :maNV";
//            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
//            query.setParameter("ngayLap", ngayLap);
//            query.setParameter("maNV", maNV);
//
//            soLuongHoaDon = query.getSingleResult();
//        } catch (NoResultException e) {
//            // Nếu không có hóa đơn, trả về 0
//            soLuongHoaDon = 0L;
//        } finally {
//            entityManager.close();
//        }
//
//        return (soLuongHoaDon != null) ? soLuongHoaDon : 0L;
//    }
}
