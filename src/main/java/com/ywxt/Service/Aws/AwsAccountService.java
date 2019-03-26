package com.ywxt.Service.Aws;

import com.ywxt.Domain.Aws.AwsAccount;

import java.util.List;

public interface AwsAccountService {

    // 列表
    List<AwsAccount> getList();

    // 列表&设置密钥权限
    List<AwsAccount> getList(boolean isSpecialPermission);

    // 新增/修改
    AwsAccount saveAccount(AwsAccount awsAccount);

    // 删除账号
    void deleteAccount(int id);

}
