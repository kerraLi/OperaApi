package com.ywxt.Service.Log;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Log.LogOperation;

import java.util.HashMap;

public interface LogOperationService {

    public int create(LogOperation logOperation);

    public LogOperation update(LogOperation logOperation);

    public LogOperation getLogOperation(int id);

    public LogOperation getLogOperation(String requestId);

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize);
}
