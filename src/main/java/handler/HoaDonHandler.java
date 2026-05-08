package handler;

import java.util.List;

import dto.HoaDon_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.HoaDon_Service;
import service.impl.HoaDon_ServiceImpl;

public class HoaDonHandler implements CommandHandler {

    private final HoaDon_Service hoaDonService;

    public HoaDonHandler() {
        hoaDonService = new HoaDon_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {

                case HOADON_GET_ALL: {

                    List<HoaDon_DTO> ds = hoaDonService.getAllHoaDons();

                    return Response.builder()
                            .success(true)
                            .data(ds)
                            .message("Lấy danh sách hóa đơn thành công")
                            .build();
                }

                case HOADON_GET_BY_MADATBAN: {

                    String maDatBan = (String) request.getData();

                    HoaDon_DTO hd = hoaDonService.getHoaDonTheoMaDatBan(maDatBan);

                    return Response.builder()
                            .success(true)
                            .data(hd)
                            .build();
                }

                case HOADON_ADD: {

                    HoaDon_DTO hd = (HoaDon_DTO) request.getData();

                    boolean result = hoaDonService.themHoaDon(hd);

                    return Response.builder()
                            .success(result)
                            .message(result
                                    ? "Thêm hóa đơn thành công"
                                    : "Thêm hóa đơn thất bại")
                            .build();
                }

                case HOADON_UPDATE: {

                    return Response.builder()
                            .success(false)
                            .message("Service chưa hỗ trợ cập nhật hóa đơn")
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
                    .message("Lỗi xử lý HoaDon: " + e.getMessage())
                    .build();
        }
    }
}