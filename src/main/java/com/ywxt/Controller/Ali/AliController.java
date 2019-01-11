package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/ali")
public class AliController extends CommonController {

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
        if (!(request.getParameter("key") == null)) {
            params.put("filter", request.getParameter("key"));
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
        if (!(request.getParameter("ifMarked") == null)) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new AliServiceImpl().getEcsList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/ecs/mark/{id}"}, method = RequestMethod.POST)
    public JSONObject ecsMark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliServiceImpl().getEcs(id);
        new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/ecs/unmark/{id}"}, method = RequestMethod.POST)
    public JSONObject ecsUnmark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliServiceImpl().getEcs(id);
        new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
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
        if (!(request.getParameter("ifMarked") == null)) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new AliServiceImpl().getCdnDomainList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/cdn/mark/{id}"}, method = RequestMethod.POST)
    public JSONObject cdnMark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliServiceImpl().getEcs(id);
        new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/cdn/unmark/{id}"}, method = RequestMethod.POST)
    public JSONObject cdnUnmark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliServiceImpl().getEcs(id);
        new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
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
