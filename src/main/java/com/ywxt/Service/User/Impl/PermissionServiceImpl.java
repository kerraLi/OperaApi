package com.ywxt.Service.User.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Dao.User.UserPermissionDao;
import com.ywxt.Dao.User.UserRolePermissionDao;
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
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private UserPermissionDao userPermissionDao;
    @Resource
    private UserRolePermissionDao userRolePermissionDao;

    @Transactional
    public List<UserPermission> refreshApi(HttpServletRequest request) throws Exception {
        String type = "api";
        // 获取已有全部权限
        List<UserPermission> oldPs = this.getList(type);
        Map<String, JSONObject> todoMap = new HashMap<String, JSONObject>();
        for (UserPermission oup : oldPs) {
            JSONObject obj = new JSONObject() {{
                put("oldId", oup.getId());
            }};
            todoMap.put(oup.getAction(), obj);
        }
        // 删除已有api
        this.removeByType(type);
        List<UserPermission> ups = new ArrayList<UserPermission>();
        Map<String, Long> parentIds = new HashMap<>();
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);//获取上下文对象
        RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            // 判断该方法是否跳过鉴权
            Method method = handlerMethods.get(rmi).getMethod();
            if (method.isAnnotationPresent(PassToken.class)) {
                PassToken passToken = method.getAnnotation(PassToken.class);
                if (passToken.permission()) {
                    continue;
                }
            }
            // 未配置方法名称
            if (rmi.getName() == null) {
                continue;
            }
            String[] names = rmi.getName().split("#");
            PatternsRequestCondition prc = rmi.getPatternsCondition();
            Set<String> patterns = prc.getPatterns();
            for (String uStr : patterns) {
                UserPermission up = new UserPermission();
                if (parentIds.containsKey(names[0])) {
                    up.setParentId(parentIds.get(names[0]));
                } else {
                    // 增加父级权限
                    UserPermission upParent = new UserPermission();
                    upParent.setName(names[0]);
                    upParent.setAction("");
                    upParent.setType(type);
                    Long parentId = this.create(upParent);
                    parentIds.put(names[0], parentId);
                    upParent.setId(parentId);
                    ups.add(upParent);
                    // 处理待更新map
                    if (todoMap.containsKey(upParent.getAction())) {
                        JSONObject obj = todoMap.get(upParent.getAction());
                        obj.put("newId", upParent.getId());
                        todoMap.put(upParent.getAction(), obj);
                    }
                    //
                    up.setParentId(parentId);
                }
                up.setName(names[1]);
                up.setAction(uStr);
                up.setType(type);
                Long id = this.create(up);
                up.setId(id);
                ups.add(up);
                // 处理待更新map
                if (todoMap.containsKey(up.getAction())) {
                    JSONObject obj = todoMap.get(up.getAction());
                    obj.put("newId", up.getId());
                    todoMap.put(up.getAction(), obj);
                }
            }
        }
        // do map
        this.refreshRolePermission(todoMap);
        return ups;
    }

    @Transactional
    public List<UserPermission> refreshMenu(ArrayList<JSONObject> list) throws Exception {
        String type = "menu";
        List<UserPermission> ups = new ArrayList<UserPermission>();
        // 获取已有全部权限
        List<UserPermission> oldPs = this.getList(type);
        Map<String, JSONObject> todoMap = new HashMap<String, JSONObject>();
        for (UserPermission oup : oldPs) {
            JSONObject obj = new JSONObject() {{
                put("oldId", oup.getId());
            }};
            todoMap.put(oup.getAction(), obj);
        }
        // 删除已有menu
        this.removeByType(type);
        for (JSONObject jObject : list) {
            Long parentId = 0L;
            if (jObject.get("parentCode") != null && jObject.get("parentCode") != "") {
                for (UserPermission up : ups) {
                    if (up.getName().equals(jObject.get("parentName"))) {
                        parentId = up.getId();
                    }
                }
            }
            UserPermission up = new UserPermission();
            up.setType(type);
            up.setAction((String) jObject.get("path"));
            up.setName((String) jObject.get("name"));
            up.setParentId(parentId);
            Long id = this.create(up);
            up.setId(id);
            ups.add(up);
            // 处理待更新map
            if (todoMap.containsKey(up.getAction())) {
                JSONObject obj = todoMap.get(up.getAction());
                obj.put("newId", up.getId());
                todoMap.put(up.getAction(), obj);
            }
        }
        // do map
        this.refreshRolePermission(todoMap);
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

    // 刷新角色对应权限
    private void refreshRolePermission(Map<String, JSONObject> map) {
        Map<Long, Long> updateMap = new HashMap<Long, Long>();
        for (Map.Entry<String, JSONObject> e : map.entrySet()) {
            if (e.getValue().get("newId") == null) {
                userRolePermissionDao.delete("permissionId", (long) e.getValue().get("oldId"));
            } else {
                updateMap.put((long) e.getValue().get("oldId"), (long) e.getValue().get("newId"));
            }
        }
        userRolePermissionDao.updateUserPermissions(updateMap);
    }

}
