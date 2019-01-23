package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PermissionDaoImpl extends BaseDaoImpl<Permission> implements PermissionDao {




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

    }

    @Override
    public List<Permission> list() {
        return null;
    }
}
