package com.ywxt.Dao.Resource.Impl;

import com.ywxt.Dao.Resource.CategoryDao;
import com.ywxt.Domain.Resource.Category;
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

@Repository("categoryDao")
public class CategoryDaoImpl implements CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public int create(Category category) {
        em.persist(category);
        return category.getId();
    }

    @Transactional
    public boolean delete(int id) {
        em.remove(this.getCategory(id));
        return true;
    }

    @Transactional
    public boolean delete(Category category) {
        em.remove(category);
        return true;
    }

    @Transactional
    public Category update(Category category) {
        em.merge(category);
        return category;
    }

    @Transactional
    public Category getCategory(int id) {
        return em.find(Category.class, id);
    }

    public List<Category> getList(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        // 查询条件
        Root<Category> root = query.from(Category.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        // 默认按照id倒叙
        query.orderBy(builder.desc(root.get("id")));
        return em.createQuery(query)
                .getResultList();
    }

    public int getListTotal(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        // 查询条件
        Root<Category> root = query.from(Category.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();

    }

    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<Category> root, HashMap<String, Object> params) {
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
