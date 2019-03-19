package com.ywxt.Domain.ServerManage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 配置管理实体类
 */
@Data
@Entity
@Table(name = "manage_ConfigManage")
public class ConfigManage implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 50)
    private String id;
    private Long serverId;//服务器id
    @Column(length = 20)
    private String fileType;//配置类型
    @Column(length = 200)
    private String fileName;//配置名字
    @Column(length = 1)
    private Integer state=2;//配置上传状态 0：失败 1：成功 2:执行中
    @Column(length = 1)
    private Integer runResult=2;//配置运行结果 0：失败 1：成功 2:执行中
    @Column(columnDefinition="TEXT")
    private String content;//内容
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;//修改时间
}
