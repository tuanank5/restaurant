package network.server;

import java.util.HashMap;
import java.util.Map;
import handler.BanHandler;
import handler.ChiTietHoaDonHandler;
import handler.DonDatBanHandler;
import handler.HoaDonHandler;
import handler.KhachHangHandler;
import handler.KhuyenMaiHandler;
import handler.MonAnHandler;
import handler.TaiKhoanHandler;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;


public class RequestDispatcher {

    private Map<String, CommandHandler> handlerMap = new HashMap<>();

    public RequestDispatcher() {
        handlerMap.put("TAIKHOAN", new TaiKhoanHandler());
        handlerMap.put("KHUYENMAI", new KhuyenMaiHandler());
        handlerMap.put("MONAN", new MonAnHandler());
        handlerMap.put("BAN", new BanHandler());
        handlerMap.put("CTHD", new ChiTietHoaDonHandler());
        handlerMap.put("HOADON", new HoaDonHandler());
        handlerMap.put("DONDATBAN", new DonDatBanHandler());
        handlerMap.put("KHACHHANG", new KhachHangHandler());
    }

    public Response dispatch(Request request) {
        String command = request.getCommandType().name();

        String prefix = command.split("_")[0];

        CommandHandler handler = handlerMap.get(prefix);

        if (handler == null) {
            return Response.builder()
                    .success(false)
                    .message("Unknown command")
                    .build();
        }

        return handler.handle(request);
    }
}