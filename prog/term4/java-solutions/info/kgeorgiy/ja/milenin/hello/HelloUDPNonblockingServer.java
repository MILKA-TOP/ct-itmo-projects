package info.kgeorgiy.ja.milenin.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HelloUDPNonblockingServer implements HelloServer {

    private final UtilNonBlocking utils = new UtilNonBlocking();
    private ExecutorService serverExecutor;
    private DatagramChannel mainChannel;
    private Selector selector;

    /**
     * Main method that starts the class;
     *
     * @param args Array of arguments containing
     *             {@code [port][threads count]}
     */
    public static void main(String[] args) {
        if (!new UtilNonBlocking().mainServerCheckArgs(args)) {
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);
            new HelloUDPNonblockingServer().start(port, threads);
        } catch (NumberFormatException e) {
            System.err.println("Please, input correct port/threads");
        }
    }

    /**
     * Start server and send respond-messages like:
     * {@code "Hello, [message_text]"};
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(int port, int threads) {
        if (selector != null) {
            return;
        }
        try {
            selector = Selector.open();
            mainChannel = DatagramChannel.open();
            mainChannel.configureBlocking(false);
            mainChannel.bind(new InetSocketAddress(port));
            mainChannel.register(selector, SelectionKey.OP_READ, new ServerClientResult());
        } catch (IOException ignored) {
        }
        serverExecutor = Executors.newSingleThreadExecutor();
        serverExecutor.submit(() -> {
            while (selector.isOpen() && mainChannel.isOpen()) {
                try {
                    selector.select();
                    for (final Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                        final SelectionKey key = i.next();
                        try {
                            final DatagramChannel chn = (DatagramChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(2048);
                            ServerClientResult servRes = (ServerClientResult) key.attachment();
                            String outputMessage = servRes.message;
                            Consumer<String> writingFunc = messageFunc -> {
                                try {
                                    chn.send(ByteBuffer.wrap(messageFunc.getBytes(StandardCharsets.UTF_8)), servRes.socketAddressMessage);
                                } catch (IOException ignored) {
                                }
                            };

                            utils.readingServer(key, buffer, servRes, chn);
                            utils.writing(key, writingFunc, outputMessage);
                        } finally {
                            i.remove();
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        });

    }

    /**
     * Close all threads and turn-off server;
     */
    @Override
    public void close() {
        serverExecutor.shutdownNow();
        // :NOTE: awaitTermination
        try {
            mainChannel.close();
            selector.close();
        } catch (IOException ignored) {
        }
    }


}
