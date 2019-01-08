package com.ywxt.Dao;

import com.ywxt.Domain.AliAccount;

import java.util.List;

public interface AliAccountDao {
    public abstract AliAccount getAliAccount(int id);

    public abstract AliAccount getAliAccount(String accessKeyId);

    public abstract List<AliAccount> getAliAccountsNormal();

    public abstract List<AliAccount> getAliAccounts();

    public abstract int saveAliAccount(AliAccount aliAccount);

    public abstract boolean deleteAliAccount(int aliAccountId);
}