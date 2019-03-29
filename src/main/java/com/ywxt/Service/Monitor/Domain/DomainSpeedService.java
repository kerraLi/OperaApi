package com.ywxt.Service.Monitor.Domain;

import com.alibaba.fastjson.JSONObject;

public interface DomainSpeedService {

    JSONObject speedTest(String url) throws Exception;

    JSONObject speedMonitor() throws Exception;
}
