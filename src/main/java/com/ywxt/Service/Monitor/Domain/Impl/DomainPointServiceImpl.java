package com.ywxt.Service.Monitor.Domain.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Monitor.Domain.DomainPointDao;
import com.ywxt.Domain.Monitor.Domain.MonitorPoint;
import com.ywxt.Service.Monitor.Domain.DomainPointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class DomainPointServiceImpl implements DomainPointService {

    @Resource
    private DomainPointDao domainPointDao;

    public int create(MonitorPoint monitorPoint) throws Exception {
        return domainPointDao.create(monitorPoint);
    }

    public boolean remove(int id) throws Exception {
        return domainPointDao.delete(id);
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        return true;
    }

    public MonitorPoint update(MonitorPoint monitorPoint) throws Exception {
        return domainPointDao.update(monitorPoint);
    }

    public MonitorPoint getMonitorPoint(int id) throws Exception {
        return domainPointDao.getMonitorPoint(id);
    }

    public MonitorPoint getMonitorPoint(String path) throws Exception {
        return domainPointDao.getMonitorPoint(path);
    }

    public MonitorPoint save(MonitorPoint monitorPoint) throws Exception {
        if (monitorPoint.getId() == null) {
            int id = this.create(monitorPoint);
            return this.getMonitorPoint(id);
        } else {
            return this.update(monitorPoint);
        }
    }

    public List<MonitorPoint> getList(HashMap<String, Object> params) throws Exception {
        return domainPointDao.getList(params);
    }

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<MonitorPoint> list = domainPointDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", domainPointDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }


}