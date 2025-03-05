package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private ServerSocket serverSocket;

        public void start(int port) throws IOException {
                this.serverSocket = new ServerSocket(port);
                this.clientSocket = this.serverSocket.accept();
                this.out = this.clientSocket.getOutputStream();
                this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

                String filename = in.readLine();
                System.out.println("Requested file: "+filename);
                File myFile = new File("server/"+filename);
                System.out.println(myFile.getAbsolutePath());
                if (myFile.length() == 0){
                    System.out.println("File doesn't exist!");
                } else{
                    FileInputStream  fis = new FileInputStream(myFile);
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

                this.stop();
            }

        public void stop() throws IOException {
            this.in.close();
            this.out.close();
            this.clientSocket.close();
            this.serverSocket.close();
        }
    }
}
