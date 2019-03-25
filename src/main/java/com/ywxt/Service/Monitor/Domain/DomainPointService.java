package com.ywxt.Service.Monitor.Domain;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.Domain.MonitorPoint;

import java.util.HashMap;
import java.util.List;

public interface DomainPointService {

    public int create(MonitorPoint monitorPoint) throws Exception;

    public boolean remove(int id) throws Exception;

    public boolean removeAll(Integer[] ids) throws Exception;

    public MonitorPoint update(MonitorPoint monitorPoint) throws Exception;

    public MonitorPoint getMonitorPoint(int id) throws Exception;

    public MonitorPoint getMonitorPoint(String path) throws Exception;

    public MonitorPoint save(MonitorPoint monitorPoint) throws Exception;

    public List<MonitorPoint> getList(HashMap<String, Object> params) throws Exception;

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

}
