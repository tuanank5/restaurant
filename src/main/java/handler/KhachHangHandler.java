package handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.impl.HangKhachHang_DAOImpl;
import dao.impl.KhachHang_DAOlmpl;
import dto.KhachHang_DTO;
import entity.HangKhachHang;
import entity.KhachHang;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.MapperUtil;

public class KhachHangHandler implements CommandHandler {

	private final KhachHang_DAOlmpl khDao = new KhachHang_DAOlmpl();
	private final HangKhachHang_DAOImpl hangDao = new HangKhachHang_DAOImpl();

	@Override
	public Response handle(Request request) {
		CommandType type = request.getCommandType();

		if (type == null) {
			return new Response(false, null, "CommandType is null");
		}

		return switch (type) {
		case KHACHHANG_GET_ALL -> {
			List<KhachHang> entities = khDao.getDanhSach(KhachHang.class, new HashMap<>());
			List<KhachHang_DTO> list = entities == null ? List.of() : entities.stream().map(this::mapToDto).toList();
			yield new Response(true, list, "OK");
		}

		case KHACHHANG_ADD -> {
			if (!(request.getData() instanceof KhachHang_DTO dto)) {
				yield new Response(false, null, "Invalid data type for KHACHHANG_ADD");
			}

			KhachHang entity = mapToEntity(dto);
			boolean ok = khDao.them(entity);
			yield new Response(ok, null, ok ? "OK" : "FAILED");
		}

		case KHACHHANG_UPDATE -> {
			if (!(request.getData() instanceof KhachHang_DTO dto)) {
				yield new Response(false, null, "Invalid data type for KHACHHANG_UPDATE");
			}

			KhachHang entity = mapToEntity(dto);
			boolean ok = khDao.capNhat(entity);
			yield new Response(ok, null, ok ? "OK" : "FAILED");
		}

		default -> new Response(false, null, "Invalid command");
		};
	}

	private KhachHang mapToEntity(KhachHang_DTO dto) {
		KhachHang entity = MapperUtil.map(dto, KhachHang.class);

		String maHang = dto.getMaHangKhachHang();
		if (maHang != null && !maHang.isBlank()) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("maHang", maHang);
			List<HangKhachHang> hangs = hangDao.getDanhSach(HangKhachHang.class, filter);
			HangKhachHang hang = (hangs == null || hangs.isEmpty()) ? null : hangs.get(0);
			entity.setHangKhachHang(hang);
		} else {
			entity.setHangKhachHang(null);
		}

		return entity;
	}

	private KhachHang_DTO mapToDto(KhachHang entity) {
		KhachHang_DTO dto = MapperUtil.map(entity, KhachHang_DTO.class);
		if (entity != null && entity.getHangKhachHang() != null) {
			dto.setMaHangKhachHang(entity.getHangKhachHang().getMaHang());
		}
		return dto;
	}
}

