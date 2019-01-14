package com.ywxt.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Impl.MessageDaoImpl;
import com.ywxt.Domain.Message;
import com.ywxt.Utils.Parameter;

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

    // 设置状态
    public void setStatus(int id, String status) {
        Message message = new MessageDaoImpl().getById(id);
        message.setStatus(status);
        message.setModifyTime(new Date());
        new MessageDaoImpl().save(message);
    }

    // 新建消息
    public int create(String action, String themeId, Map<String, String> parameters) {
        Message message = new Message();
        String context = Parameter.MessageActions.get(action);
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            String value = (e.getValue() == null) ? "" : e.getValue();
            context = context.replace("{" + e.getKey() + "}", value);
        }
        String[] actions = action.split("_");
        message.setTitle(Parameter.MessageTitles.get(action));
        message.setTheme(actions[0]);
        message.setThemeId(themeId);
        message.setMessage(context);
        message.setCreatedTime(new Date());
        message.setModifyTime(new Date());
        message.setStatus("new");
        int id = new MessageDaoImpl().save(message);
        if (id > 0) {
            // todo 调用websocket发送消息
        }
        return id;
    }
}
