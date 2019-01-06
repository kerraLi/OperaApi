package com.ywxt.Command;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.MD5Utils;

public class TestCommand {

    public static void main(String[] args) {
        System.out.println(123);
        UserDaoImpl userDao = new UserDaoImpl();
//        User u = new User();
//        u.setUsername("admin123");
//        u.setPassword(MD5Utils.md5("123456"));
//        Long id = userDao.saveUser(u);
        User user = new UserServiceImpl().getUserById(1);
        //User user = new UserServiceImpl().getUserByUsername("admin");
        System.out.println(user.getUsername());
    }
}
