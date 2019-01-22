package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PermissionDaoImpl extends CommonDao implements PermissionDao {
    @Override
    public List<Permission> list() {
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Permission.class);
            List<Permission>permissions= criteria.list();
            session.getTransaction().commit();
            return permissions;
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }

    }

    @Override
    public int add(Permission permission) {
        return 0;
    }
}
