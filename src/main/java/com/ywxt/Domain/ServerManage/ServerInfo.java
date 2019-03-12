package com.ywxt.Domain.ServerManage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 服务器信息实体类
 */
@Data
@Entity
@Table(name = "manage_serverInfo")
public class ServerInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;
    @Column(name = "ip", length = 50)
    private String ip;//IP地址
    @Column(name = "name", length = 200)
    private String name;//名称
    @Column(name = "state", length = 10)
    private Byte state = 0;//状态 0:关闭 1：开启
    @Column(name = "Operator", length = 200)
    private String operator;//运营商
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;//修改时间
}
