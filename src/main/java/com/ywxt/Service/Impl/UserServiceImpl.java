package com.ywxt.Service.Impl;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Dao.UserDao;

import com.ywxt.Domain.User;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    public UserDao userDao;

    // 登陆
    public String login(String clientUsername, String clientPassword) throws Exception {
        User u = this.getUserByUsername(clientUsername);
        if (u == null) {
            throw new Exception("账号或密码错误");
        }
        if (!u.getPassword().equals(MD5Utils.md5(clientPassword))) {
            throw new Exception("账号或密码错误");
        }
        String authToken = AuthUtils.createJWT(Parameter.loginTtlMs, u);
        // 存入redis
        new RedisUtils().getJedis().setex(Parameter.redisKeyUserToken.replace("{token}", authToken), Parameter.redisTllUserToken, authToken);
        return authToken;
    }

    // 退出
    public boolean logout(String token) {
        // 删除redis记录
        new RedisUtils().getJedis().del(Parameter.redisKeyUserToken.replace("{token}", token));
        return true;
    }

    // 注册
    public User register(User user) {

        return user;
    }

    // 获取用户
    public User getUserById(long id) {
        return new UserDaoImpl().getUserById(id);
    }

    // 获取用户
    public User getUserByUsername(String username) throws Exception {
        return new UserDaoImpl().getUserByUsername(username);
    }

    @Override
    public List<User> list() {
        return userDao.list();
    }

    @Override
    public Long add(User user) {
        return null;
     /*   String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
//        Long account= userDao.saveUser(user);

        return userDao.saveUser(user);*/
    }

    @Override
    public User login(User model) {
        return null;
    }

    @Override
    public void editPassword(User user) {
        Long id = user.getId();
        String password = MD5Utils.md5(user.getPassword());
        userDao.executeUpdate("user.editpassword", password,id);
    }



}
