package com.ywxt.Service;

import com.ywxt.Domain.User;


import java.util.List;

public interface UserService{

    public abstract String login(String clientUsername, String clientPassword) throws Exception;

    // 退出
    public abstract boolean logout(String token);

    // 注册
    public abstract User register(User user);

    // 获取用户
    public abstract User getUserById(long id);

    // 获取用户
    public abstract User getUserByUsername(String username) throws Exception;

//查询所有用户
    List<User> list();
//新增用户
    Long add(User user);
//
    public User login(User user);

    public void editPassword(User user);

    Long add(String username, String password);
}
