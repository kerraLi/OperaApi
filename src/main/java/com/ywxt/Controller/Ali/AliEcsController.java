package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Service.Ali.AliEcsService;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/ali/ecs", name = "阿里云ECS")
public class AliEcsController extends CommonController {

    @Autowired
    private AliEcsService aliEcsService;

    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult ecsList(HttpServletRequest request) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(aliEcsService.getList(params));
    }

    // ecs:状态更新
    @GetMapping(value = {"/status/update/{id}"}, name = "更新状态")
    public ApiResult updateCdnRefreshTask(@PathVariable Integer id) throws Exception {
        AliEcs aliEcs = aliEcsService.getEcs(id);
        return ApiResult.successWithObject(aliEcsService.updateEcsStatus(aliEcs));
    }

    // ecs:操作服务器状态
    @GetMapping(value = {"/status/{action}/{id}"}, name = "操作状态")
    public ApiResult ecsStatusAction(@PathVariable String action, @PathVariable Integer id) throws Exception {
        aliEcsService.actionEcs(action, id);
        return ApiResult.success();
    }

    // 预付费服务器
    @PostMapping(value = {"/perpay"}, name = "预付费")
    public ApiResult ecsPerPay(@RequestBody JSONObject jsonObject) throws Exception {
        String instanceId = (String) jsonObject.get("instanceId");
        String periodUnit = (String) jsonObject.get("periodUnit");
        int period = Integer.parseInt((String) jsonObject.get("period"));
        return ApiResult.successWithObject(aliEcsService.perPay(instanceId, periodUnit, period));
    }

}
