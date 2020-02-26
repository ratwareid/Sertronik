package com.ratwareid.sertronik.model;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//

public class Order {

    public Order(){}

    private String orderID,itemName,itemBrand,itemSize,itemSymptom,senderAddress, senderName , senderPhone, key;
    private String senderLatitude,senderLongitude,createDate,mitraID;
    private int orderType, status;

    public Order(String senderName,String senderPhone,String itemName, String itemBrand, String itemSize, String itemSymptom, String senderAddress, String senderLatitude, String senderLongitude, String createDate, String mitraID, int orderType, int status) {
        this.itemName = itemName;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.itemBrand = itemBrand;
        this.itemSize = itemSize;
        this.itemSymptom = itemSymptom;
        this.senderAddress = senderAddress;
        this.senderLatitude = senderLatitude;
        this.senderLongitude = senderLongitude;
        this.createDate = createDate;
        this.mitraID = mitraID;
        this.orderType = orderType;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemSymptom() {
        return itemSymptom;
    }

    public void setItemSymptom(String itemSymptom) {
        this.itemSymptom = itemSymptom;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderLatitude() {
        return senderLatitude;
    }

    public void setSenderLatitude(String senderLatitude) {
        this.senderLatitude = senderLatitude;
    }

    public String getSenderLongitude() {
        return senderLongitude;
    }

    public void setSenderLongitude(String senderLongitude) {
        this.senderLongitude = senderLongitude;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMitraID() {
        return mitraID;
    }

    public void setMitraID(String mitraID) {
        this.mitraID = mitraID;
    }
}
