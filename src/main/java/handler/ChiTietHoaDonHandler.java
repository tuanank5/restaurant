package handler;

import java.util.List;

import dto.ChiTietHoaDon_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.ChiTietHoaDon_Service;
import service.impl.ChiTietHoaDon_ServiceImpl;

public class ChiTietHoaDonHandler implements CommandHandler {

    private final ChiTietHoaDon_Service chiTietHoaDonService;

    public ChiTietHoaDonHandler() {
        chiTietHoaDonService = new ChiTietHoaDon_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {
                case CTHD_GET_BY_MAHD: {
                    String maHD = (String) request.getData();
                    List<ChiTietHoaDon_DTO> ds = chiTietHoaDonService.getChiTietTheoMaHoaDon(maHD);
                    return Response.builder()
                            .success(true)
                            .data(ds)
                            .build();
                }

                case CTHD_ADD_BATCH: {
                    @SuppressWarnings("unchecked")
                    List<ChiTietHoaDon_DTO> ds = (List<ChiTietHoaDon_DTO>) request.getData();
                    for (ChiTietHoaDon_DTO ct : ds) {
                        chiTietHoaDonService.themChiTiet(ct);
                    }
                    return Response.builder()
                            .success(true)
                            .message("Thêm chi tiết hóa đơn thành công")
                            .build();
                }

                case CTHD_DELETE_BY_MAHD: {
                    String maHD = (String) request.getData();
                    chiTietHoaDonService.deleteByMaHoaDon(maHD);
                    return Response.builder()
                            .success(true)
                            .message("Xóa chi tiết hóa đơn thành công")
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
                    .message("Lỗi xử lý ChiTietHoaDon: " + e.getMessage())
                    .build();
        }
    }
}