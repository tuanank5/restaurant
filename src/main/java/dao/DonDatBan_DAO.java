package dao;

import java.util.List;

import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;

public interface DonDatBan_DAO extends Entity_DAO<DonDatBan> {
	List<DonDatBan> timTheoKhachHang(KhachHang kh);
	List<DonDatBan> timTheoBan(Ban ban);
	
//	List<DonDatBan> getAllDonDatBan();
//
//    List<DonDatBan> getAllDonDatBanTheoThang(int thang, int nam);
//    
//    List<String> getKhachHangTheoThang(int thang, int nam);
}
