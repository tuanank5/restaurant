package network.common;

public interface CommandHandler {
    Response handle(Request request);
}
