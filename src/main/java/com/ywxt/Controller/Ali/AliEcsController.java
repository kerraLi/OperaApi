package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/ali/ecs", name = "阿里云ECS")
public class AliEcsController extends CommonController {

    // ecs:服务器列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public JSONObject ecsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !(request.getParameter("status").isEmpty())) {
            params.put("status", request.getParameter("status"));
        }
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            params.put("filter", request.getParameter("key"));
        }
        if (!(request.getParameter("lockReason") == null) && !(request.getParameter("lockReason").isEmpty())) {
            String[] lockReasons = request.getParameter("lockReason").split(",");
            params.put("lockReason@like", lockReasons);
        }
        if (!(request.getParameter("ifExpired") == null) && !(request.getParameter("ifExpired").isEmpty())) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expiredTime");
                params.put("status", "Running");
                params.put("expiredTime@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null) && !(request.getParameter("ifMarked").isEmpty())) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new AliEcsServiceImpl().getEcsList(params, pageNumber, pageSize);
    }

    // 批量设置mark
    @ResponseBody
    @RequestMapping(value = {"/param/{status}"}, name = "批量标记", method = RequestMethod.POST)
    public JSONObject ecsParamSetAll(Integer[] ids, @PathVariable String status) throws Exception {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        if (status.equals("mark")) {
            for (Integer i : list) {
                AliEcs aliEcs = new AliEcsServiceImpl().getEcs(i);
                new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
            }
        } else if (status.equals("unmark")) {
            for (Integer i : list) {
                AliEcs aliEcs = new AliEcsServiceImpl().getEcs(i);
                new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
            }
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 设置mark
    @ResponseBody
    @RequestMapping(value = {"/param/{status}/{id}"}, name = "标记", method = RequestMethod.POST)
    public JSONObject ecsParamSet(@PathVariable String status, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliEcsServiceImpl().getEcs(id);
        if (status.equals("mark")) {
            new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
        } else if (status.equals("unmark")) {
            new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // ecs:状态更新
    @ResponseBody
    @RequestMapping(value = {"/status/update/{id}"}, name = "更新状态", method = RequestMethod.GET)
    public AliEcs updateCdnRefreshTask(@PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliEcsServiceImpl().getEcs(id);
        return new AliEcsServiceImpl().updateEcsStatus(aliEcs);
    }

    // ecs:操作服务器状态
    @ResponseBody
    @RequestMapping(value = {"/status/{action}/{id}"}, name = "操作状态", method = RequestMethod.GET)
    public JSONObject ecsStatusAction(@PathVariable String action, @PathVariable Integer id) throws Exception {
        new AliEcsServiceImpl().actionEcs(action, id);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 预付费服务器
    @ResponseBody
    @RequestMapping(value = {"/perpay"}, name = "预付费", method = RequestMethod.POST)
    public AliEcs ecsPerPay(@RequestBody JSONObject jsonObject) throws Exception {
        String instanceId = (String) jsonObject.get("instanceId");
        String periodUnit = (String) jsonObject.get("periodUnit");
        int period = Integer.parseInt((String) jsonObject.get("period"));
        return new AliEcsServiceImpl().perPay(instanceId, periodUnit, period);
    }
}
