package com.ywxt.Service;

import com.ywxt.Domain.User.User;

public interface AuthService {

    // 登陆
    public String login(String clientUsername, String clientPassword) throws Exception;

    // 信息
    public User info(Long userId) throws Exception;

    // 修改密码
    public void resetPwd(User user, String oldPwd, String newPwd) throws Exception;

    // 退出
    public boolean logout(String token);

}
