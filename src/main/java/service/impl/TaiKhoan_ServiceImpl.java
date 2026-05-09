package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.TaiKhoan_DAO;
import dao.impl.TaiKhoan_DAOImpl;
import dto.TaiKhoan_DTO;
import entity.TaiKhoan;
import service.TaiKhoan_Service;
import util.MapperUtil;

public class TaiKhoan_ServiceImpl implements TaiKhoan_Service {

	private TaiKhoan_DAO taiKhoan_DAO;

	public TaiKhoan_ServiceImpl() {
		taiKhoan_DAO = new TaiKhoan_DAOImpl();
	}

	@Override
	public int demSoTaiKhoanTheoChucVu(String chucVu) {
		if (chucVu == null || chucVu.trim().isEmpty()) {
			throw new IllegalArgumentException("chucVu không được rỗng");
		}
		return taiKhoan_DAO.demSoTaiKhoanTheoChucVu(chucVu);
	}

	@Override
	public boolean themTaiKhoan(TaiKhoan_DTO taiKhoan_DTO) {
		if (taiKhoan_DTO == null) {
			throw new IllegalArgumentException("taiKhoan_DTO không được rỗng");
		}
		if (taiKhoan_DTO.getMaTaiKhoan() == null || taiKhoan_DTO.getMaTaiKhoan().trim().isEmpty()) {
			throw new IllegalArgumentException("taiKhoan_DTO.maTK không được rỗng");
		}
		return taiKhoan_DAO.themTaiKhoan(MapperUtil.map(taiKhoan_DTO, TaiKhoan.class));
	}

	@Override
	public String getMaxMaTK() {
		return taiKhoan_DAO.getMaxMaTK();
	}

	@Override
	public List<TaiKhoan_DTO> getAll() {

		List<TaiKhoan> list = taiKhoan_DAO.getDanhSach(TaiKhoan.class, new HashMap<>());

		if (list == null) {
			return new ArrayList<>();
		}

		return list.stream().map(tk -> {

			TaiKhoan_DTO dto = MapperUtil.map(tk, TaiKhoan_DTO.class);

			if (tk.getNhanVien() != null) {
				dto.setMaNhanVien(tk.getNhanVien().getMaNV());
				dto.setTenNhanVien(tk.getNhanVien().getTenNV());
				dto.setChucVu(tk.getNhanVien().getChucVu());
			}

			return dto;

		}).toList();
	}

}
