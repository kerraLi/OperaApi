package com.ywxt.Controller.Ali;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.Ali.AliAccountService;
import com.ywxt.Service.User.RolePermissionService;
import com.ywxt.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/ali/account", name = "阿里云账号")
public class AliAccountController extends CommonController {

    @Autowired
    private AliAccountService aliAccountService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @NotOperationAction
    @GetMapping(value = {"/list"}, name = "列表")
    public ApiResult list(HttpServletRequest request) throws Exception {
        User u = this.getUserFromAuthToken(request);
        boolean showSecretePermission = rolePermissionService.checkRolePermission("special", "AliAccountSecrete", u.getRole().getId());
        return ApiResult.successWithObject(new HashMap<>() {{
            put("list", aliAccountService.getList(true, showSecretePermission));
            put("isShowSecrete", !showSecretePermission);
        }});
    }

    @PostMapping(value = {"/save"}, name = "修改")
    public ApiResult save(@ModelAttribute AliAccount aliAccount) throws Exception {
        aliAccountService.saveAliAccount(aliAccount);
        return ApiResult.success();
    }

    @PostMapping(value = {"/delete/{id}"}, name = "删除")
    public ApiResult delete(@PathVariable Integer id) {
        aliAccountService.deleteAccount(id);
        return ApiResult.success();
    }

}
