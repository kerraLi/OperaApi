package com.ywxt.Dao.User;

import com.ywxt.Domain.User.UserPermission;

import java.util.HashMap;
import java.util.List;

public interface UserPermissionDao {

    Long create(UserPermission userPermission);

    boolean delete(long id);

    boolean delete(String type);

    boolean delete(UserPermission userPermission);

    UserPermission update(UserPermission userPermission);

    UserPermission getUserPermission(long id);

    UserPermission getUserPermission(String action) throws Exception;

    UserPermission getUserPermission(String type, String action, long roleId);

    // 获取角色所有权限
    List<UserPermission> getUserPermissions(long roleId);

    List<UserPermission> getUserPermissions(long roleId, String type);

    List<UserPermission> getList(HashMap<String, Object> params);

    List<UserPermission> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    int getListTotal(HashMap<String, Object> params);
}
