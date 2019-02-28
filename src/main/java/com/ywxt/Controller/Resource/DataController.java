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

@Controller
@RequestMapping("/resource/data")
public class DataController extends CommonController {

    @Resource
    private DataService dataService;

    @ResponseBody
    @RequestMapping(value = {"/upload/{categoryId}"}, method = RequestMethod.POST)
    public JSONObject upload(@PathVariable Integer categoryId, @RequestBody ArrayList<JSONArray> list) throws Exception {
        dataService.upload(categoryId, list);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list/{categoryId}"}, method = RequestMethod.POST)
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

    @ResponseBody
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public JSONObject save(@RequestBody Data data) throws Exception {
        dataService.save(data);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove/{id}"}, method = RequestMethod.GET)
    public JSONObject remove(@PathVariable Integer id) throws Exception {
        dataService.remove(id);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/remove"}, method = RequestMethod.POST)
    public JSONObject removeAll(Integer[] ids) throws Exception {
        dataService.removeAll(ids);
        return this.returnObject(new HashMap<>() {{
        }});
    }


}
