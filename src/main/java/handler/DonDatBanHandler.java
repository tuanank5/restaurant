package handler;

import java.util.List;

import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.DonDatBan_Service;
import service.impl.DonDatBan_ServiceImpl;

public class DonDatBanHandler implements CommandHandler {

    private final DonDatBan_Service donDatBanService;

    public DonDatBanHandler() {
        donDatBanService = new DonDatBan_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {

                case DONDATBAN_GET_ALL: {

                    List<DonDatBan_DTO> ds = donDatBanService.getAllDonDatBan();

                    return Response.builder()
                            .success(true)
                            .data(ds)
                            .message("Lấy danh sách đơn đặt bàn thành công")
                            .build();
                }

                case DONDATBAN_GET_BY_BAN: {
                    Ban_DTO banDTO = (Ban_DTO) request.getData();

                    DonDatBan_DTO ddb = donDatBanService.layDonDatTheoBan(banDTO.getMaBan());

                    return Response.builder()
                            .success(true)
                            .data(ddb)
                            .build();
                }

                case DONDATBAN_ADD: {
                    DonDatBan_DTO dto = (DonDatBan_DTO) request.getData();
                    boolean result = donDatBanService.them(dto);

                    return Response.builder()
                            .success(result)
                            .message(result
                                    ? "Thêm đơn đặt bàn thành công"
                                    : "Thêm đơn đặt bàn thất bại")
                            .build();
                }

                case DONDATBAN_UPDATE: {

                    DonDatBan_DTO ddb = (DonDatBan_DTO) request.getData();

                    // Tạm thời chưa có method update trong service
                    return Response.builder()
                            .success(false)
                            .message("Service chưa hỗ trợ cập nhật đơn đặt bàn")
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
                    .message("Lỗi xử lý DonDatBan: " + e.getMessage())
                    .build();
        }
    }
}