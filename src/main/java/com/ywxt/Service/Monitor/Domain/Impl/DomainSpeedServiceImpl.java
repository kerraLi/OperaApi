package com.ywxt.Service.Monitor.Domain.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.Domain.MonitorDomain;
import com.ywxt.Domain.Monitor.Domain.MonitorPoint;
import com.ywxt.Service.Monitor.Domain.DomainDomainService;
import com.ywxt.Service.Monitor.Domain.DomainPointService;
import com.ywxt.Service.Monitor.Domain.DomainSpeedService;
import com.ywxt.Utils.Parameter;
import com.ywxt.Service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class DomainSpeedServiceImpl implements DomainSpeedService {

    @Resource
    private DomainPointService domainPointService;
    @Resource
    private DomainDomainService domainDomainService;
    @Value("${redis.ttl.monitor}")
    private int redisTllMonitorSpeed;
    @Resource
    private RedisService redisService;

    public JSONObject speedTest(String url) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", "normal");
        List<MonitorPoint> points = domainPointService.getList(params);
        String unique = UUID.randomUUID().toString().replace("-", "");
        JSONObject object = new JSONObject();
        object.put("code", unique);
        object.put("url", url);
        object.put("points", points);
        redisService.set(Parameter.redisKeyMonitorSpeed.replace("{code}", unique), object.toJSONString(), redisTllMonitorSpeed);
        return object;
    }

    // 域名监控：不存储域名；域名在redis中节点直接读取
    public JSONObject speedMonitor() throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", "normal");
        List<MonitorPoint> points = domainPointService.getList(params);
        List<MonitorDomain> domains = domainDomainService.getList(params);
        String unique = UUID.randomUUID().toString().replace("-", "");
        JSONObject object = new JSONObject();
        object.put("code", unique);
        object.put("points", points);
        redisService.set(Parameter.redisKeyMonitorSpeed.replace("{code}", unique), object.toJSONString(), redisTllMonitorSpeed);
        return object;
    }

}
