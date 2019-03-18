package com.ywxt.Controller.System;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
import com.ywxt.Service.System.RefreshService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/refresh", name = "刷新管理")
public class RefreshController extends CommonController {

    @Resource
    private RefreshService refreshService;

    @ResponseBody
    @RequestMapping(value = "/list", name = "刷新资源", method = RequestMethod.GET)
    public List<JSONObject> list() {
        return refreshService.refreshTypes();
    }

    @ResponseBody
    @RequestMapping(value = "/data/{type}", name = "源数据刷新", method = RequestMethod.POST)
    public JSONObject refreshData(@PathVariable String type) throws Exception {
        refreshService.refreshData(type);
        return this.returnObject();
    }

}
