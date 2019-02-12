package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;


import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PermissionDaoImpl extends CommonDao  implements PermissionDao {

    @Override
    public List<Permission> list() {
        List<Permission> list = session.createCriteria(Permission.class).list();
        return list;
    }

    @Override
    public List<String> findAllUrl() {
        Criteria criteria = session.createCriteria(Permission.class);
        List<String>urls=new ArrayList<>();
        List<Permission> lists = criteria.list();
        for (Permission list : lists) {
            urls.add(list.getUrl());
        }
        return urls;
    }

    @Override
    public void update(Permission permission) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
            session.merge(permission);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void save(Permission permission) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
           session.save(permission);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
            Permission permission = session.get(Permission.class, id);
            session.delete(permission);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    @Override
    public Permission findByUrl(String url) {
        Criteria criteria = session.createCriteria(Permission.class);
        criteria.add(Expression.eq("url",url));
        List<Permission> list = criteria.list();
        Permission permission = list.get(0);
        session.close();
        return permission;
    }

    @Override
    public Permission findPermissionById(Long id) {
        Permission permission = session.get(Permission.class, id);
        session.close();
        return permission;
    }


}
