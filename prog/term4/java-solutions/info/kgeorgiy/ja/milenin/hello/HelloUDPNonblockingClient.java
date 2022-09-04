package info.kgeorgiy.ja.milenin.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;


/**
 * This class is a web client that sends a message like:
 * {@code [request prefix][stream number]_[request number in the stream]};
 * <p>
 * Implementing {@link HelloClient}
 *
 * @author Milenin_Ivan
 */
public class HelloUDPNonblockingClient implements HelloClient {

    private final UtilNonBlocking utils = new UtilNonBlocking();
    private Selector selector;

    /**
     * Main method that starts the class;
     *
     * @param args Array of arguments containing
     *             {@code [host][port][message prefix][threads count][requests in thread count]}
     */
    public static void main(String[] args) {
        if (!new UtilNonBlocking().mainClientCheckArgs(args)) {
            return;
        }
        try {
            int port = Integer.parseInt(args[1]);
            int threads = Integer.parseInt(args[3]);
            int requests = Integer.parseInt(args[4]);
            new HelloUDPNonblockingClient().run(args[0], port, args[2], threads, requests);
        } catch (NumberFormatException e) {
            System.err.println("Please, input correct port/threads/requests");
        }
    }

    /**
     * This method send messages with view:
     * {@code [request prefix][stream number]_[request number in the stream]};
     *
     * @param host     {@link String } name or ip address of the computer on which the server is running;
     * @param port     {@link Integer }  port number to send requests to;
     * @param prefix   {@link String }  request prefix (string);
     * @param threads  {@link Integer }  number of parallel request streams;
     * @param requests {@link Integer }  number of requests in each thread.
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try {
            selector = Selector.open();
            IntStream.range(0, threads).forEach(i -> {
                try {
                    DatagramChannel nowChannel = DatagramChannel.open();
                    nowChannel.configureBlocking(false);
                    nowChannel.connect(new InetSocketAddress(host, port));
                    nowChannel.register(selector, SelectionKey.OP_WRITE, new ServerClientResult(prefix, i));
                } catch (IOException ignored) {
                }
            });
            while (!selector.keys().isEmpty()) {
                if (selector.select(250) == 0) {
                    for (SelectionKey now_key : selector.keys()) {
                        now_key.interestOps(SelectionKey.OP_WRITE);
                    }
                }
                for (final Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                    final SelectionKey key = i.next();
                    try {
                        final DatagramChannel chn = (DatagramChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(2048);
                        ServerClientResult servRes = (ServerClientResult) key.attachment();
                        String outputMessage = servRes.showMessageClient();
                        Consumer<String> writingFunc = (messageFunc) -> {
                            try {
                                chn.write(ByteBuffer.wrap(messageFunc.getBytes(StandardCharsets.UTF_8)));
                            } catch (IOException ignored) {
                            }
                        };
                        utils.writing(key, writingFunc, outputMessage);
                        utils.readingClient(key, buffer, servRes, chn, requests, outputMessage);
                    } finally {
                        i.remove();
                    }
                }

            }
        } catch (IOException ignored) {
        }

    }

}
