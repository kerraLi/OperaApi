package com.ywxt.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliCdnServiceImpl;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyCertificateServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyDomainServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dash", name = "仪表盘")
public class DashboardController {

    // 面板模块数量
//    @NotOperationAction
//    @ResponseBody
//    @RequestMapping(value = {"/number"}, name = "模块", method = RequestMethod.GET)
//    public JSONObject number() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("messages", new MessageServiceImpl().getTotal(new HashMap<String, Object>()));
//        int aliNum = new AliAccountServiceImpl().getTotal(new HashMap<String, Object>());
//        int goNum = new GodaddyAccountServiceImpl().getTotal(new HashMap<String, Object>());
//        jsonObject.put("accounts", (aliNum + goNum));
//        return jsonObject;
//    }

    // 消息列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/message"}, name = "消息块", method = RequestMethod.GET)
    public JSONObject message() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lineChart", new MessageServiceImpl().getLineChartData(10));
        jsonObject.put("todoList", new MessageServiceImpl().getList(new HashMap<String, Object>(), 1, 8).get("items"));
        return jsonObject;
    }

    // 账户列表
//    @NotOperationAction
//    @ResponseBody
//    @RequestMapping(value = {"/account"}, name = "账户块", method = RequestMethod.GET)
//    public JSONObject account() throws Exception {
//        String[] xData = {"Ali-ECS", "Ali-CDN域名", "GO-域名", "GO-证书"};
//        JSONObject jsonObject = new JSONObject();
//        int[][] numbers = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
//        HashMap<String, Object> map = new HashMap<>();
//        map.putAll(new AliEcsServiceImpl().getDashData());
//        map.putAll(new AliCdnServiceImpl().getDashData());
//        map.putAll(new GodaddyDomainServiceImpl().getDashData());
//        map.putAll(new GodaddyCertificateServiceImpl().getDashData());
//        HashMap<String, Object> mapByAccount = new HashMap<>();
//        for (Map.Entry<String, Object> e : map.entrySet()) {
//            // 单个资源
//            String[] strs = e.getKey().split("-");
//            // String key = strs[0] + "-" + strs[1] + "-" + strs[2];
//            // JSONObject temp = new JSONObject();
//            // if (mapByAccount.get(key) != null) {
//            //     temp = (JSONObject) mapByAccount.get(key);
//            // } else {
//            //     String accountName = "";
//            //     if (strs[0].equals("ali")) {
//            //         accountName = new AliServiceImpl().getUserName(strs[2]);
//            //     }else{
//            //         accountName = new GodaddyServiceImpl().getUserName(strs[2]);
//            //     }
//            //     temp.put("type", strs[0]);
//            //     temp.put("username", accountName);
//            //     temp.put("theme", strs[1]);
//            // }
//            // temp.put(strs[3], e.getValue());
//            // mapByAccount.put(key, temp);
//
//            // 资源总计
//            int x = 0;
//            int y = 0;
//            switch (strs[0] + "-" + strs[1]) {
//                case "ali-ecs":
//                    y = 0;
//                    break;
//                case "ali-cdn":
//                    y = 1;
//                    break;
//                case "go-domain":
//                    y = 2;
//                    break;
//                case "go-certificate":
//                    y = 3;
//                    break;
//            }
//            switch (strs[3]) {
//                case "normal":
//                    x = 0;
//                    break;
//                case "invalid":
//                    x = 1;
//                    break;
//                case "expired":
//                    x = 2;
//                    break;
//                case "deprecated":
//                    x = 3;
//                    break;
//            }
//            numbers[x][y] += (Long) e.getValue();
//        }
//        // JSONArray jsonArray = new JSONArray();
//        // for (Map.Entry<String, Object> e : mapByAccount.entrySet()) {
//        //     jsonArray.add(e.getValue());
//        // }
//        jsonObject.put("total", new JSONObject() {{
//            put("xData", xData);
//            put("normal", numbers[0]);
//            put("invalid", numbers[1]);
//            put("expired", numbers[2]);
//            put("deprecated", numbers[3]);
//        }});
//        // jsonObject.put("divide", jsonArray);
//        return jsonObject;
//    }

}
