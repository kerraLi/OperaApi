package com.ywxt.Dao;

import com.ywxt.Domain.LogOperation;


public interface LogOperationDao {

    public abstract int create(LogOperation logOperation);

    public abstract LogOperation update(LogOperation logOperation);

    public abstract LogOperation getLogOperation(int id);

    public abstract LogOperation getLogOperation(String sessionId);

}
