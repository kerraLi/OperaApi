package com.ywxt.Service.Impl;

import com.ywxt.Domain.Role;
import com.ywxt.Domain.User;
import com.ywxt.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserDetailsServiceImpl  implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = null;
        try {
            user = userService.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == user) {
                throw new UsernameNotFoundException("用户名不存在");
        }
        // 第一个参数为用户名
        // 第二个参数为密码
        // 第三个参数为authorities 权限信息集合
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();    //GrantedAuthority是security提供的权限类，
        // 获取用户所拥有的角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 根据角色名称创建授权信息
            // 添加到集合中
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        org.springframework.security.core.userdetails.User user2 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        return user2;

    }
}
