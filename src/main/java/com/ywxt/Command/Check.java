package com.ywxt.Command;

import com.ywxt.Service.System.Impl.MessageServiceImpl;
import com.ywxt.Utils.TelegramUtils;

import java.util.HashMap;
import java.util.Map;

public class Check {

    // 设置消息
    protected static void setMessage(String action, String themeId, Map<String, String> parameters) throws Exception {
        new MessageServiceImpl().create(action, themeId, parameters, new HashMap<String, String>());
    }

    // 发送错误
    protected static void sendException(String theme, String action, Exception e) {
        try {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", theme + "-" + action + "-" + e.getMessage());
            param.put("class", e.getClass().toString());
            TelegramUtils.sendMessage("ERROR", param);
            Check.printException(e);
        } catch (Exception e2) {
            Check.printException(e2);
        }
    }

    // 输出错误（记录日志中排查问题）
    protected static void printException(Exception e) {
        StackTraceElement[] ses = e.getStackTrace();
        System.err.println("Exception " + e.toString());
        for (StackTraceElement se : ses) {
            System.err.println("\tat " + se);
        }
    }
}
