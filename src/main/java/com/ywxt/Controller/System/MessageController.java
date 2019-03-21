package com.ywxt.Controller.System;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Command.Websocket;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.System.Message;
import com.ywxt.Service.System.Impl.MessageServiceImpl;
import com.ywxt.Service.System.MessageService;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;


@RestController
@RequestMapping(value = "/message", name = "消息中心")
public class MessageController extends CommonController {

    @Autowired
    private MessageService messageService;

    // 获取消息数量
    @NotOperationAction
    @GetMapping(value = {"/number/{status}"}, name = "获取数量")
    public ApiResult number(@PathVariable String status) {
        return ApiResult.successWithObject(messageService.getCount(status));
    }

    // 消息列表
    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(messageService.getList(params));
    }

    // 消息类型
    @NotOperationAction
    @PostMapping(value = {"/types"}, name = "类型列表")
    public ApiResult types() {
        return ApiResult.successWithObject(messageService.getTypes());
    }

    // 批量设置状态
    @PostMapping(value = {"/status/{status}"}, name = "批量设置状态")
    public ApiResult statusAll(Integer[] ids, @PathVariable String status) throws Exception {
        if (ids.length == 0) {
            throw new Exception("请先选择后再设置。");
        }
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        messageService.setAllStatus(list, status);
        return ApiResult.success();
    }

    // 设置状态
    @PostMapping(value = {"/status/{status}/{id}"}, name = "设置状态")
    public ApiResult statusById(@PathVariable String status, @PathVariable Integer id) {
        messageService.setStatus(id, status);
        return ApiResult.success();
    }


    // webhook接收
    @PassToken(login = true)
    @NotOperationAction
    @PostMapping(value = {"/webhook"})
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
        messageService.create("WEBHOOK_MESSAGE", String.valueOf(requestMap.get("ruleId")) + "-" + requestMap.get("state"), msgParam, otherParam);
        return "success";
    }

    // 发送websocket消息
    @PassToken(login = true)
    @NotOperationAction
    @PostMapping(value = {"/websocket"})
    public String sendWebsocketMessage(String message) {
        new Websocket().sendMessageToAllUser(message);
        return "success";
    }

}
