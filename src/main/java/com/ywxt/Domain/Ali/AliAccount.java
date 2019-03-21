package com.ywxt.Domain.Ali;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.ywxt.Annotation.MarkCloumn;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ali_account")
public class AliAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String userName;
    @NotBlank
    private String accessKeyId;
    @NotBlank
    private String accessKeySecret;
    // status:正常使用normal/账号异常invalid（取数据错误）
    private String status = "invalid";
    @Transient
    private QueryAccountBalanceResponse.Data balanceData;
    @Transient
    private Boolean isAlertBalance = false;
    @Transient
    private Boolean isAlertMarked = false;

    public AliAccount() {
    }

    public AliAccount(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

}
