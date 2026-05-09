package handler;

import dto.TaiKhoan_DTO;
import network.common.CommandHandler;
import network.common.Request;
import network.common.Response;
import service.TaiKhoan_Service;
import service.impl.TaiKhoan_ServiceImpl;

import java.util.List;

public class TaiKhoanHandler implements CommandHandler {

	private TaiKhoan_Service service = new TaiKhoan_ServiceImpl();

	@Override
	public Response handle(Request request) {

		switch (request.getCommandType()) {

			case TAIKHOAN_GET_ALL -> {
				List<TaiKhoan_DTO> list = service.getAll();
				return new Response(true, list, "OK");
			}
		}

		return new Response(false, null, "Invalid command");
	}
}
