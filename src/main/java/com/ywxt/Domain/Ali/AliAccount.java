package com.ywxt.Domain.Ali;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ali_account")
@DynamicInsert
@DynamicUpdate
public class AliAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String accessKeyId;
    private String accessKeySecret;
    // status:正常使用normal/账号异常invalid（取数据错误）
    private String status = "invalid";
    @Transient
    private QueryAccountBalanceResponse.Data balanceData;
    @Transient
    private Boolean isAlertBalance = false;
    @Transient
    private Boolean isAlertMarked = false;
    @Transient
    private Boolean isHiddenSecrete = false;

    public AliAccount() {
    }

    public AliAccount(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

}
