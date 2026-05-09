package handler;

import java.util.HashMap;
import java.util.List;

import dao.impl.HangKhachHang_DAOImpl;
import dto.HangKhachHang_DTO;
import entity.HangKhachHang;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.MapperUtil;

public class HangKhachHangHandler implements CommandHandler {

	private final HangKhachHang_DAOImpl dao = new HangKhachHang_DAOImpl();

	@Override
	public Response handle(Request request) {
		CommandType type = request.getCommandType();

		if (type == null) {
			return new Response(false, null, "CommandType is null");
		}

		return switch (type) {
		case HANGKHACHHANG_GET_ALL -> {
			List<HangKhachHang> list = dao.getDanhSach(HangKhachHang.class, new HashMap<>());
			List<HangKhachHang_DTO> dtoList = list == null ? List.of()
					: list.stream().map(e -> MapperUtil.map(e, HangKhachHang_DTO.class)).toList();
			yield new Response(true, dtoList, "OK");
		}
		default -> new Response(false, null, "Invalid command");
		};
	}
}

