package com.ywxt.Service.Monitor.Domain.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Monitor.Domain.DomainDomainDao;
import com.ywxt.Domain.Monitor.Domain.MonitorDomain;
import com.ywxt.Service.Monitor.Domain.DomainDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class DomainDomainServiceImpl implements DomainDomainService {

    @Resource
    private DomainDomainDao domainDomainDao;

    public int create(MonitorDomain monitorDomain) throws Exception {
        return domainDomainDao.create(monitorDomain);
    }

    public boolean remove(int id) throws Exception {
        return domainDomainDao.delete(id);
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        return true;
    }

    public MonitorDomain update(MonitorDomain monitorDomain) throws Exception {
        return domainDomainDao.update(monitorDomain);
    }

    public MonitorDomain getMonitorDomain(int id) throws Exception {
        return domainDomainDao.getMonitorDomain(id);
    }

    public MonitorDomain getMonitorDomain(String path) throws Exception {
        return domainDomainDao.getMonitorDomain(path);
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
        return domainDomainDao.getList(params);
    }

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<MonitorDomain> list = domainDomainDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", domainDomainDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

}
