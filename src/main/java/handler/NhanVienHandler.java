package handler;

import java.util.HashMap;
import java.util.List;

import dao.impl.NhanVien_DAOImpl;
import dao.impl.TaiKhoan_DAOImpl;
import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;
import entity.NhanVien;
import entity.TaiKhoan;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.MapperUtil;

public class NhanVienHandler implements CommandHandler {

	private final NhanVien_DAOImpl nvDao = new NhanVien_DAOImpl();
	private final TaiKhoan_DAOImpl tkDao = new TaiKhoan_DAOImpl();

	@Override
	public Response handle(Request request) {
		CommandType type = request.getCommandType();

		if (type == null) {
			return new Response(false, null, "CommandType is null");
		}

		return switch (type) {
		case NHANVIEN_GET_ALL -> {
			List<NhanVien> list = nvDao.getDanhSach(NhanVien.class, new HashMap<>());
			List<NhanVien_DTO> dtoList = list == null ? List.of()
					: list.stream().map(e -> MapperUtil.map(e, NhanVien_DTO.class)).toList();
			yield new Response(true, dtoList, "OK");
		}

		case NHANVIEN_UPDATE -> {
			if (!(request.getData() instanceof NhanVien_DTO dto)) {
				yield new Response(false, null, "Invalid data type for NHANVIEN_UPDATE");
			}

			NhanVien entity = MapperUtil.map(dto, NhanVien.class);
			boolean ok = nvDao.capNhat(entity);
			yield new Response(ok, null, ok ? "OK" : "FAILED");
		}

		case NHANVIEN_ADD_WITH_ACCOUNT -> {
			if (!(request.getData() instanceof NhanVien_DTO dto)) {
				yield new Response(false, null, "Invalid data type for NHANVIEN_ADD_WITH_ACCOUNT");
			}

			NhanVien nv = MapperUtil.map(dto, NhanVien.class);
			boolean okNv = nvDao.them(nv);
			if (!okNv) {
				yield new Response(false, null, "FAILED_CREATE_NHANVIEN");
			}

			String chucVu = nv.getChucVu();
			int soThuTu = tkDao.demSoTaiKhoanTheoChucVu(chucVu) + 1;

			String username;
			String password;
			if (chucVu != null && chucVu.equalsIgnoreCase("Quản lý")) {
				username = String.format("QL%04d", soThuTu);
				password = "ql_pass" + String.format("%02d", soThuTu);
			} else {
				username = String.format("NV%04d", soThuTu);
				password = "nv_pass" + String.format("%02d", soThuTu);
			}

			TaiKhoan tk = new TaiKhoan();
			tk.setMaTaiKhoan(util.AutoIDUitl.sinhMaTaiKhoan());
			tk.setTenTaiKhoan(username);
			tk.setMatKhau(password);
			tk.setNhanVien(nv);

			boolean okTk = tkDao.themTaiKhoan(tk);
			if (!okTk) {
				yield new Response(false, null, "FAILED_CREATE_TAIKHOAN");
			}

			TaiKhoan_DTO tkDto = MapperUtil.map(tk, TaiKhoan_DTO.class);
			tkDto.setMaNhanVien(nv.getMaNV());
			tkDto.setTenNhanVien(nv.getTenNV());
			yield new Response(true, tkDto, "OK");
		}

		case THONGKE_FINDBYID -> {

			if (!(request.getData() instanceof String maNV)) {
				yield new Response(
						false,
						null,
						"Invalid data type for THONGKE_FINDBYID");
			}

			NhanVien entity = nvDao.findById(maNV);
			NhanVien_DTO dto =
					MapperUtil.map(entity, NhanVien_DTO.class);

			yield new Response(
					entity != null,
					dto,
					entity != null ? "OK" : "NOT_FOUND");
		}

		default -> new Response(false, null, "Invalid command");
		};
	}
}

