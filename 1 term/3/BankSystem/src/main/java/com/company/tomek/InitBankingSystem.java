package com.company.tomek;


//TODO implement GUI using JavaFX
// TODO implement test using JUNIT

import com.company.tomek.Services.BankingServices;
import java.util.Scanner;

public class InitBankingSystem {



    public static void main(String[] args) {
    if(args.length > 0){
        if(args[0].equals("-gui")) System.out.println("GUI not yet implemented");
        System.out.println("Please run this program in CLI mode");
        }else{
                showProgramOptions();
                Scanner scanner = new Scanner(System.in);
                String option = "";
                BankingServices bankingServices = BankingServices.getInstanceWithMemRepo();
                do {
                    System.out.println("Type option: ");
                    option = scanner.nextLine();
                } while (appropriateOption(option, scanner));
                bankingServices.saveUsersOnExit();
        }
    }


    private static void showProgramOptions(){
        System.out.println("Welcome to Banking System version 1.0");
        System.out.println("---------------");
        System.out.println("In order to run program in GUI mode please run it once more time with argument -gui");
        System.out.println("---------------");
        System.out.println("Available options:");
        System.out.println("c - creates new customer");
        System.out.println("d - deleting of customer");
        System.out.println("p - add to specific account");
        System.out.println("pf - withdraw money from specific account");
        System.out.println("pt - transfer between two accounts");
        System.out.println("s - search and show client information");
        System.out.println("o - show available options one more time");
        System.out.println("pa - print all bank users");
        System.out.println("q - quits the system");
        System.out.println("---------------");
    }









    private static boolean appropriateOption(String option, Scanner scanner){
        BankingServices bankingServices = BankingServices.getInstanceWithMemRepo();
            switch (option){
                case "q":
                    return false;
                    case "c":
                        bankingServices.createNewClient(scanner);
                        return true;
                case "s":
                    bankingServices.searchForSpecificUser(scanner);
                    return true;
                case "o":
                    showProgramOptions();
                    return true;
                case "pa":
                    bankingServices.printAllUser();
                    return true;
                case "d":
                    bankingServices.deleteUserWithSpecificPersonId(scanner);
                    return true;
                case "p":
                    bankingServices.addMoneyToAccount(scanner);
                    return true;
                case "pf":
                    bankingServices.withdrawMoneyFromSpecificAccount(scanner);
                    return true;
                case "pt":
                    bankingServices.transferBetweenTwoAccounts(scanner);
                    return true;
                 default:
                    System.out.println("No such option");
                    return true;
            }
    }

}
