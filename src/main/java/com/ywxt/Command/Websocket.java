package com.ywxt.Command;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ServerEndpoint(value = "/websocket/{userId}")
public class Websocket {
    //日志记录
    private Logger logger = LoggerFactory.getLogger(Websocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //记录每个用户下多个终端的连接
    private static Map<Long, Set<Websocket>> userSocket = new HashMap<>();

    //需要session来对用户发送数据, 获取连接特征userId
    private Session session;
    private Long userId;

    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) throws IOException {
        this.session = session;
        this.userId = userId;
        onlineCount++;
        System.out.println("连接用户" + userId);
        //根据该用户当前是否已经在别的终端登录进行添加操作
        if (userSocket.containsKey(this.userId)) {
            logger.debug("当前用户id:{}已有其他终端登录", this.userId);
            userSocket.get(this.userId).add(this); //增加该用户set中的连接实例
        } else {
            logger.debug("当前用户id:{}第一个终端登录", this.userId);
            Set<Websocket> addUserSet = new HashSet<>();
            addUserSet.add(this);
            userSocket.put(this.userId, addUserSet);
        }
        logger.debug("用户{}登录的终端个数是为{}", userId, userSocket.get(this.userId).size());
        logger.debug("当前在线用户数为：{},所有终端个数为：{}", userSocket.size(), onlineCount);
    }

    @OnClose
    public void onClose() {
        System.out.println("关闭用户" + userId);
        //移除当前用户终端登录的websocket信息,如果该用户的所有终端都下线了，则删除该用户的记录
        if (userSocket.get(this.userId).size() == 0) {
            userSocket.remove(this.userId);
        } else {
            userSocket.get(this.userId).remove(this);
        }
        logger.debug("用户{}登录的终端个数是为{}", this.userId, userSocket.get(this.userId).size());
        logger.debug("当前在线用户数为：{},所有终端个数为：{}", userSocket.size(), onlineCount);
    }

    /**
     * @param @param message 收到的消息
     * @param @param session 该连接的session属性
     * @Title: onMessage
     * @Description: 收到消息后的操作
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到消息" + userId);
        logger.debug("收到来自用户id为：{}的消息：{}", this.userId, message);
        if (session == null) logger.debug("session null");
    }

    /**
     * @param @param session 该连接的session
     * @param @param error 发生的错误
     * @Title: onError
     * @Description: 连接发生错误时候的操作
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("用户id为：{}的连接发送错误", this.userId);
        error.printStackTrace();
    }

    /**
     * @param @param  userId 用户id
     * @param @param  message 发送的消息
     * @param @return 发送成功返回true，反则返回false
     * @Title: sendMessageToUser
     * @Description: 发送消息给用户下的所有终端
     */
    public Boolean sendMessageToUser(Long userId, String message) {
        if (userSocket.containsKey(userId)) {
            logger.debug(" 给用户id为：{}的所有终端发送消息：{}", userId, message);
            for (Websocket WS : userSocket.get(userId)) {
                logger.debug("sessionId为:{}", WS.session.getId());
                try {
                    WS.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.debug(" 给用户id为：{}发送消息失败", userId);
                    return false;
                }
            }
            return true;
        }
        logger.debug("发送错误：当前连接不包含id为：{}的用户", userId);
        return false;
    }
}
