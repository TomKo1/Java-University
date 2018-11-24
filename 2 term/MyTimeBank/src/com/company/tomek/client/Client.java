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
        this(socket, "N/A");
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
            while (true) {
                System.out.println("Request:");
                String request = scanner.nextLine();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(request);
                out.flush();
            }
        } catch (IOException e) {
            System.out.print("Error while establishing the connection to the server");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        BufferedReader bufferedReader;
        try(InputStream in = socket.getInputStream()) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String msgFromServer;

            while(( msgFromServer = bufferedReader.readLine() ) != null) {
                //TODO: get rid of this
                String localNick = nick.equals("N/A") ? "" : this.nick;
                System.out.printf("Message for %s: %s \n", localNick + socket.getLocalSocketAddress().toString(), msgFromServer);
            }
        } catch(IOException e) {
            // TODO: prawdopodobnie trzeba sprpagować ten wyjątek albo coś
            //TODO: get rid of this exception
            //e.printStackTrace();
            //System.out.println("Error while establishing broadcast reader connection in 'Client' class");
            System.exit(1);
        }
    }
}
