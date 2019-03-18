package com.ywxt.Controller.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Resource.Data;
import com.ywxt.Service.Resource.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/resource/data", name = "资源数据")
public class DataController extends CommonController {

    @Resource
    private DataService dataService;

    @ResponseBody
    @RequestMapping(value = {"/upload/{categoryId}"}, name = "上传", method = RequestMethod.POST)
    public JSONObject upload(@PathVariable Integer categoryId, @RequestBody ArrayList<JSONArray> list) throws Exception {
        dataService.upload(categoryId, list);
        return this.returnObject();
    }

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list/{categoryId}"}, name = "列表", method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request, @PathVariable Integer categoryId) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            params.put("data@like", request.getParameter("key"));
        }
        return dataService.getList(params, pageNumber, pageSize);
    }

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/download/{categoryId}"}, name = "下载", method = RequestMethod.POST)
    public List<Data> download(@PathVariable Integer categoryId) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", categoryId);
        return dataService.getList(params);
    }


    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    public JSONObject save(@RequestBody Data data) throws Exception {
        dataService.save(data);
        return this.returnObject();
    }

    @ResponseBody
    @RequestMapping(value = {"/remove/{id}"}, name = "删除", method = RequestMethod.GET)
    public JSONObject remove(@PathVariable Integer id) throws Exception {
        dataService.remove(id);
        return this.returnObject();
    }

    @ResponseBody
    @RequestMapping(value = {"/remove"}, name = "批量删除", method = RequestMethod.POST)
    public JSONObject removeAll(Integer[] ids) throws Exception {
        dataService.removeAll(ids);
        return this.returnObject();
    }


}
