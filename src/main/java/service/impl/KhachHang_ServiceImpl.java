package service.impl;

import java.util.List;

import dao.KhachHang_DAO;
import dao.impl.KhachHang_DAOlmpl;
import dto.HoaDon_DTO;
import dto.KhachHang_DTO;
import entity.KhachHang;
import service.KhachHang_Service;
import util.MapperUtil;

public class KhachHang_ServiceImpl implements KhachHang_Service {

	private KhachHang_DAO khachHang_DAO;

	public KhachHang_ServiceImpl() {
		khachHang_DAO = new KhachHang_DAOlmpl();
	}

	@Override
	public KhachHang_DTO timTheoSDT(String sdt) {
		if (sdt == null || sdt.trim().isEmpty()) {
			throw new IllegalArgumentException("sdt không được rỗng");
		}
		KhachHang khachHang = khachHang_DAO.timTheoSDT(sdt);
		if (khachHang == null) {
			return null;
		}
		return MapperUtil.map(khachHang, KhachHang_DTO.class);
	}

	@Override
	public KhachHang_DTO timTheoMa(String maKH) {
		if (maKH == null || maKH.trim().isEmpty()) {
			throw new IllegalArgumentException("maKH không được rỗng");
		}
		KhachHang khachHang = khachHang_DAO.timTheoMa(maKH);
		if (khachHang == null) {
			return null;
		}
		return MapperUtil.map(khachHang, KhachHang_DTO.class);
	}

	@Override
	public List<KhachHang_DTO> getAll() {
		List<KhachHang> khachHangs = khachHang_DAO.getAll();
		return khachHangs.stream().map(kh -> MapperUtil.map(kh, KhachHang_DTO.class)).toList();
	}

	@Override
	public KhachHang_DTO getKhachHangTheoSDT(String sdt) {
		if (sdt == null || sdt.trim().isEmpty()) {
			throw new IllegalArgumentException("sdt không được rỗng");
		}
		KhachHang khachHang = khachHang_DAO.getKhachHangTheoSDT(sdt);
		return MapperUtil.map(khachHang, KhachHang_DTO.class);
	}

	@Override
	public boolean capNhat(KhachHang_DTO dto) {
		KhachHang khachHang = MapperUtil.map(dto,KhachHang.class);
		return khachHang_DAO.capNhat(khachHang);
	}


}
