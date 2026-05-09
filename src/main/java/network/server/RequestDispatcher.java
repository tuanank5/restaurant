package network.server;

import java.util.HashMap;
import java.util.Map;
import handler.KhuyenMaiHandler;
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