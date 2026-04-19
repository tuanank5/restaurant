package service.impl;

import dao.NhanVien_DAO;
import dao.impl.NhanVien_DAOImpl;
import service.NhanVien_Service;

public class NhanVien_ServiceImpl implements NhanVien_Service {

	private NhanVien_DAO nhanVien_DAO;

	public NhanVien_ServiceImpl() {
		nhanVien_DAO = new NhanVien_DAOImpl();
	}

	@Override
	public String getMaxMaNV() {
		return nhanVien_DAO.getMaxMaNV();
	}

}
