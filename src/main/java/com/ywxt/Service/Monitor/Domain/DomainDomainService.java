package com.ywxt.Service.Monitor.Domain;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.Domain.MonitorDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface DomainDomainService {

    void upload(ArrayList<MonitorDomain> list);

    int create(MonitorDomain monitorDomain) throws Exception;

    boolean remove(int id) throws Exception;

    boolean removeAll(Integer[] ids) throws Exception;

    MonitorDomain update(MonitorDomain monitorDomain) throws Exception;

    MonitorDomain getMonitorDomain(int id) throws Exception;

    MonitorDomain getMonitorDomain(String path) throws Exception;

    MonitorDomain save(MonitorDomain monitorDomain) throws Exception;

    List<MonitorDomain> getList(HashMap<String, Object> params) throws Exception;

    JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;
}
