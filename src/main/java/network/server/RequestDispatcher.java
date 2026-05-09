package network.server;

import handler.DonDatBanHandler;
import handler.HoaDonHandler;
import handler.TaiKhoanHandler;
import network.common.CommandHandler;
import network.common.Request;
import network.common.Response;

import java.util.HashMap;
import java.util.Map;

public class RequestDispatcher {

    private Map<String, CommandHandler> handlerMap = new HashMap<>();

    public RequestDispatcher() {
        handlerMap.put("TAIKHOAN", new TaiKhoanHandler());
        handlerMap.put("HOADON", new HoaDonHandler());
        handlerMap.put("DONDATBAN", new DonDatBanHandler());
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