package com.ywxt.Handler;

import com.auth0.jwt.JWT;

import com.auth0.jwt.exceptions.JWTDecodeException;

import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User.User;
import com.ywxt.Service.User.RolePermissionService;
import com.ywxt.Service.User.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Service.RedisService;

import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

// 用户登陆鉴权
public class AuthenticationHandler implements HandlerInterceptor {

    @Resource
    private UserService userService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private RedisService redisService;
    @Value("${redis.ttl.user}")
    private int redisTllUserToken;

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
            if (passToken.login()) {
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
        long userId;
        try {
            userId = Long.parseLong(JWT.decode(authToken).getSubject());
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        // redis:校验会话是否失效
        if (!(redisService.getJedis().exists(Parameter.redisKeyUserToken.replace("{token}", authToken)))) {
            // 会话已失效，请重新登陆
            throw new RuntimeException("401");
        }
        // 校验用户是否存在
        User user = userService.getUser(userId);
        if (user == null) {
            // 用户不存在，请重新登陆
            throw new RuntimeException("401");
        }
        // check token
        try {
            if (!AuthUtils.isVerify(authToken, user)) {
                // 非法访问
                throw new RuntimeException("401");
            }
        } catch (SignatureException e) {
            throw new RuntimeException("401");
        }
        // redis:延长登陆时间
        redisService.getJedis().expire(Parameter.redisKeyUserToken.replace("{token}", authToken), redisTllUserToken);

        /**
         * three:判断接口权限
         */
        // admin：所有权限
        if (user.getRole().getCode().equals("admin")) {
            return true;
        }
        // 免鉴权
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.permission()) {
                return true;
            }
        }
        //
        String mName = "";
        RequestMapping cMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mMapping = method.getAnnotation(RequestMapping.class);
            mName = mMapping.value()[0];
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mMapping = method.getAnnotation(GetMapping.class);
            mName = mMapping.value()[0];
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mMapping = method.getAnnotation(PostMapping.class);
            mName = mMapping.value()[0];
        }
        String action = cMapping.value()[0] + mName;
        boolean hasPermission = rolePermissionService.checkRolePermission("api", action, user.getRole().getId());
        // 无权限
        if (!hasPermission) {
            throw new RuntimeException("403");
        }
        return true;
    }
}
