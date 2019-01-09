package com.ywxt.Domain.Godaddy;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class GodaddyDomain {

    private int id;
    private String accessKeyId;
    private String domain;
    private String domainId;
    private Date createdAt;
    private Date expires;
    private Date renewDeadline;
    private boolean renewable;
    private boolean renewAuto;
    private boolean transferProtected;
    private boolean expirationProtected;
    private boolean holdRegistrar;
    private boolean locked;
    private boolean privacy;
    // ACTIVE/
    private String status;
    // 是否报警
    private boolean isAlertExpired = false;


    public GodaddyDomain() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCretedAt(Date cretedAt) {
        this.createdAt = cretedAt;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Date getRenewDeadline() {
        return renewDeadline;
    }

    public void setRenewDeadline(Date renewDeadline) {
        this.renewDeadline = renewDeadline;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public boolean isRenewAuto() {
        return renewAuto;
    }

    public void setRenewAuto(boolean renewAuto) {
        this.renewAuto = renewAuto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTransferProtected() {
        return transferProtected;
    }

    public void setTransferProtected(boolean transferProtected) {
        this.transferProtected = transferProtected;
    }

    public boolean isExpirationProtected() {
        return expirationProtected;
    }

    public void setExpirationProtected(boolean expirationProtected) {
        this.expirationProtected = expirationProtected;
    }

    public boolean isHoldRegistrar() {
        return holdRegistrar;
    }

    public void setHoldRegistrar(boolean holdRegistrar) {
        this.holdRegistrar = holdRegistrar;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public boolean isAlertExpired() {
        return isAlertExpired;
    }

    public void setAlertExpired(boolean alertExpired) {
        isAlertExpired = alertExpired;
    }
}
