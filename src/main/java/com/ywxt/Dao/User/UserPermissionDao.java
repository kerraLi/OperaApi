package com.ywxt.Dao.User;

import com.ywxt.Domain.User.UserPermission;

import java.util.HashMap;
import java.util.List;

public interface UserPermissionDao {

    public Long create(UserPermission userPermission);

    public boolean delete(long id);

    public boolean delete(String type);

    public boolean delete(UserPermission userPermission);

    public UserPermission update(UserPermission userPermission);

    public UserPermission getUserPermission(long id);

    public UserPermission getUserPermission(String action) throws Exception;

    public UserPermission getUserPermission(String type, String action, long roleId);

    // 获取角色所有权限
    public List<UserPermission> getUserPermissions(long roleId);

    public List<UserPermission> getUserPermissions(long roleId, String type);

    public List<UserPermission> getList(HashMap<String, Object> params);

    public List<UserPermission> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);
}
