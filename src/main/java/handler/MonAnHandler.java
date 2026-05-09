package handler;

import java.util.List;

import dto.MonAn_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.MonAn_Service;
import service.impl.MonAn_ServiceImpl;

public class MonAnHandler implements CommandHandler {

    private final MonAn_Service monAnService;

    public MonAnHandler() {
        monAnService = new MonAn_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        try {

            CommandType type = request.getCommandType();

            switch (type) {

                case MONAN_GET_ALL: {

                    List<MonAn_DTO> ds = monAnService.getDanhSachMonAn();

                    return Response.builder()
                            .success(true)
                            .data(ds)
                            .message("Lấy danh sách món ăn thành công")
                            .build();
                }

                case MONAN_FIND_BY_ID: {

                    String maMon = (String) request.getData();

                    MonAn_DTO mon = monAnService.timTheoMa(maMon);

                    return Response.builder()
                            .success(true)
                            .data(mon)
                            .build();
                }

                case MONAN_ADD: {

                    MonAn_DTO mon = (MonAn_DTO) request.getData();

                    boolean result = monAnService.them(mon);

                    return Response.builder()
                            .success(result)
                            .message(result ? "Thêm món ăn thành công"
                                    : "Thêm món ăn thất bại")
                            .build();
                }

                case MONAN_UPDATE: {

                    MonAn_DTO mon = (MonAn_DTO) request.getData();

                    boolean result = monAnService.capNhat(mon);

                    return Response.builder()
                            .success(result)
                            .message(result ? "Cập nhật món ăn thành công"
                                    : "Cập nhật món ăn thất bại")
                            .build();
                }

                case MONAN_DELETE: {

                    String maMon = (String) request.getData();

                    boolean result = monAnService.xoa(maMon);

                    return Response.builder()
                            .success(result)
                            .message(result ? "Xóa món ăn thành công"
                                    : "Xóa món ăn thất bại")
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
                    .message("Lỗi xử lý MonAn: " + e.getMessage())
                    .build();
        }
    }
}