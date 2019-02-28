package com.ywxt.Dao.Resource.Impl;

import com.ywxt.Dao.Resource.DataDao;
import com.ywxt.Domain.Resource.Data;
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

@Repository("dataDao")
public class DataDaoImpl implements DataDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public int create(Data data) {
        em.persist(data);
        return data.getId();
    }

    @Transactional
    public boolean delete(int id) {
        em.remove(this.getData(id));
        return true;
    }

    @Transactional
    public boolean delete(Data data) {
        em.remove(data);
        return true;
    }

    @Transactional
    public Data update(Data data) {
        em.merge(data);
        return data;
    }

    @Transactional
    public Data getData(int id) {
        return em.find(Data.class, id);
    }

    public List<Data> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Data> query = builder.createQuery(Data.class);
        // 查询条件
        Root<Data> root = query.from(Data.class);
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
        Root<Data> root = query.from(Data.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }

    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<Data> root, HashMap<String, Object> params) {
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
