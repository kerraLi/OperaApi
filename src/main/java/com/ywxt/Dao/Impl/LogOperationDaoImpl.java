package com.ywxt.Dao.Impl;

import com.ywxt.Dao.LogOperationDao;
import com.ywxt.Domain.LogOperation;
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

@Repository("logOperationDao")
public class LogOperationDaoImpl implements LogOperationDao {

    @PersistenceContext
    private EntityManager em;

    // 新建
    @Transactional
    public int create(LogOperation logOperation) {
        em.persist(logOperation);
        return logOperation.getId();
    }

    // 更新
    @Transactional
    public LogOperation update(LogOperation logOperation) {
        em.merge(logOperation);
        return logOperation;
    }

    @Transactional
    public LogOperation getLogOperation(int id) {
        return em.find(LogOperation.class, id);
    }


    @Transactional
    public LogOperation getLogOperation(String sessionId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<LogOperation> criteriaQuery = criteriaBuilder.createQuery(LogOperation.class);
        Root<LogOperation> from = criteriaQuery.from(LogOperation.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("sessionId").in(sessionId));
        List<LogOperation> list = em.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        return (LogOperation) list.get(0);
    }

    // 列表查询
    public List<LogOperation> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LogOperation> query = builder.createQuery(LogOperation.class);
        // 查询条件
        Root<LogOperation> root = query.from(LogOperation.class);
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
        Root<LogOperation> root = query.from(LogOperation.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }


    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<LogOperation> root, HashMap<String, Object> params) {
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
