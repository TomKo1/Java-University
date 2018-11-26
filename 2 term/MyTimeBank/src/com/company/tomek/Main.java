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
        try {
            System.out.println("### RESERVATION EXAMPLE ####");
            makeSimultaneousOperation("reserve");
            System.out.println("########################");
            System.out.println("### PRINT ALL APPOINTMENTS ###");
            System.out.println("########################");
            makePrintRequest();
            System.out.println("########################");
            System.out.println("########################");
            System.out.println("### CANCEL EXAMPLE ####");
            System.out.println("########################");
            makeSimultaneousOperation("cancel");
            System.out.println("########################");
            System.out.println("### PRINT ALL APPOINTMENTS ###");
            System.out.println("########################");
            makePrintRequest();
            System.out.println("########################");
        } catch(InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error occured in simulation, please try later");
            System.exit(1);
        }
     }

     private static void makePrintRequest() throws InterruptedException {
         Thread thread = new Thread(()-> {
             makeConnection("print", "Clever user");
         });
         thread.start();
         thread.join();
     }

    private static void makeSimultaneousOperation(String operation) throws InterruptedException {
        Thread dumbUserThread = new Thread(()-> {
            makeConnection(operation + " 1 111111 DUMMY true", "Dumb user"); //dumb user
        });

        Thread cleverUserThread = new Thread(()-> {
            makeConnection(operation+" 1 222222 CLEVER false", "Clever user"); //dumb user
        });


        dumbUserThread.start();
        cleverUserThread.start();
        dumbUserThread.join();
        cleverUserThread.join();
    }

    private static void makeConnection(String request, String clientNick) {
        try (Socket socket = new Socket("localhost", 4040)) {
            // run separate thread to get broadcast
            new Thread(new Client(socket, clientNick)).start();

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(request);
            out.flush();

            out.println("quit");
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
