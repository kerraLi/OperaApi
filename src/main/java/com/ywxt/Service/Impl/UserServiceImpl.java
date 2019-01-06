package com.ywxt.Service.Impl;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.User;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    // 登陆
    public String login(String clientUsername, String clientPassword) throws Exception {
        User u = this.getUserByUsername(clientUsername);
        if (u == null) {
            throw new Exception("账号或密码错误");
        }
        if (!u.getPassword().equals(MD5Utils.md5(clientPassword))) {
            throw new Exception("账号或密码错误");
        }
        return AuthUtils.createJWT(Parameter.loginTtlMs, u);
    }

    // 注册
    public User register(User user) {

        return user;
    }

    // 获取用户
    public User getUserById(long id) {
        return this.userDao.getUserById(id);
    }

    // 获取用户
    public User getUserByUsername(String username) {
        return this.userDao.getUserByUsername(username);
    }
}
