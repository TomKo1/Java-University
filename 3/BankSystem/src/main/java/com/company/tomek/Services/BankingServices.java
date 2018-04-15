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
        return repository.saveAllUsers();
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
            BankClient bankClient = null;
            do {
                System.out.println("Type person ID: ");
                personID = scanner.nextLine();
                bankClient = repository.findUserById(personID);
                if(bankClient != null ) System.out.println("User with this id is already in the system");
            }while(personID.length() != 11 || !personID.matches("[0-9]+") || bankClient != null);
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


        if(confirmOperation(scanner)) {
            addClient(new BankClient(firstFreeClientId, name, lastName, personID, initialBalance, address));
            repository.saveAllUsers();
        }

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

            System.out.println("Add money money");
            System.out.println("---------------");
            BankClient bankClient = null;
        try {
            do {
                System.out.print("Please type customer's Person ID (q to quit): ");
                String personId = scanner.nextLine();
                if (personId.equals("q")) return;
                bankClient = repository.findUserById(personId);
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

            if(confirmOperation(scanner)){
                bankClient.addAdditionalMoney(moneyToAdd);
                repository.saveAllUsers();
            }

        }catch(Exception e){
            printExceptionRemarks();
        }
    }

//todo eliminate "big try"

    public void searchForSpecificUser(Scanner scanner) {
        try {
            String optionSelected ="";
                do {
                    System.out.println("Searching engine");
                    System.out.println("---------------");
                    optionSelected = selectSearchingOption(scanner);
                }while(runAppropriateSearchingOption(optionSelected, scanner));
        }catch(Exception e){
           printExceptionRemarks();
        }

    }

    private boolean runAppropriateSearchingOption(String option, Scanner scanner){
        switch(option) {
            case "c":
                searchByClientNumber(scanner);
                return false;
            case "n":
                searchByName(scanner);
                return false;
            case "s":
                searchBySurname(scanner);
                return false;
            case "i":
                searchById(scanner);
                return false;
            case "a":
                searchByAddress(scanner);
                return false;
            case "q":
                return false;
            default:
                System.out.println("No such option!");
                return true;
        }
    }

    private void searchByAddress(Scanner scanner) {
        String address = takeAppriopriateParamForSearching("address", scanner);
        List<BankClient> bankClients = repository.findUsersByAddress(address);
        printAllUsersFromList(bankClients);
    }

    private void searchByClientNumber(Scanner scanner){
        String clientId = takeAppriopriateParamForSearching("number", scanner);
        BankClient bankClient = repository.findUsersByClientNumber(clientId);
        printOneUserAfterSearching(bankClient);
    }

    private void printOneUserAfterSearching(BankClient bankClient) {
        if (bankClient == null) {
            System.out.println("No such user!");
        } else {
            System.out.println("User found: ");
            System.out.println("---------------");
            System.out.println(bankClient);
        }
    }

    private void searchByName(Scanner scanner) {
        String name = takeAppriopriateParamForSearching("name", scanner);
        List<BankClient> bankClients = repository.findUsersByName(name);
        printAllUsersFromList(bankClients);
    }


    private void searchBySurname(Scanner scanner) {
        String surname = takeAppriopriateParamForSearching("surname", scanner);
        List<BankClient> bankClients = repository.findUsersBySurname(surname);
        printAllUsersFromList(bankClients);
    }

    private String takeAppriopriateParamForSearching(String paramName, Scanner scanner){
        System.out.print("Please type customer's "+paramName+":");
        String surname = scanner.nextLine();
        return surname;
    }

    private void printAllUsersFromList(List<BankClient> bankClientsList){
        System.out.println("Users found: ");
        if(bankClientsList.isEmpty()){
            System.out.println("No such users!");
            return ;
        }
        for(BankClient bankClient1: bankClientsList){
            System.out.println(bankClient1);
        }
    }

    private void searchById(Scanner scanner){
        String personId = takeAppriopriateParamForSearching("Person ID", scanner);
        BankClient bankClient = repository.findUserById(personId);
        printOneUserAfterSearching(bankClient);
    }

    private String selectSearchingOption(Scanner scanner){
        printSearchingOptions();
        System.out.println("---------------");
        System.out.println("Please select searching option: ");
        String option = scanner.nextLine();
        return option;
    }

    private void printSearchingOptions() {
        System.out.println("---------------");
        System.out.println("SEARCH BY: ");
        System.out.println("c - client nubmer");
        System.out.println("n - name");
        System.out.println("s - surname");
        System.out.println("i - person id");
        System.out.println("a - address");
        System.out.println("q - quit");
    }


    public  void deleteUserWithSpecificPersonId(Scanner scanner) {

            System.out.println("Deleting user");
            System.out.println("---------------");
            BankClient bankClient = null;
        try {
            do {
                System.out.print("Please type customer's Person ID (q to quit): ");
                String personId = scanner.nextLine();//todo with multiple string with spaces crash
                if (personId.equals("q")) return;
                bankClient = repository.findUserById(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);

            if(confirmOperation(scanner)){
                repository.deleteUser(bankClient);
                repository.saveAllUsers();
            }

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
                bankClient = repository.findUserById(personId);
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

            if(confirmOperation(scanner)) {
                bankClient.addAdditionalMoney(-moneyToAdd);
                repository.saveAllUsers();
            }
         }catch(Exception e){
                printExceptionRemarks();
            }

        }




    //todo refactor the code with this method to BankingServices
    public  void transferBetweenTwoAccounts(Scanner scanner){

            BankClient bankClient = null;
            BankClient bankClient2 = null;
            System.out.println("Transfer engine");
            System.out.println("---------------");
        try {
            do {
                System.out.print("Please type first customer's (sender) Person ID: ");
                String personId = scanner.nextLine();
                bankClient = repository.findUserById(personId);
                if (bankClient == null) System.out.println("No such user!");

            } while (bankClient == null);
            System.out.println("---------------");
            do {
                System.out.print("Please type customer's (receiver's) Person ID: ");
                String personId2 = scanner.nextLine();
                bankClient2 = repository.findUserById(personId2);
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
                repository.saveAllUsers();
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
        }while(checkAnswer(answer));
        if(answer.equals("y")||answer.equals("Y")){
            return true;
          }else {
            System.out.println("Operation canceled");
             return false;
         }
    }

    private boolean checkAnswer(String answer){
        if(answer.equals("Y") || answer.equals("y")||answer.equals("n")||answer.equals("N")){
            return false;
        }else{
            return true;
        }
    }

}
