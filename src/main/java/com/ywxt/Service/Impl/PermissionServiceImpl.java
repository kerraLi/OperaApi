package com.ywxt.Service.Impl;

import com.ywxt.Dao.PermissionDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
//@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;



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
     * @param permission
     * @return
     */
    @Override
    public void save(Permission permission) {
       permissionDao.save(permission);
    }





    @Override
    public Permission findPermissionById(Long id) {
        return permissionDao.findPermissionById(id);
    }

    @Override
    public List<String> findAllUrl() {
      return   permissionDao.findAllUrl();

}

    @Override
    public void update(Permission permission) {
       permissionDao.update(permission);
    }

    @Override
    public void deleteById(Long id) {
       permissionDao.deleteById(id);
    }

    @Override
    public Permission findByUrl(String url) {
        return permissionDao.findByUrl(url);
    }


}
