package com.ywxt.Domain.Monitor.Domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "monitor_domain_domain")
public class MonitorDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String path;
    private String name = "";
    private String status = "normal";

}