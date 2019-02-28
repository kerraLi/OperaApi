package com.ywxt.Service.Monitor.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Monitor.MonitorPointDao;
import com.ywxt.Domain.Monitor.MonitorPoint;
import com.ywxt.Service.Monitor.MonitorPointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("monitorPointService")
public class MonitorPointServiceImpl implements MonitorPointService {

    @Resource
    private MonitorPointDao monitorPointDao;

    public int create(MonitorPoint monitorPoint) throws Exception {
        return monitorPointDao.create(monitorPoint);
    }

    public boolean remove(int id) throws Exception {
        return monitorPointDao.delete(id);
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        return true;
    }

    public MonitorPoint update(MonitorPoint monitorPoint) throws Exception {
        return monitorPointDao.update(monitorPoint);
    }

    public MonitorPoint getMonitorPoint(int id) throws Exception {
        return monitorPointDao.getMonitorPoint(id);
    }

    public MonitorPoint getMonitorPoint(String path) throws Exception {
        return monitorPointDao.getMonitorPoint(path);
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
        return monitorPointDao.getList(params);
    }

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<MonitorPoint> list = monitorPointDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", monitorPointDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }


}