package com.ywxt.Service.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyDomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GodaddyDomainService {

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params) throws Exception;

    public abstract Map<String, Object> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

}
