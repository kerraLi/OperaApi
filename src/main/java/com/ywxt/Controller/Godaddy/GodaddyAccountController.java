package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Service.Godaddy.GodaddyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/go/account", name = "GO账户")
public class GodaddyAccountController {

    @Autowired
    private GodaddyAccountService godaddyAccountService;

    @NotOperationAction
    @GetMapping(value = {"/list"}, name = "列表")
    public ApiResult list() {
        return ApiResult.successWithObject(godaddyAccountService.getList());
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
