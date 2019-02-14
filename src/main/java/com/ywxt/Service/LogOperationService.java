package com.ywxt.Service;

import com.ywxt.Domain.LogOperation;

public interface LogOperationService {

    public int create(LogOperation logOperation);

    public LogOperation update(LogOperation logOperation);

    public LogOperation getLogOperation(int id);

    public LogOperation getLogOperation(String sessionId);

}
