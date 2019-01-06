package com.ywxt.Controller;

import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.AuthUtils;

import javax.servlet.http.HttpServletRequest;

public class CommonController {


    protected User user;

    protected User getUserFromAuthToken(HttpServletRequest request) {
        if (this.user == null) {
            String authToken = request.getHeader("Authorization");
            int userId = Integer.parseInt(AuthUtils.getSubject(authToken));
            this.user = new UserServiceImpl().getUserById(userId);
        }
        return this.user;
    }
}
