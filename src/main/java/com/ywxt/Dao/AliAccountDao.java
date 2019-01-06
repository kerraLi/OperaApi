package com.ywxt.Dao;

import com.ywxt.Domain.AliAccount;

import java.util.List;

public interface AliAccountDao {
    public abstract List<AliAccount> getAliAccounts();

    public abstract int saveAliAccount(AliAccount aliAccount);

    public abstract boolean deleteAliAccount(int aliAccountId);
}