package com.ywxt.Service.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User.User;


import java.util.HashMap;

public interface UserService {


    public User getUser(Long id);

    public User getUser(String username) throws Exception;

    // 用户列表
    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    public long create(User user);

    public boolean remove(long id);

    public User update(User user);

    public User save(User user) throws Exception;
}
