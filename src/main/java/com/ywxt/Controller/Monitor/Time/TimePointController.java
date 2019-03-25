package com.ywxt.Controller.Monitor.Time;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.Monitor.Time.MonitorPoint;
import com.ywxt.Service.Monitor.Time.TimePointService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/monitor/time/point", name = "时间监控点")
public class TimePointController {

    @Resource
    private TimePointService timePointService;

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public List<MonitorPoint> list(HttpServletRequest request) throws Exception {
        return timePointService.getList();
    }
}
