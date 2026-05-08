package handler;

import dto.Ban_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.Ban_Service;
import service.impl.Ban_ServiceImpl;

public class BanHandler implements CommandHandler {

    private final Ban_Service banService;

    public BanHandler() {
        banService = new Ban_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {

                case BAN_UPDATE: {

                    Ban_DTO ban = (Ban_DTO) request.getData();

                    boolean result = banService.sua(ban);

                    return Response.builder()
                            .success(result)
                            .message(result
                                    ? "Cập nhật bàn thành công"
                                    : "Cập nhật bàn thất bại")
                            .build();
                }

                case BAN_GET_ALL: {

                    return Response.builder()
                            .success(false)
                            .message("Service chưa hỗ trợ lấy danh sách bàn")
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
                    .message("Lỗi xử lý Bàn: " + e.getMessage())
                    .build();
        }
    }
}