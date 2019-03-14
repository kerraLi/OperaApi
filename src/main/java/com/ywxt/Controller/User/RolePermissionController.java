package com.ywxt.Controller.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Service.User.RolePermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/role/permission", name = "角色权限")
public class RolePermissionController extends CommonController {

    @Resource
    private RolePermissionService rolePermissionService;

    @ResponseBody
    @RequestMapping(value = {"/save"}, name = "保存", method = RequestMethod.POST)
    public JSONObject savePermission(long roleId, long[] pIds) throws Exception {
        rolePermissionService.saveRolePermissions(roleId, pIds);
        return this.returnObject();
    }

    @ResponseBody
    @NotOperationAction
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    public List<UserPermission> getPermissions(long roleId) throws Exception {
        return rolePermissionService.getRolePermissions(roleId);
    }
}
