package info.kgeorgiy.ja.milenin.hello;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Util class for {@link HelloUDPNonblockingServer} and {@link HelloUDPNonblockingClient}
 *
 * @author Milenin Ivan
 */
public class UtilNonBlocking {

    /**
     * If {@link SelectionKey key} is writable, then completing {@link Consumer writeFunc}
     *
     * @param key          {@link SelectionKey} of server or client;
     * @param writeFunc    function, which will be completed, if {@link SelectionKey key} is writable;
     * @param funcArgument argument for {@link Consumer writeFunc};
     * @param <T>          type of funcArgument;
     */
    public <T> void writing(SelectionKey key, Consumer<T> writeFunc, T funcArgument) throws IOException {
        if (key.isWritable()) {
            writeFunc.accept(funcArgument);
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     * If {@link SelectionKey key} is readable, then check, that message in buffer equals for expected message
     *
     * @param key           {@link SelectionKey} of server or client;
     * @param buffer        {@link ByteBuffer} of {@link HelloUDPNonblockingClient};
     * @param servRes       result from client;
     * @param chn           {@link DatagramChannel} of request;
     * @param requests      count of maximum requests from {@link HelloUDPNonblockingClient};
     * @param outputMessage excepted message
     */
    public void readingClient(SelectionKey key,
                              ByteBuffer buffer,
                              ServerClientResult servRes,
                              DatagramChannel chn,
                              int requests,
                              String outputMessage) throws IOException {
        if (key.isReadable()) {
            chn.receive(buffer);
            if (new String(buffer.array(), StandardCharsets.UTF_8).contains(outputMessage)) {
                servRes.requestMaximum++;
                if (servRes.requestMaximum == requests) {
                    chn.close();
                }
            }
            if (chn.isOpen()) key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    /**
     * If {@link SelectionKey key} is readable, then write for {@link ServerClientResult} message
     * and save {@link java.net.SocketAddress};
     *
     * @param key     {@link SelectionKey} of server or client;
     * @param buffer  {@link ByteBuffer} of {@link HelloUDPNonblockingServer};
     * @param servRes result from server;
     * @param chn     {@link DatagramChannel} of request;
     */
    public void readingServer(SelectionKey key,
                              ByteBuffer buffer,
                              ServerClientResult servRes,
                              DatagramChannel chn) throws IOException {
        if (key.isReadable()) {
            servRes.socketAddressMessage = chn.receive(buffer);
            servRes.message = "Hello, " + new String(buffer.array(), StandardCharsets.UTF_8).trim();

            key.interestOps(SelectionKey.OP_WRITE);
        }

    }

    /**
     * Check `main` arguments of {@link HelloUDPNonblockingClient};
     *
     * @param args arguments of {@link HelloUDPNonblockingClient}
     * @return true, if args are correct. Otherwise, false;
     */
    public boolean mainClientCheckArgs(String[] args) {
        if (args == null || args.length < 5 || args[0] == null
                || args[1] == null || args[2] == null || args[3] == null || args[4] == null) {
            System.err.println(
                    "[Args input exception]: [host][port][message prefix][threads count][requests in thread count]");
            return false;
        }
        return true;

    }

    /**
     * Check `main` arguments of {@link HelloUDPNonblockingServer};
     *
     * @param args arguments of {@link HelloUDPNonblockingServer}
     * @return true, if args are correct. Otherwise, false;
     */
    public boolean mainServerCheckArgs(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println(
                    "[Args input exception]: [port][threads count]");
            return false;
        }
        return true;
    }


}
