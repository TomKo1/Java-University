package com.company.tomek.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//TODO: walidacja po stronie klienta itd.
public class Client implements Runnable {

    private Socket socket;
    // nick for simulaton purposes
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
        System.out.println("##########################################");
        System.out.println("Format of requests:");
        System.out.println("reserve index your_secret_id_number your_nick");
        System.out.println("print");
        System.out.println("cancel index your_secret_id_number");
        System.out.println("##########################################");
    }

    @Override
    public void run() {
        BufferedReader bufferedReader;
        try(InputStream in = socket.getInputStream()) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String msgFromServer;

            while(( msgFromServer = bufferedReader.readLine() ) != null) {
                if(!nick.isEmpty()) {
                    // print message in simulation mode format
                    System.out.printf("Message for %s: %s \n", nick, msgFromServer);
                    continue;
                }
                System.out.println(msgFromServer);
            }
        } catch(IOException e) {
            //e.printStackTrace();
            //System.out.println("Error while establishing broadcast reader connection in 'Client' class");
            System.exit(1);
        }
    }
}
