package org.example;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        for(int i =0;i<10;i++){
            FileClient client = new FileClient();
            client.startConnection("127.0.0.1", 6666);
            String filename = "payload"+i%4+".txt";
            client.requestFile(filename,filename);
            client.stopConnection();
            System.out.println("Processed client "+i+" with payload "+i%4);
        }

    }
}