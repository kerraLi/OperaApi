package com.ywxt.Dao.User.Impl;


import com.ywxt.Dao.User.UserDao;
import com.ywxt.Domain.User.User;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long create(User user) {
        em.persist(user);
        return user.getId();
    }

    @Transactional
    public boolean delete(long id) {
        em.remove(this.getUser(id));
        return true;
    }

    @Transactional
    public boolean delete(User user) {
        em.remove(user);
        return true;
    }


    @Transactional
    public User update(User user) {
        em.merge(user);
        return user;
    }

    @Transactional
    public User getUser(long id) {
        return em.find(User.class, id);
    }


    @Transactional
    public User getUser(String username) throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("username").in(username));
        List<User> list = em.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("账号名或密码错误");
        }
        return (User) list.get(0);
    }

    // 列表查询
    public List<User> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        // 查询条件
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
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
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }

    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<User> root, HashMap<String, Object> params) {
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
