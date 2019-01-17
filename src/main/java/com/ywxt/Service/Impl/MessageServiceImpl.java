package com.ywxt.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Command.Websocket;
import com.ywxt.Dao.Impl.MessageDaoImpl;
import com.ywxt.Domain.Message;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import org.apache.tomcat.jni.Global;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageServiceImpl {

    public MessageServiceImpl() {
    }

    // message 线性图表数据
    public JSONObject getLineChartData(int limitDay) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String[] dateData = new String[limitDay];
        Long[] aliMoney = new Long[limitDay];
        Long[] aliExpired = new Long[limitDay];
        Long[] godaddyExpired = new Long[limitDay];
        Long[] webhookAlert = new Long[limitDay];
        // line chart（10天）
        Calendar startDate = Calendar.getInstance();
        // 不计算当天
        startDate.setTime(new Date());
        startDate.add(Calendar.DATE, -(limitDay + 1));
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        HashMap<String, Object> params = new HashMap<String, Object>();
        // >=
        params.put("createdTime@ge", startDate.getTime());
        // <
        params.put("createdTime@lt", endDate.getTime());
        List<Object[]> list = new MessageDaoImpl().getCountGroup(params);
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        for (Object[] os : list) {
            resultParams.put((String) os[0] + os[1] + os[2], os[3]);
        }
        SimpleDateFormat dfOut = new SimpleDateFormat("M/d");
        SimpleDateFormat dfGroup = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < limitDay; i++) {
            dateData[i] = dfOut.format(startDate.getTime());
            aliMoney[i] = resultParams.get("ALI续费" + dfGroup.format(startDate.getTime())) == null ? 0 : (Long) (resultParams.get("ALI续费" + dfGroup.format(startDate.getTime())));
            aliExpired[i] = resultParams.get("ALI过期" + dfGroup.format(startDate.getTime())) == null ? 0 : (Long) (resultParams.get("ALI过期" + dfGroup.format(startDate.getTime())));
            godaddyExpired[i] = resultParams.get("GODADDY过期" + dfGroup.format(startDate.getTime())) == null ? 0 : (Long) (resultParams.get("GODADDY过期" + dfGroup.format(startDate.getTime())));
            webhookAlert[i] = resultParams.get("WEBHOOK报警" + dfGroup.format(startDate.getTime())) == null ? 0 : (Long) (resultParams.get("WEBHOOK报警" + dfGroup.format(startDate.getTime())));
            startDate.add(Calendar.DATE, +1);
        }
        jsonObject.put("dateData", dateData);
        jsonObject.put("aliMoney", aliMoney);
        jsonObject.put("aliExpired", aliExpired);
        jsonObject.put("godaddyExpired", godaddyExpired);
        jsonObject.put("webhookAlert", webhookAlert);
        return jsonObject;
    }

    // 获取总数
    public int getTotal(HashMap<String, Object> params) throws Exception {
        return new MessageDaoImpl().getListTotal(params);
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
        if (this.isHttpEnvironment()) {
            new Websocket().sendMessageToAllUser(jsonObject.toJSONString());
        } else {
            String params = HttpUtils.getParamContext(new HashMap<String, String>() {{
                put("message", jsonObject.toString());
            }});
            HttpUtils.sendConnPost(Parameter.urlWebsocket, params);
        }
    }

    // 判断当前环境
    private boolean isHttpEnvironment() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (NullPointerException e) {
            // 非http请求
            return false;
        }
        return true;
    }
}
