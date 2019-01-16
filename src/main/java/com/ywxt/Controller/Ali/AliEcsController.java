package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
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
@RequestMapping("/ali/ecs")
public class AliEcsController extends CommonController {

    // ecs:服务器列表
    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
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
        if (!(request.getParameter("ifExpired") == null) && !(request.getParameter("ifExpired").isEmpty())) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expiredTime");
                params.put("status", "Running");
                params.put("expiredTime@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null) && !(request.getParameter("ifMarked").isEmpty())) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new AliServiceImpl().getEcsList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/param/{status}"}, method = RequestMethod.POST)
    public JSONObject ecsParamSetAll(Integer[] ids, @PathVariable String status) throws Exception {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        if (status.equals("mark")) {
            for (Integer i : list) {
                AliEcs aliEcs = new AliServiceImpl().getEcs(i);
                new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
            }
        } else if (status.equals("unmark")) {
            for (Integer i : list) {
                AliEcs aliEcs = new AliServiceImpl().getEcs(i);
                new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
            }
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/param/{status}/{id}"}, method = RequestMethod.POST)
    public JSONObject ecsParamSet(@PathVariable String status, @PathVariable Integer id) throws Exception {
        AliEcs aliEcs = new AliServiceImpl().getEcs(id);
        if (status.equals("mark")) {
            new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
        } else if (status.equals("unmark")) {
            new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }
}
