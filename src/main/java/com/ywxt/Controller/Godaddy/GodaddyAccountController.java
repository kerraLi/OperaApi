package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.Godaddy.GodaddyAccountService;
import com.ywxt.Service.User.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/go/account", name = "GO账户")
public class GodaddyAccountController extends CommonController {

    @Autowired
    private GodaddyAccountService godaddyAccountService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @NotOperationAction
    @GetMapping(value = {"/list"}, name = "列表")
    public ApiResult list(HttpServletRequest request) {
        User u = this.getUserFromAuthToken(request);
        boolean showSecretePermission = rolePermissionService.checkRolePermission("special", "GodaddyAccountSecrete", u.getRole().getId());
        return ApiResult.successWithObject(new HashMap<>() {{
            put("list", godaddyAccountService.getList(showSecretePermission));
            put("isShowSecrete", !showSecretePermission);
        }});
    }

    @PostMapping(value = {"/save"}, name = "修改")
    public ApiResult save(@ModelAttribute GodaddyAccount godaddyAccount) throws Exception {
        godaddyAccountService.saveAccount(godaddyAccount);
        return ApiResult.success();
    }

    @PostMapping(value = {"/delete/{id}"}, name = "删除")
    public ApiResult delete(@PathVariable Integer id) {
        godaddyAccountService.deleteAccount(id);
        return ApiResult.success();
    }
}
