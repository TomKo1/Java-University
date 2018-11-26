package com.company.tomek.server.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//TODO: send to one! ;)
public class ClientStorage {

    private ArrayList<Socket> clientsSockets = new ArrayList<>();

    public void addConnection(Socket socket) {
        clientsSockets.add(socket);
    }

    public void sendToAll(String message) throws IOException {
        try {
            for (Socket socket : clientsSockets) {
                OutputStream out = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(out);
                printWriter.println(message);
                printWriter.flush();
            }
        } catch(IOException e) {
            System.out.println("Error while sending to all!");
            throw e;
        }
    }

    public void removeClient(Socket socket) {
        clientsSockets.remove(socket);
    }
}
