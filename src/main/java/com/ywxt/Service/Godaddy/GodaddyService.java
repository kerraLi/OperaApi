package com.ywxt.Service.Godaddy;

import com.alibaba.fastjson.JSONArray;

public interface GodaddyService {


    // domain-查询所有域名
    public JSONArray getDomainList(Integer limit, String markerDomain) throws Exception;

    // certificates-查询所有证书
    public JSONArray getCertificateList() throws Exception;


}
