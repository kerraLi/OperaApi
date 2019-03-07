package com.ywxt.Controller.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.Repository.UserRepository;
import com.ywxt.Service.User.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;

@Validated
@Controller
@RequestMapping("/user")
public class UserController extends CommonController {

    @Resource
    private UserService userService;
    @Resource
    private UserRepository userRepository;

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    @PassToken
    @NotOperationAction
    public JSONObject login(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password) throws Exception {
        String authToken = userService.login(username, password);
        return this.returnObject(new HashMap<String, Object>() {{
            put("token", authToken);
        }});
    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    @NotOperationAction
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userRepository.getUserWithRoles(this.getUserIdFromAuthToken(request));
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    @ResponseBody
    @NotOperationAction
    public JSONObject logout(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        userService.logout(authToken);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/reset/password"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject resetPwd(HttpServletRequest request, @NotBlank String oldPwd, @NotBlank @Size(min = 6, max = 15) String newPwd) throws Exception {
        userService.resetPwd(this.getUserFromAuthToken(request), oldPwd, newPwd);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

}
