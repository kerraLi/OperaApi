package com.ywxt.Controller.Ali;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Service.Ali.AliCdnService;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/ali/cdn", name = "阿里云CDN")
public class AliCdnController extends CommonController {

    @Autowired
    private AliCdnService aliCdnService;

    // cdn域名列表
    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult cdnDomainList(HttpServletRequest request) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(aliCdnService.getList(params));
    }

    // cdn:节点刷新&预热
    @ResponseBody
    @PostMapping(value = {"/refresh"}, name = "刷新&预热")
    public ApiResult cdnRefreshObjectCache(String operateType, String refreshType, String content) throws Exception {
        return ApiResult.successWithObject(aliCdnService.refreshCdn(operateType, refreshType, content));
    }

    // cdn:节点刷新任务查看
    @ResponseBody
    @PostMapping(value = {"/refresh/task/list"}, name = "查看刷新任务")
    public ApiResult getCdnRefreshTaskList(HttpServletRequest request) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(aliCdnService.getTaskList(params));
    }

    // cdn:节点刷新任务状态更新
    @ResponseBody
    @GetMapping(value = {"/refresh/task/update/{id}"}, name = "查看刷新任务状态")
    public ApiResult updateCdnRefreshTask(@PathVariable Integer id) throws Exception {
        return ApiResult.successWithObject(aliCdnService.updateTask(id));
    }

}
