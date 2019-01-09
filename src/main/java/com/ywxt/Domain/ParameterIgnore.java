package com.ywxt.Domain;

import javax.persistence.Entity;

@Entity
public class ParameterIgnore {

    private int id;
    // 忽略主体类型
    private String domain;
    // 忽略参照字段
    private String column;
    // 具体数据
    private String ignore;

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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }
}
