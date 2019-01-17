package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliCdnServiceImpl;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyCertificateServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyDomainServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/dash")
public class DashboardController {

    // 面板模块数量
    @ResponseBody
    @RequestMapping(value = {"/number"}, method = RequestMethod.GET)
    public JSONObject number() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", new MessageServiceImpl().getTotal(new HashMap<String, Object>()));
        int aliNum = new AliAccountServiceImpl().getTotal(new HashMap<String, Object>());
        int goNum = new GodaddyAccountServiceImpl().getTotal(new HashMap<String, Object>());
        jsonObject.put("accounts", (aliNum + goNum));
        return jsonObject;
    }

    // 消息列表
    @ResponseBody
    @RequestMapping(value = {"/message"}, method = RequestMethod.GET)
    public JSONObject message() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lineChart", new MessageServiceImpl().getLineChartData(10));
        jsonObject.put("todoList", new MessageServiceImpl().getList(new HashMap<String, Object>(), 1, 8).get("items"));
        return jsonObject;
    }

    // 账户列表
    @ResponseBody
    @RequestMapping(value = {"/account"}, method = RequestMethod.GET)
    public JSONObject account() throws Exception {
        String[] xData = {"Ali-ECS", "Ali-CDN域名", "GO-域名", "GO-证书"};
        JSONObject jsonObject = new JSONObject();
        HashMap ecsMap = new AliEcsServiceImpl().getDashData();
        new AliCdnServiceImpl().getDashData();
        new GodaddyDomainServiceImpl().getDashData();
        new GodaddyCertificateServiceImpl().getDashData();
        return jsonObject;
    }

    public static void main(String[] args) throws Exception {
        String[] xData = {"Ali-ECS", "Ali-CDN域名", "GO-域名", "GO-证书"};
        new DashboardController().account();
    }

}
