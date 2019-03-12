package com.ywxt.Service.User.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.User.UserPermissionDao;
import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Service.User.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private UserPermissionDao userPermissionDao;

    public List<UserPermission> refreshApi(HttpServletRequest request) {
        // 删除已有api
        this.removeByType("api");
        // 默认已有权限api：忽略不做配置
        List<String> forgetUL = new ArrayList<String>() {{
            add("/user/login");
            add("/user/info");
            add("/user/logout");
            add("/user/reset/password");
            add("/message/webhook");
            add("/message/websocket");
        }};
        List<UserPermission> ups = new ArrayList<UserPermission>();
        Map<String, Long> parentIds = new HashMap<>();
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);//获取上下文对象
        RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            if (rmi.getName() == null) {
                continue;
            }
            String[] names = rmi.getName().split("#");
            PatternsRequestCondition prc = rmi.getPatternsCondition();
            Set<String> patterns = prc.getPatterns();
            for (String uStr : patterns) {
                if (!forgetUL.contains(uStr)) {
                    UserPermission up = new UserPermission();
                    if (parentIds.containsKey(names[0])) {
                        up.setParentId(parentIds.get(names[0]));
                    } else {
                        // 增加父级权限
                        UserPermission upParent = new UserPermission();
                        upParent.setName(names[0]);
                        upParent.setAction("");
                        upParent.setType("api");
                        Long parentId = this.create(upParent);
                        parentIds.put(names[0], parentId);
                        upParent.setId(parentId);
                        ups.add(upParent);
                        //
                        up.setParentId(parentId);
                    }
                    up.setName(names[1]);
                    up.setAction(uStr);
                    up.setType("api");
                    Long id = this.create(up);
                    up.setId(id);
                    ups.add(up);
                }
            }
        }
        return ups;
    }

    public List<UserPermission> refreshMenu(ArrayList<JSONObject> list) {
        List<UserPermission> ups = new ArrayList<UserPermission>();
        // 删除已有menu
        this.removeByType("menu");
        for (JSONObject jObject : list) {
            Long parentId = 0L;
            if (jObject.get("parentCode") != null && jObject.get("parentCode") != "") {
                for (UserPermission up : ups) {
                    if (up.getName().equals(jObject.get("parentName"))) {
                        System.out.println("========???");
                        parentId = up.getId();
                    }
                }
            }
            UserPermission up = new UserPermission();
            up.setType("menu");
            up.setAction((String) jObject.get("path"));
            up.setName((String) jObject.get("name"));
            up.setParentId(parentId);
            Long id = this.create(up);
            up.setId(id);
            ups.add(up);
        }
        return ups;
    }

    public List<UserPermission> getList() throws Exception {
        return userPermissionDao.getList(new HashMap<>() {{
        }});
    }

    public List<UserPermission> getList(String type) throws Exception {
        if (!type.equals("menu") && !type.equals("api")) {
            throw new Exception("权限类型错误");
        }
        return userPermissionDao.getList(new HashMap<>() {{
            put("type", type);
        }});
    }

    public Long create(UserPermission userPermission) {
        return userPermissionDao.create(userPermission);
    }

    public void removeByType(String type) {
        userPermissionDao.delete(type);
    }

}
