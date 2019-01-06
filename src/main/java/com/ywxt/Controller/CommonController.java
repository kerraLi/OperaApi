package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.Parameter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommonController {


    protected User user;

    // 从token中获取当前登陆用户信息
    protected User getUserFromAuthToken(HttpServletRequest request) {
        if (this.user == null) {
            String authToken = request.getHeader("Authorization");
            int userId = Integer.parseInt(AuthUtils.getSubject(authToken));
            this.user = new UserServiceImpl().getUserById(userId);
        }
        return this.user;
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
