package com.rathaur.earning.Modal;

public class Users {
    String name;
    String email;
    String mobile;
    String password;
    String withdrawPassword;
    String friendReferralCode;
    String myReferralCode;
    boolean referAmountReceive;

    public Users() {
    }

    public Users(String name, String email, String mobile, String password, String withdrawPassword, String friendReferralCode, String myReferralCode, boolean referAmountReceive) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.withdrawPassword = withdrawPassword;
        this.friendReferralCode = friendReferralCode;
        this.myReferralCode = myReferralCode;
        this.referAmountReceive = referAmountReceive;
    }

    public boolean isReferAmountReceive() {
        return referAmountReceive;
    }

    public void setReferAmountReceive(boolean referAmountReceive) {
        this.referAmountReceive = referAmountReceive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWithdrawPassword() {
        return withdrawPassword;
    }

    public void setWithdrawPassword(String withdrawPassword) {
        this.withdrawPassword = withdrawPassword;
    }

    public String getFriendReferralCode() {
        return friendReferralCode;
    }

    public void setFriendReferralCode(String friendReferralCode) {
        this.friendReferralCode = friendReferralCode;
    }

    public String getMyReferralCode() {
        return myReferralCode;
    }

    public void setMyReferralCode(String myReferralCode) {
        this.myReferralCode = myReferralCode;
    }
}