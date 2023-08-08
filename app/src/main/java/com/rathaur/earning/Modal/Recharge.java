package com.rathaur.earning.Modal;

public class Recharge {
    String mobile;
    String amount;
    String time;
    String status;
    String utr;


    public Recharge() {
    }

    public Recharge(String utr) {
        this.utr = utr;
    }

    public String getUtr() {
        return utr;
    }

    public void setUtr(String utr) {
        this.utr = utr;
    }

    public Recharge(String mobile, String amount, String time, String status) {
        this.mobile = mobile;
        this.amount = amount;
        this.time = time;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

