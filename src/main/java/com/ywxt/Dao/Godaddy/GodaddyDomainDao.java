package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyDomain;

import java.util.HashMap;
import java.util.List;

public interface GodaddyDomainDao {
    public abstract int getDomainTotal(HashMap<String, Object> params);

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params);

    public abstract List<GodaddyDomain> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public abstract void saveDomains(List<GodaddyDomain> list);

    public abstract int saveDomain(GodaddyDomain godaddyDomain);

    public abstract void deleteDomainByAccessId(String accessId);
}

