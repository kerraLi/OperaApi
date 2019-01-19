package com.ywxt.Service;

import com.ywxt.Domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public abstract String login(String clientUsername, String clientPassword) throws Exception;

    // 退出
    public abstract boolean logout(String token);

    // 注册
    public abstract User register(User user);

    // 获取用户
    public abstract User getUserById(long id);

    // 获取用户
    public abstract User getUserByUsername(String username) throws Exception;
}
