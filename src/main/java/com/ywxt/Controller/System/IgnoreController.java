package com.ywxt.Controller.System;

import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.System.Ignore;
import com.ywxt.Service.System.IgnoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/ignore", name = "参数忽略")
public class IgnoreController {

    @Autowired
    private IgnoreService ignoreService;

    // 批量设置mark
    @PostMapping(value = {"/set/batch/{status}"}, name = "批量标记")
    public ApiResult ecsParamSetAll(@PathVariable String status, @RequestBody ArrayList<Ignore> list) throws Exception {
        ignoreService.setMarked(status, list);
        return ApiResult.success();
    }

    // 设置mark
    @PostMapping(value = {"/set/{status}"}, name = "标记")
    public ApiResult ecsParamSet(@PathVariable String status, @RequestBody Ignore ignore) throws Exception {
        ignoreService.setMarked(status, ignore);
        return ApiResult.success();
    }
}
