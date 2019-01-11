package com.ywxt.Domain;

import javax.persistence.Entity;

@Entity
public class ParameterIgnore {

    private int id;
    // 忽略主体类型
    private String domain;
    // 忽略参照字段
    private String markKey;
    // 具体数据
    private String markValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMarkKey() {
        return markKey;
    }

    public void setMarkKey(String markKey) {
        this.markKey = markKey;
    }

    public String getMarkValue() {
        return markValue;
    }

    public void setMarkValue(String markValue) {
        this.markValue = markValue;
    }
}
