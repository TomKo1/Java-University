package com.company.tomek;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankClient implements Serializable{

    private BigDecimal clientNumber;
    private String name;
    private String lastName;
    private String personId; // pesel
    private double money; // zmienic to na String (duze DOuble moga byc)
    private String address;





    public BankClient(BigDecimal clientNumber, String name, String lastName, String personId, double money, String address) {
        this.clientNumber = clientNumber;
        this.name = name;
        this.lastName = lastName;
        this.personId = personId;
        this.money = money;
        this.address = address;
    }

    public BankClient() {

    }

    @Override
    public String toString() {
        return "BankClient{" +
                "clientNumber=" + clientNumber +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personId='" + personId + '\'' +
                ", money=" + money +
                ", address='" + address + '\'' +
                '}';
    }

    public BigDecimal getClientNumber() {
        return clientNumber;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonId() {
        return personId;
    }

    public double getMoney() {
        return money;
    }

    public String getAddress() {
        return address;
    }

    public void setClientNumber(BigDecimal clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addAdditionalMoney(double moneyToAdd) {
        this.money += moneyToAdd;
    }


}



