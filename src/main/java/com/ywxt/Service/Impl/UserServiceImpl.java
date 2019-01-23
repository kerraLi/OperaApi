package com.ywxt.Service.Impl;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.Role;
import com.ywxt.Domain.User;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    public UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
//        Long account= userDao.saveUser(user);

        return userDao.saveUser(user);
    }

    @Override
    public User login(User model) {
        return null;
    }

    @Override
    public void editPassword(User user) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= null;
        try {
            user = userDao.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null==user){
                throw new UsernameNotFoundException("用户名不存在");
            }
        // 第一个参数为用户名
        // 第二个参数为密码
        // 第三个参数为authorities 权限信息集合
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // 获取用户所拥有的角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 根据角色名称创建授权信息
            // 添加到集合中
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        org.springframework.security.core.userdetails.User user2 = new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        return user2;
    }

}
