package com.ywxt.Dao.User;

import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Domain.User.UserRolePermission;

import java.util.List;
import java.util.Map;

public interface UserRolePermissionDao {

    // 新建关联
    public Long create(UserRolePermission userRolePermission);

    // 删除：按照ID或者RoleID/permissionId批量删除 type:id roleId permissionId
    public boolean delete(String type, long typeId);

    // 删除单个关联
    public boolean delete(UserRolePermission userRolePermission);

    // 更新单个关联
    public UserRolePermission update(UserRolePermission userRolePermission);

    // 批量更新用户关联
    public void updateUserPermissions(Map<Long, Long> map);

    // 获取单个关联
    public UserRolePermission getUserRolePermission(long id);

    // 获取角色所有权限
    public List<UserPermission> getRolePermissions(long roleId);
}
