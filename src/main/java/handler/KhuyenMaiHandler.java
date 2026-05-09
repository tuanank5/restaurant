package handler;

import dto.KhuyenMai_DTO;
import network.common.*;
import service.KhuyenMai_Service;
import service.impl.KhuyenMai_ServiceImpl;

import java.util.List;

public class KhuyenMaiHandler implements CommandHandler {

    private final KhuyenMai_Service service = new KhuyenMai_ServiceImpl();

    @Override
    public Response handle(Request request) {

        switch (request.getCommandType()) {

            case KHUYENMAI_GET_ALL -> {
                List<KhuyenMai_DTO> list = service.getAll();
                return new Response(true, list, "OK");
            }

            case KHUYENMAI_ADD -> {
                boolean rs = service.add((KhuyenMai_DTO) request.getData());
                return new Response(rs, null, rs ? "Added" : "Failed");
            }

            case KHUYENMAI_UPDATE -> {
                boolean rs = service.update((KhuyenMai_DTO) request.getData());
                return new Response(rs, null, rs ? "Updated" : "Failed");
            }

            case KHUYENMAI_DELETE -> {
                boolean rs = service.delete((String) request.getData());
                return new Response(rs, null, rs ? "Deleted" : "Failed");
            }
        }

        return new Response(false, null, "Invalid Command");
    }
}