package com.company.tomek;

import com.company.tomek.client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// TO run simulation:
// 1. run server with --sim option
// 2. run this program
public class Main {

    public static void main(String[] args) {
        makeSimultaneousOperation("reserve");
        makeSimultaneousOperation("cancel");
    }

    private static void makeSimultaneousOperation(String operation) {
        // reserve the same object
        new Thread(()-> {
            makeConnection(operation + "1 true", "Dumb user"); //dumb user
        }).start();

        new Thread(()-> {
            makeConnection(operation+" 1 false", "Clever user"); //dumb user
        }).start();
    }

    private static void makeConnection(String request, String clientNick) {
        try (Socket socket = new Socket("localhost", 4040)) {
            // run separate thread to get broadcast
            new Thread(new Client(socket, clientNick)).start();

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(request);
            out.flush();

            //TODO: get rid of this
            try {
                Thread.sleep(750);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.print("Error while establishing the connection to the server");
            System.exit(1);
        }
    }
}
