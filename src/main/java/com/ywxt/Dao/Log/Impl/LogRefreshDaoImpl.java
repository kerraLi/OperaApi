package com.ywxt.Dao.Log.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.Log.LogRefreshDao;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.Log.LogRefresh;
import org.hibernate.criterion.Order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class LogRefreshDaoImpl extends CommonDao implements LogRefreshDao {

    public LogRefresh getLast(String type) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LogRefresh> criteriaQuery = criteriaBuilder.createQuery(LogRefresh.class);
        Root<LogRefresh> from = criteriaQuery.from(LogRefresh.class);
        // 设置查询属性
        criteriaQuery
                .select(from)
                .where(from.get("type").in(type))
                .orderBy(criteriaBuilder.desc(from.get("time")));
        List<LogRefresh> list = session.createQuery(criteriaQuery)
                .setMaxResults(1)
                .getResultList();
        this.closeSession();
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    // 保存更新
    public int saveLogRefresh(LogRefresh logRefresh) {
        try {
            session.beginTransaction();
            if (logRefresh.getId() == 0) {
                int id = (Integer) session.save(logRefresh);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(logRefresh);
                session.getTransaction().commit();
                return logRefresh.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }
}
