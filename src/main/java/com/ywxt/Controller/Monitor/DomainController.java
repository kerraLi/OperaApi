package com.ywxt.Controller.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Monitor.MonitorDomain;
import com.ywxt.Service.Monitor.MonitorDomainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/monitor/domain", name = "监控域名")
public class DomainController extends CommonController {

    @Resource
    private MonitorDomainService monitorDomainService;

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null) && !(request.getParameter("status").isEmpty())) {
            params.put("status", request.getParameter("status"));
        }
        return monitorDomainService.getList(params, pageNumber, pageSize);
    }


    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    public JSONObject save(@RequestBody MonitorDomain monitorDomain) throws Exception {
        if (monitorDomain.getPath().isEmpty()) {
            throw new Exception("域名路径不能为空");
        }
        monitorDomainService.save(monitorDomain);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove/{id}"}, name = "删除", method = RequestMethod.GET)
    public JSONObject remove(@PathVariable Integer id) throws Exception {
        monitorDomainService.remove(id);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove"}, name = "批量删除", method = RequestMethod.POST)
    public JSONObject removeAll(Integer[] ids) throws Exception {
        monitorDomainService.removeAll(ids);
        return this.returnObject(new HashMap<>() {{
        }});
    }
}
