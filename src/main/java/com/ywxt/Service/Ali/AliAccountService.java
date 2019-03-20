package com.ywxt.Service.Ali;

import com.ywxt.Domain.Ali.AliAccount;

import java.util.List;

public interface AliAccountService {

    // 获取账户
    public AliAccount getAliAccount(int id);

    // 列表
    public List<AliAccount> getList();

    // 列表&校验金额
    public List<AliAccount> getList(boolean checkMoney) throws Exception;

    // 新增/修改
    public AliAccount saveAliAccount(AliAccount aliAccount) throws Exception;

    // 删除账号
    public void deleteAccount(int id);

    // 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception;
}
