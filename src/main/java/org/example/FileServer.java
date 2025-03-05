package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    public void stop() throws IOException {
        this.serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        FileServer server = new FileServer();
        server.start(6666);
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private OutputStream out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            this.out = this.clientSocket.getOutputStream();
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            String filename = in.readLine();
            System.out.println("Requested file: " + filename);
            File myFile = new File("server/" + filename);
            if (!myFile.exists() || myFile.length() == 0) {
                System.out.println("File doesn't exist!");
            } else {
                FileInputStream fis = new FileInputStream(myFile);
                BufferedOutputStream bos = new BufferedOutputStream(this.clientSocket.getOutputStream());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                fis.close();
                bos.flush();
                bos.close();
                System.out.println("File sent successfully!");
            }

            this.in.close();
            this.out.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
