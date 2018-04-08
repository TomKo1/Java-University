package com.company.tomek.Repositories;

import com.company.tomek.BankClient;

import java.math.BigDecimal;
import java.util.List;

public interface BankingRepositories {

    public boolean saveClient(BankClient bankClient);
    public boolean deleteClient();
    public boolean updateClientAccount();
    public boolean saveUsersOnExit();
    public boolean loadUsersOnEntry();
    public BankClient findUser(String personId);
    public List<BankClient> getAllBankCustomers();
    public void deleteUser(BankClient bankClient);
    BigDecimal findTheLargestClientID();
}
