package network.server;

import java.util.HashMap;
import java.util.Map;

import handler.*;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;

public class RequestDispatcher {

    private Map<String, CommandHandler> handlerMap = new HashMap<>();

    public RequestDispatcher() {
        handlerMap.put("TAIKHOAN", new TaiKhoanHandler());
        handlerMap.put("KHACHHANG", new KhachHangHandler());
        handlerMap.put("HANGKHACHHANG", new HangKhachHangHandler());
        handlerMap.put("NHANVIEN", new NhanVienHandler());
        handlerMap.put("MONAN", new MonAnHandler());
        handlerMap.put("DONDATBAN", new DonDatBanHandler());
        handlerMap.put("HOADON", new HoaDonHandler());
        handlerMap.put("CTHD", new ChiTietHoaDonHandler());
        handlerMap.put("BAN", new BanHandler());
        handlerMap.put("LOAIBAN", new LoaiBanHandler());
        handlerMap.put("KHUYENMAI", new KhuyenMaiHandler());
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

        try {
            return handler.handle(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            return Response.builder()
                    .success(false)
                    .message(msg != null && !msg.isBlank() ? msg : "Lỗi xử lý yêu cầu trên server")
                    .build();
        }
    }
}