package handler;

import java.util.List;

import dto.TaiKhoan_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.TaiKhoan_Service;
import service.impl.TaiKhoan_ServiceImpl;
import dao.impl.TaiKhoan_DAOImpl;
public class TaiKhoanHandler implements CommandHandler {

	private TaiKhoan_Service service = new TaiKhoan_ServiceImpl();
	private TaiKhoan_DAOImpl dao = new TaiKhoan_DAOImpl();

	@Override
	public Response handle(Request request) {

		switch (request.getCommandType()) {

		case TAIKHOAN_GET_ALL -> {
			List<TaiKhoan_DTO> list = service.getAll();
			return new Response(true, list, "OK");
		}

		case TAIKHOAN_UPDATE_PASSWORD -> {
			if (!(request.getData() instanceof TaiKhoan_DTO dto)) {
				return new Response(false, null, "Invalid data type for TAIKHOAN_UPDATE_PASSWORD");
			}
			if (dto.getMaTaiKhoan() == null || dto.getMaTaiKhoan().isBlank()) {
				return new Response(false, null, "Ma tai khoan is required");
			}
			boolean ok = dao.capNhatBangDto(dto);
			return new Response(ok, null, ok ? "OK" : "FAILED");
		}
		}

		return new Response(false, null, "Invalid command");
	}
}
