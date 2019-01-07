package com.ywxt.Command;

import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.RedisUtils;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

public class TestCommand {

    public static void main(String[] args) {
        System.out.println(123);
//        UserDaoImpl userDao = new UserDaoImpl();
//        // 创建用户
//        User u = new User();
//        u.setUsername("admin2");
//        u.setPassword(MD5Utils.md5("123456"));
//        u.setNickname("超级管理员2");
//        u.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        u.setIntroduction("我是超级管理员2");
//        String[] roles = {"admin"};
//        u.setRoles(roles);
//        userDao.saveUser(u);
        // 读取用户
//        User user = new UserServiceImpl().getUserById(1);
//        for (String s :user.getRoles()){
//            System.out.println(s);
//        }
//        System.out.println();
//        System.out.println(user.getUsername());

        Jedis jedis = new RedisUtils().getJedis();
        // redis
        System.out.println("======================key==========================");
        // 清空数据
        System.out.println("清空库中所有数据：" + jedis.flushDB());
        // 判断key否存在
//        System.out.println("判断key999键是否存在：" + shardedJedis.exists("key999"));
//        System.out.println("新增key001,value001键值对：" + shardedJedis.set("key001", "value001"));
//        System.out.println("判断key001是否存在：" + shardedJedis.exists("key001"));
        // 输出系统中所有的key
        System.out.println("新增key002,value002键值对：" + jedis.set("key002", "value002"));
        System.out.println("系统中所有键如下：");
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key);
        }
    }
}
