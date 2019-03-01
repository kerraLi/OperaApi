package com.ywxt.Service.User;

import com.ywxt.Domain.User.User;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;


import java.util.List;

public interface UserService {


    // 登陆
    public String login(String clientUsername, String clientPassword) throws Exception;

    // 修改密码
    public void resetPwd(User user, String oldPwd, String newPwd) throws Exception;

    // 退出
    public boolean logout(String token);

    public User getUser(Long id);

    public User getUser(String username) throws Exception;

}
