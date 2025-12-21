package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import entity.Ban;
import entity.ChiTietHoaDon;
import entity.DonDatBan;
import entity.HoaDon;
import entity.KhachHang;

public interface HoaDon_DAO extends Entity_DAO<HoaDon> {
	long tongSoHoaDon();
	boolean themHoaDon(HoaDon hoaDon);
    List<HoaDon> getAllHoaDons();

    List<HoaDon> getAllHoaDonTheoThang(int thang, int nam);

    List<HoaDon> getHoaDonTheoNam(int nam);

    Double getTongDoanhThuTheoThang(int thang, int nam);

    Double getTongDoanhThuTheoNam(int nam);

    List<HoaDon> getHoaDonTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

    Double getTongDoanhThuTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);

    //
    Double getTongDoanhThuTheoNgayVaNhanVien(Date ngayLap, String maNV);

    Long countHoaDonTheoNgayVaNhanVien(Date ngayLap, String maNV);
    //
    
    List<HoaDon> getAllHoaDonNVTheoThang(int thang, int nam, String maNV);

    List<HoaDon> getHoaDonNVTheoNam(int nam, String maNV);

    Double getTongDoanhThuNVTheoThang(int thang, int nam, String maNV);

    Double getTongDoanhThuNVTheoNam(int nam, String maNV);

    List<HoaDon> getHoaDonNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);

    Double getTongDoanhThuNVTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);
    String getMaxMaHoaDon();
    
    HoaDon getHoaDonTheoMaDatBan(String maDatBan);
    public KhachHang getKhachHangTheoMaDatBan(String maDatBan);
    HoaDon timHoaDonDangPhucVuTheoBan(Ban ban,LocalDate ngay);
    HoaDon timHoaDonTheoDonDatBan(DonDatBan donDatBan);
    List<ChiTietHoaDon> findByMaHoaDon(String maHoaDon);
    
}
