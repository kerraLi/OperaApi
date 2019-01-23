package com.ywxt.Service.Impl;

import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Service.PermissionService;
import com.ywxt.Utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
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
     * @param Permission
     * @return
     */
    @Override
    public int add(Permission permission) {
        return permissionDao.add(permission);
    }

    @Override
    public void pageQuery(PageBean pageBean) {
            permissionDao.pageQuery(pageBean);
    }
}
