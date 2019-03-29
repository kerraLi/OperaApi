package com.ywxt.Command;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.Domain.MonitorPoint;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class Websocket extends TextWebSocketHandler {
    //记录当前在线链接
    public static Map<Long, Websocket> userSocket = new HashMap<>();
    public static Map<String, HashMap<Long, Websocket>> featureSocket = new HashMap<>();

    //日志记录
    private static Logger logger = LoggerFactory.getLogger(Websocket.class);

    @Autowired
    private RedisService redisService;
    private WebSocketSession session;
    private Long userId;

    // 建立连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        String[] path = session.getUri().getPath().split("/");
        if (path.length < 3 || path[2].isEmpty()) {
            throw new Exception("链接信息错误");
        }
        userId = Long.parseLong(path[2]);
        //根据该用户当前是否已经在别的终端登录进行添加操作
        if (userSocket.containsKey(this.userId)) {
            logger.debug("当前用户id:{}已有其他终端登录", this.userId);
        } else {
            logger.debug("当前用户id:{}第一个终端登录", this.userId);
            userSocket.put(this.userId, this);
        }
        logger.debug("当前在线用户数为：{}", userSocket.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.debug("收到来自用户id为：{}的消息：{}", this.userId, message);
        String msg = message.getPayload().toString();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String action = (String) jsonObject.get("action");
        // 订阅message
        if (action.equals("message")) {
            this.subscription("message");
            session.sendMessage(this.getJsonInfo(action, "start", "subscription:" + action));
        }
        // 订阅speed
        if (action.equals("speed-test")) {
            String code = (String) jsonObject.get("code");
            this.subscription("speed-test-" + code);
            session.sendMessage(this.getJsonInfo(action, "start", "subscription:" + action));
            this.speedTest(code);
        }
        // 订阅speed-monitor
        if (action.equals("speed-monitor")) {
            String code = (String) jsonObject.get("code");
            this.subscription("speed-monitor-" + code);
            session.sendMessage(this.getJsonInfo(action, "start", "subscription:" + action));
            this.speedMonitor(code);
        }
        if (session == null) logger.debug("session null");
    }

    // 处理error
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.debug("用户id为：{}的连接发送错误", this.userId);
        exception.printStackTrace();
    }

    // 关闭连接
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //移除当前用户终端登录的websocket信息,如果该用户的所有终端都下线了，则删除该用户的记录
        userSocket.remove(this.userId);
        this.unSubscription("message");
        logger.debug("当前在线用户数为：{}", userSocket.size());
    }

    // 是否处理分片消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 订阅
    private void subscription(String action) {
        if (featureSocket.containsKey(action)) {
            featureSocket.get(action).put(userId, this);
        } else {
            HashMap<Long, Websocket> temp = new HashMap<Long, Websocket>();
            temp.put(userId, this);
            featureSocket.put(action, temp);
        }
    }

    // 取消订阅
    private void unSubscription(String action) {
        if (featureSocket.containsKey(action)) {
            featureSocket.get(action).remove(userId);
            if (featureSocket.size() == 0) {
                featureSocket.remove(action);
            }
        }
    }

    // speed-test订阅
    private void speedTest(String code) throws IOException {
        // speed test
        String speedInfo = redisService.get(Parameter.redisKeyMonitorSpeed.replace("{code}", code));
        if (speedInfo == null) {
            session.sendMessage(this.getSpeedJsonInfo("speed-test", 0, "end", ""));
            return;
        }
        JSONObject infoJson = JSONObject.parseObject(speedInfo);
        String url = (String) infoJson.get("url");
        JSONArray points = (JSONArray) infoJson.get("points");
        HashMap<String, Object> totalData = new HashMap<String, Object>();
        for (Object object : points) {
            MonitorPoint point = JSONObject.toJavaObject((JSONObject) object, MonitorPoint.class);
            String params = HttpUtils.getParamContext(new HashMap<String, String>() {{
                put("url", url);
                put("action", "speed_test");
            }});
            String msg = "";
            try {
                msg = HttpUtils.sendConnPost("http://" + point.getPath() + ":8888/speed", params);
            } catch (Exception e) {
                // 监控点未响应
                msg = "error";
            }
            session.sendMessage(this.getSpeedJsonInfo("speed-test", point.getId(), "new", msg));
        }
        session.sendMessage(this.getSpeedJsonInfo("speed-test", 0, "end", ""));
        // 取消订阅
        this.unSubscription("speed-test-" + code);
    }

    // speed-monitor订阅
    private void speedMonitor(String code) throws IOException {
        // speed monitor
        String speedInfo = redisService.get(Parameter.redisKeyMonitorSpeed.replace("{code}", code));
        if (speedInfo == null) {
            session.sendMessage(this.getSpeedJsonInfo("speed-monitor", 0, "end", ""));
            return;
        }
        JSONObject infoJson = JSONObject.parseObject(speedInfo);
        JSONArray urls = (JSONArray) infoJson.get("urls");
        JSONArray points = (JSONArray) infoJson.get("points");
        HashMap<String, Object> totalData = new HashMap<String, Object>();
        for (Object object : points) {
            MonitorPoint point = JSONObject.toJavaObject((JSONObject) object, MonitorPoint.class);
            String params = HttpUtils.getParamContext(new HashMap<String, String>() {{
                put("action", "speed_monitor");
            }});
            String msg = "";
            try {
                msg = HttpUtils.sendConnPost("http://" + point.getPath() + ":8888/speed", params);
            } catch (Exception e) {
                // 监控点未响应
                msg = "error";
            }
            session.sendMessage(this.getSpeedJsonInfo("speed-monitor", point.getId(), "new", msg));
        }
        session.sendMessage(this.getSpeedJsonInfo("speed-monitor", 0, "end", ""));
        // 取消订阅
        this.unSubscription("speed-monitor-" + code);
    }

    // message订阅-发送给所有用户
    public static void sendMessageToAllUser(String message) {
        if (featureSocket.containsKey("message")) {
            for (Map.Entry<Long, Websocket> entry : featureSocket.get("message").entrySet()) {
                Websocket ws = entry.getValue();
                logger.debug("sessionId为:{}", ws.session.getId());
                try {
                    ws.session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.debug(" 给用户id为：{}发送消息失败", ws.session.getId());
                }
            }
        }
    }

    // json-通用
    private TextMessage getJsonInfo(String action, String type, String msg) {
        String str = new JSONObject() {{
            put("Time", new Date().getTime());
            put("action", action);
            put("type", type);
            put("msg", msg);
        }}.toJSONString();
        return new TextMessage(str);
    }

    // json-speed-test
    private TextMessage getSpeedJsonInfo(String action, int pointId, String type, String out) {
        String str = new JSONObject() {{
            put("Time", new Date().getTime());
            put("action", action);
            put("pointId", pointId);
            put("type", type);
            put("result", out);
        }}.toJSONString();
        return new TextMessage(str);
    }

}
