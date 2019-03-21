package com.ywxt.Controller.Aws;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Service.Aws.AwsEc2Service;
import com.ywxt.Service.Aws.Impl.AwsEc2ServiceImpl;
import com.ywxt.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/aws/ec2", name = "亚马逊EC2")
public class AwsEc2Controller {

    @Autowired
    private AwsEc2Service awsEc2Service;

    // ec2:服务器列表
    @NotOperationAction
    @PostMapping(value = {"/list"}, name = "列表")
    public ApiResult ecsList(HttpServletRequest request) {
        Map<String, String> params = CommonUtils.preSpringParams(request.getParameterMap());
        return ApiResult.successWithObject(awsEc2Service.getList(params));
    }

}
