package com.ywxt.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Command.Websocket;
import com.ywxt.Dao.Impl.MessageDaoImpl;
import com.ywxt.Domain.Message;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import org.springframework.util.ClassUtils;

import java.util.*;

public class MessageServiceImpl {

    public MessageServiceImpl() {
    }

    // 查询所有&分页
    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 最近时间倒叙
        params.put("orderDesc", "createdTime");
        List<Message> list = new MessageDaoImpl().getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new MessageDaoImpl().getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // 批量设置状态
    public void setAllStatus(List<Integer> ids, String status) throws Exception {
        new MessageDaoImpl().saveAliEcses(ids, status);
    }

    // 设置状态
    public void setStatus(int id, String status) {
        Message message = new MessageDaoImpl().getById(id);
        message.setStatus(status);
        message.setModifyTime(new Date());
        new MessageDaoImpl().save(message);
    }

    // 新建消息
    public int create(String action, String themeId, Map<String, String> msgParam, Map<String, String> otherParam) throws Exception {
        Message message = new Message();
        String context = Parameter.MessageActions.get(action);
        for (Map.Entry<String, String> e : msgParam.entrySet()) {
            String value = (e.getValue() == null) ? "" : e.getValue();
            context = context.replace("{" + e.getKey() + "}", value);
        }
        // 存数据转换城br
        context = context.replace("\r\n", "</br>");
        String[] actions = action.split("_");
        // 额外设置参数 目前“webhook”参数
        if (otherParam.size() > 0) {
            message.setImageUrl(otherParam.get("imageUrl"));
            message.setEvalMatches(otherParam.get("evalMatches"));
            context = context + "</br><img src='" + otherParam.get("imageUrl") + "'>";
        }
        message.setTitle(Parameter.MessageTitles.get(action));
        message.setTheme(actions[0]);
        message.setThemeId(themeId);
        message.setMessage(context);
        message.setCreatedTime(new Date());
        message.setModifyTime(new Date());
        message.setStatus("new");
        int id = new MessageDaoImpl().save(message);
        if (id > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("title", message.getTitle());
            jsonObject.put("status", message.getStatus());
            jsonObject.put("id", message.getId());
            jsonObject.put("themeId", message.getThemeId());
            jsonObject.put("message", message.getMessage());
            if (message.hasImageUrl()) {
                jsonObject.put("imageUrl", message.getImageUrl());
            }
            this.sendWebsocket(jsonObject);
        }
        return id;
    }

    // 发送ws消息
    private void sendWebsocket(JSONObject jsonObject) throws Exception {
        if (this.isWebEnvironment()) {
            new Websocket().sendMessageToAllUser(jsonObject.toJSONString());
        } else {
            String params = HttpUtils.getParamContext(new HashMap<String, String>() {{
                put("json", jsonObject.toString());
            }});
            HttpUtils.sendConnPost("http://localhost:9000/api/message/websocket", params);
        }
    }

    // 判断当前环境
    private boolean isWebEnvironment() {
        String[] WEB_ENVIRONMENT_CLASSES = {"javax.servlet.Servlet",
                "org.springframework.web.context.ConfigurableWebApplicationContext"};
        for (String className : WEB_ENVIRONMENT_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                System.out.println("非web环境");
                return false;
            }
        }
        System.out.println("web环境");
        return true;
    }
}
