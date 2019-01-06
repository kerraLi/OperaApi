package com.ywxt.Domain;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;

import javax.persistence.Entity;

@Entity
public class AliAccount {

    private int id;
    private String userName;
    private String accessKeyId;
    private String accessKeySecret;
    private QueryAccountBalanceResponse.Data balanceData;
    private Boolean isAlertBalance = false;

    public AliAccount() {
    }

    public AliAccount(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    public QueryAccountBalanceResponse.Data getBalanceData() {
        return balanceData;
    }

    public void setBalanceData(QueryAccountBalanceResponse.Data balanceData) {
        this.balanceData = balanceData;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public Boolean getAlertBalance() {
        return isAlertBalance;
    }

    public void setAlertBalance(Boolean alertBalance) {
        isAlertBalance = alertBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
