package com.ywxt.Domain.Ali;
import com.aliyuncs.scdn.model.v20171115.DescribeScdnUserDomainsResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@Entity
@Table(name="ali_scdn")
public class AliScdn implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String userName;
    private String accessKeyId;
    @Column(length = 50)
    private String domainName;//域名
    @Column(length = 50)
    private String cname;//CNAME域名
    private String domainStatus;//域名状态
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date gmtCreated;//加速域名创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date gmtModified;//加速域名修改时间
    private String description;//描述
    @Column(length = 10)
    private String sslProtocol;//https开关， on已开启；off未开启
    @Column(length = 50)
    private String resourceGroupId;//资源组id
    @Column(length = 1)
    private String abandon = "0";//是否弃用数据 0：没有弃用  1：已经弃用

    public AliScdn(){}

    public AliScdn(AliAccount account, DescribeScdnUserDomainsResponse.PageData data) throws ParseException {
        this.userName = account.getUserName();
        this.accessKeyId = account.getAccessKeyId();
        this.domainName = data.getDomainName();
        this.cname = data.getCname();
        this.domainStatus = data.getDomainStatus();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.gmtCreated = df.parse(data.getGmtCreated().replace("Z", " UTC"));
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm Z");
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.gmtModified = df1.parse(data.getGmtModified().replace("Z", " UTC"));
        this.description = data.getDescription();
        this.sslProtocol = data.getSSLProtocol();
        this.resourceGroupId = data.getResourceGroupId();
    }
}
