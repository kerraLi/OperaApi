package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.GodaddyDomainService;
import com.ywxt.Service.Godaddy.Impl.GodaddyDomainServiceImpl;
import com.ywxt.Service.System.Impl.IgnoreServiceImpl;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/go/domain", name = "GO域名")
public class GodaddyDomainController extends CommonController {

    @Autowired
    private GodaddyDomainService godaddyDomainService;

    // domain 列表
    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult domainList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(godaddyDomainService.getList(params));
    }

}
