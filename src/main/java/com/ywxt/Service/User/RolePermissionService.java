package com.ywxt.Service.User;

import com.ywxt.Domain.User.UserPermission;

import java.util.List;

public interface RolePermissionService {

    public void saveRolePermissions(long roleId, long[] pIds) throws Exception;

    public List<UserPermission> getRolePermissions(long roleId) throws Exception;

    public List<UserPermission> getRolePermissions(long roleId, String type) throws Exception;

    // 校验是否有该权限
    public boolean checkRolePermission(String type, String action, Long roleId);

}
