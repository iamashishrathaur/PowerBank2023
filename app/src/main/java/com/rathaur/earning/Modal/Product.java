package com.rathaur.earning.Modal;

public class Product {
    String name;
    String days;
    String dailyProfit;
    String value;
    int image;
    String startDate;
    String endDate;
    String isCheck;
    String productId;
    boolean isExpire;


    public Product(String name, String days, String dailyProfit, String value, int image, String startDate, String endDate, String isCheck, String productId, boolean isExpire) {
        this.name = name;
        this.days = days;
        this.dailyProfit = dailyProfit;
        this.value = value;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCheck = isCheck;
        this.productId = productId;
        this.isExpire = isExpire;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Product(String name, String days, String dailyProfit, String value, int image) {
        this.name = name;
        this.days = days;
        this.dailyProfit = dailyProfit;
        this.value = value;
        this.image = image;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDailyProfit() {
        return dailyProfit;
    }

    public void setDailyProfit(String dailyProfit) {
        this.dailyProfit = dailyProfit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
