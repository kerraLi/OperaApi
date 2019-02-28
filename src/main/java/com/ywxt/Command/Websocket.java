package com.ywxt.Command;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.MonitorPoint;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;


@ServerEndpoint(value = "/websocket/{userId}")
public class Websocket {
    //日志记录
    private Logger logger = LoggerFactory.getLogger(Websocket.class);

    //记录当前在线链接
    private static Map<Long, Websocket> userSocket = new HashMap<>();
    private static Map<String, HashMap<Long, Websocket>> featureSocket = new HashMap<>();

    private Session session;
    private Long userId;

    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) throws IOException {
        this.session = session;
        this.userId = userId;
        //根据该用户当前是否已经在别的终端登录进行添加操作
        if (userSocket.containsKey(this.userId)) {
            logger.debug("当前用户id:{}已有其他终端登录", this.userId);
        } else {
            logger.debug("当前用户id:{}第一个终端登录", this.userId);
            userSocket.put(this.userId, this);
        }
        logger.debug("当前在线用户数为：{}", userSocket.size());
    }

    @OnClose
    public void onClose() {
        //移除当前用户终端登录的websocket信息,如果该用户的所有终端都下线了，则删除该用户的记录
        userSocket.remove(this.userId);
        logger.debug("当前在线用户数为：{}", userSocket.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.debug("收到来自用户id为：{}的消息：{}", this.userId, message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        String action = (String) jsonObject.get("action");
        // 订阅message
        if (action.equals("message")) {
            this.subscription("message");
            this.session.getBasicRemote().sendText(this.getJsonInfo(action, "subscription"));
        }
        // 订阅speed
        if (action.equals("speed-test")) {
            this.subscription("speed-test");
            String code = (String) jsonObject.get("code");
            this.session.getBasicRemote().sendText(this.getJsonInfo(action, "subscription"));
            this.speedTest(code);
        }
        if (session == null) logger.debug("session null");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("用户id为：{}的连接发送错误", this.userId);
        error.printStackTrace();
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

    // message订阅-发送给所有用户
    public void sendMessageToAllUser(String message) {
        Map<Long, Websocket> map = featureSocket.get("message");
        for (Map.Entry<Long, Websocket> entry : map.entrySet()) {
            Websocket ws = entry.getValue();
            logger.debug("sessionId为:{}", ws.session.getId());
            try {
                ws.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
                logger.debug(" 给用户id为：{}发送消息失败", userId);
            }
        }
    }

    // speed-test
    private void speedTest(String code) throws IOException {
        // speed test
        String speedInfo = new RedisUtils().getJedis().get(Parameter.redisKeyMonitorSpeed.replace("{code}", code));
        if (speedInfo == null) {
            this.session.getBasicRemote().sendText(this.getSpeedJsonInfo(0, "end", ""));
            logger.debug("no speed info");
            return;
        }
        logger.debug("speed info:{}", speedInfo);
        JSONObject infoJson = JSONObject.parseObject(speedInfo);
        String url = (String) infoJson.get("url");
        JSONArray points = (JSONArray) infoJson.get("points");
        String[] out = new String[points.size()];
        HashMap<String, Object> totalData = new HashMap<String, Object>();
        int i = 0;
        for (Object object : points) {
            MonitorPoint point = JSONObject.toJavaObject((JSONObject) object, MonitorPoint.class);
            String params = HttpUtils.getParamContext(new HashMap<String, String>() {{
                put("url", url);
            }});
            try {
                out[i] = HttpUtils.sendConnPost(point.getPath(), params);
                JSONObject temp = JSONObject.parseObject(out[i]);
            } catch (Exception e) {
                // 监控点未响应
                out[i] = "error";
            }
            logger.debug("speed out:{}", out[i]);
            this.session.getBasicRemote().sendText(this.getSpeedJsonInfo(point.getId(), "new", out[i]));
            i++;
        }
        this.session.getBasicRemote().sendText(this.getSpeedJsonInfo(0, "end", ""));
    }

    // json-通用
    private String getJsonInfo(String action, String msg) {
        return new JSONObject() {{
            put("time", new Date().getTime());
            put("msg", msg);
        }}.toJSONString();
    }

    // json-speed-test
    private String getSpeedJsonInfo(int pointId, String type, String out) {
        return new JSONObject() {{
            put("time", new Date().getTime());
            put("action", "speed-test");
            put("pointId", pointId);
            put("type", type);
            put("result", out);
        }}.toJSONString();
    }

}
