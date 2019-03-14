package com.ywxt.Controller.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.Monitor.MonitorPoint;
import com.ywxt.Service.Monitor.MonitorDomainService;
import com.ywxt.Service.Monitor.MonitorPointService;
import com.ywxt.Service.Monitor.MonitorSpeedService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/monitor/speed", name = "监控测速")
public class SpeedController {

    @Resource
    private MonitorSpeedService monitorSpeedService;

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/test"}, name = "单域名测速", method = RequestMethod.POST)
    public JSONObject speedTest(HttpServletRequest request) throws Exception {
        if (request.getParameter("url") == null) {
            throw new Exception("请传入正确测速地址。");
        }
        String url = request.getParameter("url");
        return monitorSpeedService.speedTest(url);
    }

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/monitor"}, name = "多域名监控", method = RequestMethod.POST)
    public JSONObject speedMonitor() throws Exception {
        return monitorSpeedService.speedMonitor();
    }
}
