package com.ywxt.Controller.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Monitor.MonitorPoint;
import com.ywxt.Service.Monitor.MonitorPointService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/monitor/point", name = "监控点")
public class PointController extends CommonController {

    @Resource
    private MonitorPointService monitorPointService;

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
        return monitorPointService.getList(params, pageNumber, pageSize);
    }

    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    public JSONObject save(@RequestBody MonitorPoint monitorPoint) throws Exception {
        if (monitorPoint.getLocation().isEmpty()) {
            throw new Exception("监控点地区不能为空。");
        }
        if (monitorPoint.getPath().isEmpty()) {
            throw new Exception("监控路由不能为空。");
        }
        monitorPointService.save(monitorPoint);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove/{id}"}, name = "删除", method = RequestMethod.GET)
    public JSONObject remove(@PathVariable Integer id) throws Exception {
        monitorPointService.remove(id);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove"}, name = "批量删除", method = RequestMethod.POST)
    public JSONObject removeAll(Integer[] ids) throws Exception {
        monitorPointService.removeAll(ids);
        return this.returnObject(new HashMap<>() {{
        }});
    }
}
