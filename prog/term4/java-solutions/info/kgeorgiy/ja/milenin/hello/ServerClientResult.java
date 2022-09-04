package info.kgeorgiy.ja.milenin.hello;

import java.net.SocketAddress;

public class ServerClientResult {

    String message;
    int requestMaximum = 0;
    SocketAddress socketAddressMessage;

    public ServerClientResult(String prefix, int number) {
        message = prefix + number + "_";
    }

    public ServerClientResult() {
    }

    public String showMessageClient() {
        return message + requestMaximum;
    }

    public String showMessageServer() {
        return message;
    }
}
