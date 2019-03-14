package com.ywxt.Controller.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.User.UserRole;
import com.ywxt.Service.User.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping(value = "/role", name = "用户角色")
public class RoleController extends CommonController {

    @Resource
    private RoleService roleService;

    @ResponseBody
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public List<UserRole> list() throws Exception {
        return roleService.getList();
    }

    @ResponseBody
    @RequestMapping(value = {"/list/valid"}, name = "有效列表", method = RequestMethod.POST)
    public List<UserRole> listValid() throws Exception {
        return roleService.getListValid();
    }

    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    public JSONObject save(@RequestBody UserRole userRole) throws Exception {
        roleService.save(userRole);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"}, name = "删除", method = RequestMethod.POST)
    public JSONObject delete(HttpServletRequest request, @PathVariable Long id) throws Exception {
        if (roleService.remove(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }

}