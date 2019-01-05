package com.ywxt.Domain;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;

public class AliAccount {

    private String userName;
    private String accessKeyId;
    private String accessKeySecret;
    private QueryAccountBalanceResponse.Data balanceData;
    private Boolean isAlertBalance = false;

    private AliAccount() {
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
}
