package com.ywxt.Controller.Aws;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.AwsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/aws/account", name = "亚马逊账户")
public class AwsAccountController extends CommonController {

    @Autowired
    private AwsAccountService awsAccountService;

    @NotOperationAction
    @GetMapping(value = {"/list"}, name = "列表")
    public ApiResult list() {
        return ApiResult.successWithObject(awsAccountService.getList());
    }

    @PostMapping(value = {"/save"}, name = "修改")
    public ApiResult save(@ModelAttribute AwsAccount awsAccount) {
        awsAccountService.saveAccount(awsAccount);
        return ApiResult.success();
    }

    @PostMapping(value = {"/delete/{id}"}, name = "删除")
    public ApiResult delete(@PathVariable Integer id) {
        awsAccountService.deleteAccount(id);
        return ApiResult.success();
    }
}
