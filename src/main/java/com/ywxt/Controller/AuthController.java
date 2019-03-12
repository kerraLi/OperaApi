package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;

@Validated
@Controller
@RequestMapping(value = "/auth", name = "用户管理")
public class AuthController extends CommonController {

    @Resource
    private AuthService authService;

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    @PassToken
    @NotOperationAction
    public JSONObject login(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password) throws Exception {
        String authToken = authService.login(username, password);
        return this.returnObject(new HashMap<String, Object>() {{
            put("token", authToken);
        }});
    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    @NotOperationAction
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return authService.info(this.getUserIdFromAuthToken(request));
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    @ResponseBody
    @NotOperationAction
    public JSONObject logout(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        authService.logout(authToken);
        return this.returnObject();
    }

    @RequestMapping(value = {"/reset/password"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject resetPwd(HttpServletRequest request, @NotBlank String oldPwd, @NotBlank @Size(min = 6, max = 15) String newPwd) throws Exception {
        authService.resetPwd(this.getUserFromAuthToken(request), oldPwd, newPwd);
        return this.returnObject();
    }

}
