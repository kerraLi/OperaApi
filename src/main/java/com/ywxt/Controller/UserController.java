package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserController extends CommonController {

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    @PassToken
    public JSONObject login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String clientUsername = request.getParameter("username");
        String clientPassword = request.getParameter("password");
        String authToken = new UserServiceImpl().login(clientUsername, clientPassword);
        return this.returnObject(new HashMap<String, Object>() {{
            put("token", authToken);
        }});
    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.getUserFromAuthToken(request);
    }
}
