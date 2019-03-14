package com.ywxt.Service.Log.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Log.LogOperationDao;
import com.ywxt.Domain.Log.LogOperation;
import com.ywxt.Service.Log.LogOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("logOperationService")
public class LogOperationServiceImpl implements LogOperationService {

    @Resource
    private LogOperationDao logOperationDao;

    public int create(LogOperation logOperation) {
        return logOperationDao.create(logOperation);
    }

    public LogOperation update(LogOperation logOperation) {
        return logOperationDao.update(logOperation);
    }

    public LogOperation getLogOperation(int id) {
        return logOperationDao.getLogOperation(id);
    }

    public LogOperation getLogOperation(String requestId) {
        return logOperationDao.getLogOperation(requestId);
    }

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        List<LogOperation> list = logOperationDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", logOperationDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

}
