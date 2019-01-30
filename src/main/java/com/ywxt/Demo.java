package com.ywxt;

import com.ywxt.Domain.User;
import com.ywxt.Utils.MD5Utils;

public class Demo  {
    public static void main(String[] args) {
        System.out.println("sdf");
        User user = new User();
        user.setUsername("dd");
        user.setPassword("123");
        System.out.println(user);
        String password = MD5Utils.md5("123");
        System.out.println(password);

    }

}
