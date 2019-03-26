package com.ywxt.Domain.Godaddy;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "godaddy_account")
@DynamicInsert
@DynamicUpdate
public class GodaddyAccount {

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
    private Boolean isHiddenSecrete = false;


    public GodaddyAccount() {

    }

    public GodaddyAccount(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

}
