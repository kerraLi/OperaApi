package com.ywxt.Service.User.Impl;

import com.ywxt.Dao.User.UserPermissionDao;
import com.ywxt.Dao.User.UserRoleDao;
import com.ywxt.Dao.User.UserRolePermissionDao;
import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Domain.User.UserRole;
import com.ywxt.Domain.User.UserRolePermission;
import com.ywxt.Service.User.RolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private UserPermissionDao userPermissionDao;
    @Resource
    private UserRolePermissionDao userRolePermissionDao;

    @Override
    @Transactional
    public void saveRolePermissions(long roleId, long[] pIds) throws Exception {
        UserRole role = userRoleDao.getUserRole(roleId);
        if (role == null) {
            throw new Exception("无效的角色信息");
        }
        // 删除旧权限
        userRolePermissionDao.delete("roleId", roleId);
        for (long pid : pIds) {
            UserPermission permission = userPermissionDao.getUserPermission(pid);
            if (permission == null) {
                throw new Exception("无效的权限信息");
            }
            UserRolePermission temp = new UserRolePermission();
            temp.setUserRole(role);
            temp.setUserPermission(permission);
            userRolePermissionDao.create(temp);
        }
    }

    @Override
    public List<UserPermission> getRolePermissions(long roleId) throws Exception {
        UserRole role = userRoleDao.getUserRole(roleId);
        if (role == null) {
            throw new Exception("无效的角色信息");
        }
        return userRolePermissionDao.getRolePermissions(roleId);
    }
}
