package com.ywxt.Service.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.MonitorPoint;

import java.util.List;

public interface MonitorSpeedService {

    public JSONObject connPoints(String url) throws Exception;
}
