package com.ywxt.Dao.Ali;

import com.ywxt.Domain.AliCdn;

import java.util.HashMap;
import java.util.List;

public interface AliCdnDao {

    public int getCdnTotal(HashMap<String, Object> params);

    public abstract List<AliCdn> getCdnList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public abstract void saveAliCdns(List<AliCdn> list);

    public abstract void deleteAliCdnByAccessId(String accessId);
}
