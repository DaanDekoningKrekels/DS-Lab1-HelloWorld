package ua.fti.udp;

import java.io.*;
import java.net.*;

public class FileClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;

    public void startConnection(String ip, int port) throws IOException {
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(ip);
        this.serverPort = port;
    }

    public void requestFile(String filename, String localName) {
        try {
            // Send filename request
            byte[] requestData = filename.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
            socket.send(requestPacket);
            // Prepare to receive file
            byte[] buffer = new byte[4096];
            FileOutputStream fos = new FileOutputStream("client/" + localName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                // Check if it's the end signal
                String receivedText = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (receivedText.equals("END_OF_FILE")) {
                    break;
                }

                // Write received data to file
                bos.write(receivePacket.getData(), 0, receivePacket.getLength());
            }

            bos.flush();
            bos.close();
            System.out.println("File received successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
