package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliEcs;

import java.util.HashMap;
import java.util.List;

public interface AliEcsDao {

    public abstract int getAliEcsesTotal(HashMap<String, Object> params) throws Exception;

    public abstract List<AliEcs> getAliEcsesList(HashMap<String, Object> params) throws Exception;

    public abstract List<AliEcs> getAliEcsesList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    public abstract void saveAliEcses(List<AliEcs> list);

    public abstract int saveAliEcs(AliEcs aliEcs) throws Exception;

}
