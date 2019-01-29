package com.ywxt.Domain.Resource;

public class Hardware {
    private int id;
    // 账号
    private String username;
    // 密码
    private String password;
    // 机柜
    private String cabinet;
    // 型号
    private String model;
    // 资产编号
    private String assetNumber;
    // sn
    private String sn;
    // 操作系统
    private String operateSystem;
    // 作用
    private String effect;
    // 外网/专线ip
    private String externalIp;
    // 外网主线
    private String externalMainLine;
    // 外网备线
    private String externalBackupLine;
    // 内网ip
    private String intranetIp;
    // 管理卡ip
    private String manageCardIp;
    // 掩码
    private String mask;
    // 远程端口
    private String remotePort;
    // 内网端口（本柜）
    private String intranetPort;
    // 管理端口（本柜）
    private String managePort;
    // 外网/专线端口
    private String externalPort;
    // CPU
    private String cpu;
    // 内存
    private String memory;
    // RAID
    private String raid;
    // 硬盘
    private String hardDisk;
    // 电源
    private String power;
    // U数
    private String uNumber;
    // 备注
    private String remark;
    // normal
    private String status;

    public Hardware() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOperateSystem() {
        return operateSystem;
    }

    public void setOperateSystem(String operateSystem) {
        this.operateSystem = operateSystem;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getExternalIp() {
        return externalIp;
    }

    public void setExternalIp(String externalIp) {
        this.externalIp = externalIp;
    }

    public String getExternalMainLine() {
        return externalMainLine;
    }

    public void setExternalMainLine(String externalMainLine) {
        this.externalMainLine = externalMainLine;
    }

    public String getExternalBackupLine() {
        return externalBackupLine;
    }

    public void setExternalBackupLine(String externalBackupLine) {
        this.externalBackupLine = externalBackupLine;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getManageCardIp() {
        return manageCardIp;
    }

    public void setManageCardIp(String manageCardIp) {
        this.manageCardIp = manageCardIp;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntranetPort() {
        return intranetPort;
    }

    public void setIntranetPort(String intranetPort) {
        this.intranetPort = intranetPort;
    }

    public String getManagePort() {
        return managePort;
    }

    public void setManagePort(String managePort) {
        this.managePort = managePort;
    }

    public String getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(String externalPort) {
        this.externalPort = externalPort;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getRaid() {
        return raid;
    }

    public void setRaid(String raid) {
        this.raid = raid;
    }

    public String getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(String hardDisk) {
        this.hardDisk = hardDisk;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getuNumber() {
        return uNumber;
    }

    public void setuNumber(String uNumber) {
        this.uNumber = uNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
