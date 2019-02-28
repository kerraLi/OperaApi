package com.ywxt.Controller.Monitor;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Monitor.MonitorPoint;
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
@RequestMapping("/monitor/speed")
public class SpeedController {

    @Resource
    private MonitorPointService monitorPointService;
    @Resource
    private MonitorSpeedService monitorSpeedService;

    @ResponseBody
    @RequestMapping(value = {"/test"}, method = RequestMethod.POST)
    public JSONObject connPoint(HttpServletRequest request) throws Exception {
        if (request.getParameter("url") == null) {
            throw new Exception("请传入正确测速地址。");
        }
        String url = request.getParameter("url");
        return monitorSpeedService.connPoints(url);
    }
}
