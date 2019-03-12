package com.ywxt.Dao.User;

import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Domain.User.UserRolePermission;

import java.util.List;

public interface UserRolePermissionDao {

    public Long create(UserRolePermission userRolePermission);

    // type:id userId permissionId roleId
    public boolean delete(String type, long typeId);

    public boolean delete(UserRolePermission userRolePermission);

    public UserRolePermission update(UserRolePermission userRolePermission);

    public UserRolePermission getUserRolePermission(long id);

    public List<UserPermission> getRolePermissions(long roleId);
}
