package com.rathaur.earning.Modal;

public class Withdraw {
    String id;
    String amount;
    String mobile;
    String status;

    public Withdraw(String id, String amount, String mobile, String status) {
        this.id = id;
        this.amount = amount;
        this.mobile = mobile;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Withdraw() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
