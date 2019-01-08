package com.ywxt.Dao;

import com.ywxt.Domain.AliEcs;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.List;

public interface AliEcsDao {

    public abstract int getAliEcsesTotal(HashMap<String, Object> params);

    public abstract List<AliEcs> getAliEcsesList(HashMap<String, Object> params);

    public abstract List<AliEcs> getAliEcsesList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public abstract void saveAliEcses(List<AliEcs> list);

    public abstract int saveAliEcs(AliEcs aliEcs);

}
