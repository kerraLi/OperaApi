package com.ywxt.Handler;

import com.auth0.jwt.JWT;

import com.auth0.jwt.exceptions.JWTDecodeException;

import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.Permission;
import com.ywxt.Domain.Role;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Service.PermissionService;
import com.ywxt.Service.RoleService;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
// 用户登陆鉴权
// todo 后期增加权限校验
public class AuthenticationHandler implements HandlerInterceptor {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    // 在业务处理器处理请求之前被调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * one:判断是否是请求方法
         */
        // 判断释放时映射方法，如果不是（静态文件等），返回true，鉴权通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /**
         * two:判断是否是登陆方法等不需要验证的方法
         *   check passToken注解
         */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        /**
         * three:判断鉴权
         */
        String authToken = request.getHeader("Authorization");
        if (authToken == null) {
            // 用户鉴权失败，请重新登陆 todo 有空了可以优化异常
            throw new RuntimeException("401");
        }
        // user id
        int userId;
        try {
            userId = Integer.parseInt(JWT.decode(authToken).getSubject());
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        // 校验会话是否失效
        if (!(new RedisUtils().getJedis().exists(Parameter.redisKeyUserToken.replace("{token}", authToken)))) {
            // 会话已失效，请重新登陆
            throw new RuntimeException("401");
        }
        // 校验用户是否存在
        User user = new UserServiceImpl().getUserById(userId);
        if (user == null) {
            // 用户不存在，请重新登陆
            throw new RuntimeException("401");
        }
        // check token
        if (!AuthUtils.isVerify(authToken, user)) {
            // 非法访问
            throw new RuntimeException("401");
        }


//        String requestUrl = request.getScheme()+"://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath() + request.getServletPath() + (StringUtils.isBlank(request.getQueryString())?"":("?"+request.getQueryString())); //请求参

        /*StringBuffer url2 = request.getRequestURL();
        String url = url2.toString();*/

          String url = request.getServletPath();
        Set<Role> roles = user.getRoles();
        List<String>urlList=new ArrayList<>();
        for (Role role : roles) {
            Set<Permission> permissions2 = role.getPermissions();
            for (Permission permission : permissions2) {
                urlList.add(permission.getUrl());
            }
        }

        for (String s : urlList) {
            if (!url.startsWith(s)){
                throw new RuntimeException("401");
            }
        }


      /*  Permission byUrl = permissionService.findByUrl(url);
        Set<Role> roles1 = byUrl.getRoles();*/

        return true;
    }
}
