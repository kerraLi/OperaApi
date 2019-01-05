package com.ywxt.Service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.ywxt.Service.GodaddyService;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;

import java.util.HashMap;
import java.util.Map;

public class GodaddyServiceImpl implements GodaddyService {

    private String accessKeyId;
    private String accessKeySecret;

    public GodaddyServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取数据
    private String getData(String action, String paramContext) throws Exception {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Authorization", "sso-key " + this.accessKeyId + ":" + this.accessKeySecret);
        headerParams.put("Content-type", "application/json");
        return HttpUtils.sendConnGet(Parameter.godaddyUrl + Parameter.godaddyActions.get(action), paramContext, headerParams);
    }

    // domain-查询所有域名
    public JSONArray getDomainList(Integer limit, String markerDomain) throws Exception {
        String paramContext = HttpUtils.getParamContext(new HashMap<>() {{
            put("limit", limit.toString());
            put("marker", markerDomain);
        }});
        return JSONArray.parseArray(this.getData("GET_DOMAIN_LIST", paramContext));
    }

    // certificates-查询所有证书
    public JSONArray getCertificateList() throws Exception {
        String paramContext = "";
        return JSONArray.parseArray(this.getData("GET_CERTIFICATE_LIST", paramContext));
    }

}
