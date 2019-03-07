package com.ywxt.Controller.User;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Service.User.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/permission", name = "用户权限")
public class PermissionController extends CommonController {

    @Resource
    private PermissionService permissionService;


    @ResponseBody
    @RequestMapping(value = {"/list/{type}"}, name = "列表", method = RequestMethod.POST)
    public List<UserPermission> list(@PathVariable String type) throws Exception {
        return permissionService.getList(type);
    }

    // 重新生成所有接口权限(建议每次升级后需重置一次)
    // * 要求：只有配置了名称的接口会自动生成权限
    @ResponseBody
    @RequestMapping(value = {"/api/refresh"}, name = "重置接口权限", method = RequestMethod.POST)
    public JSONObject refreshApi(HttpServletRequest request) throws Exception {
        List<UserPermission> ups = permissionService.refreshApi(request);
        return this.returnObject(new HashMap<String, Object>() {{
            put("permissions", ups);
        }});
    }

    // 菜单权限：增删改查，由前端控制，字段需要跟前端一致，或者由前端生成权限菜单列表
    @ResponseBody
    @RequestMapping(value = {"/menu/refresh"}, name = "重置菜单权限", method = RequestMethod.POST)
    public JSONObject refreshMenu(@RequestBody ArrayList<JSONObject> list) {
        List<UserPermission> ups = permissionService.refreshMenu(list);
        return this.returnObject(new HashMap<String, Object>() {{
            put("permissions", ups);
        }});
    }
}
