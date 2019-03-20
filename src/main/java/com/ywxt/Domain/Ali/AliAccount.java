package com.ywxt.Domain.Ali;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.ywxt.Annotation.MarkCloumn;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ali_account")
public class AliAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String userName;
    @NotBlank
    private String accessKeyId;
    @NotBlank
    private String accessKeySecret;
    // status:正常使用normal/账号异常invalid（取数据错误）
    private String status = "invalid";
    @Transient
    private QueryAccountBalanceResponse.Data balanceData;
    @Transient
    private Boolean isAlertBalance = false;
    @Transient
    private Boolean isAlertMarked = false;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAlertMarked() {
        return isAlertMarked;
    }

    public void setAlertMarked(Boolean alertMarked) {
        isAlertMarked = alertMarked;
    }
}
