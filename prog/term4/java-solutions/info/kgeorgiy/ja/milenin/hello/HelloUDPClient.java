package info.kgeorgiy.ja.milenin.hello;


import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class is a web client that sends a message like:
 * {@code [request prefix][stream number]_[request number in the stream]};
 * <p>
 * Implementing {@link HelloClient}
 *
 * @author Milenin_Ivan
 */
public class HelloUDPClient implements HelloClient {


    /**
     * Main method that starts the class;
     *
     * @param args Array of arguments containing
     *             {@code [host][port][message prefix][threads count][requests in thread count]}
     */
    public static void main(String[] args) {
        if (args == null || args.length < 5 || args[0] == null
                || args[1] == null || args[2] == null || args[3] == null || args[4] == null) {
            System.err.println(
                    "[Args input exception]: [host][port][message prefix][threads count][requests in thread count]");
            return;
        }
        try {
            int port = Integer.parseInt(args[1]);
            int threads = Integer.parseInt(args[3]);
            int requests = Integer.parseInt(args[4]);
            new HelloUDPClient().run(args[0], port, args[2], threads, requests);
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
    @SuppressWarnings("all")
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        final InetSocketAddress socketAddress;
        ExecutorService clientExecutor = Executors.newFixedThreadPool(threads);
        try {
            socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            System.err.println("[Host exception]:" + e.getMessage());
            return;
        }

        for (int i = 0; i < threads; i++) {
            final int finalI = i;
            clientExecutor.submit(() -> clientRunnable(socketAddress, finalI, prefix, requests));
        }
        clientExecutor.shutdown();
        try {
            clientExecutor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            //System.err.println("[Await threads exception]:" + e.getMessage());
        }
    }

    private void clientRunnable(InetSocketAddress socketAddress, int finalI, String prefix, int requests) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(999);
            int socketSize = socket.getReceiveBufferSize();
            DatagramPacket req = new DatagramPacket(new byte[socketSize], socketSize, socketAddress);
            DatagramPacket resp = new DatagramPacket(new byte[socketSize], socketSize);
            for (int reqNumber = 0; reqNumber < requests; reqNumber++) {
                String message = prefix + finalI + "_" + reqNumber;
                req.setData(message.getBytes(StandardCharsets.UTF_8));
                while (!socket.isClosed()) {
                    try {
                        socket.send(req);
                        socket.receive(resp);
                        String gotMessage = new String(resp.getData(),
                                resp.getOffset(),
                                resp.getLength(),
                                StandardCharsets.UTF_8);
                        if (gotMessage.contains(message)) {
                            //System.out.println(message);
                            break;
                        }
                    } catch (final IOException e) {
                        //System.err.println("[Send-message error]:" + e.getMessage());
                    }
                }
            }

        } catch (final SocketException e) {
            //System.err.println("[Socket error]:" + e.getMessage());
        }
    }
}
