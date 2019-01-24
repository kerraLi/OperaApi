package com.ywxt.Handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.Permission;
import com.ywxt.Domain.Role;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Service.PermissionService;
import com.ywxt.Service.UserService;
import com.ywxt.Utils.AuthUtils;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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


// 模板
        Permission root = getTemplate();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User suser = userService.getUserByUsername(username);
        //    User suser = userService.findByUsername(username);
        // 用户的所有的权限菜单
        List<Permission> userPermissions = new ArrayList<Permission>();
        for (Role role : suser.getRoles()) {
            userPermissions.addAll(role.getPermissions());
        }


        // 复制后的结果
        List<Permission> result = new ArrayList<Permission>();

        Set<Permission> l1Permissions = root.getChildren();
        for (Permission l1Permission : l1Permissions) {
            // 复制一级菜单
            Permission _l1 =  clone(l1Permission);
            for (Permission l2Permission : l1Permission.getChildren()) {
                // 二级菜单
                // 判断用户下是否有二级菜单
                if(userPermissions.contains(l2Permission)){
                    // 复制二级菜单
                    Permission _l2 =  clone(l2Permission);
                    _l1.add(_l2);
                }
            }
            if(null != _l1.getChildren() && _l1.getChildren().size() > 0){
                // 有二级菜单就添加进来
                result.add(_l1);
            }
        }
        // 把菜单的结果放入session中
        request.getSession().setAttribute("menus",result);




        return true;
    }
    private Permission getTemplate(){
        List<Permission> permissionList = permissionService.list();
        HashMap<Long, Permission> hashMap = new HashMap<>();
        for (Permission permission : permissionList) {
            hashMap.put(permission.getId(),permission);
        }
        Permission root = new Permission();
        root.setId(0l);
        hashMap.put(0l,root);
        for (Permission permission : permissionList) {
            Permission parent = hashMap.get(permission.getPid());
            parent.add(permission);
        }
        return root;
    }

    private Permission clone(Permission src){
        Permission newP = new Permission();
        newP.setId(src.getId());
        newP.setPermissionName(src.getPermissionName());
        newP.setUrl(src.getUrl());
        newP.setPid(src.getPid());
        return newP;
    }
    // 在业务处理器处理请求完成之后，生成视图之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
