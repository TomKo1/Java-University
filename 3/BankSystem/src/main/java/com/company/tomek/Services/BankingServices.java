package com.company.tomek.Services;


import com.company.tomek.BankClient;
import com.company.tomek.Repositories.BankingRepositories;
import com.company.tomek.Repositories.ClientsMemoryRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BankingServices {

    private BigDecimal firstFreeClientId;
    private BankingRepositories repository;
    private static BankingServices bankingServices;

    public static BankingServices getInstanceWithMemRepo(){
        if(bankingServices == null){
            bankingServices = new BankingServices(new ClientsMemoryRepository());
        }
        return bankingServices;
    }


    private BankingServices(BankingRepositories repository) {
        this.repository = repository;
        this.loadUsersOnEntry(); // not quite good ...
        this.firstFreeClientId = this.repository.findTheLargestClientID();
    }




    public boolean saveUsersOnExit(){
        return repository.saveUsersOnExit();
    }

    public boolean loadUsersOnEntry(){
        return repository.loadUsersOnEntry();
    }


    public void printAllUser(){
        List<BankClient> bankClientList = repository.getAllBankCustomers();
        for(BankClient bankClient: bankClientList){
            System.out.println(bankClient);
        }
    }


    private boolean addClient(BankClient bankClient){
        firstFreeClientId=firstFreeClientId.add(new BigDecimal("1"));
        return repository.saveClient(bankClient);
    }


    public void createNewClient(Scanner scanner){
        String name="";
        String lastName ="";
        String address="";
        String personID="";
        double initialBalance = 0.0;
        try {
            name = takeInfo("Type first name: ",scanner);
            System.out.println("---------------");
            lastName = takeInfo("Type last name: ",scanner);
            System.out.println("---------------");
            address = takeInfo("Type address: ",scanner);
            System.out.println("---------------");
            do {
                System.out.println("Type person ID: ");
                personID = scanner.nextLine();
            }while(personID.length() != 11 || !personID.matches("[0-9]+"));
            System.out.println("---------------");
            do {
                System.out.print("Type initial account balance: ");
                initialBalance = scanner.nextDouble();
                scanner.nextLine();
                if (initialBalance < 0) System.out.println("Initial balance can't be negative!");
            } while (initialBalance < 0);
            System.out.println("---------------");
        }catch(Exception e){
            printExceptionRemarks();
            return ;
        }


        if(confirmOperation(scanner)) addClient(new BankClient(firstFreeClientId, name, lastName, personID, initialBalance, address));


    }

    private String takeInfo(String s, Scanner scanner) {
        String toReturn ="";
        do{
            System.out.println(s);
            toReturn= scanner.nextLine();
        }while(toReturn.isEmpty());
           return  toReturn;
    }

    public void addMoneyToAccount(Scanner scanner) {
        try {
            System.out.println("Add money money");
            System.out.println("---------------");
            BankClient bankClient = null;
            do {
                System.out.print("Please type customer's Person ID (q to quit): ");
                String personId = scanner.nextLine();
                if (personId.equals("q")) return;
                bankClient = repository.findUser(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);
            double moneyToAdd = 0;
            do {
                System.out.println("Please type money to add (q to cancel): ");

                String input = scanner.nextLine();
                if (input.equals("q")) return;
                moneyToAdd = Double.parseDouble(input);
                if (moneyToAdd <= 0) System.out.println("You can add only positive/non-zero numbers");
            } while (moneyToAdd <= 0);

            if(confirmOperation(scanner)) bankClient.addAdditionalMoney(moneyToAdd);

        }catch(Exception e){
            printExceptionRemarks();
        }
    }



    public void searchForUserUsingBankID(Scanner scanner) {
        try {
            System.out.println("Searching engine");
            System.out.println("---------------");
            System.out.print("Please type customer's Person ID: ");
            String personId = scanner.nextLine();

            BankClient bankClient = repository.findUser(personId);
            if (bankClient == null) {
                System.out.println("No such user!");
            } else {
                System.out.println("User found: ");
                System.out.println("---------------");
                System.out.println(bankClient);
            }
        }catch(Exception e){
           printExceptionRemarks();
        }

    }




    //TODO ppowtorzenie kodu !!!

    public  void deleteUserWithSpecificPersonId(Scanner scanner) {
        try {
            System.out.println("Deleting user");
            System.out.println("---------------");
            BankClient bankClient = null;
            do {
                System.out.print("Please type customer's Person ID (q to quit): ");
                String personId = scanner.nextLine();//todo with multiple string with spaces crash
                if (personId.equals("q")) return;
                bankClient = repository.findUser(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);

            if(confirmOperation(scanner)) repository.deleteUser(bankClient);

        }catch(Exception e){
            printExceptionRemarks();
        }

    }




    //todo avoid repetition!!!
    public  void withdrawMoneyFromSpecificAccount(Scanner scanner){
        try {
            System.out.println("Withdraw money");
            System.out.println("---------------");
            BankClient bankClient = null;
            do {
                System.out.print("Please type customer's Person ID (q to quit): ");
                //TODO try to be more specific
                String personId = scanner.nextLine();
                if (personId.equals("q")) return;
                bankClient = repository.findUser(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);
            double moneyToAdd = 0;
            do {
                System.out.println("Please type money to withdraw (q to cancel): ");
                String input = scanner.nextLine();
                if (input.equals("q")) return;
                moneyToAdd = Double.parseDouble(input);
                if (moneyToAdd <= 0) System.out.println("You can only withdraw positive/non-zero numbers");
                if (moneyToAdd > bankClient.getMoney()) System.out.println("You can't withdraw more than you have!");
            } while (moneyToAdd <= 0 || moneyToAdd > bankClient.getMoney());

            if(confirmOperation(scanner)) bankClient.addAdditionalMoney(-moneyToAdd);
         }catch(Exception e){
                printExceptionRemarks();
            }

        }




    //todo refactor the code with this method to BankingServices
    public  void transferBetweenTwoAccounts(Scanner scanner){
        try {
            BankClient bankClient = null;
            BankClient bankClient2 = null;
            System.out.println("Transfer engine");
            System.out.println("---------------");
            do {
                System.out.print("Please type first customer's (sender) Person ID: ");
                String personId = scanner.nextLine();
                bankClient = repository.findUser(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);
            System.out.println("---------------");
            do {
                System.out.print("Please type customer's (receiver's) Person ID: ");
                String personId2 = scanner.nextLine();
                bankClient2 = repository.findUser(personId2);
            } while (bankClient2 == null);
            double transferMoney = 0;
            do {
                System.out.print("Type amount of money to transfer between accounts: ");
                transferMoney = scanner.nextDouble();
                if (transferMoney < 0) System.out.println("Amount of money can't be negative");
                if (transferMoney > bankClient.getMoney())
                    System.out.println("You can't transfer more than the client has");
            } while (transferMoney < 0 || transferMoney > bankClient.getMoney());

            if(confirmOperation(scanner)){
                bankClient.addAdditionalMoney(-transferMoney);
                bankClient2.addAdditionalMoney(transferMoney);
            }
        }catch(Exception e){
            printExceptionRemarks();
        }

    }

    private static void printExceptionRemarks(){
        System.out.println("---------------");
        System.out.println("Some error occured please try later!");
        System.out.println("or contact support ;)");
        System.out.println("---------------");
    }


    private boolean confirmOperation(Scanner scanner){
        String answer = "";
        do {
            System.out.println("**********************************");
            System.out.println("Please confirm operation. [Y/N]");
            answer = scanner.nextLine();
            System.out.println("**********************************");
        }while(checkAnsewr(answer));
        if(answer.equals("y")||answer.equals("Y")){
            return true;
          }else {
            System.out.println("Operation canceled");
             return false;
         }
    }

    private boolean checkAnsewr(String answer){
        if(answer.equals("Y") || answer.equals("y")||answer.equals("n")||answer.equals("N")){
            return false;
        }else{
            return true;
        }
    }

}
