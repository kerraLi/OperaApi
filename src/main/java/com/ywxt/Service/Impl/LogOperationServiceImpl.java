package com.ywxt.Service.Impl;

import com.ywxt.Dao.LogOperationDao;
import com.ywxt.Domain.LogOperation;
import com.ywxt.Service.LogOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("logOperationService")
public class LogOperationServiceImpl implements LogOperationService {

    @Resource
    private LogOperationDao logOperationDao;

    public int create(LogOperation logOperation) {
        return logOperationDao.save(logOperation);
    }
}
