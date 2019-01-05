package com.ywxt.Controller;

import com.ywxt.Domain.User;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    public User login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String clientUsername = request.getParameter("username");
        String clientPassword = request.getParameter("password");
        User user = new User();
        String defaultUsername = Parameter.defaultUsername;
        String defaultPassword = Parameter.defaultPassword;
        user.setUsername(defaultUsername);
        user.setPassword(defaultPassword);
        if (user.check(clientUsername, clientPassword)) {
            return user;
        } else {
            throw new Exception("no user");
        }
    }
}
