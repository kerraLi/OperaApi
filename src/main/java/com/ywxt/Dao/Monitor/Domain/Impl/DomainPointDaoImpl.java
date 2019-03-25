package com.ywxt.Dao.Monitor.Domain.Impl;

import com.ywxt.Dao.Monitor.Domain.DomainPointDao;
import com.ywxt.Domain.Monitor.Domain.MonitorPoint;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("monitorPointDao")
public class DomainPointDaoImpl implements DomainPointDao {

    @PersistenceContext
    private EntityManager em;

    // 新建
    @Transactional
    public int create(MonitorPoint monitorPoint) {
        em.persist(monitorPoint);
        return monitorPoint.getId();
    }

    @Transactional
    public boolean delete(int id) {
        em.remove(this.getMonitorPoint(id));
        return true;
    }

    @Transactional
    public boolean delete(MonitorPoint monitorPoint) {
        em.remove(monitorPoint);
        return true;
    }

    // 更新
    @Transactional
    public MonitorPoint update(MonitorPoint monitorPoint) {
        em.merge(monitorPoint);
        return monitorPoint;
    }

    @Transactional
    public MonitorPoint getMonitorPoint(int id) {
        return em.find(MonitorPoint.class, id);
    }


    @Transactional
    public MonitorPoint getMonitorPoint(String path) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorPoint> criteriaQuery = criteriaBuilder.createQuery(MonitorPoint.class);
        Root<MonitorPoint> from = criteriaQuery.from(MonitorPoint.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("path").in(path));
        List<MonitorPoint> list = em.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        return (MonitorPoint) list.get(0);
    }

    // 列表查询
    public List<MonitorPoint> getList(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorPoint> query = builder.createQuery(MonitorPoint.class);
        // 查询条件
        Root<MonitorPoint> root = query.from(MonitorPoint.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        // 默认按照id倒叙
        query.orderBy(builder.desc(root.get("id")));
        return em.createQuery(query)
                .getResultList();
    }


    // 列表查询
    public List<MonitorPoint> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorPoint> query = builder.createQuery(MonitorPoint.class);
        // 查询条件
        Root<MonitorPoint> root = query.from(MonitorPoint.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        // 默认按照id倒叙
        query.orderBy(builder.desc(root.get("id")));
        return em.createQuery(query)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    // 数量查询
    public int getListTotal(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        // 查询条件
        Root<MonitorPoint> root = query.from(MonitorPoint.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }


    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<MonitorPoint> root, HashMap<String, Object> params) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                String[] strings = e.getKey().split("@");
                if (strings.length == 1) {
                    predicates.add(builder.equal(root.get(e.getKey()), e.getValue()));
                } else if (strings[1].equals("like")) {
                    predicates.add(builder.like(root.get(strings[0]), "%" + e.getValue() + "%"));
                }
            }
        }
        return predicates;
    }
}
