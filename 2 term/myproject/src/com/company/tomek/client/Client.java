package com.company.tomek.client;

import com.google.common.base.Strings;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private Socket socket;
    private String nick;

    public Client(Socket socket) {
        this(socket, "");
    }

    // constructor for simulation purposes
    public Client(Socket socket, String nick) {
        this.nick = nick;
        this.socket = socket;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 4040)) {
            // run separate thread to get broadcast
            new Thread(new Client(socket)).start();
            printInitialInfo();
            while (true) {
                System.out.println("Request:");
                String request = scanner.nextLine();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(request);
                out.flush();
                if(request.equals("quit")) {
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.print("Error while establishing the connection to the server");
            System.exit(1);
        }
    }


    private static void printInitialInfo() {
        System.out.println(Strings.padEnd("", 10, '#'));
        System.out.println("1 - Rosenbrock function");
        System.out.println("2 - Booth's function");
        System.out.println("3 - Three Hump Camel function");
        System.out.println("4 - Himmelblau's function");
        System.out.println(Strings.padEnd("", 10, '#'));
        System.out.println("Format of requests:");
        System.out.println("ga NUMBER_OF_FUNCTION");
        System.out.println("sw NUMBER_OF_FUNCTION");
        System.out.println("cancel index your_secret_id_number");
        System.out.println(Strings.padEnd("", 10, '#'));
    }

    @Override
    public void run() {
        BufferedReader bufferedReader;
        try(InputStream in = socket.getInputStream()) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String msgFromServer;

            while(( msgFromServer = bufferedReader.readLine() ) != null) {
                String[] msgSplitted = msgFromServer.split(";");
                for(String msg: msgSplitted) {
                    System.out.println(msg);
                }
            }
        } catch(IOException e) {
            //e.printStackTrace();
            //System.out.println("Error while establishing broadcast reader connection in 'Client' class");
            System.exit(1);
        }
    }

}