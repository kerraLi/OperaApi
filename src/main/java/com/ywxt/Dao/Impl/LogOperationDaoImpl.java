package com.ywxt.Dao.Impl;

import com.ywxt.Dao.LogOperationDao;
import com.ywxt.Domain.LogOperation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

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
}
