package handler;

import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;
import network.common.CommandHandler;
import network.common.Request;
import network.common.Response;
import service.NhanVien_Service;
import service.TaiKhoan_Service;
import service.impl.NhanVien_ServiceImpl;
import service.impl.TaiKhoan_ServiceImpl;

import java.util.List;

public class NhanVienHandler implements CommandHandler {

    private NhanVien_Service service = new NhanVien_ServiceImpl();

    @Override
    public Response handle(Request request) {

        switch (request.getCommandType()) {

            case NHANVIEN_GET_ALL -> {
                List<NhanVien_DTO> list = service.getAll();
                return new Response(true, list, "OK");
            }
        }

        return new Response(false, null, "Invalid command");
    }
}
