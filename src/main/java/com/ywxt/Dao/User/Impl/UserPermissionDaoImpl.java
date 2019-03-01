package com.ywxt.Dao.User.Impl;


import com.ywxt.Dao.User.UserPermissionDao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("userPermissionDao")
public class UserPermissionDaoImpl implements UserPermissionDao {

    @PersistenceContext
    private EntityManager em;

}
