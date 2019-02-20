package com.ywxt.Controller.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Resource.Hardware;
import com.ywxt.Service.Resource.Impl.HardwareServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/hardware")
public class HardwareController extends CommonController {

    // 列表
    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("key") == null) && !(request.getParameter("key").isEmpty())) {
            params.put("filter", request.getParameter("key"));
        }
        params.put("orderDesc", "id");
        return new HardwareServiceImpl().getList(params, pageNumber, pageSize);
    }

    // 新增&修改
    @ResponseBody
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public JSONObject save(@RequestBody Hardware hardware) throws Exception {
        new HardwareServiceImpl().save(hardware);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 删除
    @ResponseBody
    @RequestMapping(value = {"/remove/{id}"}, method = RequestMethod.GET)
    public JSONObject remove(@PathVariable Integer id) throws Exception {
        new HardwareServiceImpl().delete(id);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 批量删除
    @ResponseBody
    @RequestMapping(value = {"/remove"}, method = RequestMethod.POST)
    public JSONObject removeAll(Integer[] ids) throws Exception {
        new HardwareServiceImpl().deleteAll(ids);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    // 上传
    @ResponseBody
    @RequestMapping(value = {"/upload"}, method = RequestMethod.POST)
    public JSONObject upload(@RequestBody ArrayList<Hardware> list) throws Exception {
        new HardwareServiceImpl().saveAll(list);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }
}
