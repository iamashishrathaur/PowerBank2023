package com.rathaur.earning.Modal;

public class Bank {
    String  name,bankName,accountNumber,IFSC;

    public Bank() {
    }

    public Bank(String name, String bankName, String accountNumber, String IFSC) {
        this.name = name;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.IFSC = IFSC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }
}
