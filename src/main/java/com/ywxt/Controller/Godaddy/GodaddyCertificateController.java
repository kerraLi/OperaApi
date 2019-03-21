package com.ywxt.Controller.Godaddy;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Service.Godaddy.GodaddyCertificateService;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/go/certificate", name = "GO证书")
public class GodaddyCertificateController extends CommonController {

    @Autowired
    private GodaddyCertificateService godaddyCertificateService;

    // certificate 列表
    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult certificateList(HttpServletRequest request) throws Exception {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(godaddyCertificateService.getList(params));
    }

}
