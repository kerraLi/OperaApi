package com.ywxt.Service.User.Impl;

import com.ywxt.Dao.User.UserDao;

import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;


import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    // 登陆
    public String login(String clientUsername, String clientPassword) throws Exception {
        User u = userDao.getUser(clientUsername);
        if (u == null) {
            throw new Exception("账号或密码错误");
        }
        if (!u.getPassword().equals(MD5Utils.md5(clientPassword))) {
            throw new Exception("账号或密码错误");
        }
        String authToken = AuthUtils.createJWT(Parameter.loginTtlMs, u);
        // 存入redis(单位秒)
        new RedisUtils().getJedis().setex(Parameter.redisKeyUserToken.replace("{token}", authToken), Parameter.redisTllUserToken, authToken);
        return authToken;
    }

    // 修改密码
    public void resetPwd(User user, String oldPwd, String newPwd) throws Exception {
        if (!user.getPassword().equals(MD5Utils.md5(oldPwd))) {
            throw new Exception("账号或密码错误");
        }
        user.setPassword(MD5Utils.md5(newPwd));
        userDao.update(user);
    }

    // 退出
    public boolean logout(String token) {
        // 删除redis记录
        new RedisUtils().getJedis().del(Parameter.redisKeyUserToken.replace("{token}", token));
        return true;
    }

    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    public User getUser(String username) throws Exception {
        return userDao.getUser(username);
    }

}
