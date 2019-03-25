package com.ywxt.Controller.Monitor.Domain;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Service.Monitor.Domain.DomainSpeedService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/monitor/domain/speed", name = "监控测速")
public class DomainSpeedController {

    @Resource
    private DomainSpeedService domainSpeedService;

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/test"}, name = "单域名测速", method = RequestMethod.POST)
    public JSONObject speedTest(HttpServletRequest request) throws Exception {
        if (request.getParameter("url") == null) {
            throw new Exception("请传入正确测速地址。");
        }
        String url = request.getParameter("url");
        return domainSpeedService.speedTest(url);
    }

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/monitor"}, name = "多域名监控", method = RequestMethod.POST)
    public JSONObject speedMonitor() throws Exception {
        return domainSpeedService.speedMonitor();
    }
}
