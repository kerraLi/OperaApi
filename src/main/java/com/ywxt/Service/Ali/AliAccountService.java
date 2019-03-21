package com.ywxt.Service.Ali;

import com.ywxt.Domain.Ali.AliAccount;

import java.util.List;

public interface AliAccountService {

    // 获取账户
    AliAccount getAliAccount(int id);

    // 列表
    List<AliAccount> getList();

    // 列表&校验金额
    List<AliAccount> getList(boolean checkMoney) throws Exception;

    // 新增/修改
    AliAccount saveAliAccount(AliAccount aliAccount) throws Exception;

    // 删除账号
    void deleteAccount(int id);

}
