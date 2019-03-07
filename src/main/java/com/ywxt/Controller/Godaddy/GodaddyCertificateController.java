package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Service.Godaddy.Impl.GodaddyCertificateServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
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
@RequestMapping(value = "/go/certificate", name = "GO证书")
public class GodaddyCertificateController extends CommonController {

    // certificate 列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public Map<String, Object> certificateList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !(request.getParameter("status").isEmpty())) {
            if (request.getParameter("status").equals("OTHERS")) {
                params.put("certificateStatus@ne", "ISSUED");
            } else {
                params.put("certificateStatus", request.getParameter("status"));
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
                calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_CERTIFICATE_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                params.put("orderAsc", "validEnd");
                params.put("certificateStatus", "ISSUED");
                params.put("validEnd@lt", thresholdDate);
            }
        }
        if (!(request.getParameter("ifMarked") == null) && !(request.getParameter("ifMarked").isEmpty())) {
            params.put("ifMarked", request.getParameter("ifMarked"));
        }
        return new GodaddyCertificateServiceImpl().getCertificateList(params, pageNumber, pageSize);
    }

    // certificate 批量保存标记
    @ResponseBody
    @RequestMapping(value = {"/param/{status}"}, name = "批量标记", method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject domainParamSetAll(Integer[] ids, @PathVariable String status) throws Exception {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(ids));
        if (status.equals("mark")) {
            for (Integer i : list) {
                GodaddyCertificate godaddyCertificate = new GodaddyCertificateServiceImpl().getCertificate(i);
                new ParameterIgnoreServiceImpl().saveMarked(godaddyCertificate);
            }
        } else if (status.equals("unmark")) {
            for (Integer i : list) {
                GodaddyCertificate godaddyCertificate = new GodaddyCertificateServiceImpl().getCertificate(i);
                new ParameterIgnoreServiceImpl().deleteMarked(godaddyCertificate);
            }
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // certificate 保存标记
    @ResponseBody
    @RequestMapping(value = {"/param/{status}/{id}"}, name = "标记", method = RequestMethod.POST)
    public com.alibaba.fastjson.JSONObject certificateParamSet(@PathVariable String status, @PathVariable Integer id) throws Exception {
        GodaddyCertificate godaddyCertificate = new GodaddyCertificateServiceImpl().getCertificate(id);
        if (status.equals("mark")) {
            new ParameterIgnoreServiceImpl().saveMarked(godaddyCertificate);
        } else if (status.equals("unmark")) {
            new ParameterIgnoreServiceImpl().deleteMarked(godaddyCertificate);
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

}
