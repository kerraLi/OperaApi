package com.ywxt.Service.Godaddy;

import com.alibaba.fastjson.JSONArray;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public interface GodaddyService {

    public abstract void freshSourceData() throws Exception;

    public abstract void freshDomain() throws Exception;

    public abstract void freshCertificate() throws Exception;

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params) throws Exception;

    public abstract JSONObject getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    public abstract List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception;

    public abstract JSONObject getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

}
