package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliEcsDao;
import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Ali.AliEcs;
import org.hibernate.*;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class AliEcsDaoImpl extends CommonDao implements AliEcsDao {

    private String domain = "AliEcs";

    public AliEcs getEcs(int id) {
        try {
            session.beginTransaction();
            AliEcs aliEcs = (AliEcs) session.get(AliEcs.class, id);
            session.getTransaction().commit();
            return aliEcs;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    public AliEcs getEcs(String instanceId) throws Exception {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliEcs> criteriaQuery = criteriaBuilder.createQuery(AliEcs.class);
        Root<AliEcs> from = criteriaQuery.from(AliEcs.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("instanceId").in(instanceId));
        List<AliEcs> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("无该服务器");
        }
        AliEcs aliEcs = (AliEcs) list.get(0);
        this.closeSession();
        return aliEcs;
    }

    // group by 查找个数&account
    public List<Object[]> getCountGroup(HashMap<String, Object> params) throws Exception {
        // 数据库限制 group时无法使用order
        params.put("NO_ORDER", true);
        Criteria criteria = this.getCriteria(AliEcs.class, params);
        ProjectionList projectionList = Projections.projectionList();
        // group by theme
        projectionList.add(Projections.groupProperty("status"));
        projectionList.add(Projections.groupProperty("accessKeyId"));
        projectionList.add(Projections.count("id"));
        criteria.setProjection(projectionList);
        List<Object[]> results = criteria.list();
        this.closeSession();
        return results;
    }

    // group by 查找个数&account
    public List<Object[]> getAliEcsesTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 数据库限制 group时无法使用order
        params.put("NO_ORDER", true);
        Criteria criteria = this.getCriteria(AliEcs.class, params);
        ProjectionList projectionList = Projections.projectionList();
        // group by theme
        projectionList.add(Projections.groupProperty("accessKeyId"));
        projectionList.add(Projections.count("id"));
        criteria.setProjection(projectionList);
        List<Object[]> results = criteria.list();
        this.closeSession();
        return results;
    }

    // 获取数量
    public int getAliEcsesTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AliEcs.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 查找所有
    public List<AliEcs> getAliEcsesList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AliEcs.class, params);
        List<AliEcs> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 分页查找
    public List<AliEcs> getAliEcsesList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(AliEcs.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<AliEcs> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 批量保存
    public void saveAliEcses(List<AliEcs> list) {
        for (AliEcs aliEcs : list) {
            session.save(aliEcs);
        }
        this.closeSession();
    }

    // 保存更新
    public int saveAliEcs(AliEcs aliEcs) throws Exception {
        try {
            session.beginTransaction();
            if (aliEcs.getId() == 0) {
                int id = (Integer) session.save(aliEcs);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(aliEcs);
                session.getTransaction().commit();
                return aliEcs.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 删除
    public void deleteAliEcsByAccessId(String accessId) {
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
