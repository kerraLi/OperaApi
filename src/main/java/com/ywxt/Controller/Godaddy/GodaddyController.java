package com.ywxt.Controller.Godaddy;

import com.ywxt.Controller.CommonController;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Utils.Parameter;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
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
        if (!(request.getParameter("ifExpired") == null)) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expires");
                params.put("status", "ACTIVE");
                params.put("expires@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("key") == null)) {
            if (!request.getParameter("key").isEmpty()) {
                params.put("filter", request.getParameter("key"));
            }
        }
        return new GodaddyServiceImpl().getDomainList(params, pageNumber, pageSize);
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
                params.put("status@ne", "ISSUED");
            } else {
                params.put("status", request.getParameter("status"));
            }
        }
        if (!(request.getParameter("ifExpired") == null)) {
            if (request.getParameter("ifExpired").equals("true")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "validEnd");
                params.put("certificateStatus", "ISSUED");
                params.put("validEnd@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("key") == null)) {
            if (!request.getParameter("key").isEmpty()) {
                params.put("filter", request.getParameter("key"));
            }
        }
        return new GodaddyServiceImpl().getCertificateList(params, pageNumber, pageSize);
    }
}
