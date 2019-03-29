package com.ywxt.Service.Monitor.Domain.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Monitor.Domain.DomainDomainDao;
import com.ywxt.Domain.Monitor.Domain.MonitorDomain;
import com.ywxt.Service.Monitor.Domain.DomainDomainService;
import com.ywxt.Service.RedisService;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DomainDomainServiceImpl implements DomainDomainService {

    @Resource
    private DomainDomainDao domainDomainDao;
    @Resource
    private RedisService redisService;

    public void upload(ArrayList<MonitorDomain> list) {
        for (MonitorDomain monitorDomain : list) {
            domainDomainDao.create(monitorDomain);
        }
        refreshCache();
    }

    public int create(MonitorDomain monitorDomain) throws Exception {
        int id = domainDomainDao.create(monitorDomain);
        refreshCache();
        return id;
    }

    public boolean remove(int id) throws Exception {
        boolean bo = domainDomainDao.delete(id);
        refreshCache();
        return bo;
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        refreshCache();
        return true;
    }

    public MonitorDomain update(MonitorDomain monitorDomain) throws Exception {
        MonitorDomain monitorDomain1 = domainDomainDao.update(monitorDomain);
        refreshCache();
        return monitorDomain1;
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
            refreshCache();
            return this.getMonitorDomain(id);
        } else {
            refreshCache();
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

    // 刷新缓存中数据
    private void refreshCache() {
        List<MonitorDomain> list = domainDomainDao.getList(new HashMap<>());
        String[] domains = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            domains[i] = list.get(i).getPath();
        }
        System.out.println(JSONObject.toJSON(domains));
        redisService.set(Parameter.redisKeyMonitorDomains, URLEncoder.encode(JSONObject.toJSON(domains).toString()));
    }

}
