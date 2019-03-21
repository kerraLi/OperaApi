package com.ywxt.Service.Aws;

import com.ywxt.Domain.Aws.AwsAccount;

import java.util.List;

public interface AwsAccountService {

    // 列表
    public List<AwsAccount> getList();

    // 新增/修改
    public AwsAccount saveAccount(AwsAccount awsAccount);

    // 删除账号
    public void deleteAccount(int id);

}
