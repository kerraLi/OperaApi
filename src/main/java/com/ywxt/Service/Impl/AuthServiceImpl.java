package com.ywxt.Service.Impl;

import com.ywxt.Dao.User.UserDao;
import com.ywxt.Domain.User.User;
import com.ywxt.Domain.User.UserRole;
import com.ywxt.Service.AuthService;
import com.ywxt.Service.User.RoleService;
import com.ywxt.Service.User.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserDao userDao;

    @Override
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

    @Override
    public User info(Long userId) throws Exception {
        User user = userDao.getUser(userId);
        return this.formatUserRoles(user);
    }

    @Override
    public void resetPwd(User user, String oldPwd, String newPwd) throws Exception {
        if (!user.getPassword().equals(MD5Utils.md5(oldPwd))) {
            throw new Exception("账号或密码错误");
        }
        user.setPassword(MD5Utils.md5(newPwd));
        userDao.update(user);
    }

    @Override
    public boolean logout(String token) {
        // 删除redis记录
        new RedisUtils().getJedis().del(Parameter.redisKeyUserToken.replace("{token}", token));
        return true;
    }

    // 格式化User roles & menus
    private User formatUserRoles(User user) throws Exception {
        UserRole role = user.getRole();
        List<String> rolesCodes = new ArrayList<>();
        rolesCodes.add(role.getCode());
        user.setRoles(rolesCodes.toArray(new String[rolesCodes.size()]));
        if (!role.getCode().equals("admin")) {
            List<String> permissionMenus = new ArrayList<>();
            // todo menu
            user.setMenus(permissionMenus.toArray(new String[permissionMenus.size()]));
        }
        return user;
    }

}
