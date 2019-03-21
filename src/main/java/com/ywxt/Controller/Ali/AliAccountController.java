package com.ywxt.Controller.Ali;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Service.Ali.AliAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ali/account", name = "阿里云账号")
public class AliAccountController {

    @Autowired
    private AliAccountService aliAccountService;

    @NotOperationAction
    @GetMapping(value = {"/list"}, name = "列表")
    public ApiResult list() throws Exception {
        return ApiResult.successWithObject(aliAccountService.getList(true));
    }

    @PostMapping(value = {"/save"}, name = "修改")
    public ApiResult save(@ModelAttribute AliAccount aliAccount) throws Exception {
        aliAccountService.saveAliAccount(aliAccount);
        return ApiResult.success();
    }

    @PostMapping(value = {"/delete/{id}"}, name = "删除")
    public ApiResult delete(@PathVariable Integer id){
        aliAccountService.deleteAccount(id);
        return ApiResult.success();
    }

}
