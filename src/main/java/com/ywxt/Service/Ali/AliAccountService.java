package com.ywxt.Service.Ali;

import com.ywxt.Domain.Ali.AliAccount;

import java.util.HashMap;
import java.util.List;

public interface AliAccountService {

    // 获取账户
    public AliAccount getAliAccount(int id);

    // 获取总数
    public int getTotal(HashMap<String, Object> params) throws Exception;

    // 列表
    public List<AliAccount> getList();

    // 列表&校验金额
    public List<AliAccount> getList(boolean checkMoney) throws Exception;

    // 新增/修改
    public int saveAliAccount(AliAccount aliAccount) throws Exception;

    // 删除账号
    public boolean deleteAccount(int aliAccountId);

    // 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception;
}
