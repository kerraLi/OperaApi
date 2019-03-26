package com.ywxt.Service.User;

import com.ywxt.Domain.User.UserPermission;

import java.util.List;

public interface RolePermissionService {

    void saveRolePermissions(long roleId, long[] pIds) throws Exception;

    List<UserPermission> getRolePermissions(long roleId) throws Exception;

    List<UserPermission> getRolePermissions(long roleId, String type) throws Exception;

    // 校验是否有该权限
    boolean checkRolePermission(String type, String action, Long roleId);

}
