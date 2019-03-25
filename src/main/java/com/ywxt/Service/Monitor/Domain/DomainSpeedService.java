package com.ywxt.Service.Monitor.Domain;

import com.alibaba.fastjson.JSONObject;

public interface DomainSpeedService {

    public JSONObject speedTest(String url) throws Exception;

    public JSONObject speedMonitor() throws Exception;
}
