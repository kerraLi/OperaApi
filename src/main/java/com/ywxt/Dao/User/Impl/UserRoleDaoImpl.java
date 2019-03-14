package com.ywxt.Dao.User.Impl;


import com.ywxt.Dao.User.UserRoleDao;

import com.ywxt.Domain.User.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("userRoleDao")
public class UserRoleDaoImpl implements UserRoleDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long create(UserRole userRole) {
        em.persist(userRole);
        return userRole.getId();
    }

    @Transactional
    public boolean delete(long id) {
        em.remove(this.getUserRole(id));
        return true;
    }

    @Transactional
    public boolean delete(String type) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<UserRole> criteriaQuery = criteriaBuilder.createCriteriaDelete(UserRole.class);
        Root<UserRole> from = criteriaQuery.from(UserRole.class);
        // 设置查询属性
        criteriaQuery.where(from.get("type").in(type));
        em.createQuery(criteriaQuery).executeUpdate();
        return true;
    }

    @Transactional
    public boolean delete(UserRole userRole) {
        em.remove(userRole);
        return true;
    }


    @Transactional
    public UserRole update(UserRole userRole) {
        em.merge(userRole);
        return userRole;
    }

    @Transactional
    public UserRole getUserRole(long id) {
        return em.find(UserRole.class, id);
    }


    @Transactional
    public UserRole getUserRole(String code) throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserRole> criteriaQuery = criteriaBuilder.createQuery(UserRole.class);
        Root<UserRole> from = criteriaQuery.from(UserRole.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("code").in(code));
        List<UserRole> list = em.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        return (UserRole) list.get(0);
    }

    public List<UserRole> getListValid() {
        return em.createQuery(
                "select distinct new " +
                        "   com.ywxt.Domain.User.UserRole(" +
                        "       ur.id,ur.name,ur.code" +
                        "   ) " +
                        "from UserRolePermission urp " +
                        "join urp.userRole ur ", UserRole.class)
                .getResultList();
    }

    public List<UserRole> getList(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        // 查询条件
        Root<UserRole> root = query.from(UserRole.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        return em.createQuery(query)
                .getResultList();
    }

    // 列表查询
    public List<UserRole> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        // 查询条件
        Root<UserRole> root = query.from(UserRole.class);
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
        Root<UserRole> root = query.from(UserRole.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }

    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<UserRole> root, HashMap<String, Object> params) {
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
