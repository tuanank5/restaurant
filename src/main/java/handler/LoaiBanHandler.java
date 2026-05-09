package handler;

import java.util.List;

import dto.LoaiBan_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.LoaiBan_Service;
import service.impl.LoaiBan_ServiceImpl;

public class LoaiBanHandler implements CommandHandler {

    private final LoaiBan_Service loaiBanService;

    public LoaiBanHandler() {
        loaiBanService = new LoaiBan_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {

                case LOAIBAN_GET_ALL: {

                    List<LoaiBan_DTO> ds = loaiBanService.getAll();

                    return Response.builder()
                            .success(true)
                            .data(ds)
                            .message("Lấy danh sách loại bàn thành công")
                            .build();
                }

                default:
                    return Response.builder()
                            .success(false)
                            .message("Không hỗ trợ command: " + type)
                            .build();
            }

        } catch (Exception e) {

            e.printStackTrace();

            return Response.builder()
                    .success(false)
                    .message("Lỗi xử lý LoaiBan: " + e.getMessage())
                    .build();
        }
    }
}