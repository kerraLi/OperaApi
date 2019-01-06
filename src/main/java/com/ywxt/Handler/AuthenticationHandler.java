package com.ywxt.Handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.AuthUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

// 用户登陆鉴权
// todo 后期增加权限校验
public class AuthenticationHandler implements HandlerInterceptor {

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
            throw new RuntimeException("用户鉴权失败，请重新登陆");
        }
        // user id
        int userId;
        try {
            userId = Integer.parseInt(JWT.decode(authToken).getSubject());
            System.out.println("userId" + userId);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        // 校验用户是否存在
        User user = new UserServiceImpl().getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，请重新登陆");
        }
        System.out.println("pw" + user.getPassword());
        // check token
        if (!AuthUtils.isVerify(authToken, user)) {
            throw new RuntimeException("非法访问");
        }
        return true;
    }
}
