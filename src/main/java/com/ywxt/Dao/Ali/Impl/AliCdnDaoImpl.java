package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliCdnDao;
import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Ali.AliCdn;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class AliCdnDaoImpl extends CommonDao implements AliCdnDao {

    protected String domain = "AliCdn";

    public AliCdn getCdn(int id) {
        try {
            session.beginTransaction();
            AliCdn aliCdn = (AliCdn) session.get(AliCdn.class, id);
            session.getTransaction().commit();
            return aliCdn;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 获取数量
    public int getCdnTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = getCriteria(AliCdn.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 分页查找
    public List<AliCdn> getCdnList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = getCriteria(AliCdn.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<AliCdn> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 保存
    public void saveAliCdns(List<AliCdn> list) {
        for (AliCdn aliCdn : list) {
            session.save(aliCdn);
        }
        this.closeSession();
    }

    // 删除
    public void deleteAliCdnByAccessId(String accessId) {
        String hqlDelete = "delete AliCdn where accessKeyId = :accessKeyId";
        try {
            session.beginTransaction();
            session.createQuery(hqlDelete)
                    .setParameter("accessKeyId", accessId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
    }

}
