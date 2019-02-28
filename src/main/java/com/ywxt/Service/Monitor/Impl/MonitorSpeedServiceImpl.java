package com.ywxt.Service.Monitor.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.MonitorPoint;
import com.ywxt.Service.Monitor.MonitorPointService;
import com.ywxt.Service.Monitor.MonitorSpeedService;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service("monitorSpeedService")
public class MonitorSpeedServiceImpl implements MonitorSpeedService {

    @Resource
    private MonitorPointService monitorPointService;

    public JSONObject connPoints(String url) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", "normal");
        List<MonitorPoint> points = monitorPointService.getList(params);
        String unique = UUID.randomUUID().toString().replace("-", "");
        JSONObject object = new JSONObject();
        object.put("code", unique);
        object.put("url", url);
        object.put("points", points);
        new RedisUtils().getJedis().setex(Parameter.redisKeyMonitorSpeed.replace("{code}", unique), Parameter.redisTllMonitorSpeed, object.toJSONString());
        return object;
    }

}
