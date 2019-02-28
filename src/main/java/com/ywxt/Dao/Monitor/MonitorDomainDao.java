package com.ywxt.Dao.Monitor;

import com.ywxt.Domain.Monitor.MonitorDomain;

import java.util.HashMap;
import java.util.List;

public interface MonitorDomainDao {

    public int create(MonitorDomain monitorDomain);

    public boolean delete(int id);

    public boolean delete(MonitorDomain monitorDomain);

    public MonitorDomain update(MonitorDomain monitorDomain);

    public MonitorDomain getMonitorDomain(int id);

    public MonitorDomain getMonitorDomain(String path);

    public List<MonitorDomain> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);
}
