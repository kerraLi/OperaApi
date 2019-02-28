package com.ywxt.Domain.Aws;

import com.ywxt.Annotation.MarkCloumn;
import com.ywxt.Annotation.NotFilterColumn;
import software.amazon.awssdk.services.ec2.model.Instance;

import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class AwsEc2 {
    private int id;
    // 账号
    @NotFilterColumn
    private String userName;
    private String accessKeyId;
    private String region;
    private Date launchTime;
    private String imageId;
    @MarkCloumn
    private String instanceId;
    private String instanceType;
    private String kernelId;
    private String keyName;
    private String platform;
    private String privateDnsName;
    private String privateIpAddress;
    private String publicDnsName;
    private String publicIpAddress;
    private String ramdiskId;
    // 状态:running
    private String status;
    private String stateTransitionReason;
    private String subnetId;
    private String vpcId;
    private String architecture;
    private String rootDeviceName;
    private String rootDeviceType;

    public AwsEc2() {

    }

    public AwsEc2(String accessKeyId, String region, Instance instance) throws Exception {
        this.accessKeyId = accessKeyId;
        this.region = region;
        this.imageId = instance.imageId();
        this.instanceId = instance.instanceId();
        this.instanceType = instance.instanceType().name();
        this.kernelId = instance.kernelId();
        this.keyName = instance.keyName();
        // 源码bug：源码大小写异常=》无法解析"windows"
        // this.platform = instance.platform() == null ? null : instance.platform().name();
        this.platform = instance.platform() == null ? null : (instance.platform().name().equals("UNKNOWN_TO_SDK_VERSION") ? "Windows" : instance.platform().name());
        this.privateDnsName = instance.privateDnsName();
        this.privateIpAddress = instance.privateIpAddress();
        this.publicDnsName = instance.publicDnsName();
        this.publicIpAddress = instance.publicIpAddress();
        this.ramdiskId = instance.ramdiskId();
        this.status = instance.state().nameAsString();
        this.stateTransitionReason = instance.stateTransitionReason();
        this.subnetId = instance.subnetId();
        this.vpcId = instance.vpcId();
        this.architecture = instance.architecture().toString();
        this.rootDeviceName = instance.rootDeviceName();
        this.rootDeviceType = instance.rootDeviceType().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.launchTime = df.parse(instance.launchTime().toString().replace("Z", " UTC"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getKernelId() {
        return kernelId;
    }

    public void setKernelId(String kernelId) {
        this.kernelId = kernelId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrivateDnsName() {
        return privateDnsName;
    }

    public void setPrivateDnsName(String privateDnsName) {
        this.privateDnsName = privateDnsName;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getPublicDnsName() {
        return publicDnsName;
    }

    public void setPublicDnsName(String publicDnsName) {
        this.publicDnsName = publicDnsName;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public void setPublicIpAddress(String publicIpAddress) {
        this.publicIpAddress = publicIpAddress;
    }

    public String getRamdiskId() {
        return ramdiskId;
    }

    public void setRamdiskId(String ramdiskId) {
        this.ramdiskId = ramdiskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStateTransitionReason() {
        return stateTransitionReason;
    }

    public void setStateTransitionReason(String stateTransitionReason) {
        this.stateTransitionReason = stateTransitionReason;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public void setRootDeviceName(String rootDeviceName) {
        this.rootDeviceName = rootDeviceName;
    }

    public String getRootDeviceType() {
        return rootDeviceType;
    }

    public void setRootDeviceType(String rootDeviceType) {
        this.rootDeviceType = rootDeviceType;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
}

