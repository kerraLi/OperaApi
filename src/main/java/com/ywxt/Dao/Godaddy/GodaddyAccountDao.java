package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyAccount;

import java.util.List;

public interface GodaddyAccountDao {
    public abstract GodaddyAccount getAccount(int id);

    public abstract GodaddyAccount getAccount(String accessKeyId);

    public abstract List<GodaddyAccount> getAccountsNormal();

    public abstract List<GodaddyAccount> getAccounts();

    public abstract int saveAccount(GodaddyAccount account);

    public abstract boolean deleteAccount(int accountId);

}
