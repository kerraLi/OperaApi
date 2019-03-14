package com.ywxt.Service.Monitor.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Monitor.MonitorDomainDao;
import com.ywxt.Domain.Monitor.MonitorDomain;
import com.ywxt.Service.Monitor.MonitorDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("monitorDomainService")
public class MonitorDomainServiceImpl implements MonitorDomainService {

    @Resource
    private MonitorDomainDao monitorDomainDao;

    public int create(MonitorDomain monitorDomain) throws Exception {
        return monitorDomainDao.create(monitorDomain);
    }

    public boolean remove(int id) throws Exception {
        return monitorDomainDao.delete(id);
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        return true;
    }

    public MonitorDomain update(MonitorDomain monitorDomain) throws Exception {
        return monitorDomainDao.update(monitorDomain);
    }

    public MonitorDomain getMonitorDomain(int id) throws Exception {
        return monitorDomainDao.getMonitorDomain(id);
    }

    public MonitorDomain getMonitorDomain(String path) throws Exception {
        return monitorDomainDao.getMonitorDomain(path);
    }

    public MonitorDomain save(MonitorDomain monitorDomain) throws Exception {
        if (monitorDomain.getId() == null) {
            int id = this.create(monitorDomain);
            return this.getMonitorDomain(id);
        } else {
            return this.update(monitorDomain);
        }
    }

    public List<MonitorDomain> getList(HashMap<String, Object> params) throws Exception {
        return monitorDomainDao.getList(params);
    }

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<MonitorDomain> list = monitorDomainDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", monitorDomainDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

}
