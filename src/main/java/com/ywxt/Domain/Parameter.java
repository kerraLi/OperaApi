package com.ywxt.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Parameter {

    private int id;
    private String key;
    private String value;
    // fixed:常驻固定 || temp:临时
    private String status;
    private String introduce;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "\"key\"", nullable = false, unique = true)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
