package ua.fti.udp;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServer {
    private static final int PORT = 6666;
    private static final int BUFFER_SIZE = 4096;
    private static final int THREAD_POOL_SIZE = 4; // Adjust based on workload

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("UDP Server started on port " + PORT);

            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(requestPacket); // Wait for a client request

                // Handle request in a separate thread
                executor.execute(new ClientHandler(requestPacket, serverSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final DatagramPacket requestPacket;
    private final DatagramSocket serverSocket;
    private static final int BUFFER_SIZE = 4096;

    public ClientHandler(DatagramPacket requestPacket, DatagramSocket serverSocket) {
        this.requestPacket = requestPacket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            String requestedFile = new String(requestPacket.getData(), 0, requestPacket.getLength()).trim();
            InetAddress clientAddress = requestPacket.getAddress();
            int clientPort = requestPacket.getPort();

            System.out.println("Client requested file: " + requestedFile);

            File file = new File("server/" + requestedFile);
            if (!file.exists() || file.length() == 0) {
                System.out.println("File not found: " + requestedFile);
                return;
            }

            byte[] buffer = new byte[BUFFER_SIZE];
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(buffer, bytesRead, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                }
            }

            // Send end-of-file signal
            byte[] endSignal = "END_OF_FILE".getBytes();
            DatagramPacket endPacket = new DatagramPacket(endSignal, endSignal.length, clientAddress, clientPort);
            serverSocket.send(endPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
