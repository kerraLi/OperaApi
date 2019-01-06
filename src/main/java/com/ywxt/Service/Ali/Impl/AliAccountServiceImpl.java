package com.ywxt.Service.Ali.Impl;

import com.ywxt.Dao.AliAccountDao;
import com.ywxt.Dao.Impl.AliAccountDaoImpl;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Service.Ali.AliAccountService;

import java.util.List;

public class AliAccountServiceImpl implements AliAccountService {

    private AliAccountDao aliAccountDao = new AliAccountDaoImpl();

    // 列表
    public List<AliAccount> getList() {
        return this.aliAccountDao.getAliAccounts();
    }

    // 新增/修改
    public int saveAliAccount(AliAccount aliAccount) {
        //todo 更新ali数据源
        return this.aliAccountDao.saveAliAccount(aliAccount);
    }

    // 删除账号
    public boolean deleteAccount(int aliAccountId) {
        //todo 更新ali数据源
        return this.aliAccountDao.deleteAliAccount(aliAccountId);
    }
}
