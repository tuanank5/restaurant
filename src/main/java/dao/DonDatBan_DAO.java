package dao;

import java.util.List;

import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.Ve;

public interface DonDatBan_DAO extends Entity_DAO<DonDatBan> {
	List<DonDatBan> timTheoKhachHang(KhachHang kh);
	List<DonDatBan> timTheoBan(Ban ban);
	
	List<Ve> getAllVe();

    List<Ve> getAllVeTheoThang(int thang, int nam);
}
