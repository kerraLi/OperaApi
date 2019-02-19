package com.ywxt.Dao.Resource.Impl;

import com.ywxt.Dao.Resource.TypeDao;
import com.ywxt.Domain.Resource.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository("typeDao")
public class TypeDaoImpl implements TypeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public int create(Type type) {
        em.persist(type);
        return type.getId();
    }

    @Transactional
    public boolean delete(int id) {
        em.remove(this.getType(id));
        return true;
    }

    @Transactional
    public boolean delete(Type type) {
        em.remove(type);
        return true;
    }

    @Transactional
    public Type update(Type type) {
        em.merge(type);
        return type;
    }

    @Transactional
    public Type getType(int id) {
        return em.find(Type.class, id);
    }

    public List<Type> getList() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Type> query = builder.createQuery(Type.class);
        Root<Type> root = query.from(Type.class);
        query.orderBy(builder.asc(root.get("id")));
        return em.createQuery(query)
                .getResultList();
    }


}
