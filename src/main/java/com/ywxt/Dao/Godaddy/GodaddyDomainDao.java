package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyDomain;

import java.util.HashMap;
import java.util.List;

public interface GodaddyDomainDao {
    public abstract int getDomainTotal(HashMap<String, Object> params) throws Exception;

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params) throws Exception;

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    public abstract void saveDomains(List<GodaddyDomain> list);

    public abstract int saveDomain(GodaddyDomain godaddyDomain) throws Exception;

    public abstract void deleteDomainByAccessId(String accessId);
}

