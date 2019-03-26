package com.ywxt.Service.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User.User;


import java.util.HashMap;

public interface UserService {


    User getUser(Long id);

    User getUser(String username) throws Exception;

    // 用户列表
    JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    long create(User user);

    boolean remove(long id);

    User update(User user);

    User save(User user) throws Exception;
}
