package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Service.RoleService;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;


@Validated
@Controller
@RequestMapping("/user")
public class UserController extends CommonController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    @PassToken
    public JSONObject login(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password) throws Exception {
        String authToken = new UserServiceImpl().login(username, password);
        return this.returnObject(new HashMap<String, Object>() {{
            put("token", authToken);
        }});
    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.getUserFromAuthToken(request);
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        new UserServiceImpl().logout(authToken);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    @ResponseBody
    @PassToken
    public void test() {
        System.out.println(22222222);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (NullPointerException e) {
            System.out.println(11111111);
            System.out.println(e.getClass());
        }
        System.out.println(333333);
    }


    @RequestMapping("/list")
    public List<User> list(){
            List<User>users =userService.list();
            return users;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody User User){
        try {
            Long account=  userService.add(user);
            if (account>0){
                return new Result(true,"增加成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"增加失败");
    }



}
