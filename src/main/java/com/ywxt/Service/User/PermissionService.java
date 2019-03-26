package com.ywxt.Service.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User.UserPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public interface PermissionService {

    List<UserPermission> refreshApi(HttpServletRequest request) throws Exception;

    List<UserPermission> refreshMenu(ArrayList<JSONObject> list) throws Exception;

    List<UserPermission> getList() throws Exception;

    List<UserPermission> getList(String type) throws Exception;

    Long create(UserPermission userPermission);

    void removeByType(String type);

}
