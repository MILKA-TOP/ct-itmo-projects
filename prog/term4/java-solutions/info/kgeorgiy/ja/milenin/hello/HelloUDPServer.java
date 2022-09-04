package info.kgeorgiy.ja.milenin.hello;


import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class is a server that sends respond-messages like:
 * {@code "Hello, [message_text]"};
 * <p>
 * Implementing {@link HelloServer}
 *
 * @author Milenin_Ivan
 */
public class HelloUDPServer implements HelloServer {

    private ExecutorService serverExecutor;
    private DatagramSocket socket;


    /**
     * Main method that starts the class;
     *
     * @param args Array of arguments containing
     *             {@code [port][threads count]}
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println(
                    "[Args input exception]: [port][threads count]");
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);
            new HelloUDPServer().start(port, threads);
        } catch (NumberFormatException e) {
            System.err.println("Please, input correct port/threads");
        }
    }


    // :NOTE: утечка ресурсов
    /**
     * Start server and send respond-messages like:
     * {@code "Hello, [message_text]"};
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(int port, int threads) {
        try {
            serverExecutor = Executors.newFixedThreadPool(threads);
            socket = new DatagramSocket(port);
            int socketSize = socket.getReceiveBufferSize();
            for (int i = 0; i < threads; i++) {
                serverExecutor.submit(() -> {
                    DatagramPacket resp = new DatagramPacket(new byte[socketSize], socketSize);
                    while (!socket.isClosed()) {
                        try {
                            socket.receive(resp);
                            String gotMessage = new String(resp.getData(),
                                    resp.getOffset(),
                                    resp.getLength(),
                                    StandardCharsets.UTF_8);
                            // :NOTE: Несмотря на то, что текущий способ получения ответа по запросу очень прост,
                            // сервер должен быть рассчитан на ситуацию,
                            // когда этот процесс может требовать много ресурсов и времени.
                            String message = "Hello, " + gotMessage;
                            socket.send(new DatagramPacket(message.getBytes(StandardCharsets.UTF_8),
                                    message.length(),
                                    resp.getSocketAddress()));

                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }

                });
            }


        } catch (SocketException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Close all threads and turn-off server;
     */
    @Override
    public void close() {
        // :NOTE: awaitTermination
        serverExecutor.shutdown();
        socket.close();
    }
}
