package com.ywxt.Dao.Monitor.Domain;

import com.ywxt.Domain.Monitor.Domain.MonitorPoint;

import java.util.HashMap;
import java.util.List;

public interface DomainPointDao {

    public int create(MonitorPoint monitorPoint);

    public boolean delete(int id);

    public boolean delete(MonitorPoint monitorPoint);

    public MonitorPoint update(MonitorPoint monitorPoint);

    public MonitorPoint getMonitorPoint(int id);

    public MonitorPoint getMonitorPoint(String path);

    public List<MonitorPoint> getList(HashMap<String, Object> params);

    public List<MonitorPoint> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);

}
