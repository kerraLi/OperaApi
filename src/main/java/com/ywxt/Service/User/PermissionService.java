package com.ywxt.Service.User;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.User.UserPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public interface PermissionService {

    public List<UserPermission> refreshApi(HttpServletRequest request);

    public List<UserPermission> refreshMenu(ArrayList<JSONObject> list);

    public List<UserPermission> getList() throws Exception;

    public List<UserPermission> getList(String type) throws Exception;

    public Long create(UserPermission userPermission);

    public void removeByType(String type);

}
