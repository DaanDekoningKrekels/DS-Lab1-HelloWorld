package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileClient {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public void startConnection(String ip, int port) throws IOException {
            this.clientSocket = new Socket(ip, port);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }


    public void requestFile(String filename,String localName){
        try {
            this.out.println(filename);
            InputStream is = this.clientSocket.getInputStream();
            FileOutputStream fos = new FileOutputStream("client/"+localName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.flush();
            System.out.println("File received successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        public void stopConnection() throws IOException {
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        }
}
