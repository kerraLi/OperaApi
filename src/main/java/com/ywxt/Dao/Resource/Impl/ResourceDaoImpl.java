package com.ywxt.Dao.Resource.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ResourceDaoImpl<T> {
    protected Class<T> entityClass;

    @PersistenceContext
    private EntityManager em;

    public ResourceDaoImpl() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
    }

    public void add(T t) {
        em.persist(t);
    }
}
