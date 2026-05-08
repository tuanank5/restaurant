package service.impl;

import dao.NhanVien_DAO;
import dao.impl.NhanVien_DAOImpl;
import db.JPAUtils;
import dto.NhanVien_DTO;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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

	@Override
	public NhanVien_DTO findById(String maNV) {
		EntityManager em = JPAUtils.getEntityManager();
		try {
			NhanVien nv = em.find(NhanVien.class, maNV);
			if (nv == null) {
				return null;
			}
			return NhanVien_DTO.builder()
					.maNV(nv.getMaNV())
					.tenNV(nv.getTenNV())
					.email(nv.getEmail())
					.namSinh(nv.getNamSinh())
					.diaChi(nv.getDiaChi())
					.gioiTinh(nv.isGioiTinh())
					.ngayVaoLam(nv.getNgayVaoLam())
					.trangThai(nv.isTrangThai())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

}
