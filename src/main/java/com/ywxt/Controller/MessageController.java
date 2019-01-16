package com.ywxt.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Command.Websocket;
import com.ywxt.Service.Impl.MessageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;


@Controller
@RequestMapping("/message")
public class MessageController extends CommonController {

    // 消息列表
    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !request.getParameter("status").isEmpty()) {
            params.put("status", request.getParameter("status"));
        }
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            params.put("filter", request.getParameter("key"));
        }
        return new MessageServiceImpl().getList(params, pageNumber, pageSize);
    }

    // 批量设置
    @RequestMapping(value = {"/status/{status}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject statusAll(Integer[] ids, @PathVariable String status) throws Exception {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        new MessageServiceImpl().setAllStatus(list, status);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 设置状态
    @RequestMapping(value = {"/status/{status}/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject statusById(@PathVariable String status, @PathVariable Integer id) throws Exception {
        new MessageServiceImpl().setStatus(id, status);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }


    // webhook接收
    @PassToken
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
        PrintWriter out = null;
        out = response.getWriter();
        out.print("success");
        out.flush();
        out.close();
        return "success";
    }

    // 发送websocket消息
    @PassToken
    @RequestMapping(value = {"/websocket"}, method = RequestMethod.POST)
    @ResponseBody
    public String sendWebsocketMessage(String message) {
        new Websocket().sendMessageToAllUser(message);
        return "success";
    }

}
