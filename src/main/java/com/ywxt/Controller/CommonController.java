package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.Impl.UserServiceImpl;
import com.ywxt.Service.User.UserService;
import com.ywxt.Utils.AuthUtils;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommonController {

    @Resource
    private UserService userService;

    protected User user;

    // 从token中获取当前登陆用户Id
    protected Long getUserIdFromAuthToken(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        return Long.parseLong(AuthUtils.getSubject(authToken));
    }

    // 从token中获取当前登陆用户信息
    protected User getUserFromAuthToken(HttpServletRequest request) {
        if (this.user == null) {
            String authToken = request.getHeader("Authorization");
            long userId = Long.parseLong(AuthUtils.getSubject(authToken));
            this.user = userService.getUser(userId);
        }
        return this.user;
    }

    // 统一返回接口
    protected JSONObject returnObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("status", "success");
        return jsonObject;
    }

    // 统一返回接口
    protected JSONObject returnObject(HashMap<String, Object> hashMap) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("status", "success");
        for (Map.Entry<String, Object> e : hashMap.entrySet()) {
            jsonObject.put(e.getKey(), e.getValue());
        }
        return jsonObject;
    }
}
