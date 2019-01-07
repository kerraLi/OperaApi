package com.ywxt.Service.Ali;

import com.ywxt.Domain.AliAccount;

import java.util.List;

public interface AliAccountService {

    public abstract List<AliAccount> getList();

    public abstract int saveAliAccount(AliAccount aliAccount);

    public abstract boolean deleteAccount(int aliAccountId);
}
