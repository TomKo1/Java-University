package com.company.tomek.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.company.tomek.algorithms.functions.*;
import com.company.tomek.algorithms.ResultOfAlgorithm;
import com.company.tomek.algorithms.annealing.Annealing;
import com.company.tomek.algorithms.genetic.GeneticAlgorithm;

public class Server implements Runnable {
    private Socket clientSocket;


    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4040)) {
            System.out.println("Running on port 4040");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Server(clientSocket)).start();
                //clientStorage.addConnection(clientSocket);
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
                    handleRequest(request, clientSocket);
                } catch(IllegalArgumentException e) {
                    printWriter.write("Wrong index of function!");
                    printWriter.flush();
                }
            }

        } catch(SocketException e){
            System.out.println("Goodbye");
            return ;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading client's input/output stream");
            System.exit(1);
        }
    }

    // TODO: spaghetii
    private void handleRequest(String request, Socket clientSocket) throws IOException {
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
        String[] arrayRequest = request.split(" ");
        Function function = getFunction(arrayRequest[1]);
        switch(arrayRequest[0]) {
            case "ga":
                System.out.println("Genetic algorithm");
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(function);
                // nightmare
                printWriter.println("Started genetic algorithm - please wait for results");
                printWriter.flush();
                ResultOfAlgorithm resultGa =  geneticAlgorithm.performGeneticAlgorithm();
                printWriter.write(resultGa.getSteps());
                printWriter.flush();
                printWriter.write("SOLUTION:");
                printWriter.flush();
                printWriter.write(resultGa.getSolution() + "\n");
                printWriter.flush();
                printWriter.println("Genetic algorithm finished!");
                printWriter.flush();
                break;
            case "sw":
                Annealing annealing = new Annealing(function);
                System.out.println("Annealing algorithm");
                printWriter.println("Annealing algorithm started - please wait for results");
                printWriter.flush();
                ResultOfAlgorithm resultAn = annealing.performAlgorithm();
                printWriter.write(resultAn.getSteps());
                printWriter.flush();
                printWriter.write("SOLUTION:");
                printWriter.flush();
                printWriter.write(resultAn.getSolution() + "\n");
                printWriter.flush();
                printWriter.println("Annealing algorithm finished!");
                printWriter.flush();
                break;
             case "quit":
                clientSocket.close();
                break;
            default:
                printWriter.println("Wrong operation!");
                printWriter.flush();
        }
    }

    private Function getFunction(String index) {
        switch(index) {
            case "1":
                return new RosenbrockFunction();
            case "2":
                return new BoothFunction();
            case "3":
                return new ThreeHumpCamel();
            case "4":
                return new Himmelblau();
                default:
                    throw new IllegalArgumentException();
        }
    }


}
