package com.ywxt.Service.User.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.User.UserDao;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.UserService;


import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    public User getUser(String username) throws Exception {
        return userDao.getUser(username);
    }

    @Override
    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<User> list = userDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", userDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    @Override
    public long create(User user) {
        return userDao.create(user);
    }

    @Override
    public boolean remove(long id) {
        return userDao.delete(id);
    }

    @Override
    public User update(User user) {
        return userDao.update(user);
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getId() == 0) {
            user.setAvatar(Parameter.defaultAvatar);
            user.setPassword(MD5Utils.md5(user.getPassword()));
            long id = this.create(user);
            return this.getUser(id);
        } else {
            // 单独处理password
            if (user.getPassword() == null) {
                User oldU = userDao.getUser(user.getId());
                user.setPassword(oldU.getPassword());
            }
            return userDao.update(user);
        }
    }
}
