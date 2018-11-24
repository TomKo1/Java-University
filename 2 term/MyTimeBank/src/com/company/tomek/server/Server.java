package com.company.tomek.server;


import com.company.tomek.server.storage.AppointmentsStorage;
import com.company.tomek.server.storage.ClientStorage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

public class Server implements Runnable {

    private Socket clientSocket;
    // storage of appointments
    //TODO: remove static
    //TODO: get rid of new line signs
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
                //TODO: NumberFormatException, NoSuchElementException
                 try {
                     handleRequest(request, printWriter);
                 } catch(NumberFormatException | NoSuchElementException | IndexOutOfBoundsException e) {
                     printWriter.println("Eror while handling request, please try again.");
                     printWriter.flush();
                 }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error reading client's input stream");
            System.exit(1);
        }
    }

    // format of input operacja [ktoryIndeks] jesli indeks ma sens w danym kontekscie
    // np. w kontekscie wyswietlania wszystkiegostring nie ma sensu
    //TODO: refactor it
    private void handleRequest(String request, PrintWriter printWriter) throws NumberFormatException, IndexOutOfBoundsException, NoSuchElementException, IOException, InterruptedException {
        //tokenize request
        StringTokenizer stringTokenizer = new StringTokenizer(request, " ");
        String operation = stringTokenizer.nextToken();
        switch(operation) {
            //TODO: refactor
            case "reserve":
               if(isSimulation) simulationBehaviour(request);
               semaphore.acquire();
                 System.out.println(Thread.currentThread().getName());
                 String clientId = clientSocket.getRemoteSocketAddress().toString();
                 int indexToBook = Integer.parseInt(stringTokenizer.nextToken());
                 boolean isSuccesful =  appointmentsStorage.reserveOne(indexToBook, clientId);
                 if(isSuccesful) {
                     clientStorage.sendToAll("!!! Appointment: " + appointmentsStorage.getAppointment(indexToBook)+" booked !!!");
                 } else {
                     printWriter.println("Appointment already booked!");
                     printWriter.flush();
                 }
                System.out.println(Thread.currentThread().getName());
               semaphore.release();
                break;
            case "cancel":
                if(isSimulation) simulationBehaviour(request);
                semaphore.acquire();
                int indexToCancel = Integer.parseInt(stringTokenizer.nextToken());
                String clientId1 = clientSocket.getRemoteSocketAddress().toString();
                boolean isSuccesfullyCancelled = appointmentsStorage.cancel(indexToCancel, clientId1);
                if(isSuccesfullyCancelled) {
                    clientStorage.sendToAll("!!! Appointment: " + appointmentsStorage.getAppointment(indexToCancel) + " cancelled !!!");
                } else {
                    printWriter.println("You are not allowed to cancel not your reservation!");
                    printWriter.flush();
                }
                semaphore.release();
                break;
            case "print":
                for(String appointment : appointmentsStorage.getAll()) {
                    printWriter.println(appointment);
                    printWriter.flush();
                }
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

        boolean isDumb = Boolean.valueOf(request.split(" ")[2]);
        if(isDumb) {
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                System.exit(1);
            }
        }
    }
}
