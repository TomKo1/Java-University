package com.company.tomek.server;


import com.company.tomek.server.storage.AppointmentsStorage;
import com.company.tomek.server.storage.ClientStorage;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

public class Server implements Runnable {

    private Socket clientSocket;
    private static final AppointmentsStorage appointmentsStorage = new AppointmentsStorage();
    private static final ClientStorage clientStorage = new ClientStorage();
    private final Semaphore semaphore = new Semaphore(1);
    private static boolean isSimulation = false;


    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    public static void main(String[] args) {
        if(args.length == 1 && args[0].equals("--sim")) {
            System.out.println("### RUNNING IN SIMULATION MODE ###");
            isSimulation = true;
        } else {
            isSimulation = false;
        }

        try (ServerSocket serverSocket = new ServerSocket(4040)) {
            System.out.println("Running on port 4040");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Server(clientSocket)).start();
                clientStorage.addConnection(clientSocket);
            }

        } catch (IOException e) {
            System.out.println("Error while starting server");
            System.exit(1);
        }
    }

    // handles client's requests
    @Override
    public void run() {

        try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            PrintWriter printWriter = new PrintWriter(out);
            String request;
            while ((request = bufferedReader.readLine()) != null) {
                 try {
                     try {
                         handleRequest(request, clientSocket);
                     } catch(NumberFormatException | NoSuchElementException e) {
                         printWriter.println("Error occured please corect your request format");
                         printWriter.flush();
                     }
                 } catch(NumberFormatException | NoSuchElementException | IndexOutOfBoundsException e) {
                     printWriter.println("Eror while handling request, please try again.");
                     printWriter.flush();
                 }
            }

        } catch(SocketException e){
            System.out.println("Goodbye");
            return ;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error reading client's input/output stream");
            System.exit(1);
        }
    }

    // format of input operation [index] if in given context makes sense
    // e.g 'print' request doesn't have index
    private void handleRequest(String request, Socket clientSocket) throws NumberFormatException, IndexOutOfBoundsException, NoSuchElementException, IOException, InterruptedException {
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
        StringTokenizer stringTokenizer = new StringTokenizer(request, " ");
        String operation = stringTokenizer.nextToken();
        switch(operation) {
            case "reserve":
            case "cancel":
                simulationBehaviour(request);
                semaphore.acquire();
                int index = Integer.parseInt(stringTokenizer.nextToken());
                String secretKey = stringTokenizer.nextToken();
                boolean isSuccesful = false;
                String broadcastMsg = "";
                String msgToOne = "";
                switch(operation) {
                    case "reserve":
                        String nick = stringTokenizer.nextToken();
                        isSuccesful = appointmentsStorage.reserveOne(index, nick, secretKey);
                        msgToOne = "Appointment already booked!";
                        broadcastMsg = "!!! Appointment: " + appointmentsStorage.getAppointment(index)+" booked !!!";
                        break;
                    case "cancel":
                        isSuccesful = appointmentsStorage.cancel(index, secretKey);
                        msgToOne = "You are not allowed to cancel not your reservation!";
                        broadcastMsg = "!!! Appointment: " + appointmentsStorage.getAppointment(index) + " cancelled !!!";
                        break;
                }
                if(isSuccesful) {
                    clientStorage.sendToAll(broadcastMsg);
                } else {
                    printWriter.println(msgToOne);
                    printWriter.flush();
                }
                semaphore.release();
                break;
            case "print":
                 appointmentsStorage.printAllAppointments(printWriter);
                break;
            case "quit":
                    clientStorage.removeClient(clientSocket);
                    clientSocket.close();
                break;
            default:
                printWriter.println("Wrong operation!");
                printWriter.flush();
        }
    }


    // simulates behaviour of dumb user
    // may cause exception when called not by simulation
    // this method do not have to be synchronized as
    // threads have separate stack
    private void simulationBehaviour(String request) {
        if(!isSimulation) return;
        boolean isDumb = Boolean.valueOf(request.split(" ")[4]);
        if(isDumb) {
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                System.out.println("Error in simulation waiting of dumb thread");
                System.exit(1);
            }
        }
    }
}
