package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.Godaddy.GodaddyDomainDao;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class GodaddyDomainDaoImpl extends CommonDao implements GodaddyDomainDao {

    protected String domain = "GodaddyDomain";

    public GodaddyDomain getDomain(int id) {
        try {
            session.beginTransaction();
            GodaddyDomain godaddyDomain = (GodaddyDomain) session.get(GodaddyDomain.class, id);
            session.getTransaction().commit();
            return godaddyDomain;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 获取数量
    public int getDomainTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyDomain.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 查找所有
    public List<GodaddyDomain> getDomainList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyDomain.class, params);
        List<GodaddyDomain> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 分页查找
    public List<GodaddyDomain> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyDomain.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<GodaddyDomain> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 批量保存
    public void saveDomains(List<GodaddyDomain> list) {
        for (GodaddyDomain godaddyDomain : list) {
            session.save(godaddyDomain);
        }
        this.closeSession();
    }

    // 保存更新
    public int saveDomain(GodaddyDomain godaddyDomain) {
        int id = (int) session.save(godaddyDomain);
        this.closeSession();
        return id;
    }

    // 删除
    public void deleteDomainByAccessId(String accessId) {
        String hqlDelete = "delete " + this.domain + " where accessKeyId = :accessKeyId";
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
