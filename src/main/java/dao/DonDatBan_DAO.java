package dao;

import java.util.List;

import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;

public interface DonDatBan_DAO extends Entity_DAO<DonDatBan> {
	List<DonDatBan> timTheoKhachHang(KhachHang kh);
	List<DonDatBan> timTheoBan(Ban ban);
}
