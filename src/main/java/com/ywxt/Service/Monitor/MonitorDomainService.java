package com.ywxt.Service.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.MonitorDomain;

import java.util.HashMap;
import java.util.List;

public interface MonitorDomainService {

    public int create(MonitorDomain monitorDomain) throws Exception;

    public boolean remove(int id) throws Exception;

    public boolean removeAll(Integer[] ids) throws Exception;

    public MonitorDomain update(MonitorDomain monitorDomain) throws Exception;

    public MonitorDomain getMonitorDomain(int id) throws Exception;

    public MonitorDomain getMonitorDomain(String path) throws Exception;

    public MonitorDomain save(MonitorDomain monitorDomain) throws Exception;

    public List<MonitorDomain> getList(HashMap<String, Object> params) throws Exception;

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;
}
