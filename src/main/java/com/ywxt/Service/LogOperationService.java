package com.ywxt.Service;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.LogOperation;

import java.util.HashMap;

public interface LogOperationService {

    public int create(LogOperation logOperation);

    public LogOperation update(LogOperation logOperation);

    public LogOperation getLogOperation(int id);

    public LogOperation getLogOperation(String sessionId);

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize);
}
