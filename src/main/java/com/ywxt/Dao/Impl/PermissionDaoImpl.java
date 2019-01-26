package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;


import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;

@Repository
public class PermissionDaoImpl extends CommonDao /*BaseDaoImpl<Permission>*/ implements PermissionDao {


/*

    @Override
    public void save(Permission entity) {

    }

    @Override
    public void delete(Permission entity) {

    }

    @Override
    public void update(Permission entity) {

    }

    @Override
    public void saveOrUpdate(Permission entity) {

    }*/

    @Override
    public List<Permission> list() {
        return null;
    }

    @Override
    public List<String> findAllUrl() {
        String hql="";
        Query query = session.createQuery(hql);

        return null;
    }

}
