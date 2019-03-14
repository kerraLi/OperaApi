package com.ywxt.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Command.Websocket;
import com.ywxt.Service.Impl.MessageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;


@Controller
@RequestMapping(value = "/message", name = "消息中心")
public class MessageController extends CommonController {

    // 获取消息数量
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/number/{status}"}, name = "获取数量", method = RequestMethod.GET)
    public JSONObject number(@PathVariable String status) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("status", status);
        int number = new MessageServiceImpl().getTotal(params);
        return this.returnObject(new HashMap<String, Object>() {{
            put("number", number);
        }});
    }

    // 消息列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !request.getParameter("status").isEmpty()) {
            params.put("status", request.getParameter("status"));
        }
        if (!(request.getParameter("theme") == null) && !request.getParameter("theme").isEmpty()) {
            params.put("theme", request.getParameter("theme"));
        }
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            params.put("filter", request.getParameter("key"));
        }
        return new MessageServiceImpl().getList(params, pageNumber, pageSize);
    }

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/types"}, name = "类型列表", method = RequestMethod.GET)
    public List<String> types() {
        return new MessageServiceImpl().getTypes();
    }

    // 批量设置
    @RequestMapping(value = {"/status/{status}"}, name = "批量设置状态", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject statusAll(Integer[] ids, @PathVariable String status) throws Exception {
        if (ids.length == 0) {
            throw new Exception("请先选择后再设置。");
        }
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        new MessageServiceImpl().setAllStatus(list, status);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 设置状态
    @RequestMapping(value = {"/status/{status}/{id}"}, name = "设置状态", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject statusById(@PathVariable String status, @PathVariable Integer id) throws Exception {
        new MessageServiceImpl().setStatus(id, status);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }


    // webhook接收
    @PassToken(login = true)
    @NotOperationAction
    @RequestMapping(value = {"/webhook"}, method = RequestMethod.POST)
    @ResponseBody
    public String getWebhookMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String requestBody = buffer.toString();
        Map requestMap = (Map) JSON.parse(requestBody);
        // 基础msg参数
        Map<String, String> msgParam = new HashMap<String, String>();
        msgParam.put("ruleName", String.valueOf(requestMap.get("ruleName")));
        msgParam.put("ruleUrl", String.valueOf(requestMap.get("ruleUrl")));
        msgParam.put("state", String.valueOf(requestMap.get("state")));
        msgParam.put("title", String.valueOf(requestMap.get("title")));
        msgParam.put("message", String.valueOf(requestMap.get("message")));
        // 字段参数
        Map<String, String> otherParam = new HashMap<String, String>();
        otherParam.put("imageUrl", String.valueOf(requestMap.get("imageUrl")));
        otherParam.put("evalMatches", requestMap.get("evalMatches").toString());
        new MessageServiceImpl().create("WEBHOOK_MESSAGE", String.valueOf(requestMap.get("ruleId")) + "-" + requestMap.get("state"), msgParam, otherParam);
        return "success";
    }

    // 发送websocket消息
    @PassToken(login = true)
    @NotOperationAction
    @RequestMapping(value = {"/websocket"}, method = RequestMethod.POST)
    @ResponseBody
    public String sendWebsocketMessage(String message) {
        new Websocket().sendMessageToAllUser(message);
        return "success";
    }

}
