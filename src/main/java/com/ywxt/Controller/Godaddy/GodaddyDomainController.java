package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.Impl.GodaddyDomainServiceImpl;
import com.ywxt.Service.System.Impl.IgnoreServiceImpl;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/go/domain", name = "GO域名")
public class GodaddyDomainController extends CommonController {

    // domain 列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
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
                calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_DOMAIN_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "expires");
                params.put("status", "ACTIVE");
                params.put("expires@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null) && !(request.getParameter("ifMarked").isEmpty())) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new GodaddyDomainServiceImpl().getDomainList(params, pageNumber, pageSize);
    }

}
