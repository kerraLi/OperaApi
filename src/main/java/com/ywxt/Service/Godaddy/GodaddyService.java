package com.ywxt.Service.Godaddy;

import com.alibaba.fastjson.JSONArray;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GodaddyService {

    public abstract void freshSourceData() throws Exception;

    public abstract void freshDomain() throws Exception;

    public abstract void freshCertificate() throws Exception;

}
