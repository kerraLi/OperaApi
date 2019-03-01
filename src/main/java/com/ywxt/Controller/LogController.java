package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.Impl.UserServiceImpl;
import com.ywxt.Service.LogOperationService;
import com.ywxt.Service.User.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogOperationService logOperationService;
    @Resource
    private UserService userService;

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/handle/list"}, method = RequestMethod.GET)
    public JSONObject list(HttpServletRequest request) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!(request.getParameter("username") == null) && !request.getParameter("username").isEmpty()) {
            User u = userService.getUser(request.getParameter("username"));
            params.put("userId", u.getId());
        }
        if (!(request.getParameter("userId") == null) && !request.getParameter("userId").isEmpty()) {
            params.put("userId", request.getParameter("userId"));
        }
        if (!(request.getParameter("path") == null) && !(request.getParameter("path").isEmpty())) {
            params.put("path@like", request.getParameter("path"));
        }
        if (!(request.getParameter("ip") == null) && !(request.getParameter("ip").isEmpty())) {
            params.put("ip", request.getParameter("ip"));
        }
        if (!(request.getParameter("status") == null) && !(request.getParameter("status").isEmpty())) {
            params.put("status", request.getParameter("status"));
        }
        return logOperationService.getList(params, pageNumber, pageSize);
    }
}
