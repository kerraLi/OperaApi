package com.ywxt.Dao.Monitor.Domain.Impl;

import com.ywxt.Dao.Monitor.Domain.DomainDomainDao;
import com.ywxt.Domain.Monitor.Domain.MonitorDomain;
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

@Repository("monitorDomainDao")
public class DomainDomainDaoImpl implements DomainDomainDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public int create(MonitorDomain monitorDomain) {
        em.persist(monitorDomain);
        return monitorDomain.getId();
    }

    @Transactional
    public boolean delete(int id) {
        em.remove(this.getMonitorDomain(id));
        return true;
    }

    @Transactional
    public boolean delete(MonitorDomain monitorDomain) {
        em.remove(monitorDomain);
        return true;
    }


    // 更新
    @Transactional
    public MonitorDomain update(MonitorDomain monitorDomain) {
        em.merge(monitorDomain);
        return monitorDomain;
    }

    @Transactional
    public MonitorDomain getMonitorDomain(int id) {
        return em.find(MonitorDomain.class, id);
    }


    @Transactional
    public MonitorDomain getMonitorDomain(String path) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorDomain> criteriaQuery = criteriaBuilder.createQuery(MonitorDomain.class);
        Root<MonitorDomain> from = criteriaQuery.from(MonitorDomain.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("path").in(path));
        List<MonitorDomain> list = em.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        return (MonitorDomain) list.get(0);
    }

    // 列表查询
    public List<MonitorDomain> getList(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorDomain> query = builder.createQuery(MonitorDomain.class);
        // 查询条件
        Root<MonitorDomain> root = query.from(MonitorDomain.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        // 默认按照id倒叙
        query.orderBy(builder.desc(root.get("id")));
        return em.createQuery(query)
                .getResultList();
    }

    // 列表查询
    public List<MonitorDomain> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MonitorDomain> query = builder.createQuery(MonitorDomain.class);
        // 查询条件
        Root<MonitorDomain> root = query.from(MonitorDomain.class);
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
        Root<MonitorDomain> root = query.from(MonitorDomain.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }


    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<MonitorDomain> root, HashMap<String, Object> params) {
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
