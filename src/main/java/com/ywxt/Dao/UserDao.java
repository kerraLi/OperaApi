package com.ywxt.Dao;

import com.ywxt.Domain.User;
import com.ywxt.Utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;


public interface UserDao /*extends IBaseDao<User>*/{

    public abstract Long saveUser(User user);

    public abstract User getUserById(long id);

    public abstract User getUserByUsername(String username) throws Exception;

    List<User> list();


    Long add(String username, String password);
}
