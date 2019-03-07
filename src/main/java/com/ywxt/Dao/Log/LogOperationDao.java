package com.ywxt.Dao.Log;

import com.ywxt.Domain.Log.LogOperation;

import java.util.HashMap;
import java.util.List;


public interface LogOperationDao {

    public abstract int create(LogOperation logOperation);

    public abstract LogOperation update(LogOperation logOperation);

    public abstract LogOperation getLogOperation(int id);

    public abstract LogOperation getLogOperation(String requestId);

    public abstract List<LogOperation> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public abstract int getListTotal(HashMap<String, Object> params);
}
