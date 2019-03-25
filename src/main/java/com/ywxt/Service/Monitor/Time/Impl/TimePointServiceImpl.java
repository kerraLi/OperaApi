package com.ywxt.Service.Monitor.Time.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.Time.MonitorPoint;
import com.ywxt.Service.Monitor.Time.TimePointService;
import com.ywxt.Utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TimePointServiceImpl implements TimePointService {

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.api}")
    private String address;

    // 获取当前消息队列连接发布者
    public List<MonitorPoint> getList() throws Exception {
        String url = address + "/api/channels";
        String chanStr = HttpUtils.sendHttpGetWithBasicAuth(username, password, url, new ArrayList<NameValuePair>(), new HashMap<String, String>());
        List<MonitorPoint> list = new ArrayList<>();
        List<JSONObject> chanList = JSONObject.parseArray(chanStr, JSONObject.class);
        for (JSONObject chan : chanList) {
            JSONObject msgStats = JSONObject.parseObject(chan.get("message_stats").toString());
            if (msgStats.containsKey("publish")) {
                MonitorPoint mp = new MonitorPoint();
                // conn info
                JSONObject conn = JSONObject.parseObject(chan.get("connection_details").toString());
                mp.setName(conn.getString("name"));
                mp.setHost(conn.getString("peer_host"));
                mp.setPort(conn.getInteger("peer_port"));
                // state
                mp.setState(chan.getString("state"));
                // publish
                mp.setPublish(msgStats.getInteger("publish"));
                list.add(mp);
            }
        }
        return list;
    }
}
