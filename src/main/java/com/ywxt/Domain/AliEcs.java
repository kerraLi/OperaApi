package com.ywxt.Domain;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class AliEcs {
    private int id;
    private String accessKeyId;
    private Boolean isAlertExpired = false;
    private String instanceId;
    private String instanceName;
    private String instanceType;
    private String instanceNetworkType;
    // ?
    private String instanceChargeType;
    private String hostName;
    private String imageId;
    private String regionId;
    private String zoneId;
    private Date creationTime;
    private Date expiredTime;
    // 内外ip '|'分离
    private String innerIps;
    // 外网ip '|'分离
    private String publicIps;
    // 安全组 '|'分离
    private String securityGroupIds;
    private String serialNumber;
    // 状态
    private String status;

    public AliEcs(String accessKeyId, DescribeInstancesResponse.Instance instance) throws Exception {
        this.accessKeyId = accessKeyId;
        this.instanceId = instance.getInstanceId();
        this.instanceName = instance.getInstanceName();
        this.instanceType = instance.getInstanceType();
        this.instanceNetworkType = instance.getInstanceNetworkType();
        this.instanceChargeType = instance.getInstanceChargeType();
        this.hostName = instance.getHostName();
        this.imageId = instance.getImageId();
        this.regionId = instance.getRegionId();
        this.zoneId = instance.getZoneId();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.creationTime = df.parse(instance.getCreationTime().replace("Z", " UTC"));
        this.expiredTime = df.parse(instance.getExpiredTime().replace("Z", " UTC"));
        this.innerIps = String.join("|", instance.getInnerIpAddress());
        this.publicIps = String.join("|", instance.getPublicIpAddress());
        this.securityGroupIds = String.join("|", instance.getSecurityGroupIds());
        this.serialNumber = instance.getSerialNumber();
        this.status = instance.getStatus();
//        Field[] fields = instance.getClass().getDeclaredFields();
//        for (int i = 0; i < fields.length; i++) {
//            fields[i].setAccessible(true);
//            Object value = fields[i].get(instance);
//            if (null != value) {
//                fields[i].set(this, value);
//            }
//            fields[i].setAccessible(false);
//        }
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

    public Boolean getAlertExpired() {
        return isAlertExpired;
    }

    public void setAlertExpired(Boolean alertExpired) {
        isAlertExpired = alertExpired;
    }


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceNetworkType() {
        return instanceNetworkType;
    }

    public void setInstanceNetworkType(String instanceNetworkType) {
        this.instanceNetworkType = instanceNetworkType;
    }

    public String getInstaceChargeType() {
        return instanceChargeType;
    }

    public void setInstaceChargeType(String instaceChargeType) {
        this.instanceChargeType = instaceChargeType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getInnerIps() {
        return innerIps;
    }

    public void setInnerIps(String innerIps) {
        this.innerIps = innerIps;
    }

    public String getPublicIps() {
        return publicIps;
    }

    public void setPublicIps(String publicIps) {
        this.publicIps = publicIps;
    }

    public String getSecurityGroupIds() {
        return securityGroupIds;
    }

    public void setSecurityGroupIds(String securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
