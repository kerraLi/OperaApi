package com.ywxt.Controller.Godaddy;

import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
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
@RequestMapping("/go/domain")
public class GodaddyDomainController extends CommonController {

    // domain 列表
    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
    public Map<String, Object> domainList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !(request.getParameter("status").isEmpty())) {
            if (request.getParameter("status").equals("OTHERS")) {
                params.put("status@ne", "ACTIVE");
            } else {
                params.put("status", request.getParameter("status"));
            }
        }
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            if (!request.getParameter("key").isEmpty()) {
                params.put("filter", request.getParameter("key"));
            }
        }
        if (!(request.getParameter("ifExpired") == null) && !(request.getParameter("ifExpired").isEmpty())) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expires");
                params.put("status", "ACTIVE");
                params.put("expires@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null) && !(request.getParameter("ifMarked").isEmpty())) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new GodaddyServiceImpl().getDomainList(params, pageNumber, pageSize);
    }

    // domain 批量保存标记
    @ResponseBody
    @RequestMapping(value = {"/param/{status}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject domainParamSetAll(Integer[] ids, @PathVariable String status) throws Exception {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        if (status.equals("mark")) {
            for (Integer i : list) {
                GodaddyDomain godaddyDomain = new GodaddyServiceImpl().getDomain(i);
                new ParameterIgnoreServiceImpl().saveMarked(godaddyDomain);
            }
        } else if (status.equals("unmark")) {
            for (Integer i : list) {
                GodaddyDomain godaddyDomain = new GodaddyServiceImpl().getDomain(i);
                new ParameterIgnoreServiceImpl().deleteMarked(godaddyDomain);
            }
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // domain 保存标记
    @ResponseBody
    @RequestMapping(value = {"/param/{status}/{id}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject domainParamSet(@PathVariable String status, @PathVariable Integer id) throws Exception {
        GodaddyDomain godaddyDomain = new GodaddyServiceImpl().getDomain(id);
        if (status.equals("mark")) {
            new ParameterIgnoreServiceImpl().saveMarked(godaddyDomain);
        } else if (status.equals("unmark")) {
            new ParameterIgnoreServiceImpl().deleteMarked(godaddyDomain);
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

}
