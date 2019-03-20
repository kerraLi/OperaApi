package com.ywxt.Domain.System;

import javax.persistence.*;

@Entity
@Table(name = "system_ignore")
public class Ignore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String domain;
    @Column(nullable = false)
    private String markKey;
    @Column(nullable = false)
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
