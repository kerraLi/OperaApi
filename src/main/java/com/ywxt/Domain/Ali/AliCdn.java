package com.ywxt.Domain.Ali;


import com.aliyuncs.cdn.model.v20141111.DescribeUserDomainsResponse;
import com.ywxt.Annotation.MarkCloumn;
import com.ywxt.Annotation.NotFilterCloumn;

import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class AliCdn {

    private int id;
    @NotFilterCloumn
    private String userName;
    private String accessKeyId;
    private String cdnType;
    private String sourceType;
    @MarkCloumn
    private String domainName;
    private String cname;
    private Date gmtModified;
    private Date gmtCreated;
    private String description;
    private String resourceGroupId;
    private String domainStatus;
    private Boolean isAlertMarked = false;

    public AliCdn() {
    }

    public AliCdn(String accessKeyId, DescribeUserDomainsResponse.PageData pageData) throws Exception {
        this.accessKeyId = accessKeyId;
        this.cdnType = pageData.getCdnType();
        this.domainName = pageData.getDomainName();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.gmtModified = df.parse(pageData.getGmtModified().replace("Z", " UTC"));
        this.gmtCreated = df.parse(pageData.getGmtCreated().replace("Z", " UTC"));
        this.description = pageData.getDescription();
        this.resourceGroupId = pageData.getResourceGroupId();
        this.sourceType = pageData.getSourceType();
        this.cname = pageData.getCname();
        this.domainStatus = pageData.getDomainStatus();
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCdnType() {
        return cdnType;
    }

    public void setCdnType(String cdnType) {
        this.cdnType = cdnType;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public Boolean getAlertMarked() {
        return isAlertMarked;
    }

    public void setAlertMarked(Boolean alertMarked) {
        isAlertMarked = alertMarked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }
}
