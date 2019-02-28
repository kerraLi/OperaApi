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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

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
        try {
            if (!AuthUtils.isVerify(authToken, user)) {
                // 非法访问
                throw new RuntimeException("401");
            }
        } catch (SignatureException e) {
            throw new RuntimeException("401");
        }


//        String requestUrl = request.getScheme()+"://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath() + request.getServletPath() + (StringUtils.isBlank(request.getQueryString())?"":("?"+request.getQueryString())); //请求参

        /*StringBuffer url2 = request.getRequestURL();
        String url = url2.toString();*/

          String url = request.getServletPath();
      /*  Set<Role> roles = user.getRoles();
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
        }*/


//        List<String> allUrl = permissionService.findAllUrl();        //数据库里的所有url集合
//      if (CollectionUtils.isNotEmpty(allUrl)){
//          for (String resURL : allUrl) {
//                if (StringUtils.startsWith(url,resURL)){
//                    Set<Role> roleSets = permissionService.findByUrl(resURL).getRoles();    //通过数据库的url找url对应的角色，
//                    if (CollectionUtils.isNotEmpty(roleSets)){
//                        Iterator<Role> iterator = roleSets.iterator();
//                        while (iterator.hasNext()){
//                            Role next = iterator.next();
//                            String roleName = next.getRoleName();
//                            for (Role role : user.getRoles()) {
//                                String roleName1 = role.getRoleName();
//                                if (StringUtils.equals(roleName,roleName1)){
//                                    return true;
//                                }
//                            }
//                        }
//                        response.getWriter().print("没有访问权限");                  //无对URL权限
//                        return false;
//                    } else {
//                        return true;               //url没有宿主（都可以访问）
//                    }
//                }
//          }
//          return  true;
//      }
//

      /*  Permission byUrl = permissionService.findByUrl(url);
        Set<Role> roles1 = byUrl.getRoles();*/

        return true;
    }
}
