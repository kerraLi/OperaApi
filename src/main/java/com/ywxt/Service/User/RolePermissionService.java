package com.ywxt.Service.User;

import com.ywxt.Domain.User.UserPermission;

import java.util.List;

public interface RolePermissionService {

    public void saveRolePermissions(long roleId, long[] pIds) throws Exception;

    public List<UserPermission> getRolePermissions(long roleId) throws Exception;

}
