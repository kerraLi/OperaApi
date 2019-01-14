package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Service.Impl.MessageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping("/message")
public class MessageController extends CommonController {

    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("status") == null)) {
            params.put("status", request.getParameter("status"));
        }
        if (!(request.getParameter("theme") == null)) {
            params.put("theme", request.getParameter("theme"));
        }
        return new MessageServiceImpl().getList(params, pageNumber, pageSize);
    }

    @RequestMapping(value = {"/status/{status}/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject info(@PathVariable String status, @PathVariable Integer id) throws Exception {
        new MessageServiceImpl().setStatus(id, status);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

}
