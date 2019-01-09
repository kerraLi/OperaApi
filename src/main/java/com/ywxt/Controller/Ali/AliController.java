package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/ali")
public class AliController {

    // ecs:服务器列表
    @ResponseBody
    @RequestMapping(value = {"/ecs/list"}, method = RequestMethod.POST)
    public JSONObject ecsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null)) {
            params.put("status", request.getParameter("status"));
        }
        if (!(request.getParameter("ifExpired") == null)) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expiredTime");
                params.put("status", "Running");
                params.put("expiredTime@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("key") == null)) {
            params.put("filter", request.getParameter("key"));
        }
        return new AliServiceImpl().getEcsList(params, pageNumber, pageSize);
    }

    // cdn:cdn域名列表
    @ResponseBody
    @RequestMapping(value = {"/cdn/domain/list"}, method = RequestMethod.POST)
    public JSONObject cdnDomainList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("key") == null)) {
            params.put("filter", request.getParameter("key"));
        }
        return new AliServiceImpl().getCdnDomainList(params, pageNumber, pageSize);
    }


    // cdn:节点刷新
    @ResponseBody
    @RequestMapping(value = {"/cdn/refresh"}, method = RequestMethod.POST)
    public Map<String, String> cdnRefreshObjectCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessId = request.getParameter("access-id");
        String objectPath = request.getParameter("object-path");
        String objectType = request.getParameter("object-type");
        if (objectType.isEmpty()) {
            objectType = "File";
        }
        return new AliServiceImpl(accessId).refreshCdnObjectCaches(objectPath, objectType);
    }

    // cdn:节点刷新任务查看
    @ResponseBody
    @RequestMapping(value = {"/cdn/refresh/task"}, method = RequestMethod.POST)
    public DescribeRefreshTasksResponse.CDNTask getRefreshObjectCacheTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessId = request.getParameter("access-id");
        String taskId = request.getParameter("task-id");
        return new AliServiceImpl(accessId).getCdnRefreshTask(taskId).get(0);
    }
}
