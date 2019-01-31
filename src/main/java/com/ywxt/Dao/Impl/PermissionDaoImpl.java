package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;


import org.hibernate.Criteria;
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
        try {
            session.beginTransaction();
            session.update(permission);
            session.getTransaction().commit();
        } catch (Exception e) {
           session.getTransaction().rollback();
           throw e;
        } finally {
            session.close();
        }

    }

    @Override
    public int add(Permission permission) {
        try {
            session.beginTransaction();
           int  id = (int) session.save(permission);
            session.getTransaction().commit();
            return id;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            session.beginTransaction();
            Permission permission = session.get(Permission.class, id);
            session.delete(permission);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
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
        return permission;
    }


}
