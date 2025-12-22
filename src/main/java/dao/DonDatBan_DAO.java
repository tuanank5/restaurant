package dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.LoaiBan;

public interface DonDatBan_DAO extends Entity_DAO<DonDatBan> {
	DonDatBan layDonDatTheoBan(String maBan);
	List<DonDatBan> timTheoKhachHang(KhachHang kh);
	List<DonDatBan> timTheoBan(Ban ban);
	
	List<DonDatBan> getAllDonDatBan();

    List<DonDatBan> getAllDonDatBanTheoThang(int thang, int nam);
    
    List<String> getKhachHangTheoThang(int thang, int nam);
    
    List<DonDatBan> getAllDonDatBanTheoNam(int nam);
    
    List<String> getKhachHangTheoNam(int nam);
    
    List<DonDatBan> getAllDonDatBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);
    
    List<String> getKhachHangTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);
    
    public Map<String, Integer> countDonDatBanTheoNam(int year);
    
    Map<String, Integer> countDonDatBanTheoNamNVCuThe(int year, String maNV);
    
    public Map<LoaiBan, Integer> countLoaiBanTheoThang(int month, int year);
    
    public Map<LoaiBan, Integer> countLoaiBanTheoNam(int year);
    
    public Map<LoaiBan, Integer> countLoaiBanTheoNgayCuThe(LocalDate dateStart, LocalDate dateEnd);
    String getMaxMaDatBan();
    public KhachHang getKhachHangTheoMaDatBan(String maDatBan);
    
    public List<DonDatBan> getAllDDBTruoc();
    
    List<String> getKhachHangTheoThangNVCuThe(int thang, int nam, String maNV);
    List<String> getKhachHangTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);
    List<String> getKhachHangTheoNamNVCuThe(int nam, String maNV);
    
    List<DonDatBan> getAllDonDatBanNVTheoThang(int thang, int nam, String maNV);
    List<DonDatBan> getAllDonDatBanTheoNamNVCuThe(int nam, String maNV);
    List<DonDatBan> getAllDonDatBanTheoNgayNVCuThe(LocalDate dateStart, LocalDate dateEnd, String maNV);
}
