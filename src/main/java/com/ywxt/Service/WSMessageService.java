package com.ywxt.Service;

import com.ywxt.Command.Websocket;

public class WSMessageService {
    //声明websocket连接类
    private Websocket websocket = new Websocket();

    /**
     * @param @param  userId 用户id
     * @param @param  message 消息
     * @param @return 发送成功返回true，否则返回false
     * @Title: sendToAllTerminal
     * @Description: 调用websocket类给用户下的所有终端发送消息
     */
    public Boolean sendToAllTerminal(Long userId, String message) {
        return websocket.sendMessageToUser(userId, message);
    }
}
