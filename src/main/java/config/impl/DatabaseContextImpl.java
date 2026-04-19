package config.impl;

import config.DatabaseContext;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.Coc_DAO;
import dao.DonDatBan_DAO;
import dao.DonLapDoiHuyBan_DAO;
import dao.Entity_DAO;
import dao.HangKhachHang_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.LoaiBan_DAO;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import dao.impl.Ban_DAOImpl;
import dao.impl.ChiTietHoaDon_DAOImpl;
import dao.impl.Coc_DAOImpl;
import dao.impl.DonDatBan_DAOImpl;
import dao.impl.DonLapDoiHuyBan_DAOImpl;
import dao.impl.HangKhachHang_DAOImpl;
import dao.impl.HoaDon_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dao.impl.KhuyenMai_DAOImpl;
import dao.impl.LoaiBan_DAOImpl;
import dao.impl.MonAn_DAOImpl;
import dao.impl.NhanVien_DAOImpl;
import dao.impl.TaiKhoan_DAOImpl;

public class DatabaseContextImpl implements DatabaseContext {
	@SuppressWarnings("unchecked")
	@Override
	public <R extends Entity_DAO<?>> R newEntity_DAO(Class<R> entityType) {
		if (entityType == Ban_DAO.class) {
			return (R) new Ban_DAOImpl();
		}
		if (entityType == ChiTietHoaDon_DAO.class) {
			return (R) new ChiTietHoaDon_DAOImpl();
		}
		if (entityType == Coc_DAO.class) {
			return (R) new Coc_DAOImpl();
		}
		if (entityType == DonDatBan_DAO.class) {
			return (R) new DonDatBan_DAOImpl();
		}
		if (entityType == DonLapDoiHuyBan_DAO.class) {
			return (R) new DonLapDoiHuyBan_DAOImpl();
		}
		if (entityType == HangKhachHang_DAO.class) {
			return (R) new HangKhachHang_DAOImpl();
		}
		if (entityType == HoaDon_DAO.class) {
			return (R) new HoaDon_DAOImpl();
		}
		if (entityType == KhachHang_DAO.class) {
			return (R) new KhachHang_DAOlmpl();
		}
		if (entityType == KhuyenMai_DAO.class) {
			return (R) new KhuyenMai_DAOImpl();
		}
		if (entityType == LoaiBan_DAO.class) {
			return (R) new LoaiBan_DAOImpl();
		}
		if (entityType == MonAn_DAO.class) {
			return (R) new MonAn_DAOImpl();
		}
		if (entityType == NhanVien_DAO.class) {
			return (R) new NhanVien_DAOImpl();
		}
		if (entityType == TaiKhoan_DAO.class) {
			return (R) new TaiKhoan_DAOImpl();
		}
		throw new IllegalArgumentException("There is no Entity Dao for " + entityType.getName());

	}

}
