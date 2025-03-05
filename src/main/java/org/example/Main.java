package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {

//        FileClient client = new FileClient();
//        client.startConnection("127.0.0.1", 6666);
//        String filename = "test.html";
//        String response = client.sendMessage(filename);
//        System.out.println(response);

        for(int i =0;i<10;i++){
            FileClient client = new FileClient();
            client.startConnection("127.0.0.1", 6666);
            String filename = "payload.png";
            String localName = "payload"+i+".png";
            client.requestFile(filename,localName);
            client.stopConnection();
            System.out.println("Processed client "+i);
        }

    }
}