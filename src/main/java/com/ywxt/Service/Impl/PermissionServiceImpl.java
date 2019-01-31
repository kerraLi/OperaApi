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
   /* @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }*/

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
        return null;
    }

    /**
     * 添加权限资源
     *
     * @param permission
     * @return
     */
    @Override
    public int add(Permission permission) {
        return (int) permissionDao.add(permission);
    }



    @Override
    public Permission findPermissionById(Long id) {
        return null;
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
    public void deleteById(long id) {
       permissionDao.deleteById(id);
    }

    @Override
    public Permission findByUrl(String url) {
        return permissionDao.findByUrl(url);
    }


}
