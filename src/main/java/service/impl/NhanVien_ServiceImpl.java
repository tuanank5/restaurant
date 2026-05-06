package service.impl;

import dao.NhanVien_DAO;
import dao.impl.NhanVien_DAOImpl;
import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;
import entity.NhanVien;
import entity.TaiKhoan;
import service.NhanVien_Service;
import util.MapperUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public List<NhanVien_DTO> getAll() {
		List<NhanVien> list = nhanVien_DAO.getDanhSach(NhanVien.class, new HashMap<>());

		if (list == null) {
			return new ArrayList<>();
		}

		return list.stream().map(nv -> {

			NhanVien_DTO dto = MapperUtil.map(nv, NhanVien_DTO.class);

			return dto;

		}).toList();
	}

}
