package com.ywxt.Service.Impl;

import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findAll() {
        return null;
    }

    /**
     * 查询所有权限信息
     *
     * @return
     */
    @Override
    public List<Permission> list() {

        return permissionDao.list();
    }

    /**
     * 添加权限资源
     *
     * @param sysPermission
     * @return
     */
    @Override
    public int add(Permission permission) {
        return 0;
    }
}
