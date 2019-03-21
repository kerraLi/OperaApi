package com.ywxt.Service.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyAccount;

import java.util.List;

public interface GodaddyAccountService {
    // 列表
    List<GodaddyAccount> getList();

    // 新增/修改
    GodaddyAccount saveAccount(GodaddyAccount godaddyAccount) throws Exception;

    // 删除账号
    void deleteAccount(int id);
}
