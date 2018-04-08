package com.company.tomek.Repositories;

import com.company.tomek.BankClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  Class which saves client to memory
*
 *
 * */



public class ClientsMemoryRepository implements  BankingRepositories{

    // dla uproszczenia w czasie dzialania programu klientow przechowuje na liscie
    List<BankClient> allBankClient;


    public ClientsMemoryRepository() {
            this.allBankClient = new ArrayList<>();
    }


    @Override
    public boolean saveClient(BankClient bankClient) {
        allBankClient.add(bankClient);
        return true;
    }

    @Override
    public boolean deleteClient() {
        throw new NotImplementedException();

    }

    @Override
    public boolean updateClientAccount() {
        throw new NotImplementedException();


    }

    @Override
    public boolean saveUsersOnExit() {
        try(ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("users.txt"))){
            for(BankClient bankClient: allBankClient) {
                outStream.writeObject(bankClient);
            }
        }catch(Exception ex){
            printExceptionRemarks("saving",ex);
        }
        return true;
    }


    // TODO GSON  - > https://github.com/google/gson

    @Override
    public boolean loadUsersOnEntry(){
        File usersFile = new File("users.txt");
        if(!usersFile.exists()) return true;
       try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("users.txt"))) {
          while(true) {
              BankClient bankClient = (BankClient) input.readObject();
              allBankClient.add(bankClient);
          }
       }catch(EOFException e){
            return true;
       } catch(Exception ex){
           printExceptionRemarks("loading",ex);
       }

        return true;
    }

    private void printExceptionRemarks(String operationName,Exception ex){
        ex.printStackTrace();
        System.out.println("Error while "+ operationName +" the file!\nPlease contact support! ");
        System.exit(1);
    }

    @Override
    public BankClient findUser(String personId) {
        Optional<BankClient> bankClients = allBankClient.stream().filter(
                bankClient ->
                        bankClient.getPersonId().equals(personId)
        ).findFirst();

        if (bankClients.isPresent()) {
            return bankClients.get();
        } else {
            return null;
        }

    }

    @Override
    public List<BankClient> getAllBankCustomers() {
        return allBankClient;
    }

    @Override
    public void deleteUser(BankClient bankClient) {
        allBankClient.remove(bankClient);
    }

    @Override
    public BigDecimal findTheLargestClientID() {
      Optional<BankClient> optionalBankClient = allBankClient.stream().max(
              (o1, o2) ->
                      o1.getClientNumber().compareTo(o2.getClientNumber())

      );
        if(optionalBankClient.isPresent()){
            return optionalBankClient.get().getClientNumber().add(new BigDecimal("1"));
        }else{
            return new BigDecimal("1");
        }

    }


}
