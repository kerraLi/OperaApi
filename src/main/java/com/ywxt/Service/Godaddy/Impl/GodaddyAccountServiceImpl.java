package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyAccountDao;
import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Domain.GodaddyAccount;
import com.ywxt.Service.Godaddy.GodaddyAccountService;

import java.util.List;

public class GodaddyAccountServiceImpl implements GodaddyAccountService {

    private GodaddyAccountDao godaddyAccountDao = new GodaddyAccountDaoImpl();

    // 列表
    public List<GodaddyAccount> getList() {
        return this.godaddyAccountDao.getAccounts();
    }

    // 新增/修改
    public int saveAccount(GodaddyAccount godaddyAccount) throws Exception {
        // check ali key
        if (this.checkAccount(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret())) {
            godaddyAccount.setStatus("normal");
            // todo update ali Data
            // new AliServiceImpl(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret()).freshSourceData();
        } else {
            godaddyAccount.setStatus("invalid");
        }
        return this.godaddyAccountDao.saveAccount(godaddyAccount);
    }

    // 删除账号
    public boolean deleteAccount(int godaddyAccountId) {
        // update ali Data
        GodaddyAccount aliAccount = this.godaddyAccountDao.getAccount(godaddyAccountId);
        if (aliAccount.getStatus().equals("normal")) {
            // todo 删除ecs
            // new AliEcsDaoImpl().deleteAliEcsByAccessId(aliAccount.getAccessKeyId());
            // todo 删除cdn
            // new AliCdnDaoImpl().deleteAliCdnByAccessId(aliAccount.getAccessKeyId());
        }
        return this.godaddyAccountDao.deleteAccount(godaddyAccountId);
    }

    // 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
        // todo
        return true;
    }

}
