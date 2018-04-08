package com.company.tomek.Repositories;

import com.company.tomek.BankClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.List;

//TODO implement
public class ClientDbRepository implements BankingRepositories {


    @Override
    public boolean saveClient(BankClient bankClient) {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    @Override
    public boolean loadUsersOnEntry() {
        throw new NotImplementedException();
    }

    @Override
    public BankClient findUser(String personId) {
        throw new NotImplementedException();
    }

    @Override
    public List<BankClient> getAllBankCustomers() {
        throw  new NotImplementedException();
    }

    @Override
    public void deleteUser(BankClient bankClient) {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal findTheLargestClientID() {
        throw new NotImplementedException();
    }
}
