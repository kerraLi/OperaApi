package com.ywxt.Service.Impl;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.User;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserService , UserDetailsService {


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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        return null;
    }
}
