package com.company.tomek.Repositories;

import com.company.tomek.BankClient;

import java.math.BigDecimal;
import java.util.List;

public interface BankingRepositories {

    boolean saveClient(BankClient bankClient);
    boolean deleteClient();
    boolean updateClientAccount();
    boolean saveAllUsers();
    boolean loadUsersOnEntry();
    BankClient findUserById(String personId);
    List<BankClient> getAllBankCustomers();
    void deleteUser(BankClient bankClient);
    BigDecimal findTheLargestClientID();
    List<BankClient> findUsersByName(String name);
    List<BankClient> findUsersBySurname(String surname);
    BankClient findUsersByClientNumber(String surname);
    List<BankClient> findUsersByAddress(String address);
}
