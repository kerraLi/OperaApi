package com.ywxt.Controller.Godaddy;

import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
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
@RequestMapping("/go")
public class GodaddyController extends CommonController {

    // domain 列表
    @ResponseBody
    @RequestMapping(value = {"/domain/list"}, method = RequestMethod.POST)
    public Map<String, Object> domainList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null)) {
            if (request.getParameter("status").equals("OTHERS")) {
                params.put("status@ne", "ACTIVE");
            } else {
                params.put("status", request.getParameter("status"));
            }
        }
        if (!(request.getParameter("key") == null)) {
            if (!request.getParameter("key").isEmpty()) {
                params.put("filter", request.getParameter("key"));
            }
        }
        if (!(request.getParameter("ifExpired") == null)) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expires");
                params.put("status", "ACTIVE");
                params.put("expires@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null)) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new GodaddyServiceImpl().getDomainList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/domain/mark/{id}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject domainMark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        GodaddyDomain godaddyDomain = new GodaddyServiceImpl().getDomain(id);
        new ParameterIgnoreServiceImpl().saveMarked(godaddyDomain);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/domain/unmark/{id}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject domainUnmark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        GodaddyDomain godaddyDomain = new GodaddyServiceImpl().getDomain(id);
        new ParameterIgnoreServiceImpl().deleteMarked(godaddyDomain);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // certificate 列表
    @ResponseBody
    @RequestMapping(value = {"/certificate/list"}, method = RequestMethod.POST)
    public Map<String, Object> certificateList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null)) {
            if (request.getParameter("status").equals("OTHERS")) {
                params.put("certificateStatus@ne", "ISSUED");
            } else {
                params.put("certificateStatus", request.getParameter("status"));
            }
        }
        if (!(request.getParameter("key") == null)) {
            if (!request.getParameter("key").isEmpty()) {
                params.put("filter", request.getParameter("key"));
            }
        }
        if (!(request.getParameter("ifExpired") == null)) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "validEnd");
                params.put("certificateStatus", "ISSUED");
                params.put("validEnd@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null)) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new GodaddyServiceImpl().getCertificateList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/certificate/mark/{id}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject certificateMark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        GodaddyCertificate godaddyCertificate = new GodaddyServiceImpl().getCertificate(id);
        new ParameterIgnoreServiceImpl().saveMarked(godaddyCertificate);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/certificate/unmark/{id}"}, method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject certificateUnmark(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        GodaddyCertificate godaddyCertificate = new GodaddyServiceImpl().getCertificate(id);
        new ParameterIgnoreServiceImpl().deleteMarked(godaddyCertificate);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }
}
