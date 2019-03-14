package com.ywxt.Controller.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Validated
@Controller
@RequestMapping(value = "/user", name = "用户管理")
public class UserController extends CommonController {

    @Resource
    private UserService userService;

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/list"}, name = "用户列表", method = RequestMethod.POST)
    public JSONObject list(HttpServletRequest request) throws Exception {
        int pageNumber = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        return userService.getList(params, pageNumber, pageSize);
    }


    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "保存用户", method = RequestMethod.POST)
    public User save(@RequestBody User user) throws Exception {
        if (user.getUsername().equals("admin")) {
            throw new Exception("非法用户名。");
        }
        return userService.save(user);
    }

    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"}, name = "删除用户", method = RequestMethod.POST)
    public JSONObject remove(@PathVariable long id) throws Exception {
        userService.remove(id);
        return this.returnObject();
    }
}
