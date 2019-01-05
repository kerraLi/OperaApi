package com.ywxt.Domain;

import com.ywxt.Utils.MD5Utils;

public class User {


    private String username;
    private String password;

    public boolean check(String clientUsername, String clientPassword) {
        return (this.username.equals(clientUsername) && this.password.equals(MD5Utils.md5(clientPassword)));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
