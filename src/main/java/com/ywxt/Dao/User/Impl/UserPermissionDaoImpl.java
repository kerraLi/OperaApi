package com.ywxt.Dao.User.Impl;


import com.ywxt.Dao.User.UserPermissionDao;

import com.ywxt.Domain.User.UserPermission;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userPermissionDao")
public class UserPermissionDaoImpl implements UserPermissionDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long create(UserPermission userPermission) {
        em.persist(userPermission);
        return userPermission.getId();
    }

    @Transactional
    public boolean delete(long id) {
        em.remove(this.getUserPermission(id));
        return true;
    }

    @Transactional
    public boolean delete(String type) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<UserPermission> criteriaQuery = criteriaBuilder.createCriteriaDelete(UserPermission.class);
        Root<UserPermission> from = criteriaQuery.from(UserPermission.class);
        // 设置查询属性
        criteriaQuery.where(from.get("type").in(type));
        em.createQuery(criteriaQuery).executeUpdate();
        return true;
    }

    @Transactional
    public boolean delete(UserPermission userPermission) {
        em.remove(userPermission);
        return true;
    }


    @Transactional
    public UserPermission update(UserPermission userPermission) {
        em.merge(userPermission);
        return userPermission;
    }

    @Transactional
    public UserPermission getUserPermission(long id) {
        return em.find(UserPermission.class, id);
    }


    @Transactional
    public UserPermission getUserPermission(String action) throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserPermission> criteriaQuery = criteriaBuilder.createQuery(UserPermission.class);
        Root<UserPermission> from = criteriaQuery.from(UserPermission.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("action").in(action));
        List<UserPermission> list = em.createQuery(criteriaQuery).getResultList();
        return (UserPermission) list.get(0);
    }

    @Transactional
    public UserPermission getUserPermission(String type, String action, long roleId) {
        List<UserPermission> list = em.createQuery(
                "select new " +
                        "   com.ywxt.Domain.User.UserPermission(" +
                        "       up.id,up.parentId,up.name,up.action,up.type" +
                        "   ) " +
                        "from UserRolePermission urp " +
                        "join urp.userRole ur " +
                        "join urp.userPermission up " +
                        "where ur.id = :roleId " +
                        "and up.type = :type " +
                        "and up.action = :action ", UserPermission.class)
                .setParameter("roleId", roleId)
                .setParameter("type", type)
                .setParameter("action", action)
                .getResultList();
        return (list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public List<UserPermission> getUserPermissionsByRoleId(long roleId) {
        return em.createQuery(
                "select new " +
                        "   com.ywxt.Domain.User.UserPermission(" +
                        "       up.id,up.parentId,up.name,up.action,up.type" +
                        "   ) " +
                        "from UserRolePermission urp " +
                        "join urp.userRole ur " +
                        "join urp.userPermission up " +
                        "where ur.id = :roleId", UserPermission.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    public List<UserPermission> getList(HashMap<String, Object> params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserPermission> query = builder.createQuery(UserPermission.class);
        // 查询条件
        Root<UserPermission> root = query.from(UserPermission.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.where(predicates.toArray(p));
        return em.createQuery(query)
                .getResultList();
    }

    // 列表查询
    public List<UserPermission> getList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserPermission> query = builder.createQuery(UserPermission.class);
        // 查询条件
        Root<UserPermission> root = query.from(UserPermission.class);
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
        Root<UserPermission> root = query.from(UserPermission.class);
        List<Predicate> predicates = this.filterParam(builder, root, params);
        Predicate[] p = new Predicate[predicates.size()];
        query.select(builder.count(root));
        query.where(predicates.toArray(p));
        return em.createQuery(query).getSingleResult().intValue();
    }

    // 过滤params
    private List<Predicate> filterParam(CriteriaBuilder builder, Root<UserPermission> root, HashMap<String, Object> params) {
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
