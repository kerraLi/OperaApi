package com.ywxt.Dao;

import com.ywxt.Domain.User;

import java.util.List;


public interface UserDao {

    public abstract Long saveUser(User user);

    public abstract User getUserById(long id);

    public abstract User getUserByUsername(String username) throws Exception;

    List<User> list();


    Long add(String username, String password);
}
