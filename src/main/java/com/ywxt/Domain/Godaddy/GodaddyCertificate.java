package com.ywxt.Domain.Godaddy;

import com.ywxt.Annotation.MarkCloumn;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class GodaddyCertificate {

    private int id;
    private String accessKeyId;
    @MarkCloumn
    private String certificateId;
    private Date createdAt;
    private Date validStart;
    private Date validEnd;
    private String productType;
    private String serialNumberHex;
    // 子subject集
    private String subjectAlternativeNames;
    private String locale;
    private String commonName;
    private String serialNumber;
    private String productGuid;
    // 状态ISSUED
    private String certificateStatus;
    // 是否报警
    private boolean isAlertExpired = false;
    // 数据是否弃用
    private boolean isAlertMarked = false;


    public GodaddyCertificate() {
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

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getValidStart() {
        return validStart;
    }

    public void setValidStart(Date validStart) {
        this.validStart = validStart;
    }

    public Date getValidEnd() {
        return validEnd;
    }

    public void setValidEnd(Date validEnd) {
        this.validEnd = validEnd;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSerialNumberHex() {
        return serialNumberHex;
    }

    public void setSerialNumberHex(String serialNumberHex) {
        this.serialNumberHex = serialNumberHex;
    }

    public String getSubjectAlternativeNames() {
        return subjectAlternativeNames;
    }

    public void setSubjectAlternativeNames(String subjectAlternativeNames) {
        this.subjectAlternativeNames = subjectAlternativeNames;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public boolean isAlertExpired() {
        return isAlertExpired;
    }

    public void setAlertExpired(boolean alertExpired) {
        isAlertExpired = alertExpired;
    }

    public boolean isAlertMarked() {
        return isAlertMarked;
    }

    public void setAlertMarked(boolean alertMarked) {
        isAlertMarked = alertMarked;
    }
}
